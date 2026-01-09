package com.guilherdrk.jbank.controller;

import com.guilherdrk.jbank.dto.CreateWalletDTO;
import com.guilherdrk.jbank.exception.WalletDataAlreadyExistsException;
import com.guilherdrk.jbank.model.WalletEntity;
import com.guilherdrk.jbank.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = "/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Void> createWallet(@RequestBody @Valid CreateWalletDTO dto){
        var wallet = walletService.createWallet(dto);
        return ResponseEntity.created(URI.create("/wallets/" + wallet.getWalletId().toString())).build();
    }
    

}
