package com.guilherdrk.jbank.repository;

import com.guilherdrk.jbank.model.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferRepository extends JpaRepository<TransferEntity, UUID> {}
