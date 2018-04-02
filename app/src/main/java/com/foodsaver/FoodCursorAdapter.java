package com.foodsaver;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;

public class FoodCursorAdapter extends CursorAdapter {
        public FoodCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        // The newView method is used to inflate a new view and return it,
        // you don't bind any data to the view at this point.
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_stored_food, parent, false);
        }

        // The bindView method is used to bind all data to a given view
        // such as setting the text on a TextView.
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find fields to populate in inflated template
            TextView tvExpireDate = (TextView) view.findViewById(R.id.tvExpireDate);
            TextView tvIngredients = (TextView) view.findViewById(R.id.tvIngredients);
            TextView tvPlacementDate = (TextView) view.findViewById(R.id.tvPlacementDate);

            StoredFood food = new StoredFood();
            food.buildFromDBCursor(cursor);

            // Populate fields with extracted properties
            tvExpireDate.setText(food.getExpirationDate());
            tvIngredients.setText(String.valueOf(food.getIngredientsForFoodList()));
            tvPlacementDate.setText(String.valueOf(food.getPlacementForFoodList()));
        }
}
