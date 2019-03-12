package com.patrushev.my_atm_emulator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AtmImplTest {
    private Atm rubleAtm;

    @Before
    public void setUp() throws Exception {
        rubleAtm = new AtmImpl(new MoneyCassette(5000, 4),
                new MoneyCassette(2000, 5),
                new MoneyCassette(1000, 5),
                new MoneyCassette(500, 4),
                new MoneyCassette(100, 5));
    }

    @After
    public void tearDown() throws Exception {
        rubleAtm = new AtmImpl(new MoneyCassette(5000, 4),
                new MoneyCassette(2000, 5),
                new MoneyCassette(1000, 5),
                new MoneyCassette(500, 5),
                new MoneyCassette(100, 5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void depositMoneyAndCheckBalance() {
        assertEquals(37500, rubleAtm.checkBalance());
        rubleAtm.depositMoney(5000, 1);
        rubleAtm.depositMoney(500, 1);
        assertEquals(43000, rubleAtm.checkBalance());
        rubleAtm.depositMoney(1, 1);
    }

    @Test
    public void withdrawMoney() {
        assertEquals(37500, rubleAtm.checkBalance());
        rubleAtm.withdrawMoney(new GreedyDispenser(),33000);
        assertEquals(4500, rubleAtm.checkBalance());
        rubleAtm.withdrawMoney(new GreedyDispenser(),2400);
        assertEquals(2100, rubleAtm.checkBalance());
        rubleAtm.withdrawMoney(new GreedyDispenser(),700);
        assertEquals(2100, rubleAtm.checkBalance());
        rubleAtm.withdrawMoney(new GreedyDispenser(),500);
        assertEquals(1600, rubleAtm.checkBalance());
    }
}