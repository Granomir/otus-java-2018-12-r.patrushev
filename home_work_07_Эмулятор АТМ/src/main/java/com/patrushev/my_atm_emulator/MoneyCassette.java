package com.patrushev.my_atm_emulator;

@SuppressWarnings("WeakerAccess")
public class MoneyCassette {
    private final int denomination;
    private final int quantity;

    public MoneyCassette(int denomination, int quantity) {
        this.denomination = denomination;
        this.quantity = quantity;
    }

    public int getDenomination() {
        return denomination;
    }

    public int getQuantity() {
        return quantity;
    }
}
