package com.paymenttcp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TagsEnum {

    KERNEL("kernel", "9F24", new byte[]{-97, 36}),
    CURRENCY("currency", "5F2A", new byte[]{95, 42}),
    AMOUNT("amount", "9F02", new byte[]{-97, 2}),
    PAN("PAN", "5A", new byte[]{90});

    private final String name;
    private final String id;
    private final byte[] tagBytes;
}