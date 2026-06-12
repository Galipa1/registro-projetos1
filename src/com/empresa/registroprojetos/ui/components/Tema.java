package com.empresa.registroprojetos.ui.components;

import java.awt.*;

/**
 * Define a paleta de cores e estilos da aplicação.
 * SOLID — SRP: Responsabilidade única — configuração visual.
 *
 * Tema: Dark modern (inspirado em IDEs modernas).
 */
public final class Tema {

    private Tema() {}

    // ── Cores de fundo ─────────────────────────────────────────────
    public static final Color BG_JANELA    = new Color(0x0E0E10);
    public static final Color BG_PAINEL    = new Color(0x17171A);
    public static final Color BG_CAMPO     = new Color(0x1F1F24);
    public static final Color BG_CABECALHO = new Color(0x141418);
    public static final Color BG_LINHA_PAR = new Color(0x1A1A1F);
    public static final Color BG_LINHA_SEL = new Color(0x2A2A40);

    // ── Bordas ─────────────────────────────────────────────────────
    public static final Color BORDA        = new Color(0x2E2E36);
    public static final Color BORDA_FOCO   = new Color(0x6E6EC0);

    // ── Texto ──────────────────────────────────────────────────────
    public static final Color TEXTO        = new Color(0xF0EFE8);
    public static final Color TEXTO_MUTED  = new Color(0x7A7A82);
    public static final Color TEXTO_CAMPO  = new Color(0xE8E8E0);

    // ── Acento e estados ──────────────────────────────────────────
    public static final Color ACENTO       = new Color(0x7C6AF5);
    public static final Color ACENTO_HOVER = new Color(0x6A58E0);
    public static final Color SUCESSO      = new Color(0x5FD4A8);
    public static final Color PERIGO       = new Color(0xFF6B6B);
    public static final Color AVISO        = new Color(0xF5C842);

    // ── Fontes ────────────────────────────────────────────────────
    public static final Font FONTE_TITULO  = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONTE_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONTE_CAMPO   = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONTE_BTN     = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONTE_TABELA  = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONTE_MONO    = new Font("Consolas", Font.PLAIN, 12);
    public static final Font FONTE_SECTION = new Font("Segoe UI", Font.BOLD, 14);

    /** Aplica o Look & Feel ao Swing antes de criar qualquer janela. */
    public static void aplicar() {
        try {
            UIManagerConfig.configurar();
        } catch (Exception e) {
            // Fallback silencioso
        }
    }
}