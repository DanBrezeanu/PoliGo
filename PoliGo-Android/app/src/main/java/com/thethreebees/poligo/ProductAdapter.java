package com.thethreebees.poligo;

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
    private ArrayList<Pair<Product, Integer>> products;

    public ProductAdapter (Activity context) {
        this.context = context;
        products = new ArrayList<>();
    }

    public void addProduct(Product product, int quant) {
        boolean added_product = false;

        for (int i = 0; i < products.size(); ++i) {
            if (products.get(i).first.getSKU().equals(product.getSKU())){
                products.set(i, new Pair<>(products.get(i).first, products.get(i).second + quant));
                ((ShoppingListActivity) context).cart.addProduct(products.get(i).first);
                added_product = true;
                break;
            }
        }


        if (!added_product)
            products.add(new Pair<>(product, 1));

        ((TextView) ((ShoppingListActivity) context).findViewById(R.id.total_sum)).
                setText(((ShoppingListActivity) context).cart.getTotalSum().toString());

        this.notifyDataSetChanged();
    }

    public void removeProduct(String SKUProduct) {
        for (int i = 0; i < products.size(); ++i) {
            if (products.get(i).first.getSKU().equals(SKUProduct)) {

                if (products.get(i).second > 1) {
                    products.set(i, new Pair<>(products.get(i).first, products.get(i).second - 1));
                } else {
                    products.remove(products.get(i));
                }

                ((ShoppingListActivity) context).cart.removeProduct(SKUProduct);
                ((TextView) ((ShoppingListActivity) context).findViewById(R.id.total_sum)).
                        setText(((ShoppingListActivity) context).cart.getTotalSum().toString());

                break;
            }
        }

        if (getCount() <= 0)
            ((ShoppingListActivity) context).findViewById(R.id.finished_shopping).setVisibility(View.GONE);
        this.notifyDataSetChanged();
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        final View element;
        if(view == null)
        {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            element = layoutInflater.inflate(R.layout.product_list, null);
        }
        else element = view;

        final TagProduct product = new TagProduct();

        product.name = element.findViewById(R.id.tv_name_element);
        product.price = element.findViewById(R.id.tv_price_element);
        product.image = element.findViewById(R.id.iv_image_element);
        final EditText quantity = element.findViewById(R.id.et_quantity);

        final Button remove = element.findViewById(R.id.tv_SKU_element);
        remove.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int totalQuantity = Integer.parseInt(quantity.getText().toString());

                        for (int i = 0; i < totalQuantity; ++i)
                            removeProduct(products.get(i).first.getSKU());

                        SharedPrefManager.getInstance(context).registerShoppingCart(
                                ((ShoppingListActivity) context).cart
                        );
                    }
                }
        );

        final ImageButton increment_quantity = element.findViewById(R.id.add_quantity);
        increment_quantity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addProduct(products.get(i).first, 1);
                        quantity.setText(products.get(i).second.toString());
                        SharedPrefManager.getInstance(context).registerShoppingCart(
                                ((ShoppingListActivity) context).cart
                        );
                    }
                }
        );

        final ImageButton decrement_quantity = element.findViewById(R.id.remove_quantity);
        decrement_quantity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeProduct(products.get(i).first.getSKU());
                        quantity.setText(products.get(i).second.toString());
                        SharedPrefManager.getInstance(context).registerShoppingCart(
                                ((ShoppingListActivity) context).cart
                        );
                    }
                }
        );


        element.setTag(product);

        TagProduct tag = (TagProduct) element.getTag();
        tag.name.setText(products.get(i).first.getName());
        tag.price.setText(products.get(i).first.getPrice().toString());
        tag.image.setImageResource(products.get(i).first.getImageResource());
        return element;
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
}


