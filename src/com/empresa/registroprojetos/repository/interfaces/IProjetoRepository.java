package com.empresa.registroprojetos.repository.interfaces;

import com.empresa.registroprojetos.domain.Projeto;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * SOLID — DIP + ISP: Abstração coesa para acesso a dados de Projeto.
 */
public interface IProjetoRepository {
    Projeto           salvar(Projeto p)          throws SQLException;
    Optional<Projeto> buscarPorId(long id)       throws SQLException;
    List<Projeto>     buscarTodos()              throws SQLException;
    void              deletar(long id)           throws SQLException;
    boolean           existePorNumero(int numero) throws SQLException;
}