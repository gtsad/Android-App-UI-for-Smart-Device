package com.example.uiproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Build;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    PendingIntent pendingIntent2;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    AlarmManager alarmManager2;

    String[] arrayPickerTemps = new String[]{"5\u2103", "10\u2103", "15\u2103", "20\u2103", "25\u2103"
            , "30\u2103", "35\u2103", "40\u2103", "45\u2103", "50\u2103", "55\u2103", "60\u2103"
            , "65\u2103", "70\u2103", "75\u2103", "80\u2103", "85\u2103", "90\u2103"
            , "95\u2103", "100\u2103", "105\u2103", "110\u2103", "115\u2103", "120\u2103",
            "125\u2103", "130\u2103", "135\u2103", "140\u2103", "145\u2103", "150\u2103", "155\u2103", "160\u2103", "165\u2103"
            , "170\u2103", "175\u2103", "180\u2103", "185\u2103", "190\u2103", "195\u2103", "200\u2103", "205\u2103"
            , "210\u2103", "220\u2103", "225\u2103", "230\u2103", "235\u2103", "240\u2103", "245\u2103", "250\u2103"};

    String[] arrayPickerProgs ;
    LayoutInflater inflater ;
    View layout ;
    Toast toast;

    int mHour, mMinute;
    TextView temptext;
    TextView pr;
    ImageView lang;
    Boolean on = false;
    TextView status;
    TextView startTime;
    TextView endTime;
    ImageView plus;
    ImageView minus;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    Calendar cal_alarm;
    ImageView fun_button;
    TextView reset;
    ConstraintLayout.LayoutParams newLayoutParams;
    ImageView oven;
    public static final String mypreference = "mypref";
    SharedPreferences sharedpreferences;

    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
        }

        setContentView(R.layout.activity_main);

        ImageView help= findViewById(R.id.help);
        temptext = findViewById(R.id.temp);
        status = findViewById(R.id.status);
        endTime = findViewById(R.id.timepickend);
        startTime = findViewById(R.id.timepickstart);
        reset= findViewById(R.id.reset);
        plus= findViewById(R.id.plusfive);
        minus= findViewById(R.id.minfive);

        dateFormat = new SimpleDateFormat("HH:mm");

        lang =  findViewById(R.id.lang);
        TextView temptext = findViewById(R.id.temppick);
        TextView progtext = findViewById(R.id.progpick);
        pr = findViewById(R.id.program);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        Log.d("ok", String.valueOf(sharedpreferences.getBoolean("onbool",false)));

        if (sharedpreferences.getString("lang", "").equals("grc")) {
                setArray();
                setLocale(sharedpreferences.getString("lang", ""));
                lang.setImageResource(R.drawable.uk_flag);
        } else {
            setArray();
            setLocale(sharedpreferences.getString("lang", ""));
            lang.setImageResource(R.drawable.flag_greece);
        }


        fun_button = findViewById(R.id.onoff);
        oven=findViewById(R.id.stoveimg);
        newLayoutParams = (ConstraintLayout.LayoutParams) fun_button.getLayoutParams();

        registerReceiver(broadcastReceiver, new IntentFilter("INTERNET_LOST"));

        final LinearLayout buttontemp = findViewById(R.id.lay);
        final LinearLayout buttonprog = findViewById(R.id.lay2);


        buttontemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(arrayPickerTemps, arrayPickerTemps.length - 1, temptext);
            }
        });

        buttonprog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(arrayPickerProgs, arrayPickerProgs.length - 1, progtext);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewDialog v= new ViewDialog();
                v.showDialog(MainActivity.this);
            }
        });

        //StarTime set
        startTime.setText("now");

        //Start Time on click Listener
        startTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                buttonSelectTime(startTime);
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                buttonSelectTime(endTime);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelStop();
                cal_alarm.set(Calendar.MINUTE,cal_alarm.get(Calendar.MINUTE)-5);
                String tm= dateFormat.format(cal_alarm.getTime());
                setStopTimer(tm,true);
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelStop();
                cal_alarm.set(Calendar.MINUTE,cal_alarm.get(Calendar.MINUTE)+5);
                String tm= dateFormat.format(cal_alarm.getTime());
                setStopTimer(tm,true);
            }
        });

        //Lang change button Listener
        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String language = sharedpreferences.getString("lang", "");

                if (language.equals("grc")) {

                    setLocale("en");
                    lang.setImageResource(R.drawable.flag_greece);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("lang", "en");
                    editor.apply();
                    setArray();

                } else {

                    setLocale("grc");
                    lang.setImageResource(R.drawable.uk_flag);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("lang", "grc");
                    editor.apply();
                    setArray();
                }

            }

        });

        //On-Off Button set and Listener
        fun_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Oven is now ON
                if (!on) {

                    // If user has selected text -> not default text
                    if((!progtext.getText().toString().equals("Pick a program")& !temptext.getText().toString().equals("Pick a temperature"))&
                            (!temptext.getText().toString().equals("Επίλεξε Θερμοκρασία")&!progtext.getText().toString().equals("Επίλεξε Πρόγραμμα"))){
                        setStartTimer(startTime.getText().toString());
                        setStopTimer(endTime.getText().toString(),false);
                        on = true;
                    }
                    else {
                        custom_toast("Please select program and temperature.",MainActivity.this);
                    }

                // Oven is now OFF
                } else {
                    fun_button.setImageResource(R.drawable.turn_on);
                    on = false;

                    custom_toast("Oven is OFF",MainActivity.this);
                    sharedpreferences.edit().putBoolean("onbool", on).apply();
                    sharedpreferences.edit().putBoolean("timer",false).apply();
                    status.setVisibility(View.INVISIBLE);
                    cancelStart();
                    cancelStop();
                    setInvisible();
                    updateValues();
                }

            }

        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime.setText(R.string.now);
                endTime.setText(R.string.set);
            }
        });

    }

    public void custom_toast(String st, Activity context) {
        inflater = context.getLayoutInflater();
        layout = inflater.inflate(R.layout.toast,
                null);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(st);
         toast= new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 850);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateValues();
        }
    };

    // Sets language array
    private void setArray() {

        if (sharedpreferences.getString("lang", "").equals("grc")) {
            arrayPickerProgs=new String[]{"Γκρίλ", "Γκρίλ με αέρα", "Κάτω αντίσταση", "Κάτω Αντίσταση με αέρα", "Κάτω Αντίσταση και γκρίλ",
                    " Πάνω αντίσταση","Πάνω-Κάτω με αέρα" ,"Πάνω-Κάτω αντιστάσεις"};


        } else {

            arrayPickerProgs=new String[]{"Grill", "Grill and fan", "Lower heat element", "Fan with lower heat","Grill and lower heat"
                    ,"Upper heat element", "Fan Over","Upper and lower heating elements",};

        }

    }

    //Method that changes the Strings Language
    private void setLocale(String lang) {
        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = new Locale(lang);
        resources.updateConfiguration(configuration, metrics);
        onConfigurationChanged(configuration);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        TextView buttemp = findViewById(R.id.temppick);
        TextView butprog = findViewById(R.id.progpick);
        TextView st = findViewById(R.id.start);
        TextView end = findViewById(R.id.end);
        butprog.setText(R.string.pickprog);
        buttemp.setText(R.string.picktemp);
        temptext.setText(R.string.temp);
        st.setText(R.string.start);
        end.setText(R.string.end);
        pr.setText(R.string.prog);
        startTime.setText(R.string.now);
        endTime.setText(R.string.set);
    }

    private void buttonSelectTime(TextView editTextTime) {

        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,TimePickerDialog.THEME_HOLO_LIGHT,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar currentTime = Calendar.getInstance();
                        currentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        currentTime.set(Calendar.MINUTE, minute);
                        editTextTime.setText(dateFormat.format(currentTime.getTime()));
                    }

                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void openDialog(String[] arrayPicker, int max, TextView textView) {

        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.view_number_dialog, null);
        NumberPicker numberpicker = linearLayout.findViewById(R.id.numberPicker1);

        numberpicker.setMinValue(0);
        numberpicker.setMaxValue(max);
        numberpicker.setDisplayedValues(arrayPicker);

        final AlertDialog builder = new MaterialAlertDialogBuilder(this, R.style.MaterialAlertDialog_rounded
        )
                .setPositiveButton("Set", null)
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
                textView.setText(arrayPicker[numberpicker.getValue()]);
                builder.dismiss();
            }
        });
    }



    private void setStopTimer(String string_time,Boolean visible ) {

        calendar=Calendar.getInstance();
        status.setVisibility(View.VISIBLE);

        if (startTime.getText().equals("now") || startTime.getText().equals("τώρα")){
            status.setText("Oven start time: "+dateFormat.format(calendar.getTime())+"\nOven stop time: -");
        }
        else {
            status.setText("Oven start time: "+startTime.getText()+"\nOven stop time: -");}

        if (!string_time.equals("set") & !string_time.equals("θέσε") ) {

            if (startTime.getText().equals("now")|| startTime.getText().equals("τώρα")){
                status.setText("Oven start time: "+dateFormat.format(calendar.getTime())+"\nOven stop time: "+ string_time);
            }
            else {
                status.setText("Oven start time: "+startTime.getText()+"\nOven stop time: "+string_time);}

            if(!visible){
                setVisible();
            }

            String[] time = string_time.split(":");
            int h = Integer.parseInt(time[0].trim());
            TextView status = findViewById(R.id.status);
            int m = Integer.parseInt(time[1].trim());

            Date dat = new Date();
            cal_alarm = Calendar.getInstance();
            Calendar cal_now = Calendar.getInstance();
            cal_alarm.set(Calendar.HOUR_OF_DAY, h);
            cal_alarm.set(Calendar.MINUTE, m);
            cal_alarm.set(Calendar.SECOND, 0);
            if (cal_alarm.getTimeInMillis() +60000 < System.currentTimeMillis()) {
                cal_alarm.set(Calendar.DAY_OF_MONTH, cal_alarm.get(Calendar.DAY_OF_MONTH) + 1);
            }

            Intent intent2 = new Intent(MainActivity.this, MyBroadcastReceiver.class);
            intent2.putExtra(MyBroadcastReceiver. NOTIFICATION_ID , 1 ) ;
            intent2.putExtra(MyBroadcastReceiver. NOTIFICATION , getNotification("Oven Turned Off")) ;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { pendingIntent2 = PendingIntent.getBroadcast(
                    MainActivity.this, 1, intent2, PendingIntent.FLAG_MUTABLE); } else { pendingIntent2 = PendingIntent.getBroadcast(
                    MainActivity.this, 1, intent2, PendingIntent.FLAG_UPDATE_CURRENT); }
            alarmManager2 = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager2.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent2);
            custom_toast("Alarm set for " + string_time,MainActivity.this);
        }

        else {
          setInvisible();
        }

    }

    public void setVisible(){
        ConstraintLayout constraintLayout = findViewById(R.id.parent_layout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        constraintSet.connect(R.id.status,ConstraintSet.BOTTOM,R.id.minfive,ConstraintSet.TOP,0);
        constraintSet.applyTo(constraintLayout);

        minus.setVisibility(View.VISIBLE);
        plus.setVisibility(View.VISIBLE);
    }

    public void setInvisible(){
        ConstraintLayout constraintLayout = findViewById(R.id.parent_layout);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.status,ConstraintSet.BOTTOM,R.id.onoff,ConstraintSet.BOTTOM,0);
        constraintSet.applyTo(constraintLayout);

        minus.setVisibility(View.INVISIBLE);
        plus.setVisibility(View.INVISIBLE);
    }

    public void updateValues(){

        Log.d("change", String.valueOf(sharedpreferences.getBoolean("changes",false)));
        Log.d("onoff", String.valueOf(sharedpreferences.getBoolean("onbool",false)));

            if ((sharedpreferences.getBoolean("onbool",false))) {
                fun_button.setImageResource(R.drawable.turn_off);
                oven.setImageResource(R.drawable.oven_on);

                startTime.setEnabled(false);
                endTime.setEnabled(false);
                reset.setEnabled(false);
                sharedpreferences.edit().putBoolean("timer",false).apply();

                TranslateAnimation animation = new TranslateAnimation(0, 230, 0, 0);
                animation.setDuration(120);
                animation.setFillAfter(false);
                animation.setAnimationListener(new MyAnimationListener());
                fun_button.startAnimation(animation);

                on = true;

            } else {

                if(sharedpreferences.getBoolean("timer",false)){
                    fun_button.setImageResource(R.drawable.cancel);
                    oven.setImageResource(R.drawable.oven);
                    startTime.setEnabled(false);
                    endTime.setEnabled(false);
                    reset.setEnabled(false);

                    TranslateAnimation animation = new TranslateAnimation(0, 230, 0, 0);
                    animation.setDuration(120);
                    animation.setFillAfter(false);
                    animation.setAnimationListener(new MyAnimationListener());
                    fun_button.startAnimation(animation);

                }

                else {

                    fun_button.setImageResource(R.drawable.turn_on);
                    oven.setImageResource(R.drawable.oven);

                    status.setVisibility(View.INVISIBLE);
                    setInvisible();

                    on = false;
                    startTime.setEnabled(true);
                    endTime.setEnabled(true);
                    reset.setEnabled(true);
                    TranslateAnimation animation = new TranslateAnimation(230, 0, 0, 0);
                    animation.setDuration(120);
                    animation.setFillAfter(false);
                    animation.setAnimationListener(new MyAnimationListener());
                    fun_button.startAnimation(animation);

                }

            }

    }

    private Notification getNotification (String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder( this, default_notification_channel_id ) ;
        builder.setContentTitle( "myOVEN Update" ) ;
        builder.setContentText(content) ;
        builder.setSmallIcon(R.mipmap.logo_myoven_round ) ;
        builder.setAutoCancel( true ) ;
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        return builder.build() ;
    }


    public void setStartTimer(String string_time){

        calendar=Calendar.getInstance();
        if (string_time.equals("now") || string_time.equals("τώρα")) {

            sharedpreferences.edit().putBoolean("onbool", true).apply();

            custom_toast("Oven is ON",MainActivity.this);
            updateValues();


        } else {

            sharedpreferences.edit().putBoolean("timer",true).apply();
            updateValues();

            String[] time = string_time.split(":");
            int h = Integer.parseInt(time[0].trim());

            int m = Integer.parseInt(time[1].trim());

            Date dat = new Date();
            cal_alarm = Calendar.getInstance();
            Calendar cal_now = Calendar.getInstance();
            cal_alarm.set(Calendar.HOUR_OF_DAY, h);
            cal_alarm.set(Calendar.MINUTE, m);
            cal_alarm.set(Calendar.SECOND, 0);

            if (cal_alarm.getTimeInMillis()+60000< System.currentTimeMillis()) {
                cal_alarm.set(Calendar.DAY_OF_MONTH, cal_alarm.get(Calendar.DAY_OF_MONTH) + 1);
            }

            else if (cal_alarm.getTimeInMillis()< System.currentTimeMillis()+10000){
                sharedpreferences.edit().putBoolean("onbool", true).apply();

                custom_toast("Oven is ON",MainActivity.this);
                updateValues();

                return;
            }

            Intent intent = new Intent(MainActivity.this, MyBroadcastReceiver.class);
            intent.putExtra(MyBroadcastReceiver. NOTIFICATION_ID , 1 ) ;
            intent.putExtra(MyBroadcastReceiver. NOTIFICATION , getNotification("Oven Turned On")) ;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { pendingIntent = PendingIntent.getBroadcast(
                    MainActivity.this, 234324243, intent, PendingIntent.FLAG_MUTABLE); } else { pendingIntent = PendingIntent.getBroadcast(
                    MainActivity.this, 234324243, intent, PendingIntent.FLAG_UPDATE_CURRENT); }
            alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            alarmManager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
            custom_toast("Oven will be turned on at " +dateFormat.format(cal_alarm.getTime()),MainActivity.this);
    }

}

    public void cancelStart(){
        if(pendingIntent!=null){
            alarmManager.cancel(pendingIntent);}
    }

    public void cancelStop(){
        if(pendingIntent2!=null){
            alarmManager2.cancel(pendingIntent2);}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }

    private class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {
            fun_button.clearAnimation();
            if (sharedpreferences.getBoolean("onbool",false)||sharedpreferences.getBoolean("timer",false)){
                newLayoutParams.leftMargin = 500;
                fun_button.setLayoutParams(newLayoutParams);

            }

        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

    }

    public class ViewDialog {

        public void showDialog(Activity activity) {

            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.help_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ImageView mDialogNo = dialog.findViewById(R.id.closedi);
            mDialogNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

    }

}