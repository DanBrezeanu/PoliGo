package com.thethreebees.poligo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestManager2 {
    private static RequestManager2 instance;
    private APIEndpoints request;
    final int image = R.drawable.lab5_car_icon;

    private RequestManager2() {
         request = new Retrofit.Builder()
                .baseUrl(APIEndpoints.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(APIEndpoints.class);
    }

    public static RequestManager2 getInstance() {
        if (instance == null) {
            instance = new RequestManager2();
        }

        return instance;
    }

    public void login(String username, String password, LoginActivity context) {
        JsonObject data = new JsonObject();
        data.addProperty("username", username);
        data.addProperty("password", password);

        ProgressBar loadingSpinner = context.findViewById(R.id.progressBar);

        request.loginUser(data).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                loadingSpinner.setVisibility(View.GONE);
                JsonObject response_json = response.body();

                if (response_json == null) {
                    //TODO: fail no connection
                    return;
                }

                if (response_json.get("code").getAsInt() == 200) {

                    User user = new User(
                            response_json.get("api_key").getAsString(),
                            response_json.get("name").getAsString(),
                            response_json.get("email").getAsString(),
                            null
                    );

                    ArrayList<BankCard> bankCards = new ArrayList<>();
                    JsonArray responseBankCards = response_json.get("cards").getAsJsonArray();

                    for (JsonElement cardElement : responseBankCards) {
                        JsonObject card = cardElement.getAsJsonObject();

                        bankCards.add(new BankCard(
                                card.get("cardNumber").getAsString(),
                                card.get("cardHolder").getAsString(),
                                card.get("cardMonthExpire").getAsString(),
                                card.get("cardYearExpire").getAsString(),
                                card.get("cardCVV").getAsString(),
                                card.get("cardCompany").getAsString()
                        ));
                    }

                    user.setCards(bankCards);

                    SharedPrefManager.getInstance(context.getApplicationContext()).userLogin(user);
                    context.finish();

                    Intent toMain = new Intent(context, MainActivity.class);
                    toMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(toMain);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                loadingSpinner.setVisibility(View.GONE);
            }
        });

        context.progressBar.setVisibility(View.VISIBLE);
    }

    public void addToCart(ShoppingListActivity activity, String barcode) {
        User user = SharedPrefManager.getInstance(activity).getUser();
        JsonObject data = new JsonObject();

        data.addProperty("SKU", barcode);
        data.addProperty("quantity", 1);
        data.addProperty("api_key", user.getId());

        request.addToCart(data).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                activity.progressBar.setVisibility(View.GONE);
                JsonObject response_json = response.body();

                if (response_json == null) {
                    //TODO: fail no connection
                    return;
                }

                if (response_json.get("code").getAsInt() == 200) {

                    Product new_prod = new Product(
                            response_json.get("SKU").getAsString(),
                            response_json.get("name").getAsString(),
                            response_json.get("price").getAsDouble(),
                            activity.image,
                            0
                    );

                    activity.productAdapter.addProduct(new_prod, 1);

                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(activity)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setMessage(response_json.get("message").getAsString())
                            .setNeutralButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                            .show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                activity.progressBar.setVisibility(View.GONE);
                Toast.makeText(activity.getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        activity.progressBar.setVisibility(View.VISIBLE);
    }

    public void removeFromCart(ShoppingListActivity activity, String barcode, Integer quanity) {
        User user = SharedPrefManager.getInstance(activity).getUser();
        JsonObject data = new JsonObject();

        data.addProperty("SKU", barcode);
        data.addProperty("api_key", user.getId());
        data.addProperty("quantity", quanity);

        request.removeFromCart(data).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                activity.progressBar.setVisibility(View.GONE);
                JsonObject response_json = response.body();

                if (response_json == null) {
                    //TODO: fail no connection
                    return;
                }

                if (response_json.get("code").getAsInt() == 200) {
                    activity.productAdapter.removeProduct(barcode);
                } else {
                    new AlertDialog.Builder(activity)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage(response_json.get("message").getAsString())
                        .setNeutralButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                activity.progressBar.setVisibility(View.GONE);
                Toast.makeText(activity.getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        activity.progressBar.setVisibility(View.VISIBLE);
    }

    public void shoppingCart(ShoppingCart cart) {
        User user = SharedPrefManager.getInstance(null).getUser();
        JsonObject params = new JsonObject();
        params.addProperty("api_key", user.getId());

        Call<JsonObject> shoppingCartRequest = request.shoppingCart(params);

        try
        {
            Response<JsonObject> response = shoppingCartRequest.execute();
            JsonObject apiResponse = response.body();

            if (apiResponse.get("code").getAsInt() == 200) {
                cart.setDate(apiResponse.get("date").getAsString());
                cart.setTotalSum(apiResponse.get("totalCost").getAsDouble());

                ArrayList<Product> products = new ArrayList<>();
                JsonArray prods = apiResponse.get("products").getAsJsonArray();
                for (JsonElement prod_element : prods) {
                    JsonObject prod = prod_element.getAsJsonObject();

                    products.add(new Product(
                            prod.get("SKU").getAsString(),
                            prod.get("name").getAsString(),
                            prod.get("price").getAsDouble(),
                            image,
                            prod.get("quantity").getAsInt()
                    ));
                }

                cart.setProducts(products);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void placeOrder(CheckoutActivity activity) {
        User user = SharedPrefManager.getInstance(null).getUser();
        JsonObject params = new JsonObject();
        params.addProperty("api_key", user.getId());


        request.placeOrder(params).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                activity.progressBar.setVisibility(View.GONE);
                JsonObject response_json = response.body();

                if (response_json == null) {
                    //TODO: fail no connection
                    return;
                }

                if (response_json.get("code").getAsInt() == 200) {
                    Log.d("PlaceOrder", "Request ok");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                activity.progressBar.setVisibility(View.GONE);
                Toast.makeText(activity.getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        activity.progressBar.setVisibility(View.VISIBLE);

    }

    public void shoppingHistory(ShoppingHistoryActivity activity, ShoppingHistory shoppingHistory) {
        User user = SharedPrefManager.getInstance(activity).getUser();
        request.shoppingHistory(user.getId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                activity.progressBar.setVisibility(View.GONE);

                JsonObject response_json = response.body();

                if (response_json == null) {
                    //TODO: fail no connection
                    return;
                }

                if (response_json.get("code").getAsInt() == 200) {

                    JsonArray shoppingCarts = response_json.get("carts").getAsJsonArray();

                    for (int i = 0; i < shoppingCarts.size(); ++i) {
                        JsonObject cart = shoppingCarts.get(i).getAsJsonObject();

                        HashMap<String, String> cartInfoJson = new HashMap<>();
                        String cartNumber = String.format("%d", cart.get("ID").getAsInt());
                        cartInfoJson.put("number", cartNumber);
                        cartInfoJson.put("date", cart.get("date").getAsString());
                        cartInfoJson.put("totalSum", String.format("%.2f", cart.get("totalCost").getAsDouble()));

                        shoppingHistory.cartInfo.add(cartInfoJson);

                        JsonArray products = cart.get("products").getAsJsonArray();
                        ArrayList<Pair<Product, Integer>> newProducts = new ArrayList<>();

                        for (int j = 0; j < products.size(); ++j) {
                            JsonObject prod = products.get(j).getAsJsonObject();

                            Product new_prod = new Product(
                                    prod.get("SKU").getAsString(),
                                    prod.get("name").getAsString(),
                                    prod.get("price").getAsDouble(),
                                    image,
                                    0
                            );

                            newProducts.add(new Pair<>(new_prod, prod.get("quantity").getAsInt()));
                        }

                        shoppingHistory.productsInfo.put(cartNumber, newProducts);

                        activity.expandableListViewAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                activity.progressBar.setVisibility(View.GONE);
                Toast.makeText(activity.getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        activity.progressBar.setVisibility(View.VISIBLE);

    }
}
