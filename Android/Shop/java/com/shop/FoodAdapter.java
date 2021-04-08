package com.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class FoodAdapter extends ArrayAdapter<Food> {
    private final Context context;
    private final List<Food> foods;

    public FoodAdapter(@NonNull Context context, List<Food> foods) {
        super(context, android.R.layout.simple_list_item_1, android.R.id.text1, foods);
        this.context = context;
        this.foods = foods;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View item = convertView;
        if (item == null) {
            item = LayoutInflater.from(context).inflate(R.layout.food_item, parent,
                    false);
        }

        Food food = foods.get(position);

        TextView name = item.findViewById(R.id.nameTextView);
        name.setText(food.getName());

        ImageView image = item.findViewById(R.id.imageView);
        image.setImageResource(food.getImage());

        TextView price = item.findViewById(R.id.priceTextView);
        price.setText(food.getPriceWithCurrency());

        return item;
    }
}