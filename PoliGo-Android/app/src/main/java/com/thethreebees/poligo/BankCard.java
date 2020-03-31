package com.thethreebees.poligo;

import androidx.annotation.NonNull;

import java.util.regex.Pattern;

public class BankCard {
    private String number, holder, expirationMonth, expirationYear, CVV;
    private String company;

    public BankCard(String number, String holder, String expirationMonth, String expirationYear, String CVV, String company) {
        this.number = number;
        this.holder = holder;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
        this.CVV = CVV;
        this.company = company;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    public String getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s|%s|%s|%s|%s|%s",
                number, holder, expirationMonth, expirationYear, CVV, company);
    }

    public static BankCard fromString(String cardString) {
        String[] cardPattern = cardString.split(Pattern.quote("|"));

        return new BankCard(cardPattern[0], cardPattern[1], cardPattern[2],
                            cardPattern[3], cardPattern[4], cardPattern[5]);
    }
}
