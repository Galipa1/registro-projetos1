package com.empresa.registroprojetos.service.interfaces;

import com.empresa.registroprojetos.domain.Projetista;
import com.empresa.registroprojetos.exception.RecursoNaoEncontradoException;
import com.empresa.registroprojetos.exception.ValidacaoException;
import java.sql.SQLException;
import java.util.List;

/**
 * SOLID — DIP: UI depende desta interface, nunca da implementação.
 * SOLID — ISP: Métodos coesos ao domínio de Projetista apenas.
 */
public interface IProjetistaService {
    Projetista       cadastrar(Projetista p)  throws ValidacaoException, SQLException;
    Projetista       buscarPorId(long id)     throws RecursoNaoEncontradoException, SQLException;
    List<Projetista> listarTodos()            throws SQLException;
    void             remover(long id)         throws RecursoNaoEncontradoException, SQLException;
}