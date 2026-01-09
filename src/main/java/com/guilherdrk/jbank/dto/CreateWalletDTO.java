package com.guilherdrk.jbank.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record CreateWalletDTO(@CPF @NotNull String cpf,
                              @Email @NotNull String email,
                              @NotNull String name) {
}
