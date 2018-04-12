package com.foodsaver;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.List;

public class ViewFoodsActivity extends AppCompatActivity {
    private SwipeMenuListView mListView;
    List<StoredFood> allFoods;
    DatabaseHelper dbh;
    FoodCursorAdapter foodAdapter;
    Cursor foodCursor;
    DatabaseHelper handler;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_foods);

        dbh = new DatabaseHelper(this);
        allFoods = dbh.getAllStoredFoods();

        mListView = (SwipeMenuListView) findViewById(R.id.food_list_view);
        mListView.setEmptyView(findViewById(R.id.empty));

        handler = new DatabaseHelper(this);
        // Get access to the underlying writeable database
        db = handler.getWritableDatabase();
        // Query for items from the database and get a cursor back
        foodCursor = db.rawQuery("SELECT rowid _id, * FROM food ORDER BY _id ASC", null);
        // Setup cursor adapter using cursor from last step
        foodAdapter = new FoodCursorAdapter(this, foodCursor);

        mListView.setAdapter(foodAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // set creator
        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        StoredFood food = allFoods.get(position);
                        if (dbh.deleteFood(food)) {
                            allFoods.remove(food);
                            foodCursor = db.rawQuery("SELECT rowid _id, * FROM food ORDER BY _id ASC", null);
                            foodAdapter.changeCursor(foodCursor);
                            mListView.invalidateViews();
                        }
                        break;
                }
                return true;
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
