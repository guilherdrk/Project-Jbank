package com.guilherdrk.jbank.dto;

public record PaginationDTO(Integer page,
                            Integer pageSize,
                            Long totalElements,
                            Integer totalPages) {
}
