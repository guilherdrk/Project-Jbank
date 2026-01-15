package com.guilherdrk.jbank.repository;

import com.guilherdrk.jbank.model.DepositEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepositRepository extends JpaRepository<DepositEntity, UUID> {
}
