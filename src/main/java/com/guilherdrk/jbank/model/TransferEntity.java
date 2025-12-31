package com.guilherdrk.jbank.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_transfers")
public class TransferEntity{
    @Id
    @Column(name = "transfer_id")
    private UUID transferId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_receiver_id")
    private WalletEntity receiver;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_sender_id")
    private WalletEntity sender;
    @Column(name = "transfer_value")
    private BigDecimal transferValue;
    @Column(name = "transfer_date_time")
    private LocalDateTime transferDateTime;

    public TransferEntity() {}

    public UUID getTransferId() {
        return transferId;
    }

    public void setTransferId(UUID transferId) {
        this.transferId = transferId;
    }

    public WalletEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(WalletEntity receiver) {
        this.receiver = receiver;
    }

    public WalletEntity getSender() {
        return sender;
    }

    public void setSender(WalletEntity sender) {
        this.sender = sender;
    }

    public BigDecimal getTransferValue() {
        return transferValue;
    }

    public void setTransferValue(BigDecimal transferValue) {
        this.transferValue = transferValue;
    }

    public LocalDateTime getTransferDateTime() {
        return transferDateTime;
    }

    public void setTransferDateTime(LocalDateTime transferDateTime) {
        this.transferDateTime = transferDateTime;
    }
}

