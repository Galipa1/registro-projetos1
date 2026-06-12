package com.empresa.registroprojetos.ui.panels;

import com.empresa.registroprojetos.domain.Projetista;
import com.empresa.registroprojetos.exception.ValidacaoException;
import com.empresa.registroprojetos.service.interfaces.IProjetistaService;
import com.empresa.registroprojetos.ui.components.Componentes;
import com.empresa.registroprojetos.ui.components.Tema;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Painel de cadastro e listagem de Projetistas.
 *
 * SOLID — SRP: Apenas responsável pela tela de Projetistas.
 * SOLID — DIP: Depende de IProjetistaService (interface) — nunca da impl.
 *
 * IoC: O serviço é injetado via construtor — a tela não sabe como
 *      o serviço é construído ou de onde vem o banco de dados.
 */
public class PainelProjetista extends JPanel {

    private static final DateTimeFormatter FMT_ENTRADA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── Dependência declarada como INTERFACE ──────────────────────
    private final IProjetistaService service;

    // ── Componentes de formulário ─────────────────────────────────
    private JTextField campoCodigo;
    private JTextField campoNome;
    private JTextField campoDtNasc;
    private JLabel     labelStatus;

    // ── Tabela de listagem ────────────────────────────────────────
    private DefaultTableModel modeloTabela;
    private JTable tabela;

    public PainelProjetista(IProjetistaService service) {
        this.service = service;
        setLayout(new BorderLayout(0, 0));
        setBackground(Tema.BG_JANELA);
        construirUI();
        carregarTabela();
    }

