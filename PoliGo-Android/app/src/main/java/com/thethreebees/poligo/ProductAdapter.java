package com.thethreebees.poligo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ProductAdapter extends BaseAdapter {
    private Activity context;
    private ShoppingCart cart;
    private SharedPrefManager sharedPref;
    private TextView totalSumTextView;
    private Button finishedShopping;
    private ArrayList<Pair<Product, Integer>> products;

    public ProductAdapter (Activity context) {
        this.context = context;
        cart = ((ShoppingListActivity) context).cart;
        sharedPref = SharedPrefManager.getInstance(context);
        totalSumTextView = context.findViewById(R.id.total_sum);
        finishedShopping = context.findViewById(R.id.finished_shopping);
        products = cart.getProducts();
    }

    public void addProduct(Product product, int quant) {
        cart.addProduct(product);
        sharedPref.registerShoppingCart(cart);

        this.notifyDataSetChanged();
    }

    public void removeProduct(String SKUProduct) {
        cart.removeProduct(SKUProduct);
        sharedPref.registerShoppingCart(cart);

        this.notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final View element;

        if(view == null) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            element = layoutInflater.inflate(R.layout.product_list, null);
        }
        else element = view;

        final TagProduct product = new TagProduct();

        product.name = element.findViewById(R.id.tv_name_element);
        product.price = element.findViewById(R.id.tv_price_element);
        product.image = element.findViewById(R.id.iv_image_element);
        product.quantity = element.findViewById(R.id.et_quantity);

        final Button remove = element.findViewById(R.id.tv_SKU_element);
        remove.setOnClickListener(
                view1 -> {
                    int totalQuantity = Integer.parseInt(product.quantity.getText().toString());

                    for (int j = 0; j < totalQuantity; ++j)
                        removeProduct(products.get(i).first.getSKU());

                    reloadElementsUI();
                    ProductAdapter.this.notifyDataSetChanged();
                }
        );

        final ImageButton increment_quantity = element.findViewById(R.id.add_quantity);
        increment_quantity.setOnClickListener(
                view12 -> {
                    addProduct(products.get(i).first, 1);
                    product.quantity.setText(products.get(i).second.toString());

                    reloadElementsUI();
                    ProductAdapter.this.notifyDataSetChanged();
                }
        );

        final ImageButton decrement_quantity = element.findViewById(R.id.remove_quantity);
        decrement_quantity.setOnClickListener(
                view13 -> {
                    int initial_quant = products.get(i).second;

                    removeProduct(products.get(i).first.getSKU());

                    if (initial_quant > 1)
                        product.quantity.setText(products.get(i).second.toString());

                    reloadElementsUI();
                    ProductAdapter.this.notifyDataSetChanged();
                }
        );


        element.setTag(product);

        TagProduct tag = (TagProduct) element.getTag();
        tag.name.setText(products.get(i).first.getName());
        tag.price.setText(products.get(i).first.getPrice().toString());
        tag.image.setImageResource(products.get(i).first.getImageResource());
        tag.quantity.setText(products.get(i).second.toString());

        reloadElementsUI();
        return element;
    }


    @SuppressLint("SetTextI18n")
    void reloadElementsUI() {
        totalSumTextView.setText(cart.getTotalSum().toString());

        if (cart.getCount() <= 0)
            finishedShopping.setVisibility(View.GONE);
        else
            finishedShopping.setVisibility(View.VISIBLE);
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}


class Product {
    private String SKU;
    private String name;
    private Double price;
    private int imageResource;

    public Product() { }

    public Product(String SKU, String name, Double price, int imageResource) {
        this.SKU = SKU;
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("%s|%s|%f|%d", SKU, name, price, imageResource);
    }

    public static Product fromString(String productString) {
            String[] productPattern = productString.split(Pattern.quote("|"));

            return new Product(productPattern[0], productPattern[1], Double.parseDouble(productPattern[2]),
                               Integer.parseInt(productPattern[3]));

    }
}

class TagProduct {
    TextView name;
    TextView price;
    ImageView image;
    EditText quantity;
}


