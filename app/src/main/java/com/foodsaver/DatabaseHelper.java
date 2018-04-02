package com.foodsaver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "stored_food_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(StoredFood.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + StoredFood.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public StoredFood insertStoredFood(String ingredient1, String ingredient2, boolean fridgeOrFreezer, boolean rawOrCooked, String expirationDate) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(StoredFood.COLUMN_INGREDIENT1, ingredient1);
        values.put(StoredFood.COLUMN_INGREDIENT2, ingredient2);
        values.put(StoredFood.COLUMN_FRIDGEORFREEZER, fridgeOrFreezer);
        values.put(StoredFood.COLUMN_RAWORCOOKED, rawOrCooked);
        values.put(StoredFood.COLUMN_EXPIRATIONDATE, expirationDate);

        // insert row
        long id = db.insert(StoredFood.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return getStoredFood(id);
    }

    public StoredFood getStoredFood(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(StoredFood.TABLE_NAME,
                new String[]{StoredFood.COLUMN_ID,
                        StoredFood.COLUMN_INGREDIENT1,
                        StoredFood.COLUMN_INGREDIENT2,
                        StoredFood.COLUMN_FRIDGEORFREEZER,
                        StoredFood.COLUMN_RAWORCOOKED,
                        StoredFood.COLUMN_EXPIRATIONDATE,
                        StoredFood.COLUMN_TIMESTAMP},
                StoredFood.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare food object
        StoredFood food = new StoredFood(
                cursor.getInt(cursor.getColumnIndex(StoredFood.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(StoredFood.COLUMN_INGREDIENT1)),
                cursor.getString(cursor.getColumnIndex(StoredFood.COLUMN_INGREDIENT2)),
                cursor.getInt(cursor.getColumnIndex(StoredFood.COLUMN_FRIDGEORFREEZER)),
                cursor.getInt(cursor.getColumnIndex(StoredFood.COLUMN_RAWORCOOKED)),
                cursor.getString(cursor.getColumnIndex(StoredFood.COLUMN_EXPIRATIONDATE)),
                cursor.getString(cursor.getColumnIndex(StoredFood.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return food;
    }

    public List<StoredFood> getAllStoredFoods() {
        List<StoredFood> foods = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + StoredFood.TABLE_NAME + " ORDER BY " +
                StoredFood.COLUMN_ID + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                StoredFood food = new StoredFood();
                food.setId(cursor.getInt(cursor.getColumnIndex(StoredFood.COLUMN_ID)));
                food.setIngredient1(cursor.getString(cursor.getColumnIndex(StoredFood.COLUMN_INGREDIENT1)));
                food.setIngredient2(cursor.getString(cursor.getColumnIndex(StoredFood.COLUMN_INGREDIENT2)));
                food.setFridgeOrFreezer(cursor.getInt(cursor.getColumnIndex(StoredFood.COLUMN_FRIDGEORFREEZER)) > 0);
                food.setRawOrCooked(cursor.getInt(cursor.getColumnIndex(StoredFood.COLUMN_RAWORCOOKED)) > 0);
                food.setExpirationDate(cursor.getString(cursor.getColumnIndex(StoredFood.COLUMN_EXPIRATIONDATE)));
                food.setTimestamp(cursor.getString(cursor.getColumnIndex(StoredFood.COLUMN_TIMESTAMP)));

                foods.add(food);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return foods;
    }

    public int getFoodsCount() {
        String countQuery = "SELECT  * FROM " + StoredFood.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateNote(StoredFood food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StoredFood.COLUMN_INGREDIENT1, food.getIngredient1());
        values.put(StoredFood.COLUMN_INGREDIENT2, food.getIngredient2());
        values.put(StoredFood.COLUMN_FRIDGEORFREEZER, food.getFridgeOrFreezer());
        values.put(StoredFood.COLUMN_RAWORCOOKED, food.getRawOrCooked());
        values.put(StoredFood.COLUMN_EXPIRATIONDATE, food.getExpirationDate());

        // updating row
        return db.update(StoredFood.TABLE_NAME, values, StoredFood.COLUMN_ID + " = ?",
                new String[]{String.valueOf(food.getId())});
    }

    public boolean deleteFood(StoredFood food) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = db.delete(StoredFood.TABLE_NAME, StoredFood.COLUMN_ID + " = ?",
                new String[]{String.valueOf(food.getId())}) > 0;
        db.close();
        return success;
    }
}
