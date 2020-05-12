package com.thethreebees.poligo;

import android.util.Pair;

import java.util.ArrayList;

public class ShoppingCart {
    private ArrayList<Product> products;
    private Double totalSum;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

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
        RequestManager2.getInstance().shoppingCart(this);

        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public int getCount() {
        return this.products.size();
    }

    public void addProduct(Product p) {

        for (int i = 0; i < products.size(); ++i) {
            if (products.get(i).getSKU().equals(p.getSKU())) {
                products.get(i).setQuantity(products.get(i).getQuantity() + 1);

                totalSum += p.getPrice();
                return;
            }
        }

        p.setQuantity(1);
        products.add(p);
        totalSum += p.getPrice();
    }

    public synchronized void removeProduct(String SKU) {
        for (int i = 0; i < products.size(); ++i) {
            if (products.get(i).getSKU().equals(SKU)) {
                totalSum = Math.max(totalSum - products.get(i).getPrice(), 0);

                if (products.get(i).getQuantity() == 1)
                    products.remove(products.get(i));
                else
                    products.get(i).setQuantity(products.get(i).getQuantity() - 1);
                return;
            }
        }
    }
}
