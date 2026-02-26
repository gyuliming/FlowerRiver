package com.wms.flowerwms.pallet.service;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class PalletCodeGenerator {

    private final AtomicLong seq;

    public PalletCodeGenerator(PalletRepository palletRepository) {
        long maxId = palletRepository.findMaxId().orElse(0L);
        this.seq = new AtomicLong(maxId + 1);
    }

    public String nextCode() {
        long n = seq.getAndIncrement();
        return String.format("PLT-%06d", n);
    }
}