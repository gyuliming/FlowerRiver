package com.wms.flowerwms.pallet.dto;

import lombok.Getter;

@Getter
public class PalletRow {
    private final Long id;
    private final String code;
    private final int maxBoxQty;
    private final int usedBoxQty;

    public PalletRow(Long id, String code, int maxBoxQty, int usedBoxQty) {
        this.id = id;
        this.code = code;
        this.maxBoxQty = maxBoxQty;
        this.usedBoxQty = usedBoxQty;
    }
}