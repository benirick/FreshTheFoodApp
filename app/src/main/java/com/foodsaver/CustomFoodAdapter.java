package com.foodsaver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

// The CustomFoodAdapter class is used to create the app's "spinner's" (called droplists everywhere
// but the Android world) that have the food and a picture of the food.

public class CustomFoodAdapter extends BaseAdapter {
    Context context;
    String[] foodNames;
    LayoutInflater inflter;

    public CustomFoodAdapter(Context applicationContext, String[] foodNames) {
        this.context = applicationContext;
        this.foodNames = foodNames;
        inflter = (LayoutInflater.from(applicationContext));
    }

    // all these @Override functions must be declared to create a custom adapter
    @Override
    public int getCount() {
        return foodNames.length;
    }

    @Override
    public Object getItem(int i) {
        return foodNames[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    // this part actually returns the "view", which is what is used to draw the item passed in.
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_food_spinner, null);

        // an item in the spinner is represented by a "view", and consists of two parts:
        // the image and the text.  we look at the view passed in, split it into its icon and text.
        ImageView food_icon = (ImageView) view.findViewById(R.id.imageView);
        TextView food_name_label = (TextView) view.findViewById(R.id.textView);

        // now that we have the food name, we will look in the res/drawable folder for the image
        // representing that food using its lower case.  so if the food name is "Chicken", we will
        // set the image to res/drawable/chicken.png.
        String food_icon_name = foodNames[i].toLowerCase();

        // convert the food name (lowercase) into a resource ID, which is how you actually set the
        // icon for the food we are looking at.
        int resourceId = view.getResources().getIdentifier(food_icon_name, "drawable", view.getContext().getPackageName());
        food_icon.setImageResource(resourceId);

        // set the text for the selected food by looking in our foodNames array of strings
        food_name_label.setText(foodNames[i]);
        return view;
    }
}
