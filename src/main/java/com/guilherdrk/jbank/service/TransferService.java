package com.guilherdrk.jbank.service;

import com.guilherdrk.jbank.dto.TransferMoneyDTO;
import com.guilherdrk.jbank.exception.TransferException;
import com.guilherdrk.jbank.exception.WalletNotFoundException;
import com.guilherdrk.jbank.model.TransferEntity;
import com.guilherdrk.jbank.repository.TransferRepository;
import com.guilherdrk.jbank.repository.WalletRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    public TransferService(TransferRepository transferRepository, WalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void trasferMoney(@Valid TransferMoneyDTO dto) {

        var receiver = walletRepository.findById(dto.receiver())
                .orElseThrow(() -> new WalletNotFoundException("receiver does not exist"));

        var sender = walletRepository.findById(dto.sender())
                .orElseThrow(() -> new WalletNotFoundException("sender does not exist"));


        if(sender.getBalance().compareTo(dto.value()) == -1){
            throw new TransferException("insufficient balance. you current balance is $" + sender.getBalance());
        }

        TransferEntity transfer = new TransferEntity();
        transfer.setReceiver(receiver);
        transfer.setSender(sender);
        transfer.setTransferValue(dto.value());
        transfer.setTransferDateTime(LocalDateTime.now());
        transferRepository.save(transfer);

        sender.setBalance(sender.getBalance().subtract(dto.value()));
        receiver.setBalance(receiver.getBalance().add(dto.value()));
        walletRepository.save(sender);
        walletRepository.save(receiver);



    }
}
