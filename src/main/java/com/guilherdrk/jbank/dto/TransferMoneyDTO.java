package com.guilherdrk.jbank.dto;

import com.guilherdrk.jbank.model.WalletEntity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferMoneyDTO(@NotNull UUID sender,
                               @NotNull @DecimalMin("0.01") BigDecimal value,
                               @NotNull UUID receiver) {
}
