package com.example.class01;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CalendarView calView = findViewById(R.id.calView);

        EditText datePicker = findViewById(R.id.datePicker);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        LocalDate date = LocalDate.now();
        datePicker.setText(date.toString());
        datePicker.setFocusable(true);

        EditText editTitle = findViewById(R.id.editTitle);
        MultiAutoCompleteTextView editContent = findViewById(R.id.editContent);

        calView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                datePicker.setText(String.format("%d-%d-%d", year, month + 1, day));
            }
        });

        datePicker.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    return !setCalendarView(calView, datePicker);
                }
                return false;
            }
        });

        datePicker.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus){
                    setCalendarView(calView, datePicker);
                }
            }
        });

    }
    public void alertShow(String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    public boolean setCalendarView(CalendarView calView, EditText datePicker){

        String dateStr = datePicker.getText().toString().replaceAll("-","");
        boolean flag = setDatePickerText(dateStr,datePicker);

        if(flag){

            int year = Integer.parseInt(dateStr.substring(0,4));
            int month = Integer.parseInt(dateStr.substring(4,6)) - 1;
            int day = Integer.parseInt(dateStr.substring(6,8));
            Calendar maxCar = Calendar.getInstance();
            maxCar.set(year , month, 1);
            if( year < 0 || month > 11 || month < 0 || day > maxCar.getActualMaximum(Calendar.DAY_OF_MONTH) || day < 1){
                alertShow("오류","잘못된 날짜입니다.");
                datePicker.setText("");
                return false;
            }
            Date date = new Date(year, month, day);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DATE, day);
            long time = cal.getTimeInMillis();
            calView.setDate(time);

        }
        return flag;
    }
    public boolean setDatePickerText(String dateStr, EditText datePicker){

        boolean flag = false;

        if(dateStr.length() > 8){
            alertShow("오류","잘못된 형식입니다.\nyyyy-mm-dd형식으로 입력해주세요.");
            datePicker.setText("");
            datePicker.requestFocus();
            return false;
        }

        if(dateStr.length() == 8){
            String year = dateStr.substring(0,4);
            String month = dateStr.substring(4,6);
            String day = dateStr.substring(6,8);
            datePicker.setText(String.format("%s-%s-%s",year,month,day));
            flag = true;
        }else if(dateStr.length() > 6){
            String year = dateStr.substring(0,4);
            String month = dateStr.substring(4,6);
            String day = dateStr.substring(6,dateStr.length());
            datePicker.setText(String.format("%s-%s-%s",year,month,day));
            flag = false;
        }else if(dateStr.length() > 4){
            String year = dateStr.substring(0,4);
            String month = dateStr.substring(4,dateStr.length());
            datePicker.setText(String.format("%s-%s",year,month));
            flag = false;
        }

        return flag;

    }
}