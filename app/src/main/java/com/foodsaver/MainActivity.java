package com.foodsaver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void printLabel(View v) {
        Intent intent = new Intent(this, PrintLabelActivity.class);
        startActivity(intent);
    }

    public void viewFoods(View v) {
        Intent intent = new Intent(this, ViewFoodsActivity.class);
        startActivity(intent);
    }
}
