package com.empresa.registroprojetos.ui;

import com.empresa.registroprojetos.config.AppContext;
import com.empresa.registroprojetos.ui.components.Tema;
import com.empresa.registroprojetos.ui.panels.PainelCliente;
import com.empresa.registroprojetos.ui.panels.PainelProjeto;
import com.empresa.registroprojetos.ui.panels.PainelProjetista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Janela principal da aplicação.
 *
 * SOLID — SRP: Monta e exibe as abas. Não contém lógica de negócio.
 * SOLID — DIP: Recebe AppContext e extrai apenas interfaces de serviço.
 *
 * IoC: AppContext é passado como parâmetro — JanelaPrincipal
 *      não cria nenhuma dependência interna.
 */
public class JanelaPrincipal extends JFrame {

    private final AppContext ctx;
    private PainelProjeto painelProjeto;

    public JanelaPrincipal(AppContext ctx) {
        this.ctx = ctx;
        configurarJanela();
        construirUI();
    }

    private void configurarJanela() {
        setTitle("Sistema de Registro de Projetos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Tema.BG_JANELA);

        // Ícone da janela
        try {
            setIconImage(criarIcone());
        } catch (Exception ignored) {}
    }

    private void construirUI() {
        setLayout(new BorderLayout());

        // ── Cabeçalho ─────────────────────────────────────────────
        JPanel header = criarHeader();
        add(header, BorderLayout.NORTH);

        // ── Abas principais ───────────────────────────────────────
        JTabbedPane abas = new JTabbedPane(JTabbedPane.TOP);
        abas.setBackground(Tema.BG_JANELA);
        abas.setForeground(Tema.TEXTO_MUTED);
        abas.setFont(Tema.FONTE_LABEL.deriveFont(Font.BOLD).deriveFont(13f));
        abas.setBorder(BorderFactory.createEmptyBorder());

        // Aba 1 — Projetistas
        PainelProjetista painelProjetista = new PainelProjetista(ctx.getProjetistaService());
        abas.addTab("  Projetistas  ", painelProjetista);

        // Aba 2 — Clientes
        PainelCliente painelCliente = new PainelCliente(ctx.getClienteService());
        abas.addTab("  Clientes  ", painelCliente);

        // Aba 3 — Projetos
        painelProjeto = new PainelProjeto(
                ctx.getProjetoService(),
                ctx.getProjetistaService(),
                ctx.getClienteService()
        );
        abas.addTab("  Projetos  ", painelProjeto);

        // Recarrega combos ao entrar na aba de projetos
        abas.addChangeListener(e -> {
            if (abas.getSelectedIndex() == 2) {
                painelProjeto.aoAtivar();
            }
        });

        add(abas, BorderLayout.CENTER);

        // ── Barra de rodapé ───────────────────────────────────────
        JPanel footer = criarFooter();
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel criarHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Tema.BG_CABECALHO);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Tema.BORDA);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        header.setPreferredSize(new Dimension(0, 56));
        header.setOpaque(false);

        JPanel esquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        esquerda.setOpaque(false);

        // Quadrado colorido como ícone
        JLabel icone = new JLabel("▪") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.ACENTO);
                g2.fillRoundRect(0, 4, 28, 28, 8, 8);
                g2.dispose();
            }
        };
        icone.setPreferredSize(new Dimension(28, 36));

        JLabel titulo = new JLabel("Registro de Projetos");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 17));
        titulo.setForeground(Tema.TEXTO);

        JLabel versao = new JLabel("Sistema 4  ·  v1.0");
        versao.setFont(Tema.FONTE_MONO.deriveFont(11f));
        versao.setForeground(Tema.TEXTO_MUTED);

        esquerda.add(Box.createHorizontalStrut(4));
        esquerda.add(icone);
        esquerda.add(titulo);
        esquerda.add(versao);
        header.add(esquerda, BorderLayout.WEST);

        // Botão de ajuda no header
        JPanel direita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 0));
        direita.setOpaque(false);
        JLabel ajuda = new JLabel("❓ Ajuda");
        ajuda.setFont(Tema.FONTE_LABEL);
        ajuda.setForeground(Tema.TEXTO_MUTED);
        ajuda.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ajuda.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { mostrarAjuda(); }
            public void mouseEntered(MouseEvent e) { ajuda.setForeground(Tema.TEXTO); }
            public void mouseExited(MouseEvent e)  { ajuda.setForeground(Tema.TEXTO_MUTED); }
        });
        direita.add(ajuda);
        header.add(direita, BorderLayout.EAST);

        // Centraliza verticalmente
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        return header;
    }

    private JPanel criarFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 6));
        footer.setBackground(Tema.BG_CABECALHO);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Tema.BORDA));

        JLabel lblBanco = new JLabel("● Banco: SQLite (registro_projetos.db)");
        lblBanco.setFont(Tema.FONTE_MONO.deriveFont(11f));
        lblBanco.setForeground(Tema.SUCESSO);

        JLabel lblSolid = new JLabel("SOLID + IoC/DI aplicados");
        lblSolid.setFont(Tema.FONTE_MONO.deriveFont(11f));
        lblSolid.setForeground(Tema.TEXTO_MUTED);

        footer.add(lblBanco);
        footer.add(new JSeparator(JSeparator.VERTICAL) {{
            setForeground(Tema.BORDA);
            setPreferredSize(new Dimension(1, 14));
        }});
        footer.add(lblSolid);

        return footer;
    }

    private void mostrarAjuda() {
        String msg = """
            SISTEMA DE REGISTRO DE PROJETOS
            ─────────────────────────────────────────
            R1 — Aba Projetistas: cadastre com código funcional, nome e data de nascimento.
            R2 — Aba Clientes: cadastre com CPF (11 dígitos), nome e data de nascimento.
            R3 — Aba Projetos: registre vinculando projetista e cliente já cadastrados.
            R4 — A data de início não pode ser anterior à data atual.
                 O sistema valida em tempo real e bloqueia o registro se inválida.
            
            Banco de dados: SQLite — arquivo criado automaticamente.
            Arquitetura: SOLID + IoC/DI manual (sem framework).
            """;
        JTextArea area = new JTextArea(msg);
        area.setEditable(false);
        area.setFont(Tema.FONTE_MONO.deriveFont(12f));
        area.setBackground(Tema.BG_CAMPO);
        area.setForeground(Tema.TEXTO);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JOptionPane.showMessageDialog(this, area, "Ajuda", JOptionPane.INFORMATION_MESSAGE);
    }

    private Image criarIcone() {
        // Ícone programático simples
        int s = 32;
        java.awt.image.BufferedImage img =
                new java.awt.image.BufferedImage(s, s, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Tema.ACENTO);
        g2.fillRoundRect(0, 0, s, s, 10, 10);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2.drawString("P", 9, 23);
        g2.dispose();
        return img;
    }
}