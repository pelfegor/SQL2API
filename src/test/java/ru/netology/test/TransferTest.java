package ru.netology.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.APIHelper.*;
import static ru.netology.data.SQLHelper.*;


public class TransferTest {
    DataHelper.UserData user;
    String token;
    DataHelper.CardData[] cards;
    int indexCartTo = 0;
    int indexCartFrom = 1;
    int balanceCartTo;
    int balanceCartFrom;

    @BeforeEach
    public void setUp() {
        reloadVerifyCodeTable();
        user = DataHelper.getUser();
        auth(user);
        token = verification(DataHelper.getValidCode(user.getLogin()));
        cards = getCards(token);
        int i = 0;
        for (DataHelper.CardData card : cards) {
            card.setNumber(getNumberCardById(card.getId()));
            i++;
        }
    }

    @AfterEach
    public void reloadBalance() {
        reloadBalanceCards(cards[indexCartTo].getId(), balanceCartTo);
        reloadBalanceCards(cards[indexCartFrom].getId(), balanceCartFrom);
    }

    @Test
    public void shouldTransferHappyPath() {
        balanceCartTo = Integer.parseInt(cards[indexCartTo].getBalance());
        balanceCartFrom = Integer.parseInt(cards[indexCartFrom].getBalance());
        int amount = balanceCartFrom / 2;

        var transferData = new DataHelper.TransferData(cards[indexCartFrom].getNumber(),
                cards[indexCartTo].getNumber(), String.valueOf(amount));
        transfer(transferData, token);
        assertEquals(balanceCartTo + amount, getBalanceCardById(cards[indexCartTo].getId()));
        assertEquals(balanceCartFrom - amount, getBalanceCardById(cards[indexCartFrom].getId()));
    }

    //todo bug
    @Test
    public void shouldNoTransferBoundaryValueOne() {
        balanceCartTo = Integer.parseInt(cards[indexCartTo].getBalance());
        balanceCartFrom = Integer.parseInt(cards[indexCartFrom].getBalance());
        int amount = -1;

        var transferData = new DataHelper.TransferData(cards[indexCartFrom].getNumber(),
                cards[indexCartTo].getNumber(), String.valueOf(amount));
        transfer(transferData, token);
        assertEquals(balanceCartTo, getBalanceCardById(cards[indexCartTo].getId()));
        assertEquals(balanceCartFrom, getBalanceCardById(cards[indexCartFrom].getId()));
    }

    @Test
    public void shouldNoTransferBoundaryValueTwo() {
        balanceCartTo = Integer.parseInt(cards[indexCartTo].getBalance());
        balanceCartFrom = Integer.parseInt(cards[indexCartFrom].getBalance());
        int amount = 0;

        var transferData = new DataHelper.TransferData(cards[indexCartFrom].getNumber(),
                cards[indexCartTo].getNumber(), String.valueOf(amount));
        transfer(transferData, token);
        assertEquals(balanceCartTo, getBalanceCardById(cards[indexCartTo].getId()));
        assertEquals(balanceCartFrom, getBalanceCardById(cards[indexCartFrom].getId()));
    }

    @Test
    public void shouldTransferBoundaryValueThree() {
        balanceCartTo = Integer.parseInt(cards[indexCartTo].getBalance());
        balanceCartFrom = Integer.parseInt(cards[indexCartFrom].getBalance());
        int amount = 1;

        var transferData = new DataHelper.TransferData(cards[indexCartFrom].getNumber(),
                cards[indexCartTo].getNumber(), String.valueOf(amount));
        transfer(transferData, token);
        assertEquals(balanceCartTo + amount, getBalanceCardById(cards[indexCartTo].getId()));
        assertEquals(balanceCartFrom - amount, getBalanceCardById(cards[indexCartFrom].getId()));
    }

    @Test
    public void shouldTransferBoundaryValueFour() {
        balanceCartTo = Integer.parseInt(cards[indexCartTo].getBalance());
        balanceCartFrom = Integer.parseInt(cards[indexCartFrom].getBalance());
        int amount = balanceCartFrom - 1;

        var transferData = new DataHelper.TransferData(cards[indexCartFrom].getNumber(),
                cards[indexCartTo].getNumber(), String.valueOf(amount));
        transfer(transferData, token);
        assertEquals(balanceCartTo + amount, getBalanceCardById(cards[indexCartTo].getId()));
        assertEquals(balanceCartFrom - amount, getBalanceCardById(cards[indexCartFrom].getId()));
    }

    @Test
    public void shouldTransferBoundaryValueFive() {
        balanceCartTo = Integer.parseInt(cards[indexCartTo].getBalance());
        balanceCartFrom = Integer.parseInt(cards[indexCartFrom].getBalance());
        int amount = balanceCartFrom;

        var transferData = new DataHelper.TransferData(cards[indexCartFrom].getNumber(),
                cards[indexCartTo].getNumber(), String.valueOf(amount));
        transfer(transferData, token);
        assertEquals(balanceCartTo + amount, getBalanceCardById(cards[indexCartTo].getId()));
        assertEquals(balanceCartFrom - amount, getBalanceCardById(cards[indexCartFrom].getId()));
    }

    //todo bug
    @Test
    public void shouldNoTransferBoundaryValueSix() {
        balanceCartTo = Integer.parseInt(cards[indexCartTo].getBalance());
        balanceCartFrom = Integer.parseInt(cards[indexCartFrom].getBalance());
        int amount = balanceCartFrom + 1;

        var transferData = new DataHelper.TransferData(cards[indexCartFrom].getNumber(),
                cards[indexCartTo].getNumber(), String.valueOf(amount));
        transfer(transferData, token);
        assertEquals(balanceCartTo, getBalanceCardById(cards[indexCartTo].getId()));
        assertEquals(balanceCartFrom, getBalanceCardById(cards[indexCartFrom].getId()));
    }

    @Test
    public void shouldNoTransferSingleCard() {
        indexCartTo = 0;
        indexCartFrom = 0;
        balanceCartTo = Integer.parseInt(cards[indexCartTo].getBalance());
        balanceCartFrom = Integer.parseInt(cards[indexCartFrom].getBalance());
        int amount = balanceCartFrom / 2;

        var transferData = new DataHelper.TransferData(cards[indexCartFrom].getNumber(),
                cards[indexCartTo].getNumber(), String.valueOf(amount));
        transfer(transferData, token);
        assertEquals(balanceCartTo, getBalanceCardById(cards[indexCartTo].getId()));
        assertEquals(balanceCartFrom, getBalanceCardById(cards[indexCartFrom].getId()));
    }
}