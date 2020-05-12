package com.thethreebees.poligo;

public class URLs {
    public static final String PROTOCOL = "http";
    public static final String AUTHORITY = "192.168.0.108:8000";
    public static final String ROOT_PATH = "api/v1";

    private static final String ROOT_URL = PROTOCOL + "://" + AUTHORITY + "/" + ROOT_PATH + "/";

    public static final String URL_REGISTER = ROOT_URL + "signup/";
    public static final String URL_LOGIN = ROOT_URL + "login/";
    public static final String URL_ADD_TO_CART = ROOT_URL + "addtocart/";
    public static final String URL_SHOPPING_HISTORY = ROOT_URL + "shoppinghistory/";
    public static final String URL_SHOPPING_CART = ROOT_URL + "shoppingcart/";
    public static final String URL_ADD_CARD = ROOT_URL + "addcard/";


}