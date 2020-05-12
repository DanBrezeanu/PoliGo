package com.thethreebees.poligo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Pair;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "volleyregisterlogin";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_CARDS = "keycards";
    private static final String KEY_ID = "keyid";

    private static final String SHARED_CART_NAME = "shoppingcartpref";

    private static SharedPrefManager mInstance;
    private static Context ctx;

    private SharedPrefManager(Context context) {
        ctx = context;
    }
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());

        Set<String> cards = new HashSet<>();

        if (user.getCards() != null)
            for (BankCard card : user.getCards())
                cards.add(card.toString());

        editor.putStringSet(KEY_CARDS, cards);
        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        User user = new User(
                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                null
        );

        Set<String> cards;
        cards = sharedPreferences.getStringSet(KEY_CARDS, null);

        if (cards != null) {
            for (String card : cards)
                user.addCard(BankCard.fromString(card));
        }

        return user;
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        ctx.startActivity(new Intent(ctx, LoginActivity.class));
    }


    public ShoppingCart getShoppingCart() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_CART_NAME, Context.MODE_PRIVATE);

        Set<String> products = sharedPreferences.getStringSet("products", null);
        ShoppingCart cart = new ShoppingCart();

        if (products != null) {
            for (String prodString : products) {
                String[] quant_prod = prodString.split(Pattern.quote("%%"));

                for (int i = 0; i < Integer.parseInt(quant_prod[0]); ++i)
                    cart.addProduct(Product.fromString(quant_prod[1]));
            }
        }

        Double total_sum = Double.valueOf(sharedPreferences.getFloat("total_sum", 0.0f));
        cart.setTotalSum(total_sum);

        return cart;
    }

    public void registerShoppingCart(ShoppingCart cart) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_CART_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        HashSet<String> prodStrings = new HashSet<>();

        for (Product prod : cart.getProducts()) {
            prodStrings.add(prod.getQuantity() + "%%" + prod.toString());
        }

        editor.putStringSet("products", prodStrings);
        editor.putFloat("total_sum", cart.getTotalSum().floatValue());

        editor.apply();
    }

    public void clearShoppingCart() {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_CART_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}