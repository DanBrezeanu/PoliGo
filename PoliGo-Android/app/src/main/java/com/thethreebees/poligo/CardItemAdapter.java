package com.thethreebees.poligo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CardItemAdapter extends BaseAdapter {
    Context context;
    int[] cardCompany;
    String[] cardNumbers;
    LayoutInflater inflater;

    static final int ADD_NEW_CARD = 5;

    public CardItemAdapter(Context applicationContext, int[] cardCompany, String[] cardNumbers) {
        this.context = applicationContext;
        this.cardCompany = cardCompany;
        this.cardNumbers= cardNumbers;
        inflater = (LayoutInflater.from(applicationContext));
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
        view = inflater.inflate(R.layout.card_spinner_items, null);
        ImageView icon = (ImageView) view.findViewById(R.id.imageView);
        TextView names = (TextView) view.findViewById(R.id.textView);
        icon.setImageResource(cardCompany[i]);
        names.setText(cardNumbers[i]);

        if (cardNumbers[i].equals("Add new card")) {
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent toPaymentOptions = new Intent(context, PaymentDetailsActivity.class);
                    toPaymentOptions.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    toPaymentOptions.putExtra("nextActivity", CheckoutActivity.class);
                    ((CheckoutActivity)context).startActivity(toPaymentOptions);
                    ((CheckoutActivity)context).finish();
                }
            });
        }

        return view;
    }


}