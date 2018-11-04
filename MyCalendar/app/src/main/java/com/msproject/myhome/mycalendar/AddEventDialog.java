package com.msproject.myhome.mycalendar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AddEventDialog extends DialogFragment{
    String day;
    LocalDate calendar;
    ArrayList<Event> myEvent;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    ImageView backButton;
    FloatingActionButton fab;
    EditText input;

    public AddEventDialog() {
        super();

        myEvent = new ArrayList<>();
    }

    public void setLocalDate(LocalDate localDate){
        this.calendar = localDate;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_event, container);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        TextView dayText = view.findViewById(R.id.date_text);
        input = view.findViewById(R.id.edittext);
        dayText.setText(calendar.getYear() + ". " + calendar.getMonthOfYear() + ". " + calendar.getDayOfMonth()  + ".");

        Button cancel = view.findViewById(R.id.cancel_button);
        Button submit = view.findViewById(R.id.submit_button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("EVENT", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit();

                SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd");

                String key = format.format(calendar.toDate());
                Log.d("keyA==", key);
                editor.putString(key, sharedPreferences.getString(key, "") + " " + input.getText().toString());
                editor.commit();
                dismiss();
                MainActivity.changeFragmentMonthly();
            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
    }






}
