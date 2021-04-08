package com.shop;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends Activity {
    private final String[] names = {"Banana", "Bread", "Burger", "Cake", "Cherry", "Chocolate",
            "Grapes", "Jam", "Noodles", "Orange", "Pancakes", "Strawberry", "Sushi", "Wine"};

    private final Integer[] images = {R.drawable.banana,
            R.drawable.bread,
            R.drawable.burger,
            R.drawable.cake,
            R.drawable.cherry,
            R.drawable.chocolate,
            R.drawable.grapes,
            R.drawable.jam,
            R.drawable.noodles,
            R.drawable.orange,
            R.drawable.pancakes,
            R.drawable.strawberry,
            R.drawable.sushi,
            R.drawable.wine};

    private final Double[] prices = {0.99, 1.99, 10.99, 25.99, 2.99, 5.99, 2.59, 7.99, 15.99, 1.59,
            13.99, 2.99, 30.99, 45.99};

    private Database database;

    private List<Food> foods;
    private FoodAdapter adapter;
    private ListView items;

    private int savedVisibility = GONE;
    private final Food savedFood = new Food("", -1, -1.);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        database = Room.databaseBuilder(this, Database.class,
                "shop").allowMainThreadQueries().build();

        foods = new ArrayList<>();
        for (int index = 0; index < names.length; index++) {
            foods.add(new Food(names[index], images[index], prices[index]));
        }
        adapter = new FoodAdapter(this, foods);
        items = findViewById(R.id.listView);
        items.setAdapter(adapter);

        items.setOnItemClickListener((parent, view, position, id) -> {
            Food food = adapter.getItem(position);

            ConstraintLayout layout = findViewById(R.id.item);
            savedVisibility = VISIBLE;
            layout.setVisibility(savedVisibility);
            registerForContextMenu(layout);

            TextView name = layout.findViewById(R.id.nameTextView);
            savedFood.setName(food.getName());
            name.setText(savedFood.getName());

            ImageView image = layout.findViewById(R.id.imageView);
            savedFood.setImage(food.getImage());
            image.setImageResource(savedFood.getImage());

            TextView price = layout.findViewById(R.id.priceTextView);
            savedFood.setPriceWithCurrency(food.getPriceWithCurrency());
            price.setText(savedFood.getPriceWithCurrency());

            Intent intent = new Intent(MainActivity.this, FoodActivity.class);
            intent.putExtra("name", food.getName());
            intent.putExtra("image", food.getImage());
            intent.putExtra("price", food.getPriceWithCurrency());
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("enters", sharedPreferences.getInt("enters", 0) + 1);
        editor.apply();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle", getClass().getSimpleName() + ": onRestart invoked");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        savedInstanceState.putInt("visibility", savedVisibility);
        savedInstanceState.putString("name", savedFood.getName());
        savedInstanceState.putInt("image", savedFood.getImage());
        savedInstanceState.putString("price", savedFood.getPriceWithCurrency());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ConstraintLayout layout = findViewById(R.id.item);
        layout.setVisibility(savedInstanceState.getInt("visibility"));

        if (layout.getVisibility() == VISIBLE) {
            TextView name = layout.findViewById(R.id.nameTextView);
            name.setText(savedInstanceState.getString("name"));

            ImageView image = layout.findViewById(R.id.imageView);
            image.setImageResource(savedInstanceState.getInt("image"));

            TextView price = layout.findViewById(R.id.priceTextView);
            price.setText(savedInstanceState.getString("price"));
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void showMenu(View view) {
        PopupMenu menu = new PopupMenu(this, view);
        menu.inflate(R.menu.popup_menu);
        menu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.sortMenuItem:
                    showSortAlertDialog();
                    return true;
                case R.id.internalStorage:
                    try {
                        FileOutputStream fileOutputStream = openFileOutput("foods.txt",
                                Context.MODE_PRIVATE);
                        fileOutputStream.write(foods.toString().getBytes());
                        fileOutputStream.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    Toast toast = Toast.makeText(this,
                            "Stored foods in internal storage", Toast.LENGTH_LONG);
                    toast.show();

                    return true;
                case R.id.externalStorage:
                    try {
                        if (Environment.getExternalStorageState().equals(Environment.
                                MEDIA_MOUNTED)) {
                            FileOutputStream fileOutputStream =
                                    new FileOutputStream(new File(getExternalFilesDir(Environment
                                            .DIRECTORY_DOWNLOADS), "foods.txt"));
                            fileOutputStream.write(foods.toString().getBytes());
                            fileOutputStream.close();
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                    toast = Toast.makeText(this,
                            "Stored foods in external storage", Toast.LENGTH_LONG);
                    toast.show();

                    return true;
                case R.id.database:
                    FoodDAO foodDAO = database.foodDAO();
                    foodDAO.insertAll(foods);

                    toast = Toast.makeText(this,
                            "Stored foods in database", Toast.LENGTH_LONG);
                    toast.show();

                    return true;
                default:
                    return false;
            }
        });
        menu.show();
    }

    public void showSortAlertDialog() {
        String[] filters = {"Name", "Price"};
        List<String> checkedFilters = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Check filters")
                .setMultiChoiceItems(filters, null, (dialog, which, isChecked) -> {
                    if (isChecked) {
                        checkedFilters.add(filters[which]);
                    } else {
                        checkedFilters.remove(filters[which]);
                    }
                }).setPositiveButton("Ok", (dialog, which) -> sortItems(checkedFilters))
                .setNegativeButton("Cancel", null);
        builder.create().show();
    }

    public void sortItems(List<String> filters) {
        if (filters.isEmpty()) {
            return;
        }
        Collections.sort(foods, (firstFood, secondFood) -> {
            int value = 0;
            for (String filter : filters) {
                switch (filter) {
                    case "Name":
                        value = firstFood.getName().compareTo(secondFood.getName());
                        if (value != 0) {
                            return value;
                        }
                        break;
                    case "Price":
                        value = firstFood.getPrice().compareTo(secondFood.getPrice());
                        if (value != 0) {
                            return value;
                        }
                        break;
                }
            }
            return value;
        });
        adapter = new FoodAdapter(this, foods);
        items.setAdapter(adapter);

        StringBuilder text = new StringBuilder("Sorted by: " + filters.get(0));
        for (String filter : filters.subList(1, filters.size())) {
            text.append(", ").append(filter);
        }
        Toast toast = Toast.makeText(this, text.toString(), Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.shareMenuItem) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Food");
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, savedFood.getName() + ": " +
                    savedFood.getPriceWithCurrency());
            startActivity(Intent.createChooser(intent, "Share"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}