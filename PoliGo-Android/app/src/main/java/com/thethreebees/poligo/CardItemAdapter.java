package com.thethreebees.poligo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CardItemAdapter extends BaseAdapter {
    Context context;
    int[] cardCompany;
    String[] cardNumbers;
    LayoutInflater inflter;

    public CardItemAdapter(Context applicationContext, int[] cardCompany, String[] cardNumbers) {
        this.context = applicationContext;
        this.cardCompany = cardCompany;
        this.cardNumbers= cardNumbers;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return cardNumbers.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.card_spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(cardCompany[i]);
        names.setText(cardNumbers[i]);
        return view;
    }
}