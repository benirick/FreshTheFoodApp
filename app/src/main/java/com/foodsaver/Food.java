package com.foodsaver;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Food {
    public String foodName;
    public int[] foodExpireDays;
    static final int NO_EXPIRATION_DATE=9999;
    static final int RAW=0;
    static final int COOKED=1;
    static final int RAW_FROZEN=2;
    static final int COOKED_FROZEN=3;
    static final int STORE_RAW=0;
    static final int STORE_COOKED=1;
    static final int STORE_FRIDGE=0;
    static final int STORE_FREEZER=1;

    public static void loadFoods(Activity activity) {
        ArrayList result = new ArrayList<Food>();
        Food newFood;

        if (((MyApplication) activity.getApplication()).allFoods == null) {
            newFood = new Food();
            newFood.foodName = "None";
            newFood.foodExpireDays = new int[4];
            newFood.foodExpireDays[COOKED] = NO_EXPIRATION_DATE;
            newFood.foodExpireDays[RAW] = NO_EXPIRATION_DATE;
            newFood.foodExpireDays[RAW_FROZEN] = NO_EXPIRATION_DATE;
            newFood.foodExpireDays[COOKED_FROZEN] = NO_EXPIRATION_DATE;
            result.add(newFood);

            newFood = new Food();
            newFood.foodName = "Poultry";
            newFood.foodExpireDays = new int[4];
            newFood.foodExpireDays[COOKED] = 4;
            newFood.foodExpireDays[RAW] = 2;
            newFood.foodExpireDays[RAW_FROZEN] = 180;
            newFood.foodExpireDays[COOKED_FROZEN] = 180;
            result.add(newFood);

            newFood = new Food();
            newFood.foodName = "Ground Beef";
            newFood.foodExpireDays = new int[4];
            newFood.foodExpireDays[COOKED] = 4;
            newFood.foodExpireDays[RAW] = 2;
            newFood.foodExpireDays[RAW_FROZEN] = 120;
            newFood.foodExpireDays[COOKED_FROZEN] = 180;
            result.add(newFood);

            newFood = new Food();
            newFood.foodName = "Steak";
            newFood.foodExpireDays = new int[4];
            newFood.foodExpireDays[COOKED] = 4;
            newFood.foodExpireDays[RAW] = 5;
            newFood.foodExpireDays[RAW_FROZEN] = 120;
            newFood.foodExpireDays[COOKED_FROZEN] = 90;
            result.add(newFood);

            newFood = new Food();
            newFood.foodName = "Roast";
            newFood.foodExpireDays = new int[4];
            newFood.foodExpireDays[COOKED] = 4;
            newFood.foodExpireDays[RAW] = 5;
            newFood.foodExpireDays[RAW_FROZEN] = 120;
            newFood.foodExpireDays[COOKED_FROZEN] = 90;
            result.add(newFood);

            newFood = new Food();
            newFood.foodName = "Pork";
            newFood.foodExpireDays = new int[4];
            newFood.foodExpireDays[COOKED] = 4;
            newFood.foodExpireDays[RAW] = 5;
            newFood.foodExpireDays[RAW_FROZEN] = 120;
            newFood.foodExpireDays[COOKED_FROZEN] = 90;
            result.add(newFood);

            newFood = new Food();
            newFood.foodName = "Pasta";
            newFood.foodExpireDays = new int[4];
            newFood.foodExpireDays[COOKED] = 5;
            newFood.foodExpireDays[RAW] = 365;
            newFood.foodExpireDays[RAW_FROZEN] = NO_EXPIRATION_DATE;
            newFood.foodExpireDays[COOKED_FROZEN] = 60;
            result.add(newFood);

            newFood = new Food();
            newFood.foodName = "Fish";
            newFood.foodExpireDays = new int[4];
            newFood.foodExpireDays[COOKED] = 4;
            newFood.foodExpireDays[RAW] = 2;
            newFood.foodExpireDays[RAW_FROZEN] = 90;
            newFood.foodExpireDays[COOKED_FROZEN] = 90;
            result.add(newFood);

            newFood = new Food();
            newFood.foodName = "Shrimp";
            newFood.foodExpireDays = new int[4];
            newFood.foodExpireDays[COOKED] = 4;
            newFood.foodExpireDays[RAW] = 3;
            newFood.foodExpireDays[RAW_FROZEN] = 365;
            newFood.foodExpireDays[COOKED_FROZEN] = 270;
            result.add(newFood);

            ((MyApplication) activity.getApplication()).allFoods = result;
        }
    }

    // if you pass this method a food (e.g. Chicken, Beef, None) it will return the
    // number of days it will expire in.
    public static int getExpireDaysForFood(Activity activity, String foodName, int rawOrCooked, int fridgeOrFreezer) {
        int result = NO_EXPIRATION_DATE;
        ArrayList<Food> allFoods = ((MyApplication) activity.getApplication()).allFoods;
        int expireIndex;

        if (rawOrCooked == STORE_RAW && fridgeOrFreezer == STORE_FRIDGE)
            expireIndex = RAW;
        else if (rawOrCooked == STORE_RAW && fridgeOrFreezer == STORE_FREEZER)
            expireIndex = RAW_FROZEN;
        else if (rawOrCooked == STORE_COOKED && fridgeOrFreezer == STORE_FRIDGE)
            expireIndex = COOKED;
        else if (rawOrCooked == STORE_COOKED && fridgeOrFreezer == STORE_FREEZER)
            expireIndex = COOKED_FROZEN;
        else {
            // this shouldn't happen but it will serve as a default value
            expireIndex = COOKED;
        }

        for (int i=0; i < allFoods.size(); i++) {
            if (allFoods.get(i).foodName == foodName) {
                result = allFoods.get(i).foodExpireDays[expireIndex];
                break;
            }
        }

        return result;
    }

    public static int getExpireDaysFromIngredients(Activity activity, ArrayList ingredients, int rawOrCooked, int fridgeOrFreezer) {
        int minExpireDays = NO_EXPIRATION_DATE;
        int curExpireDays = NO_EXPIRATION_DATE;

        for (int i=0; i < ingredients.size(); i++) {
            curExpireDays = getExpireDaysForFood(activity, (String) ingredients.get(i), rawOrCooked, fridgeOrFreezer);
            if (curExpireDays < minExpireDays)
                minExpireDays = curExpireDays;
        }

        return minExpireDays;
    }

    // this method will return a string that can be printed, with the lines separated by a newline (\n)
    public static String getPrintableText(Activity activity, ArrayList ingredients, int rawOrCooked, int fridgeOrFreezer) {
        String result;
        int minExpireDays = NO_EXPIRATION_DATE;
        int curExpireDays = NO_EXPIRATION_DATE;

        Date dtCurDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(activity.getApplicationContext());

        String line1 = "";
        String line2 = "";

        boolean hasIngredient = false;
        for(int i=0; i < ingredients.size(); i++) {
            if ((String) ingredients.get(i) != "None") {
                hasIngredient = true;
                break;
            }
        }
        if (hasIngredient) {
            if (fridgeOrFreezer == Food.STORE_FRIDGE)
                line2 = "Put in fridge on: " + dateFormat.format(dtCurDate);
            else
                line2 = "Put in freezer on: " + dateFormat.format(dtCurDate);
        }
        String line3 = "";

        // this part will look at all ingredients and find the one with the lowest expiration
        // time (for example, fish may expire in 3 days but beef in 7).  so we will add the lowest
        // number of days (minExpireDays) to our current date to get the dish's expiration date.
        for (int i=0; i < ingredients.size(); i++) {
            curExpireDays = getExpireDaysForFood(activity, (String) ingredients.get(i), rawOrCooked, fridgeOrFreezer);
            if (curExpireDays < minExpireDays)
                minExpireDays = curExpireDays;
            if (curExpireDays < NO_EXPIRATION_DATE) {
                if (i == 0) {
                    if (rawOrCooked == Food.STORE_RAW)
                        line1 = "Raw " + (String) ingredients.get(i);
                    else
                        line1 = "Cooked " + (String) ingredients.get(i);
                } else if (i == ingredients.size() - 1) {
                    line1 = line1 + " and " + (String) ingredients.get(i);
                } else {
                    line1 = line1 + ", " + (String) ingredients.get(i);
                }
            }
        }

        // if our minExpireDays is NO_EXPIRATION_DATE, then we have no expiration date
        if (minExpireDays < NO_EXPIRATION_DATE) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(dtCurDate);
            cal.add(Calendar.DATE, minExpireDays);
            Date dtExpireDate = cal.getTime();

            if (fridgeOrFreezer == Food.STORE_FRIDGE) {
                if (rawOrCooked == Food.STORE_RAW) {
                    line3 = "Cook By: " + dateFormat.format(dtExpireDate);
                } else {
                    line3 = "Eat By: " + dateFormat.format(dtExpireDate);
                }
            } else {
                line3 = "Thaw and Eat By: " + dateFormat.format(dtExpireDate);
            }
        }


        result = line1 + "\n";
        result += line2 + "\n";
        result += line3;

        return result;
    }

    // esc/pos is the standard for printing on miniature receipt printers.  this method
    // will take a list of ingredients and turn it into esc/pos language for printing
    // on a printer.
    public static String getEscPosText(Activity activity, ArrayList ingredients, int rawOrCooked, int fridgeOrFreezer) {
        String str = Food.getPrintableText(activity, ingredients, rawOrCooked, fridgeOrFreezer);
        String result;
        String ar[] = str.split("\\n");

        String textToPrint = "<BIG>Text Title<BR>Testing <BIG>BIG<BR><BIG><BOLD>" +
                "string <SMALL> text<BR><LEFT>Left aligned<BR><CENTER>" +
                "Center aligned<BR><UNDERLINE>underline text<BR><QR>12345678<BR>" +
                "<CENTER>QR: 12345678<BR>Line<BR><LINE><BR>Double Line<BR><DLINE><BR><CUT>";


        result = "";
        result += "<BOLD>" + ar[0] + "<BR>";
        result += "<BOLD>" + ar[1] + "<BR>";
        result += "<BOLD>" + ar[2] + "<BR>";

        // String addlText = "<IMAGE>http://irick.net/121806.JPG<BR>";
        // result += addlText;

        return result;
    }

    // returns our application's foods in an array of String
    public static String[] getFoodNames(Activity activity) {
        String[] result;

        if (((MyApplication) activity.getApplication()).allFoods == null) {
            Food.loadFoods(activity);
        }

        int numFoods = ((MyApplication) activity.getApplication()).allFoods.size();

        result = new String[numFoods];

        for (int i=0; i < numFoods; i++) {
            result[i] = (((MyApplication) activity.getApplication()).allFoods.get(i).foodName);
        }

        return result;
    }
}
