package com.empresa.registroprojetos.ui.panels;

import com.empresa.registroprojetos.domain.Cliente;
import com.empresa.registroprojetos.domain.Projetista;
import com.empresa.registroprojetos.domain.Projeto;
import com.empresa.registroprojetos.exception.ValidacaoException;
import com.empresa.registroprojetos.service.interfaces.IClienteService;
import com.empresa.registroprojetos.service.interfaces.IProjetistaService;
import com.empresa.registroprojetos.service.interfaces.IProjetoService;
import com.empresa.registroprojetos.ui.components.Componentes;
import com.empresa.registroprojetos.ui.components.Tema;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Painel de registro e listagem de Projetos.
 *
 * SOLID — SRP: Apenas responsável pela tela de Projetos.
 * SOLID — DIP: Depende de interfaces — nunca de implementações.
 * IoC: Todos os serviços injetados via construtor.
 *
 * Destaques de UX:
 * - Validação R4 ocorre em tempo real ao digitar a data
 * - Comboboxes carregados dinamicamente do banco
 * - Tabela com JOIN exibindo Projetista e Cliente por nome
 */
public class PainelProjeto extends JPanel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── Dependências declaradas como INTERFACES ───────────────────
    private final IProjetoService    projetoService;
    private final IProjetistaService projetistaService;
    private final IClienteService    clienteService;

    // ── Formulário ────────────────────────────────────────────────
    private JTextField   campoNumero;
    private JTextField   campoDescricao;
    private JTextField   campoDataInicio;
    private JTextField   campoDataTermino;
    private JComboBox<Projetista> comboProjetista;
    private JComboBox<Cliente>    comboCliente;
    private JLabel       labelStatus;
    private JLabel       labelDataInicioErro; // Alerta R4 inline

    // ── Tabela ────────────────────────────────────────────────────
    private DefaultTableModel modeloTabela;
    private JTable tabela;

    public PainelProjeto(IProjetoService projetoService,
                         IProjetistaService projetistaService,
                         IClienteService clienteService) {
        this.projetoService    = projetoService;
        this.projetistaService = projetistaService;
        this.clienteService    = clienteService;
        setLayout(new BorderLayout());
        setBackground(Tema.BG_JANELA);
        construirUI();
        carregarCombos();
        carregarTabela();
    }

    private void construirUI() {
        // ── Formulário (esquerda) ──────────────────────────────────
        JPanel esquerda = new JPanel(new BorderLayout());
        esquerda.setBackground(Tema.BG_JANELA);
        esquerda.setPreferredSize(new Dimension(360, 0));
        esquerda.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 10));

        JPanel card = Componentes.painelCard();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.weightx = 1;

        int y = 0;

        // Título
        gbc.gridy = y++; gbc.insets = new Insets(0, 0, 14, 0);
        card.add(Componentes.labelSecao("Registro de Projeto"), gbc);

        // R3 — Número
        gbc.gridy = y++; gbc.insets = new Insets(4, 0, 4, 0);
        card.add(Componentes.labelCampo("Número do Projeto *"), gbc);
        gbc.gridy = y++;
        campoNumero = Componentes.campoTexto("Ex.: 1042", 20);
        card.add(campoNumero, gbc);

        // R3 — Descrição
        gbc.gridy = y++; gbc.insets = new Insets(10, 0, 4, 0);
        card.add(Componentes.labelCampo("Descrição *"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(4, 0, 4, 0);
        campoDescricao = Componentes.campoTexto("Descrição do projeto", 20);
        card.add(campoDescricao, gbc);

        // R3/R4 — Data de Início
        gbc.gridy = y++; gbc.insets = new Insets(10, 0, 4, 0);
        card.add(Componentes.labelCampo("Data de Início * (dd/MM/yyyy)"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(4, 0, 0, 0);
        campoDataInicio = Componentes.campoTexto("Ex.: " + LocalDate.now().format(FMT), 20);
        card.add(campoDataInicio, gbc);

        // ── Alerta R4 inline — aparece ao digitar ─────────────────
        gbc.gridy = y++; gbc.insets = new Insets(2, 0, 4, 0);
        labelDataInicioErro = new JLabel(" ");
        labelDataInicioErro.setFont(Tema.FONTE_MONO.deriveFont(11f));
        labelDataInicioErro.setForeground(Tema.PERIGO);
        card.add(labelDataInicioErro, gbc);

        // Listener de tempo real — R4
        campoDataInicio.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { validarDataInicioTR(); }
            public void removeUpdate(DocumentEvent e)  { validarDataInicioTR(); }
            public void changedUpdate(DocumentEvent e) { validarDataInicioTR(); }
        });

        // R3 — Data de Término
        gbc.gridy = y++; gbc.insets = new Insets(4, 0, 4, 0);
        card.add(Componentes.labelCampo("Data de Término * (dd/MM/yyyy)"), gbc);
        gbc.gridy = y++;
        campoDataTermino = Componentes.campoTexto("Ex.: " + LocalDate.now().plusMonths(1).format(FMT), 20);
        card.add(campoDataTermino, gbc);

        // R3 — Projetista
        gbc.gridy = y++; gbc.insets = new Insets(10, 0, 4, 0);
        card.add(Componentes.labelCampo("Projetista Responsável *"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(4, 0, 4, 0);
        comboProjetista = new JComboBox<>();
        estilizarCombo(comboProjetista);
        card.add(comboProjetista, gbc);

        // R3 — Cliente
        gbc.gridy = y++; gbc.insets = new Insets(10, 0, 4, 0);
        card.add(Componentes.labelCampo("Cliente Solicitante *"), gbc);
        gbc.gridy = y++; gbc.insets = new Insets(4, 0, 4, 0);
        comboCliente = new JComboBox<>();
        estilizarCombo(comboCliente);
        card.add(comboCliente, gbc);

        // Status geral
        gbc.gridy = y++; gbc.insets = new Insets(14, 0, 0, 0);
        labelStatus = new JLabel(" ");
        labelStatus.setFont(Tema.FONTE_LABEL);
        card.add(labelStatus, gbc);

        // Botões
        gbc.gridy = y++; gbc.insets = new Insets(12, 0, 0, 0);
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        painelBotoes.setOpaque(false);
        Componentes.BotaoPrimario btnSalvar = new Componentes.BotaoPrimario("Registrar Projeto");
        Componentes.BotaoSecundario btnLimpar = new Componentes.BotaoSecundario("Limpar");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        card.add(painelBotoes, gbc);

        btnSalvar.addActionListener(e -> salvar());
        btnLimpar.addActionListener(e -> limpar());

        JScrollPane scrollForm = new JScrollPane(card);
        scrollForm.setBorder(BorderFactory.createEmptyBorder());
        scrollForm.setBackground(Tema.BG_JANELA);
        scrollForm.getViewport().setBackground(Tema.BG_JANELA);
        esquerda.add(scrollForm, BorderLayout.CENTER);
        add(esquerda, BorderLayout.WEST);

        // ── Tabela (direita) ──────────────────────────────────────
        JPanel direita = new JPanel(new BorderLayout());
        direita.setBackground(Tema.BG_JANELA);
        direita.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 20));

        String[] colunas = { "ID", "Nº", "Início", "Término", "Projetista", "Cliente", "Descrição" };
        modeloTabela = new DefaultTableModel(colunas, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(modeloTabela);
        estilizarTabela();

        JScrollPane scrollTab = new JScrollPane(tabela);
        scrollTab.setBackground(Tema.BG_PAINEL);
        scrollTab.getViewport().setBackground(Tema.BG_PAINEL);
        scrollTab.setBorder(BorderFactory.createLineBorder(Tema.BORDA, 1));

        JPanel painelDir = new JPanel(new BorderLayout());
        painelDir.setBackground(Tema.BG_JANELA);
        JLabel titulo = new JLabel("Projetos Registrados");
        titulo.setFont(Tema.FONTE_SECTION);
        titulo.setForeground(Tema.TEXTO);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        painelDir.add(titulo, BorderLayout.NORTH);
        painelDir.add(scrollTab, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 8));
        rodape.setBackground(Tema.BG_JANELA);
        Componentes.BotaoPerigo btnRemover = new Componentes.BotaoPerigo("Remover Selecionado");
        rodape.add(btnRemover);
        painelDir.add(rodape, BorderLayout.SOUTH);
        btnRemover.addActionListener(e -> remover());

        direita.add(painelDir);
        add(direita, BorderLayout.CENTER);
    }

    /**
     * Validação R4 em tempo real — dispara a cada tecla digitada.
     * Exibe/oculta o alerta inline sem bloquear o campo.
     */
    private void validarDataInicioTR() {
        String txt = campoDataInicio.getText().trim();
        if (txt.length() < 10) {
            labelDataInicioErro.setText(" ");
            return;
        }
        try {
            LocalDate data = LocalDate.parse(txt, FMT);
            LocalDate hoje = LocalDate.now();
            if (data.isBefore(hoje)) {
                long dias = hoje.toEpochDay() - data.toEpochDay();
                labelDataInicioErro.setText(
                        "⚠ R4: " + dias + " dia(s) no passado. Use hoje ou data futura.");
                labelDataInicioErro.setForeground(Tema.PERIGO);
            } else {
                labelDataInicioErro.setText("✓ Data válida");
                labelDataInicioErro.setForeground(Tema.SUCESSO);
            }
        } catch (DateTimeParseException e) {
            labelDataInicioErro.setText("⚠ Formato inválido — use dd/MM/yyyy");
            labelDataInicioErro.setForeground(Tema.AVISO);
        }
    }

    private void salvar() {
        try {
            // Validar e parsear campos
            int numero = Integer.parseInt(campoNumero.getText().trim());
            LocalDate dataInicio  = LocalDate.parse(campoDataInicio.getText().trim(), FMT);
            LocalDate dataTermino = LocalDate.parse(campoDataTermino.getText().trim(), FMT);
            String descricao = campoDescricao.getText().trim();

            Projetista p = (Projetista) comboProjetista.getSelectedItem();
            Cliente    c = (Cliente)    comboCliente.getSelectedItem();

            if (p == null) throw new ValidacaoException("projetista", "Selecione um projetista.");
            if (c == null) throw new ValidacaoException("cliente", "Selecione um cliente.");

            Projeto projeto = new Projeto(numero, dataInicio, dataTermino, descricao, p, c);

            // ── Serviço aplica R4 (DataInicioValidator) internamente ──
            projetoService.registrar(projeto);

            mostrarStatus("✓  Projeto #" + numero + " registrado com sucesso.", Tema.SUCESSO);
            limpar();
            carregarTabela();

        } catch (NumberFormatException e) {
            mostrarStatus("⚠  Número do projeto inválido.", Tema.PERIGO);
        } catch (DateTimeParseException e) {
            mostrarStatus("⚠  Data inválida. Use dd/MM/yyyy.", Tema.PERIGO);
        } catch (ValidacaoException e) {
            // ── Exibe a mensagem de negócio (incluindo R4 do backend) ──
            mostrarStatus("⚠  " + e.getMessage(), Tema.PERIGO);
            if ("dataInicio".equals(e.getCampo())) {
                labelDataInicioErro.setText("⚠ R4: " + e.getMessage());
                labelDataInicioErro.setForeground(Tema.PERIGO);
            }
        } catch (Exception e) {
            mostrarStatus("⚠  " + e.getMessage(), Tema.PERIGO);
        }
    }

    private void remover() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) { mostrarStatus("Selecione um projeto.", Tema.AVISO); return; }
        Long id = (Long) modeloTabela.getValueAt(linha, 0);
        int c = JOptionPane.showConfirmDialog(this,
                "Remover o projeto selecionado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (c != JOptionPane.YES_OPTION) return;
        try {
            projetoService.remover(id);
            carregarTabela();
            mostrarStatus("✓  Projeto removido.", Tema.SUCESSO);
        } catch (Exception e) {
            mostrarStatus("⚠  " + e.getMessage(), Tema.PERIGO);
        }
    }

    private void carregarCombos() {
        comboProjetista.removeAllItems();
        comboCliente.removeAllItems();
        try {
            projetistaService.listarTodos().forEach(comboProjetista::addItem);
            clienteService.listarTodos().forEach(comboCliente::addItem);
        } catch (Exception e) {
            mostrarStatus("⚠  Erro ao carregar dados: " + e.getMessage(), Tema.PERIGO);
        }
    }

    private void carregarTabela() {
        modeloTabela.setRowCount(0);
        try {
            for (Projeto p : projetoService.listarTodos()) {
                modeloTabela.addRow(new Object[]{
                        p.getId(), p.getNumero(),
                        p.getDataInicio().format(FMT),
                        p.getDataTermino().format(FMT),
                        p.getProjetista().getNome(),
                        p.getCliente().getNome(),
                        p.getDescricao()
                });
            }
        } catch (Exception e) {
            mostrarStatus("⚠  " + e.getMessage(), Tema.PERIGO);
        }
    }

    /** Chamado pela aba ao ser selecionada para recarregar combos. */
    public void aoAtivar() {
        carregarCombos();
        carregarTabela();
    }

    private void limpar() {
        campoNumero.setText("");
        campoDescricao.setText("");
        campoDataInicio.setText("");
        campoDataTermino.setText("");
        labelDataInicioErro.setText(" ");
        campoNumero.requestFocus();
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
        tabela.getColumnModel().getColumn(1).setMaxWidth(60);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
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

    private void estilizarCombo(JComboBox<?> combo) {
        combo.setFont(Tema.FONTE_CAMPO);
        combo.setBackground(Tema.BG_CAMPO);
        combo.setForeground(Tema.TEXTO_CAMPO);
        combo.setBorder(BorderFactory.createLineBorder(Tema.BORDA, 1));
        combo.setPreferredSize(new Dimension(combo.getPreferredSize().width, 34));
    }

    private void mostrarStatus(String msg, Color cor) {
        labelStatus.setText(msg);
        labelStatus.setForeground(cor);
    }
}