package com.empresa.registroprojetos.service.validator;

import com.empresa.registroprojetos.exception.ValidacaoException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * ══════════════════════════════════════════════════════════════
 * Implementação da Regra de Negócio R4:
 * "A data de início do projeto não pode ser anterior à data corrente."
 *
 * SOLID — SRP: Esta classe existe APENAS para esta validação.
 *              Qualquer outro comportamento pertence a outra classe.
 *
 * IoC sem framework: instanciada em AppContext e injetada em
 * ProjetoServiceImpl via construtor — desacoplamento total.
 * ══════════════════════════════════════════════════════════════
 */
public class DataInicioValidator implements IDataInicioValidator {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void validar(LocalDate dataInicio) throws ValidacaoException {
        if (dataInicio == null) {
            throw new ValidacaoException("dataInicio", "Data de início é obrigatória.");
        }

        LocalDate hoje = LocalDate.now();

        // ── Regra R4 ─────────────────────────────────────────────────
        if (dataInicio.isBefore(hoje)) {
            throw new ValidacaoException("dataInicio",
                    "Data de início (" + dataInicio.format(FMT) + ") não pode ser "
                            + "anterior à data corrente (" + hoje.format(FMT) + ").");
        }
    }
}