package com.guilherdrk.jbank.controller;

import com.guilherdrk.jbank.dto.TransferMoneyDTO;
import com.guilherdrk.jbank.service.TransferService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<Void> transfer(@RequestBody @Valid TransferMoneyDTO dto){

        transferService.trasferMoney(dto);

        return ResponseEntity.ok().build();

    }
}
