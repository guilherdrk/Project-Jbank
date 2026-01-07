package com.guilherdrk.jbank.service;

import com.guilherdrk.jbank.dto.CreateWalletDTO;
import com.guilherdrk.jbank.exception.WalletDataAlreadyExistsException;
import com.guilherdrk.jbank.model.WalletEntity;
import com.guilherdrk.jbank.repository.WalletRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
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
}
