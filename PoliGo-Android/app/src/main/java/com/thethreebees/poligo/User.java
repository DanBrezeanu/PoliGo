package com.thethreebees.poligo;

import java.util.ArrayList;

public class User {
    private String id;
    private String name, email;
    private ArrayList<BankCard> cards;

    public User(String id, String name, String email, ArrayList<BankCard> cards) {
        this.id = id;
        this.email = email;
        this.cards = cards;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCards(ArrayList<BankCard> cards) {
        this.cards = cards;
    }

    public ArrayList<BankCard> getCards() {
        return cards;
    }

    public void addCard(BankCard cardNumber) {
        if (cards == null) {
            cards = new ArrayList<>();
        }

        this.cards.add(cardNumber);
    }

    public void removeCardNumber(BankCard cardNumber) {
        if (cards == null)
            return;

        cards.remove(cardNumber);
    }

}