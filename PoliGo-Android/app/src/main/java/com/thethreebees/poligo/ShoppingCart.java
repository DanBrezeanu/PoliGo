package com.thethreebees.poligo;

import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.util.ArrayList;

public class ShoppingCart {
    private ArrayList<Pair<Product, Integer>> products;
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

    public ArrayList<Pair<Product, Integer>> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Pair<Product, Integer>> products) {
        this.products = products;
    }

    public int getCount() {
        return this.products.size();
    }

    public void addProduct(Product p) {

        for (int i = 0; i < products.size(); ++i) {
            if (products.get(i).first.getSKU().equals(p.getSKU())) {
                products.set(i, new Pair<>(products.get(i).first,
                                           products.get(i).second + 1));

                totalSum += p.getPrice();
                return;
            }
        }

        products.add(new Pair<>(p, 1));
        totalSum += p.getPrice();
    }

    public synchronized void removeProduct(String SKU) {
        for (int i = 0; i < products.size(); ++i) {
            if (products.get(i).first.getSKU().equals(SKU)) {
                totalSum = Math.max(totalSum - products.get(i).first.getPrice(), 0);

                if (products.get(i).second == 1)
                    products.remove(products.get(i));
                else
                    products.set(i, new Pair<>(products.get(i).first, products.get(i).second - 1));
                return;
            }
        }
    }
}
