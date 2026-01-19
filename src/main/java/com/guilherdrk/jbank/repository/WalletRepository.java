package com.guilherdrk.jbank.repository;

import com.guilherdrk.jbank.model.WalletEntity;
import com.guilherdrk.jbank.repository.dto.StatementeView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<WalletEntity, UUID> {

    String SQL_STATEMENT = """
        SELECT
             BIN_TO_UUID(transfer_id) as statement_id,
             "transfer" as type,
             transfer_value as statement_value,
             BIN_TO_UUID(wallet_receiver_id) as wallet_receiver,
             BIN_TO_UUID(wallet_sender_id) as wallet_sender,
             transfer_date_time as statement_date_time
         FROM
             tb_transfers
         WHERE wallet_receiver_id = ?1 OR wallet_sender_id = ?1  -- Removido UUID_TO_BIN
         UNION ALL
         SELECT
             BIN_TO_UUID(deposit_id) as statement_id,
             "deposit" as type,
             deposit_value as statement_value,
             BIN_TO_UUID(wallet_id) as wallet_receiver,
             "" as wallet_sender,
             deposit_date_time as statement_date_time
         FROM tb_deposit
         WHERE
             wallet_id = ?1  -- Removido UUID_TO_BIN
        """;

    String SQL_COUNT_STATEMENT = """
            SELECT COUNT(*) FROM
            (
            ""\" + SQL_STATEMENT + ""\"
            ) as total
            """;

    Optional<WalletEntity> findByCpfAndEmail(String cpf, String email);

    @Query(value = SQL_STATEMENT, countQuery = SQL_COUNT_STATEMENT, nativeQuery = true)
    Page<StatementeView> findStatements(UUID walletId, PageRequest pageRequest);
}
