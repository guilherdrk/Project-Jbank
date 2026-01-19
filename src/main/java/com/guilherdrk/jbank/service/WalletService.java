package com.guilherdrk.jbank.service;

import com.guilherdrk.jbank.dto.CreateWalletDTO;
import com.guilherdrk.jbank.dto.DepositMoneyDTO;
import com.guilherdrk.jbank.exception.DeleteWalletException;
import com.guilherdrk.jbank.exception.WalletDataAlreadyExistsException;
import com.guilherdrk.jbank.exception.WalletNotFoundException;
import com.guilherdrk.jbank.model.DepositEntity;
import com.guilherdrk.jbank.model.WalletEntity;
import com.guilherdrk.jbank.repository.DepositRepository;
import com.guilherdrk.jbank.repository.WalletRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

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
