package com.wms.flowerwms.pallet.service;

import com.wms.flowerwms.global.concurrency.OptimisticLockRetry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PalletCommandService {

    private final OptimisticLockRetry optimisticLockRetry;
    private final PalletWriter palletWriter;

    public void inbound(Long palletId, int qty) {
        optimisticLockRetry.run(() -> palletWriter.inbound(palletId, qty));
    }

    public void outbound(Long palletId, int qty) {
        optimisticLockRetry.run(() -> palletWriter.outbound(palletId, qty));
    }
}