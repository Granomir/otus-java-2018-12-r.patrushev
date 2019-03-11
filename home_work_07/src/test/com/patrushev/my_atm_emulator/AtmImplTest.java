package com.patrushev.my_atm_emulator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AtmImplTest {
    private Atm rubleAtm;

    @Before
    public void setUp() throws Exception {
        rubleAtm = new AtmImpl(new MoneyCassette(5000, 0),
                new MoneyCassette(2000, 0),
                new MoneyCassette(1000, 0),
                new MoneyCassette(500, 0),
                new MoneyCassette(100, 1));
    }

    @After
    public void tearDown() throws Exception {
        rubleAtm = new AtmImpl(new MoneyCassette(5000, 0),
                new MoneyCassette(2000, 0),
                new MoneyCassette(1000, 0),
                new MoneyCassette(500, 0),
                new MoneyCassette(100, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void depositMoney() {
        rubleAtm.depositMoney(5000, 1);
        rubleAtm.depositMoney(500, 3);
        assertEquals(6600, rubleAtm.checkBalance());
        rubleAtm.depositMoney(1, 1);
    }

    @Test
    public void withdrawMoney() {
    }

    @Test
    public void checkBalance() {
        assertEquals(100, rubleAtm.checkBalance());
        rubleAtm.depositMoney(5000, 1);
        assertEquals(5100, rubleAtm.checkBalance());
    }
}