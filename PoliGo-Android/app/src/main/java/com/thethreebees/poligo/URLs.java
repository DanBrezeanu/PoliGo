package com.thethreebees.poligo;

public class URLs {
    public static final String PROTOCOL = "http";
    public static final String AUTHORITY = "192.168.0.248:8000";
    public static final String ROOT_PATH = "api/v1";

    private static final String ROOT_URL = PROTOCOL + "://" + AUTHORITY + "/" + ROOT_PATH + "/";

    public static final String URL_REGISTER = ROOT_URL + "signup/";
    public static final String URL_LOGIN = ROOT_URL + "login/";
    public static final String URL_ITEM = ROOT_URL + "checkstocks/";
}