package com.empresa.registroprojetos.service.validator;

import com.empresa.registroprojetos.exception.ValidacaoException;
import java.time.LocalDate;

/**
 * ══════════════════════════════════════════════════════════════
 * SOLID — SRP: Interface com único propósito — validar data
 *              de início. Nenhuma outra responsabilidade.
 *
 * SOLID — DIP: ProjetoServiceImpl depende desta interface,
 *              permitindo trocar a regra (ex: testes com mock)
 *              sem modificar o serviço.
 *
 * SOLID — OCP: Novas regras (ex: bloquear finais de semana)
 *              criam nova implementação — sem alterar código.
 * ══════════════════════════════════════════════════════════════
 */
public interface IDataInicioValidator {
    /**
     * Valida a regra R4: data de início não pode ser anterior
     * à data corrente.
     *
     * @param dataInicio data informada pelo usuário
     * @throws ValidacaoException se a data for inválida
     */
    void validar(LocalDate dataInicio) throws ValidacaoException;
}