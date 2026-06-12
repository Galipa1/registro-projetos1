package com.empresa.registroprojetos.ui.panels;

import com.empresa.registroprojetos.domain.Cliente;
import com.empresa.registroprojetos.exception.ValidacaoException;
import com.empresa.registroprojetos.service.interfaces.IClienteService;
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
 * Painel de cadastro e listagem de Clientes.
 *
 * SOLID — SRP: Apenas responsável pela tela de Clientes.
 * SOLID — DIP: Depende de IClienteService (interface).
 * IoC: Serviço injetado via construtor.
 */
public class PainelCliente extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final IClienteService service;

    private JTextField campoCpf;
    private JTextField campoNome;
    private JTextField campoDtNasc;
    private JLabel     labelStatus;
    private DefaultTableModel modeloTabela;
    private JTable tabela;

    public PainelCliente(IClienteService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBackground(Tema.BG_JANELA);
        construirUI();
        carregarTabela();
    }

    private void construirUI() {
        // ── Formulário ─────────────────────────────────────────────
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

        gbc.gridy = 0;
        card.add(Componentes.labelSecao("Cadastro de Cliente"), gbc);

        // R2 — CPF
        gbc.gridy = 1;
        card.add(Componentes.labelCampo("CPF * (somente números)"), gbc);
        gbc.gridy = 2;
        campoCpf = Componentes.campoTexto("Ex.: 12345678901", 20);
        card.add(campoCpf, gbc);

        // R2 — Nome
        gbc.gridy = 3; gbc.insets = new Insets(10, 0, 5, 0);
        card.add(Componentes.labelCampo("Nome *"), gbc);
        gbc.gridy = 4; gbc.insets = new Insets(5, 0, 5, 0);
        campoNome = Componentes.campoTexto("Nome completo", 20);
        card.add(campoNome, gbc);

        // R2 — Data de nascimento
        gbc.gridy = 5; gbc.insets = new Insets(10, 0, 5, 0);
        card.add(Componentes.labelCampo("Data de Nascimento * (dd/MM/yyyy)"), gbc);
        gbc.gridy = 6; gbc.insets = new Insets(5, 0, 5, 0);
        campoDtNasc = Componentes.campoTexto("Ex.: 20/07/1985", 20);
        card.add(campoDtNasc, gbc);

        // Status
        gbc.gridy = 7; gbc.insets = new Insets(12, 0, 0, 0);
        labelStatus = new JLabel(" ");
        labelStatus.setFont(Tema.FONTE_LABEL);
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

        // ── Tabela ─────────────────────────────────────────────────
        JPanel direita = new JPanel(new BorderLayout());
        direita.setBackground(Tema.BG_JANELA);
        direita.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));

        String[] colunas = { "#", "CPF", "Nome", "Data de Nascimento" };
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        estilizarTabela();

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBackground(Tema.BG_PAINEL);
        scroll.getViewport().setBackground(Tema.BG_PAINEL);
        scroll.setBorder(BorderFactory.createLineBorder(Tema.BORDA, 1));

        JPanel painelDir = new JPanel(new BorderLayout());
        painelDir.setBackground(Tema.BG_JANELA);

        JLabel titulo = new JLabel("Clientes Cadastrados");
        titulo.setFont(Tema.FONTE_SECTION);
        titulo.setForeground(Tema.TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        painelDir.add(titulo, BorderLayout.NORTH);
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
        tabela.getColumnModel().getColumn(0).setMaxWidth(50);
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
        try {
            LocalDate dataNasc = LocalDate.parse(campoDtNasc.getText().trim(), FMT);
            Cliente c = new Cliente(campoCpf.getText().trim(), campoNome.getText().trim(), dataNasc);
            service.cadastrar(c);
            mostrarStatus("✓  Cliente cadastrado com sucesso.", Tema.SUCESSO);
            limpar();
            carregarTabela();
        } catch (DateTimeParseException e) {
            mostrarStatus("⚠  Data inválida. Use dd/MM/yyyy.", Tema.PERIGO);
        } catch (ValidacaoException e) {
            mostrarStatus("⚠  " + e.getMessage(), Tema.PERIGO);
        } catch (Exception e) {
            mostrarStatus("⚠  " + e.getMessage(), Tema.PERIGO);
        }
    }

    private void remover() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) { mostrarStatus("Selecione um cliente.", Tema.AVISO); return; }
        Long id = (Long) modeloTabela.getValueAt(linha, 0);
        int c = JOptionPane.showConfirmDialog(this, "Remover cliente selecionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        try {
            service.remover(id);
            carregarTabela();
            mostrarStatus("✓  Cliente removido.", Tema.SUCESSO);
        } catch (Exception e) {
            mostrarStatus("⚠  " + e.getMessage(), Tema.PERIGO);
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        try {
            for (Cliente c : service.listarTodos()) {
                modeloTabela.addRow(new Object[]{
                        c.getId(), c.getCpf(), c.getNome(),
                        c.getDataNascimento().format(FMT)
                });
            }
        } catch (Exception e) {
            mostrarStatus("⚠  " + e.getMessage(), Tema.PERIGO);
        }
    }

    private void limpar() {
        campoCpf.setText(""); campoNome.setText(""); campoDtNasc.setText("");
        campoCpf.requestFocus();
    }

    private void mostrarStatus(String msg, Color cor) {
        labelStatus.setText(msg); labelStatus.setForeground(cor);
    }
}