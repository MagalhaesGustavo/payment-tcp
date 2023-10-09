package com.paymenttcp.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum KernelProvidersEnum {


    TWO("2", "Mastercard"),
    THREE("3", "VISA"),
    FOUR("4", "American Express");

    private final String kernelProviderValue;
    private final String kernelProviderName;

    public static boolean isKernelProviderValid(String value) {
        return Arrays.stream(KernelProvidersEnum.values())
                .anyMatch(kernelProvidersEnum -> kernelProvidersEnum.kernelProviderValue.equals(value));
    }

    public static KernelProvidersEnum searchKernelById(String value) {
        return Arrays.stream(KernelProvidersEnum.values())
                .filter(tagsEnum -> tagsEnum.kernelProviderValue.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Kernel not found for id: " + value));
    }
}