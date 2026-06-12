package com.empresa.registroprojetos.repository.impl;

import com.empresa.registroprojetos.domain.Cliente;
import com.empresa.registroprojetos.repository.interfaces.IClienteRepository;
import com.empresa.registroprojetos.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SOLID — SRP: Persiste apenas dados de Cliente.
 * SOLID — DIP: Implementa IClienteRepository.
 */
public class ClienteRepositoryImpl implements IClienteRepository {

    @Override
    public Cliente salvar(Cliente c) throws SQLException {
        if (c.getId() == null) return inserir(c);
        else                    return atualizar(c);
    }

    private Cliente inserir(Cliente c) throws SQLException {
        String sql = "INSERT INTO cliente (cpf, nome, data_nascimento) VALUES (?,?,?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getCpf());
            ps.setString(2, c.getNome());
            ps.setString(3, c.getDataNascimento().toString());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) c.setId(rs.getLong(1));
            }
        }
        return c;
    }

    private Cliente atualizar(Cliente c) throws SQLException {
        String sql = "UPDATE cliente SET cpf=?, nome=?, data_nascimento=? WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getCpf());
            ps.setString(2, c.getNome());
            ps.setString(3, c.getDataNascimento().toString());
            ps.setLong(4, c.getId());
            ps.executeUpdate();
        }
        return c;
    }

    @Override
    public Optional<Cliente> buscarPorId(long id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE id = ?";
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
    public List<Cliente> buscarTodos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente ORDER BY nome";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    @Override
    public void deletar(long id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean existePorCpf(String cpf) throws SQLException {
        String sql = "SELECT COUNT(*) FROM cliente WHERE cpf = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cpf);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getLong("id"));
        c.setCpf(rs.getString("cpf"));
        c.setNome(rs.getString("nome"));
        c.setDataNascimento(LocalDate.parse(rs.getString("data_nascimento")));
        return c;
    }
}