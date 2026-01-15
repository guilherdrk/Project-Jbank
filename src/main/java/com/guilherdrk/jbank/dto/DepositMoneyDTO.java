package com.guilherdrk.jbank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DepositMoneyDTO(@NotNull @DecimalMin("10.00") BigDecimal value) {
}
