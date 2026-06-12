package com.empresa.registroprojetos.ui.components;

import java.awt.Insets;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Configura o UIManager do Swing com o tema escuro.
 * SOLID — SRP: Única responsabilidade — setup global de UI.
 */
public final class UIManagerConfig {

    private UIManagerConfig() {}

    public static void configurar() {
        UIManager.put("Panel.background",              Tema.BG_PAINEL);
        UIManager.put("OptionPane.background",         Tema.BG_PAINEL);
        UIManager.put("OptionPane.messageForeground",  Tema.TEXTO);
        UIManager.put("Label.foreground",              Tema.TEXTO);
        UIManager.put("Label.background",              Tema.BG_PAINEL);
        UIManager.put("TextField.background",          Tema.BG_CAMPO);
        UIManager.put("TextField.foreground",          Tema.TEXTO_CAMPO);
        UIManager.put("TextField.caretForeground",     Tema.ACENTO);
        UIManager.put("TextField.border",
                BorderFactory.createCompoundBorder(
                        new LineBorder(Tema.BORDA, 1, true),
                        BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        UIManager.put("TextArea.background",           Tema.BG_CAMPO);
        UIManager.put("TextArea.foreground",           Tema.TEXTO_CAMPO);
        UIManager.put("ComboBox.background",           Tema.BG_CAMPO);
        UIManager.put("ComboBox.foreground",           Tema.TEXTO_CAMPO);
        UIManager.put("ComboBox.selectionBackground",  Tema.BG_LINHA_SEL);
        UIManager.put("ComboBox.selectionForeground",  Tema.TEXTO);
        UIManager.put("List.background",               Tema.BG_CAMPO);
        UIManager.put("List.foreground",               Tema.TEXTO_CAMPO);
        UIManager.put("List.selectionBackground",      Tema.BG_LINHA_SEL);
        UIManager.put("List.selectionForeground",      Tema.TEXTO);
        UIManager.put("Table.background",              Tema.BG_PAINEL);
        UIManager.put("Table.foreground",              Tema.TEXTO);
        UIManager.put("Table.selectionBackground",     Tema.BG_LINHA_SEL);
        UIManager.put("Table.selectionForeground",     Tema.TEXTO);
        UIManager.put("Table.gridColor",               Tema.BORDA);
        UIManager.put("TableHeader.background",        Tema.BG_CABECALHO);
        UIManager.put("TableHeader.foreground",        Tema.TEXTO_MUTED);
        UIManager.put("ScrollPane.background",         Tema.BG_PAINEL);
        UIManager.put("Viewport.background",           Tema.BG_PAINEL);
        UIManager.put("TabbedPane.background",         Tema.BG_JANELA);
        UIManager.put("TabbedPane.foreground",         Tema.TEXTO_MUTED);
        UIManager.put("TabbedPane.selected",           Tema.BG_PAINEL);
        UIManager.put("TabbedPane.selectedForeground", Tema.TEXTO);
        UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
        UIManager.put("Button.background",             Tema.ACENTO);
        UIManager.put("Button.foreground",             Tema.TEXTO);
        UIManager.put("Button.focusPainted",           false);
        UIManager.put("PasswordField.background",      Tema.BG_CAMPO);
        UIManager.put("PasswordField.foreground",      Tema.TEXTO_CAMPO);
        UIManager.put("Spinner.background",            Tema.BG_CAMPO);
        UIManager.put("Spinner.foreground",            Tema.TEXTO_CAMPO);
        UIManager.put("FormattedTextField.background", Tema.BG_CAMPO);
        UIManager.put("FormattedTextField.foreground", Tema.TEXTO_CAMPO);
        UIManager.put("SplitPane.background",          Tema.BG_JANELA);
        UIManager.put("SplitPane.dividerSize",         6);
    }
}