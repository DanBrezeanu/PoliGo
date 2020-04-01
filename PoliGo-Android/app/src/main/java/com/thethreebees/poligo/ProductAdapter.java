package com.thethreebees.poligo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ProductAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<Product> products;

    public ProductAdapter (Activity context) {
        this.context = context;
        products = new ArrayList<>();
    }

    public void addProduct(String SKUProduct, String nameProduct, Double priceProduct, int resource) {
        Product product = new Product(
            SKUProduct,
            nameProduct,
            priceProduct,
            resource
        );
        products.add(product);
        this.notifyDataSetChanged();
    }

    public void removeProduct(String SKUProduct) {
        for (Product prod : products) {
            if (prod.getSKU().equals(SKUProduct)) {
                products.remove(prod);

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
        View element;
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


        final Button remove = element.findViewById(R.id.tv_SKU_element);
        remove.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeProduct(products.get(i).getSKU());
                        SharedPrefManager.getInstance(context).registerShoppingCart(
                                ((ShoppingListActivity) context).cart
                        );
                    }
                }
        );


        element.setTag(product);

        TagProduct tag = (TagProduct) element.getTag();
        tag.name.setText(products.get(i).getName());
        tag.price.setText(products.get(i).getPrice().toString());
        tag.image.setImageResource(products.get(i).getImageResource());
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


