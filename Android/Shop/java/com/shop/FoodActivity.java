package com.shop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class FoodActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.item_activity);

        TextView name = findViewById(R.id.nameTextViewActivity);
        name.setText(getIntent().getStringExtra("name"));

        ImageView image = findViewById(R.id.imageViewActivity);
        image.setImageResource(getIntent().getIntExtra("image", 0));

        TextView price = findViewById(R.id.priceTextViewActivity);
        price.setText(getIntent().getStringExtra("price"));

        Button share = findViewById(R.id.shareButton);
        share.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Food");
            intent.setType("image/png");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("android.resource://com.shop/" +
                    getIntent().getIntExtra("image", 0)));
            startActivity(Intent.createChooser(intent, "Share"));
        });
    }
}