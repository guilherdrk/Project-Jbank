package com.guilherdrk.jbank.repository;

import com.guilherdrk.jbank.model.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

    Optional<WalletEntity> findByCpfAndEmail(String cpf, String email);
}
