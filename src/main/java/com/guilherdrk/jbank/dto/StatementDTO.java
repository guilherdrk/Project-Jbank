package com.guilherdrk.jbank.dto;

import java.util.List;

public record StatementDTO(WalletDTO wallet,
                           List<StatementItemDTO> statements,
                           PaginationDTO pagination) {
}
