package com.guilherdrk.jbank.service;

import com.guilherdrk.jbank.dto.*;
import com.guilherdrk.jbank.exception.DeleteWalletException;
import com.guilherdrk.jbank.exception.StatementException;
import com.guilherdrk.jbank.exception.WalletDataAlreadyExistsException;
import com.guilherdrk.jbank.exception.WalletNotFoundException;
import com.guilherdrk.jbank.model.DepositEntity;
import com.guilherdrk.jbank.model.WalletEntity;
import com.guilherdrk.jbank.repository.DepositRepository;
import com.guilherdrk.jbank.repository.WalletRepository;
import com.guilherdrk.jbank.repository.dto.StatementeView;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final DepositRepository depositRepository;


    public WalletService(WalletRepository walletRepository, DepositRepository depositRepository) {
        this.walletRepository = walletRepository;
        this.depositRepository = depositRepository;
    }

    public StatementDTO getStatements(UUID walletId, Integer page, Integer pageSize) {

        var wallet =walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("there is no wallet with this id"));

        var pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "statement_date_time");

        var statements = walletRepository.findStatements(walletId, pageRequest)
                .map(view -> mapToDTO(walletId, view));

        return new StatementDTO(
                new WalletDTO(wallet.getWalletId(), wallet.getCpf(), wallet.getName(), wallet.getEmail(), wallet.getBalance()),
                statements.getContent(),
                new PaginationDTO(statements.getNumber(), statements.getSize(), statements.getTotalElements(), statements.getTotalPages())
        );

    }

    private StatementItemDTO mapToDTO(UUID walletId, StatementeView view) {

        if (view.getType().equalsIgnoreCase("deposit")){
            return mapToDeposit(view);
        };
        if(view.getType().equalsIgnoreCase("transfer")
                && view.getWalletSender().equalsIgnoreCase(walletId.toString())){
            return mapWhenTransferSend(walletId, view);
        }
        if(view.getType().equalsIgnoreCase("transfer")
                && view.getWalletReceiver().equalsIgnoreCase(walletId.toString())){
            return mapWhenTransferReceived(walletId, view);
        }

        throw new StatementException("invalid type: " + view.getType());
    }

    private StatementItemDTO mapWhenTransferReceived(UUID walletId, StatementeView view) {
        return new StatementItemDTO(
                view.getStatementId(),
                view.getType(),
                "Money received from " + view.getWalletSender(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperationEnum.CREDIT
        );
    }

    private StatementItemDTO mapWhenTransferSend(UUID walletId, StatementeView view) {
        return new StatementItemDTO(
                view.getStatementId(),
                view.getType(),
                "Money send to " + view.getWalletReceiver(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperationEnum.DEBIT
        );
    }

    private static StatementItemDTO mapToDeposit(StatementeView view) {
        return new StatementItemDTO(
                view.getStatementId(),
                view.getType(),
                "Money deposit",
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperationEnum.CREDIT
        );
    }

    public WalletEntity createWallet(CreateWalletDTO dto) {

        var walletDb = walletRepository.findByCpfAndEmail(dto.cpf(), dto.email());

        if(walletDb.isPresent()){
            throw new WalletDataAlreadyExistsException("cfp or email already exists");
        }

        var wallet = new WalletEntity();
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCpf(dto.cpf());
        wallet.setEmail(dto.email());
        wallet.setName(dto.name());

        return walletRepository.save(wallet);

    }

    public Boolean deleteWallet(UUID walletId){
        var wallet = walletRepository.findById(walletId);
        if(wallet.isPresent()){
            if (wallet.get().getBalance().compareTo(BigDecimal.ZERO) != 0){
                throw new DeleteWalletException(
                        "the balance is not zero. The current amount is $" + wallet.get().getBalance());
            }
            walletRepository.deleteById(walletId);
        }
        return wallet.isPresent();

    }

    @Transactional
    public void depositMoney(UUID walletId, @Valid DepositMoneyDTO dto, String ipAddress) {

        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("there is no wallet with this id"));

        var deposit = new DepositEntity();
        deposit.setWallet(wallet);
        deposit.setDepositValue(dto.value());
        deposit.setDepositDateTime(LocalDateTime.now());
        deposit.setIpAddress(ipAddress);

        depositRepository.save(deposit);

        wallet.setBalance(wallet.getBalance().add(dto.value()));
        walletRepository.save(wallet);


    }
}