    private void construirUI() {
        // ── Formulário (esquerda) ──────────────────────────────────
        JPanel esquerda = new JPanel(new BorderLayout());
        esquerda.setBackground(Tema.BG_JANELA);
        esquerda.setPreferredSize(new Dimension(340, 0));
        esquerda.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));

        JPanel card = Componentes.painelCard();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0; gbc.weightx = 1;

        // Título
        gbc.gridy = 0;
        card.add(Componentes.labelSecao("Cadastro de Projetista"), gbc);

        // R1 — Código funcional
        gbc.gridy = 1;
        card.add(Componentes.labelCampo("Código Funcional *"), gbc);
        gbc.gridy = 2;
        campoCodigo = Componentes.campoTexto("Ex.: FUN-001", 20);
        card.add(campoCodigo, gbc);

        // R1 — Nome
        gbc.gridy = 3; gbc.insets = new Insets(10, 0, 5, 0);
        card.add(Componentes.labelCampo("Nome *"), gbc);
        gbc.gridy = 4; gbc.insets = new Insets(5, 0, 5, 0);
        campoNome = Componentes.campoTexto("Nome completo", 20);
        card.add(campoNome, gbc);

        // R1 — Data de nascimento
        gbc.gridy = 5; gbc.insets = new Insets(10, 0, 5, 0);
        card.add(Componentes.labelCampo("Data de Nascimento * (dd/MM/yyyy)"), gbc);
        gbc.gridy = 6; gbc.insets = new Insets(5, 0, 5, 0);
        campoDtNasc = Componentes.campoTexto("Ex.: 15/03/1990", 20);
        card.add(campoDtNasc, gbc);

        // Status
        gbc.gridy = 7; gbc.insets = new Insets(12, 0, 0, 0);
        labelStatus = new JLabel(" ");
        labelStatus.setFont(Tema.FONTE_LABEL);
        labelStatus.setForeground(Tema.TEXTO_MUTED);
        card.add(labelStatus, gbc);

        // Botões
        gbc.gridy = 8; gbc.insets = new Insets(14, 0, 0, 0);
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        painelBotoes.setOpaque(false);
        Componentes.BotaoPrimario btnSalvar = new Componentes.BotaoPrimario("Salvar");
        Componentes.BotaoSecundario btnLimpar = new Componentes.BotaoSecundario("Limpar");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        card.add(painelBotoes, gbc);

        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limpar());

        esquerda.add(card, BorderLayout.NORTH);
        add(esquerda, BorderLayout.WEST);

        // ── Tabela (direita) ──────────────────────────────────────
        JPanel direita = new JPanel(new BorderLayout());
        direita.setBackground(Tema.BG_JANELA);
        direita.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));

        String[] colunas = { "#", "Código Funcional", "Nome", "Data de Nascimento" };
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        estilizarTabela();

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBackground(Tema.BG_PAINEL);
        scroll.getViewport().setBackground(Tema.BG_PAINEL);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.BORDA, 1));

        // Botão remover
        JPanel painelDir = new JPanel(new BorderLayout());
        painelDir.setBackground(Tema.BG_JANELA);
        JPanel topDir = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topDir.setBackground(Tema.BG_JANELA);
        JLabel tituloLista = new JLabel("Projetistas Cadastrados");
        tituloLista.setFont(Tema.FONTE_SECTION);
        tituloLista.setForeground(Tema.TEXTO);
        topDir.add(tituloLista);
        painelDir.add(topDir, BorderLayout.NORTH);
        painelDir.add(scroll, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        rodape.setBackground(Tema.BG_JANELA);
        Componentes.BotaoPerigo btnRemover = new Componentes.BotaoPerigo("Remover Selecionado");
        rodape.add(btnRemover);
        painelDir.add(rodape, BorderLayout.SOUTH);
        btnRemover.addActionListener(e -> remover());

        direita.add(painelDir, BorderLayout.CENTER);
        add(direita, BorderLayout.CENTER);
    }

    private void estilizarTabela() {
        tabela.setFont(Tema.FONTE_TABELA);
        tabela.setForeground(Tema.TEXTO);
        tabela.setBackground(Tema.BG_PAINEL);
        tabela.setSelectionBackground(Tema.BG_LINHA_SEL);
        tabela.setSelectionForeground(Tema.TEXTO);
        tabela.setGridColor(Tema.BORDA);
        tabela.setRowHeight(32);
        tabela.setShowHorizontalLines(true);
        tabela.setShowVerticalLines(false);
        tabela.getTableHeader().setFont(Tema.FONTE_LABEL.deriveFont(Font.BOLD));
        tabela.getTableHeader().setForeground(Tema.TEXTO_MUTED);
        tabela.getTableHeader().setBackground(Tema.BG_CABECALHO);
        tabela.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDA));
        tabela.setColumnSelectionAllowed(false);
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
        // Renderizador com linhas alternadas
        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                setBackground(sel ? Tema.BG_LINHA_SEL
                        : (row % 2 == 0 ? Tema.BG_PAINEL : Tema.BG_LINHA_PAR));
                setForeground(Tema.TEXTO);
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
    }

    private void salvar() {
        String codigo = campoCodigo.getText().trim();
        String nome   = campoNome.getText().trim();
        String dtStr  = campoDtNasc.getText().trim();

        try {
            LocalDate dataNasc = LocalDate.parse(dtStr, FMT_ENTRADA);
            Projetista p = new Projetista(codigo, nome, dataNasc);
            service.cadastrar(p);
            mostrarStatus("✓  Projetista cadastrado com sucesso.", Tema.SUCESSO);
            limpar();
            carregarTabela();
        } catch (DateTimeParseException e) {
            mostrarStatus("⚠  Data inválida. Use o formato dd/MM/yyyy.", Tema.PERIGO);
        } catch (ValidacaoException e) {
            mostrarStatus("⚠  " + e.getMessage(), Tema.PERIGO);
        } catch (Exception e) {
            mostrarStatus("⚠  Erro: " + e.getMessage(), Tema.PERIGO);
        }
    }

    private void remover() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            mostrarStatus("Selecione um projetista na tabela.", Tema.AVISO);
            return;
        }
        Long id = (Long) modeloTabela.getValueAt(linha, 0);
        // Usa o ID oculto — mas exibimos o # sequencial; armazenamos o ID real:
        int confirm = JOptionPane.showConfirmDialog(this,
                "Remover o projetista selecionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            service.remover(id);
            carregarTabela();
            mostrarStatus("✓  Projetista removido.", Tema.SUCESSO);
        } catch (Exception e) {
            mostrarStatus("⚠  " + e.getMessage(), Tema.PERIGO);
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Projetista> lista = service.listarTodos();
            for (Projetista p : lista) {
                modeloTabela.addRow(new Object[]{
                        p.getId(),
                        p.getCodigoFuncional(),
                        p.getNome(),
                        p.getDataNascimento().format(FMT_ENTRADA)
                });
            }
        } catch (Exception e) {
            mostrarStatus("⚠  Erro ao carregar lista: " + e.getMessage(), Tema.PERIGO);
        }
    }

    private void limpar() {
        campoCodigo.setText("");
        campoNome.setText("");
        campoDtNasc.setText("");
        campoCodigo.requestFocus();
    }

    private void mostrarStatus(String msg, Color cor) {
        labelStatus.setText(msg);
        labelStatus.setForeground(cor);
    }
}