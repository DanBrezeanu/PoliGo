package com.thethreebees.poligo;

import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class ShoppingCart {
    private ArrayList<Product> products;
    private Double totalSum;

    public ShoppingCart() {
        products = new ArrayList<>();
        totalSum = 0.0;
    }

    public Double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Double totalSum) {
        this.totalSum = totalSum;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public int getCount() {
        return this.products.size();
    }

    public void addProduct(Product p) {
        products.add(p);
        totalSum += p.getPrice();
    }

    public synchronized void removeProduct(String SKU) {
        for (int i = 0; i < products.size(); ++i) {
            if (products.get(i).getSKU().equals(SKU)) {
                Log.d("Removed", "product");

                totalSum = Math.max(totalSum - products.get(i).getPrice(), 0);
                products.remove(products.get(i));
                return;
            }
        }
    }
}
