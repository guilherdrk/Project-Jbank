package com.guilherdrk.jbank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StatementItemDTO(String statementId,
                               String type,
                               String literal,
                               BigDecimal value,
                               LocalDateTime dateTime,
                               StatementOperationEnum operation) {
}
