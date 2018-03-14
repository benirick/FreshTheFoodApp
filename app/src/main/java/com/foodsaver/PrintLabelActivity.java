package com.foodsaver;

import android.content.Context;
import android.content.Intent;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PrintLabelActivity extends AppCompatActivity {

    AdapterView.OnItemSelectedListener foodChange;
    CompoundButton.OnCheckedChangeListener switchChange;

    Spinner spinner1; // first selected ingredient
    Spinner spinner2; // second selected ingredient
    Switch swRawOrCooked;
    Switch swFridgeOrFreezer;
    TextView textIngredientList; // turn our selected ingredients into text to view on the screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // this activity will use the layout defined in res/laout/activity_print_label.xml
        setContentView(R.layout.activity_print_label);

        spinner1 = findViewById(R.id.spinner_select_ingredient1);
        spinner2 = findViewById(R.id.spinner_select_ingredient2);
        swRawOrCooked = findViewById(R.id.swRawOrCooked);
        swFridgeOrFreezer = findViewById(R.id.swFridgeOrFreezer);

        textIngredientList = findViewById(R.id.textIngredientList);

        // this is the new spinner with labels for the foods
        CustomFoodAdapter spinnerFoodsAdapter = new CustomFoodAdapter(getApplicationContext(), Food.getFoodNames(this));

        // this is the old spinner with just text
        // ArrayAdapter<String> spinnerFoodsAdapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, Food.getFoodNames(this));

        spinner1.setAdapter(spinnerFoodsAdapter);
        spinner2.setAdapter(spinnerFoodsAdapter);

        // this block defines what we do when the user changes an ingredient.
        foodChange = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // change the textList that previews our label
                setIngredientList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // change the textList that previews our label
                setIngredientList();
            }
        };

        // this block defined what we do when the user changes fridge/freezer or raw/cooked
        switchChange = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setIngredientList();
            }
        };

        swRawOrCooked.setOnCheckedChangeListener(switchChange);
        swFridgeOrFreezer.setOnCheckedChangeListener(switchChange);

        spinner1.setOnItemSelectedListener(foodChange);
        spinner2.setOnItemSelectedListener(foodChange);
    }

    // when the user changes an ingredient, we need to change the textList that shows what the
    // label will look like
    private void setIngredientList() {
        int rawOrCooked = getRawOrCooked();
        int fridgeOrFreezer = getFridgeOrFreezer();
        textIngredientList.setText(Food.getPrintableText(this, getIngredients(), rawOrCooked, fridgeOrFreezer));
    }

    // look at our ingredient spinners and return an ArrayList of Strings with our selected ingredients.
    private ArrayList getIngredients() {
        ArrayList ingredients = new ArrayList();

        ingredients.add(spinner1.getSelectedItem().toString());
        ingredients.add(spinner2.getSelectedItem().toString());

        return ingredients;
    }

    // what we do when the user clicks "Print Label"
    public void printLabel(View v) {
        doEscPosPrint();
    }

    private int getFridgeOrFreezer() {
        if (swFridgeOrFreezer.isChecked())
            return Food.STORE_FREEZER;
        else
            return Food.STORE_FRIDGE;
    }

    private int getRawOrCooked() {
        if (swRawOrCooked.isChecked())
            return Food.COOKED;
        else
            return Food.RAW;
    }

    private void doEscPosPrint() {
        String textToPrint = "<BIG>Text Title<BR>Testing <BIG>BIG<BR><BIG><BOLD>" +
                "string <SMALL> text<BR><LEFT>Left aligned<BR><CENTER>" +
                "Center aligned<BR><UNDERLINE>underline text<BR><QR>12345678<BR>" +
                "<CENTER>QR: 12345678<BR>Line<BR><LINE><BR>Double Line<BR><DLINE><BR><CUT>";

        int rawOrCooked = getRawOrCooked();
        int fridgeOrFreezer = getFridgeOrFreezer();


        textToPrint = Food.getEscPosText(this, getIngredients(), rawOrCooked, fridgeOrFreezer);

        // the pe.diegoveleper.printing is how we talk to the esc/pos printer driver that must
        // be installed on the Android device.
        // https://play.google.com/store/apps/details?id=pe.diegoveloper.printerserverapp&hl=en

        Intent intent = new Intent("pe.diegoveloper.printing");
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_TEXT,textToPrint);
        startActivity(intent);
    }

    // no longer using this, may want to delete.  this uses a different printer driver that was unreliable.
    private void doEscPosPrintOLD() {
        String dataToPrint="$big$This is a printer test$intro$posprinterdriver.com$intro$$intro$$cut$$intro$";
        // String dataToPrint="$big$This is a printer test$intro$posprinterdriver.com$intro$a á à ä e é è ë i í ì ï o ó ò ö u ú ù ü $intro$$cut$$intro$";
        //String BtDevice = "DC:0D:30:07:1F:A5"; puqu
        String BtDevice = "0F:02:17:A1:78:94"; // welquic

        Intent intentPrint = new Intent();
        intentPrint.setAction(Intent.ACTION_SEND);
        intentPrint.putExtra(Intent.EXTRA_TEXT, dataToPrint);
        intentPrint.putExtra("printer_type_id", "4");// For bluetooth
        intentPrint.putExtra("printer_bt_adress", BtDevice);
        // intentPrint.putExtra("printer_bt_address", BtDevice);
        intentPrint.setType("text/plain");
        this.startActivity(intentPrint);
    }
}
