package com.empresa.registroprojetos.repository.impl;

import com.empresa.registroprojetos.domain.Projetista;
import com.empresa.registroprojetos.repository.interfaces.IProjetistaRepository;
import com.empresa.registroprojetos.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementação JDBC do repositório de Projetista.
 *
 * SOLID — SRP: Responsável apenas por persistência de Projetista.
 * SOLID — DIP: Implementa IProjetistaRepository (abstração).
 *              Os serviços nunca instanciam esta classe diretamente —
 *              recebem a interface via Injeção de Dependência manual
 *              configurada em AppContext (IoC sem framework).
 */
public class ProjetistaRepositoryImpl implements IProjetistaRepository {

    @Override
    public Projetista salvar(Projetista p) throws SQLException {
        if (p.getId() == null) {
            return inserir(p);
        } else {
            return atualizar(p);
        }
    }

    private Projetista inserir(Projetista p) throws SQLException {
        String sql = "INSERT INTO projetista (codigo_funcional, nome, data_nascimento) VALUES (?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getCodigoFuncional());
            ps.setString(2, p.getNome());
            ps.setString(3, p.getDataNascimento().toString());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getLong(1));
            }
        }
        return p;
    }

    private Projetista atualizar(Projetista p) throws SQLException {
        String sql = "UPDATE projetista SET codigo_funcional=?, nome=?, data_nascimento=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getCodigoFuncional());
            ps.setString(2, p.getNome());
            ps.setString(3, p.getDataNascimento().toString());
            ps.setLong(4, p.getId());
            ps.executeUpdate();
        }
        return p;
    }

    @Override
    public Optional<Projetista> buscarPorId(long id) throws SQLException {
        String sql = "SELECT * FROM projetista WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapear(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Projetista> buscarTodos() throws SQLException {
        List<Projetista> lista = new ArrayList<>();
        String sql = "SELECT * FROM projetista ORDER BY nome";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    @Override
    public void deletar(long id) throws SQLException {
        String sql = "DELETE FROM projetista WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean existePorCodigo(String codigo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM projetista WHERE codigo_funcional = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /** Converte ResultSet → Projetista. Centralizado para evitar repetição. */
    private Projetista mapear(ResultSet rs) throws SQLException {
        Projetista p = new Projetista();
        p.setId(rs.getLong("id"));
        p.setCodigoFuncional(rs.getString("codigo_funcional"));
        p.setNome(rs.getString("nome"));
        p.setDataNascimento(LocalDate.parse(rs.getString("data_nascimento")));
        return p;
    }
}