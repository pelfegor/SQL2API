package ru.netology.data;

import lombok.Data;
import lombok.Value;

public class DataHelper {

    private DataHelper() {
    }

    @Value
    public static class UserData {
        String login;
        String password;
    }

    public static UserData getUser() {
        return new UserData("vasya", "qwerty123");
    }

    @Value
    public static class VerificationCode {
        String login;
        String verifyCode;
    }

    public static VerificationCode getValidCode(String login) {
        return new VerificationCode(login, SQLHelper.getVerificationCode());
    }

    @Data
    public static class CardData {
        private final String id;
        private String number;
        private final String balance;
    }

    @Value
    public static class TransferData {
        String from;
        String to;
        String amount;
    }
}