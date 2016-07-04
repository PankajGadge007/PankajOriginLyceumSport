package com.example.admin.lyceumsport;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;


public class FeeDeposite extends FragmentActivity {
    EditText mEdit,mEdit1,mEdit2,mEdit3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedeposite);
    }

    public void selectDate(View view) {
        DialogFragment newFragment = new SelectDateFragment();
        newFragment.show(getSupportFragmentManager(), "DatePicker");
    }
    public void populateSetDate(int year, int month, int day) {
        mEdit = (EditText)findViewById(R.id.editText0);
        mEdit.setText(day+"/"+month+"/"+year);

    }
    public void populateSetDate1(int year, int month, int day) {
        mEdit1 = (EditText)findViewById(R.id.editTexts);
        mEdit1.setText(day+"/"+month+"/"+year);

    }
    public void populateSetDate2(int year, int month, int day) {
        mEdit2 = (EditText)findViewById(R.id.editText5);
        mEdit2.setText(day+"/"+month+"/"+year);

    }
    public void populateSetDate3(int year, int month, int day) {
        mEdit3 = (EditText)findViewById(R.id.editText6);
        mEdit3.setText(day+"/"+month+"/"+year);

    }


    public class  SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {

            populateSetDate(yy, mm+1, dd);
            populateSetDate1(yy, mm+1, dd);
            populateSetDate2(yy, mm+1, dd);
            populateSetDate3(yy, mm+1, dd);
        }
    }

}