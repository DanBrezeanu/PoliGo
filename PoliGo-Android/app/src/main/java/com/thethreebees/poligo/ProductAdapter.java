package com.thethreebees.poligo;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<Product> products;

    public ProductAdapter (Activity context) {
        this.context = context;
        products = new ArrayList<>();
    }

    public void addProduct(String SKUProduct, String nameProduct, Double priceProduct, int resource) {
        Product product = new Product();
        product.SKU = SKUProduct;
        product.name = nameProduct;
        product.price = priceProduct;
        product.imageResource = resource;
        products.add(product);
        this.notifyDataSetChanged();
    }

    public void removeProduct(String SKUProduct) {
        for (Product prod : products) {
            if (prod.SKU.equals(SKUProduct)) {
                products.remove(prod);
                break;
            }
        }
        this.notifyDataSetChanged();
    }

    public View getView(final int i, View view, ViewGroup viewGroup) {
        View element;
        if(view == null)
        {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            element = layoutInflater.inflate(R.layout.product_list, null);
            element.findViewById(R.id.button_add_product);
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
                        removeProduct(products.get(i).SKU);
                    }
                }
        );


        element.setTag(product);

        TagProduct tag = (TagProduct) element.getTag();
        tag.name.setText(products.get(i).name);
        tag.price.setText(products.get(i).price.toString());
        tag.image.setImageResource(products.get(i).imageResource);
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
    String SKU;
    String name;
    Double price;
    int imageResource;
}

class TagProduct {
    TextView name;
    TextView price;
    ImageView image;
}
