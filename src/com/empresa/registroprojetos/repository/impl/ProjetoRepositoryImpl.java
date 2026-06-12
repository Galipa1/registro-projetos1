package com.empresa.registroprojetos.repository.impl;

import com.empresa.registroprojetos.domain.Cliente;
import com.empresa.registroprojetos.domain.Projetista;
import com.empresa.registroprojetos.domain.Projeto;
import com.empresa.registroprojetos.repository.interfaces.IProjetoRepository;
import com.empresa.registroprojetos.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SOLID — SRP: Persiste apenas dados de Projeto.
 * SOLID — DIP: Implementa IProjetoRepository.
 *
 * Usa JOIN para carregar Projetista e Cliente em uma única query
 * evitando o problema N+1.
 */
public class ProjetoRepositoryImpl implements IProjetoRepository {

    private static final String SELECT_COMPLETO = """
        SELECT p.id, p.numero, p.data_inicio, p.data_termino, p.descricao,
               pr.id as pr_id, pr.codigo_funcional, pr.nome as pr_nome,
               pr.data_nascimento as pr_dn,
               cl.id as cl_id, cl.cpf, cl.nome as cl_nome,
               cl.data_nascimento as cl_dn
        FROM projeto p
        JOIN projetista pr ON pr.id = p.projetista_id
        JOIN cliente    cl ON cl.id = p.cliente_id
        """;

    @Override
    public Projeto salvar(Projeto p) throws SQLException {
        if (p.getId() == null) return inserir(p);
        else                    return atualizar(p);
    }

    private Projeto inserir(Projeto p) throws SQLException {
        String sql = """
            INSERT INTO projeto (numero, data_inicio, data_termino,
                                 descricao, projetista_id, cliente_id)
            VALUES (?,?,?,?,?,?)
            """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1,    p.getNumero());
            ps.setString(2, p.getDataInicio().toString());
            ps.setString(3, p.getDataTermino().toString());
            ps.setString(4, p.getDescricao());
            ps.setLong(5,   p.getProjetista().getId());
            ps.setLong(6,   p.getCliente().getId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getLong(1));
            }
        }
        return p;
    }

    private Projeto atualizar(Projeto p) throws SQLException {
        String sql = """
            UPDATE projeto SET numero=?, data_inicio=?, data_termino=?,
                               descricao=?, projetista_id=?, cliente_id=?
            WHERE id=?
            """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1,    p.getNumero());
            ps.setString(2, p.getDataInicio().toString());
            ps.setString(3, p.getDataTermino().toString());
            ps.setString(4, p.getDescricao());
            ps.setLong(5,   p.getProjetista().getId());
            ps.setLong(6,   p.getCliente().getId());
            ps.setLong(7,   p.getId());
            ps.executeUpdate();
        }
        return p;
    }

    @Override
    public Optional<Projeto> buscarPorId(long id) throws SQLException {
        String sql = SELECT_COMPLETO + " WHERE p.id = ?";
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
    public List<Projeto> buscarTodos() throws SQLException {
        List<Projeto> lista = new ArrayList<>();
        String sql = SELECT_COMPLETO + " ORDER BY p.numero";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) lista.add(mapear(rs));
        }
        return lista;
    }

    @Override
    public void deletar(long id) throws SQLException {
        String sql = "DELETE FROM projeto WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean existePorNumero(int numero) throws SQLException {
        String sql = "SELECT COUNT(*) FROM projeto WHERE numero = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, numero);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private Projeto mapear(ResultSet rs) throws SQLException {
        Projetista pr = new Projetista();
        pr.setId(rs.getLong("pr_id"));
        pr.setCodigoFuncional(rs.getString("codigo_funcional"));
        pr.setNome(rs.getString("pr_nome"));
        pr.setDataNascimento(LocalDate.parse(rs.getString("pr_dn")));

        Cliente cl = new Cliente();
        cl.setId(rs.getLong("cl_id"));
        cl.setCpf(rs.getString("cpf"));
        cl.setNome(rs.getString("cl_nome"));
        cl.setDataNascimento(LocalDate.parse(rs.getString("cl_dn")));

        Projeto p = new Projeto();
        p.setId(rs.getLong("id"));
        p.setNumero(rs.getInt("numero"));
        p.setDataInicio(LocalDate.parse(rs.getString("data_inicio")));
        p.setDataTermino(LocalDate.parse(rs.getString("data_termino")));
        p.setDescricao(rs.getString("descricao"));
        p.setProjetista(pr);
        p.setCliente(cl);
        return p;
    }
}