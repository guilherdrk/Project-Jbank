package com.guilherdrk.jbank.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDTO(UUID walletId,
                        String cpf,
                        String name,
                        String email,
                        BigDecimal balance) {
}
