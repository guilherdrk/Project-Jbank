package com.guilherdrk.jbank.controller;

import com.guilherdrk.jbank.dto.CreateWalletDTO;
import com.guilherdrk.jbank.dto.DepositMoneyDTO;
import com.guilherdrk.jbank.exception.WalletDataAlreadyExistsException;
import com.guilherdrk.jbank.model.WalletEntity;
import com.guilherdrk.jbank.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

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

    @DeleteMapping("/{walletId}")
    public ResponseEntity<Void> deleteWallet(@PathVariable UUID walletId){
        var deleted = walletService.deleteWallet(walletId);
        return deleted ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @PostMapping("/{walletId}/deposits")
    public ResponseEntity<Void> depositMoney(@PathVariable UUID walletId,
                                             @RequestBody @Valid DepositMoneyDTO dto,
                                             HttpServletRequest servletRequest){
        walletService.depositMoney(walletId, dto, servletRequest.getAttribute("x-user-ip").toString());

        return ResponseEntity.ok().build();
    }

}
