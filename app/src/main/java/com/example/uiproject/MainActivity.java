package com.example.uiproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity  {

    final String month[] = {"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec"};
    final String sub[] ={"Airlaw","criminal","Income tax","Direct tax","Customs", "Defence & Security Forces" , "Disinvestment" , "Education" , "Election" ,
            "Electricity & Energy" , "Environment, Wildlife & Animal", "Exchange Control & FDI " , "Excise"};

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);




        final LinearLayout button = (LinearLayout) findViewById(R.id.lay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });

    }

    private void openDialog() {
        //Inflating a LinearLayout dynamically to add TextInputLayout
        //This will be added in AlertDialog
        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.view_number_dialog, null);
        NumberPicker numberpicker = (NumberPicker) linearLayout.findViewById(R.id.numberPicker1);
        /*NumberPicker numberPicker1 = (NumberPicker) linearLayout.findViewById(R.id.numberPicker2);
        NumberPicker numberPicker2 = (NumberPicker) linearLayout.findViewById(R.id.numberPicker3);*/


        String[] arrayPicker= new String[]{"10\u2103","20\u2103","100\u2103"};

        numberpicker.setMinValue(0);
        numberpicker.setMaxValue(2);
        numberpicker.setDisplayedValues( arrayPicker );



      /*  numberPicker1.setMinValue(0);
        numberPicker1.setMaxValue(month.length - 1);
        numberPicker1.setDisplayedValues(month);
        numberPicker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker2.setMinValue(0);
        numberPicker2.setMaxValue(sub.length - 1);
        numberPicker2.setDisplayedValues(sub);
        numberPicker2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);*/
        //Finally building an AlertDialog
        final AlertDialog builder = new MaterialAlertDialogBuilder(this,R.style.MaterialAlertDialog_rounded
        )
                .setPositiveButton("Submit", null)
                .setNegativeButton("Cancel", null)
                .setView(linearLayout)
                .setCancelable(false)
                .create();

        builder.show();


        //Setting up OnClickListener on positive button of AlertDialog
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Code on submit
                TextView temptext= findViewById(R.id.temppick);
                temptext.setText(arrayPicker[numberpicker.getValue()] );
                builder.dismiss();
            }
        });
    }
}
