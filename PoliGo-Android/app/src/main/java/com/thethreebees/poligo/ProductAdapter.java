package com.thethreebees.poligo;

import android.app.Activity;
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
        products = new ArrayList<Product>();
    }

    public void addProduct(String nameProduct, int resource){
        Product product = new Product();
        product.name = nameProduct;
        product.imageResource = resource;
        products.add(product);
        this.notifyDataSetChanged();
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View element;
        if(view == null)
        {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            element = layoutInflater.inflate(R.layout.product_list, null);
            element.findViewById(R.id.button_add_product);
        }
        else element = view; ;

        TagProduct product = new TagProduct();
        product.name = element.findViewById(R.id.tv_name_element);
        product.image = element.findViewById(R.id.iv_image_element);
        element.setTag(product);

        TagProduct tag = (TagProduct) element.getTag();
        tag.name.setText(products.get(i).name);
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
    String name;
    int imageResource;
}

class TagProduct {
    TextView name;
    ImageView image;
}
