package com.empresa.registroprojetos.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gerencia a conexão JDBC com o banco SQLite.
 *
 * Usamos SQLite para que o sistema funcione sem instalação
 * de servidor — o arquivo .db é criado automaticamente.
 *
 * SOLID — SRP: Responsável apenas por fornecer conexões.
 *              Nenhuma lógica de negócio aqui.
 */
public class DatabaseManager {

    // SQLite cria o arquivo automaticamente na pasta do executável
    private static final String URL = "jdbc:sqlite:registro_projetos.db";

    private DatabaseManager() {} // utilitário — não instanciar

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /**
     * Cria as tabelas se ainda não existirem.
     * Chamado uma única vez na inicialização do app (Main).
     */
    public static void inicializarBanco() {
        String sqlProjetista = """
            CREATE TABLE IF NOT EXISTS projetista (
                id               INTEGER PRIMARY KEY AUTOINCREMENT,
                codigo_funcional TEXT    NOT NULL UNIQUE,
                nome             TEXT    NOT NULL,
                data_nascimento  TEXT    NOT NULL
            )
            """;

        String sqlCliente = """
            CREATE TABLE IF NOT EXISTS cliente (
                id              INTEGER PRIMARY KEY AUTOINCREMENT,
                cpf             TEXT    NOT NULL UNIQUE,
                nome            TEXT    NOT NULL,
                data_nascimento TEXT    NOT NULL
            )
            """;

        String sqlProjeto = """
            CREATE TABLE IF NOT EXISTS projeto (
                id            INTEGER PRIMARY KEY AUTOINCREMENT,
                numero        INTEGER NOT NULL UNIQUE,
                data_inicio   TEXT    NOT NULL,
                data_termino  TEXT    NOT NULL,
                descricao     TEXT    NOT NULL,
                projetista_id INTEGER NOT NULL REFERENCES projetista(id),
                cliente_id    INTEGER NOT NULL REFERENCES cliente(id)
            )
            """;

        try (Connection conn = getConnection();
             Statement stmt  = conn.createStatement()) {

            stmt.execute("PRAGMA foreign_keys = ON");
            stmt.execute(sqlProjetista);
            stmt.execute(sqlCliente);
            stmt.execute(sqlProjeto);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar banco de dados: " + e.getMessage(), e);
        }
    }
}