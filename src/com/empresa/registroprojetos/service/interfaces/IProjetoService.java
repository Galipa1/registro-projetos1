package com.empresa.registroprojetos.service.interfaces;

import com.empresa.registroprojetos.domain.Projeto;
import com.empresa.registroprojetos.exception.RecursoNaoEncontradoException;
import com.empresa.registroprojetos.exception.ValidacaoException;
import java.sql.SQLException;
import java.util.List;

/**
 * SOLID — DIP + ISP: Abstração coesa para operações de Projeto.
 */
public interface IProjetoService {
    Projeto       registrar(Projeto p)  throws ValidacaoException, RecursoNaoEncontradoException, SQLException;
    Projeto       buscarPorId(long id)  throws RecursoNaoEncontradoException, SQLException;
    List<Projeto> listarTodos()         throws SQLException;
    void          remover(long id)      throws RecursoNaoEncontradoException, SQLException;
}