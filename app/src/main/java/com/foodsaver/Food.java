package com.foodsaver;

import android.app.Activity;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Food {
    public String foodName;
    public int foodExpireDays;
    static final int NO_EXPIRATION_DATE=99;

    public static void loadFoods(Activity activity) {
        ArrayList result = new ArrayList<Food>();
        Food newFood;

        if (((MyApplication) activity.getApplication()).allFoods == null) {
            newFood = new Food();
            newFood.foodName = "None";
            newFood.foodExpireDays = NO_EXPIRATION_DATE;
            result.add(newFood);

            newFood = new Food();
            newFood.foodName = "Chicken";
            newFood.foodExpireDays = 7;
            result.add(newFood);

            newFood = new Food();
            newFood.foodName = "Beef";
            newFood.foodExpireDays = 10;
            result.add(newFood);

            newFood = new Food();
            newFood.foodName = "Pork";
            newFood.foodExpireDays = 4;
            result.add(newFood);

            newFood = new Food();
            newFood.foodName = "Pasta";
            newFood.foodExpireDays = 7;
            result.add(newFood);

            newFood = new Food();
            newFood.foodName = "Fish";
            newFood.foodExpireDays = 3;
            result.add(newFood);

            newFood = new Food();
            newFood.foodName = "Shrimp";
            newFood.foodExpireDays = 4;
            result.add(newFood);

            ((MyApplication) activity.getApplication()).allFoods = result;
        }
    }

    // if you pass this method a food (e.g. Chicken, Beef, None) it will return the
    // number of days it will expire in.
    public static int getExpireDaysForFood(Activity activity, String foodName) {
        int result = NO_EXPIRATION_DATE;
        ArrayList<Food> allFoods = ((MyApplication) activity.getApplication()).allFoods;

        for (int i=0; i < allFoods.size(); i++) {
            if (allFoods.get(i).foodName == foodName) {
                result = allFoods.get(i).foodExpireDays;
                break;
            }
        }

        return result;
    }

    // this method will return a string that can be printed, with the lines separated by a newline (\n)
    public static String getPrintableText(Activity activity, ArrayList ingredients) {
        String result;
        int minExpireDays = NO_EXPIRATION_DATE;
        int curExpireDays = NO_EXPIRATION_DATE;

        Date dtCurDate = Calendar.getInstance().getTime();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(activity.getApplicationContext());

        String line1 = "";
        String line2 = "Put in fridge on: " + dateFormat.format(dtCurDate);
        String line3 = "";

        // this part will look at all ingredients and find the one with the lowest expiration
        // time (for example, fish may expire in 3 days but beef in 7).  so we will add the lowest
        // number of days (minExpireDays) to our current date to get the dish's expiration date.
        for (int i=0; i < ingredients.size(); i++) {
            curExpireDays = getExpireDaysForFood(activity, (String) ingredients.get(i));
            if (curExpireDays < minExpireDays)
                minExpireDays = curExpireDays;
            if (curExpireDays < NO_EXPIRATION_DATE)
                line1 = line1 + (String) ingredients.get(i) + ", ";
        }

        // if our minExpireDays is NO_EXPIRATION_DATE, then we have no expiration date
        if (minExpireDays < NO_EXPIRATION_DATE) {
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(dtCurDate);
            cal.add(Calendar.DATE, minExpireDays);
            Date dtExpireDate = cal.getTime();

            line3 = "Eat By: " + dateFormat.format(dtExpireDate);
        }


        result = line1 + "\n";
        result += line2 + "\n";
        result += line3;

        return result;
    }

    // esc/pos is the standard for printing on miniature receipt printers.  this method
    // will take a list of ingredients and turn it into esc/pos language for printing
    // on a printer.
    public static String getEscPosText(Activity activity, ArrayList ingredients) {
        String str = Food.getPrintableText(activity, ingredients);
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
