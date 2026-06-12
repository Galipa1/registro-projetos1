package com.empresa.registroprojetos.repository.interfaces;

import com.empresa.registroprojetos.domain.Projetista;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * SOLID — DIP: Serviços dependem desta interface, nunca da
 *              implementação JDBC concreta.
 *              Permite trocar SQLite por MySQL sem alterar
 *              nenhuma linha dos serviços.
 *
 * SOLID — ISP: Interface coesa — apenas operações de Projetista.
 */
public interface IProjetistaRepository {
    Projetista       salvar(Projetista p)            throws SQLException;
    Optional<Projetista> buscarPorId(long id)        throws SQLException;
    List<Projetista> buscarTodos()                   throws SQLException;
    void             deletar(long id)                throws SQLException;
    boolean          existePorCodigo(String codigo)  throws SQLException;
}