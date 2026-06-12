package com.empresa.registroprojetos.ui.components;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Componentes visuais reutilizáveis da aplicação.
 * SOLID — SRP: Cada classe interna tem uma única responsabilidade visual.
 * SOLID — OCP: Novos componentes são adicionados — existentes não mudam.
 */
public final class Componentes {

    private Componentes() {}

    // ── Botão principal estilizado ────────────────────────────────
    public static class BotaoPrimario extends JButton {
        private boolean hover = false;

        public BotaoPrimario(String texto) {
            super(texto);
            setFont(Tema.FONTE_BTN);
            setForeground(Color.WHITE);
            setBackground(Tema.ACENTO);
            setBorderPainted(false);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(getPreferredSize().width + 32, 38));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color bg = isEnabled()
                    ? (hover ? Tema.ACENTO_HOVER : Tema.ACENTO)
                    : new Color(0x404050);
            g2.setColor(bg);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Botão secundário (ghost) ──────────────────────────────────
    public static class BotaoSecundario extends JButton {
        private boolean hover = false;

        public BotaoSecundario(String texto) {
            super(texto);
            setFont(Tema.FONTE_BTN);
            setForeground(Tema.TEXTO_MUTED);
            setBackground(Tema.BG_CAMPO);
            setBorderPainted(false);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(getPreferredSize().width + 24, 38));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hover ? new Color(0x2A2A32) : Tema.BG_CAMPO);
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
            g2.setColor(Tema.BORDA);
            g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 10, 10));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Botão de perigo (remover) ─────────────────────────────────
    public static class BotaoPerigo extends JButton {
        private boolean hover = false;

        public BotaoPerigo(String texto) {
            super(texto);
            setFont(Tema.FONTE_BTN);
            setForeground(Tema.PERIGO);
            setBackground(Tema.BG_CAMPO);
            setBorderPainted(false);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hover ? new Color(0x3A1A1A) : new Color(0x2A1010));
            g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ── Campo de texto estilizado ─────────────────────────────────
    public static JTextField campoTexto(String placeholder, int colunas) {
        JTextField field = new JTextField(colunas) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.BG_CAMPO);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g3 = (Graphics2D) g.create();
                    g3.setColor(Tema.TEXTO_MUTED);
                    g3.setFont(Tema.FONTE_CAMPO.deriveFont(Font.ITALIC));
                    FontMetrics fm = g3.getFontMetrics();
                    g3.drawString(placeholder, 10, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                    g3.dispose();
                }
            }
        };
        field.setFont(Tema.FONTE_CAMPO);
        field.setForeground(Tema.TEXTO_CAMPO);
        field.setBackground(Tema.BG_CAMPO);
        field.setCaretColor(Tema.ACENTO);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Tema.BORDA, 1, true) {
                    @Override
                    public boolean isBorderOpaque() { return false; }
                },
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        field.setOpaque(false);
        return field;
    }

    // ── Label de seção ────────────────────────────────────────────
    public static JLabel labelSecao(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Tema.FONTE_SECTION);
        l.setForeground(Tema.TEXTO);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return l;
    }

    // ── Label de campo ────────────────────────────────────────────
    public static JLabel labelCampo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Tema.FONTE_LABEL.deriveFont(Font.BOLD));
        l.setForeground(Tema.TEXTO_MUTED);
        return l;
    }

    // ── Painel com fundo arredondado ──────────────────────────────
    public static JPanel painelCard() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Tema.BG_PAINEL);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.setColor(Tema.BORDA);
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth()-1, getHeight()-1, 14, 14));
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return p;
    }

    // ── Separador ─────────────────────────────────────────────────
    public static JSeparator separador() {
        JSeparator sep = new JSeparator();
        sep.setForeground(Tema.BORDA);
        sep.setBackground(Tema.BG_PAINEL);
        return sep;
    }

    // ── Painel de alerta ──────────────────────────────────────────
    public static JPanel alertaErro(String mensagem) {
        return criarAlerta(mensagem, new Color(0x3A1010), Tema.PERIGO, "⚠  ");
    }

    public static JPanel alertaSucesso(String mensagem) {
        return criarAlerta(mensagem, new Color(0x0A2A20), Tema.SUCESSO, "✓  ");
    }

    private static JPanel criarAlerta(String msg, Color bg, Color fg, String icone) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
            }
        };
        p.setOpaque(false);
        JLabel lbl = new JLabel(icone + msg);
        lbl.setFont(Tema.FONTE_LABEL);
        lbl.setForeground(fg);
        p.add(lbl);
        return p;
    }
}