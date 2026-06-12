package com.empresa.registroprojetos.repository.interfaces;

import com.empresa.registroprojetos.domain.Cliente;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * SOLID — DIP + ISP: Abstração coesa para acesso a dados de Cliente.
 */
public interface IClienteRepository {
    Cliente              salvar(Cliente c)           throws SQLException;
    Optional<Cliente>    buscarPorId(long id)        throws SQLException;
    List<Cliente>        buscarTodos()               throws SQLException;
    void                 deletar(long id)            throws SQLException;
    boolean              existePorCpf(String cpf)   throws SQLException;
}