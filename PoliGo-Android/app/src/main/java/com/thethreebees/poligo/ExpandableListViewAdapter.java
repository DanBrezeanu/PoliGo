package com.thethreebees.poligo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thethreebees.poligo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    ArrayList<HashMap<String, String>> listDataGroup;
    HashMap<String, ArrayList<Pair<Product, Integer>>> listDataChild;

    public ExpandableListViewAdapter(Context context, ArrayList<HashMap<String, String>> listDataGroup, HashMap<String, ArrayList<Pair<Product, Integer>>> listChildData) {
        this.context = context;
        this.listDataGroup = listDataGroup;
        this.listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(
                this.listDataGroup.get(groupPosition).get("number")
        ).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Pair<Product, Integer> child = (Pair<Product, Integer>) getChild(groupPosition, childPosition);
        final Product product = child.first;
        final Integer quantity = child.second;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row_child_shopping_history, null);
        }

        TextView productNameView = convertView.findViewById(R.id.tv_name_element);
        productNameView.setText(product.getName());

        TextView productPriceView = convertView.findViewById(R.id.tv_price_element);
        productPriceView.setText(product.getPrice().toString() + " RON");

        TextView productQuantView = convertView.findViewById(R.id.tv_quantity);
        productQuantView.setText(quantity.toString());

        TextView productSubtotal = convertView.findViewById(R.id.tv_subtotal);
        productSubtotal.setText(((Double)(quantity * product.getPrice())).toString() + " RON");

        ImageView productImage = convertView.findViewById(R.id.iv_image_element);
        productImage.setImageResource(product.getImageResource());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataGroup.get(groupPosition).get("number")).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataGroup.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataGroup.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        HashMap<String, String> header = (HashMap<String, String>) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row_group_shopping_history, null);
        }

        TextView cartNumberView = convertView.findViewById(R.id.cart_number);
        cartNumberView.setText("#" + header.get("number"));

        TextView cartDateView = convertView.findViewById(R.id.cart_date);
        cartDateView.setText(header.get("date"));

        TextView cartTotalSumView = convertView.findViewById(R.id.cart_sum);
        cartTotalSumView.setText(header.get("totalSum") + " RON");

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}