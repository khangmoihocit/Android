package com.khangmoihocit.handleevent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DateTimePickerActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText eTime, eDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time_picker);

        eTime = findViewById(R.id.eTime);
        eDate = findViewById(R.id.eDate);

        eTime.setOnClickListener(this);
        eDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == eTime){
            Calendar c = Calendar.getInstance();
            int hh = c.get(Calendar.HOUR_OF_DAY);
            int mm = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    eTime.setText(hourOfDay + ": " + minute);
                }
            }, hh,mm, false);
            timePickerDialog.show(); //hiện thị đồng hồ để chọn time
        }

        if(v == eDate){
            Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    eDate.setText(year + "/" +(month + 1) + "/" + dayOfMonth);
                }
            }, y, m, d);

            datePickerDialog.show();
        }
    }
}