package com.foodsaver;

import android.database.Cursor;

import java.sql.Date;

import 	java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
// import java.time.ZoneOffset;
// import java.time.format.DateTimeFormatter;

public class StoredFood {
    public static final String TABLE_NAME = "food";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_INGREDIENT1 = "ingredient1";
    public static final String COLUMN_INGREDIENT2 = "ingredient2";
    public static final String COLUMN_FRIDGEORFREEZER = "fridge_or_freezer";
    public static final String COLUMN_RAWORCOOKED = "raw_or_cooked";
    public static final String COLUMN_EXPIRATIONDATE = "expiration_date";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    /*
    Fridge/Freezer
    Cooked/Raw
    Ingredient1
    Ingredient2
    ExpireDate
    */

    private int id;
    private String ingredient1;
    private String ingredient2;
    private boolean fridgeOrFreezer;
    private boolean rawOrCooked;
    private String expirationDate;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_INGREDIENT1 + " TEXT,"
                    + COLUMN_INGREDIENT2 + " TEXT,"
                    + COLUMN_FRIDGEORFREEZER + " INTEGER,"
                    + COLUMN_RAWORCOOKED + " INTEGER,"
                    + COLUMN_EXPIRATIONDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public StoredFood() {
    }

    public StoredFood(int id, String ingredient1, String ingredient2, int fridgeOrFreezer, int rawOrCooked, String expirationDate, String timestamp) {
        this.id = id;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
        this.fridgeOrFreezer = (fridgeOrFreezer > 0);
        this.rawOrCooked = (rawOrCooked > 0);
        this.expirationDate = expirationDate;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getIngredient1() {
        return ingredient1;
    }

    public void setIngredient1(String ingredient1) {
        this.ingredient1 = ingredient1;
    }

    public String getIngredient2() {
        return ingredient2;
    }

    public void setIngredient2(String ingredient2) {
        this.ingredient2 = ingredient2;
    }

    public boolean getFridgeOrFreezer() {
        return fridgeOrFreezer;
    }

    public void setFridgeOrFreezer(boolean fridgeOrFreezer) {
        this.fridgeOrFreezer = fridgeOrFreezer;
    }

    public boolean getRawOrCooked() {
        return rawOrCooked;
    }

    public void setRawOrCooked(boolean rawOrCooked) {
        this.rawOrCooked = rawOrCooked;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void buildFromDBCursor(Cursor cursor) {
        this.expirationDate = cursor.getString(cursor.getColumnIndexOrThrow("expiration_date"));
        this.ingredient1 = cursor.getString(cursor.getColumnIndexOrThrow("ingredient1"));
        this.ingredient2 = cursor.getString(cursor.getColumnIndexOrThrow("ingredient2"));
        this.rawOrCooked = (cursor.getInt(cursor.getColumnIndexOrThrow("raw_or_cooked")) > 0);
        this.fridgeOrFreezer = (cursor.getInt(cursor.getColumnIndexOrThrow("fridge_or_freezer")) > 0);
        this.timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));
    }

    public String getPlacementForFoodList() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        java.util.Date date = null;
        try {
            date = format.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String placement = "Stored ";
        if (this.rawOrCooked)
            placement += "cooked";
        else
            placement += "raw";
        if (this.fridgeOrFreezer)
            placement += " in the freezer on ";
        else
            placement += " in the fridge on ";
        SimpleDateFormat simpleDate =  new SimpleDateFormat("MM/dd/yyyy");

        String strDt = simpleDate.format(date);
        placement += strDt;

        return placement;
    }

    public String getIngredientsForFoodList() {
        String ingredients = "";
        if (ingredient1.length() > 0)
            ingredients = ingredient1;
        if (ingredient2.length() > 0 && !ingredient2.equals("None")) {
            if (ingredients.length() > 0)
                ingredients += " and " + ingredient2;
            else
                ingredients = ingredient2;
        }
        return ingredients;
    }

    public String notificationText() {
        String result = "";


        return result;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
