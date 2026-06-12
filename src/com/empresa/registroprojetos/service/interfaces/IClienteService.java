package com.empresa.registroprojetos.service.interfaces;

import com.empresa.registroprojetos.domain.Cliente;
import com.empresa.registroprojetos.exception.RecursoNaoEncontradoException;
import com.empresa.registroprojetos.exception.ValidacaoException;
import java.sql.SQLException;
import java.util.List;

/**
 * SOLID — DIP + ISP: Abstração coesa para operações de Cliente.
 */
public interface IClienteService {
    Cliente       cadastrar(Cliente c)  throws ValidacaoException, SQLException;
    Cliente       buscarPorId(long id)  throws RecursoNaoEncontradoException, SQLException;
    List<Cliente> listarTodos()         throws SQLException;
    void          remover(long id)      throws RecursoNaoEncontradoException, SQLException;
}