package com.msproject.myhome.mycalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class MainActivity extends AppCompatActivity implements MonthlyFragment.OnFragmentInteractionListener, WeeklyFragment.OnFragmentInteractionListener, DailyFragment.OnFragmentInteractionListener{
    static FragmentManager fragmentManager;
    static FragmentTransaction fragmentTransaction;
    Context mContext;
    static MonthlyFragment monthlyFragment;
    static DailyFragment dailyFragment;
    static LocalDate localDate;
    static BottomNavigationView bottomNavigationView;
    static SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        // fragment 매니저 선언
        fragmentManager = getSupportFragmentManager();
        // fragment 트랜잭션 시작
        fragmentTransaction = fragmentManager.beginTransaction();

        sharedPreferences = getSharedPreferences("EVENT", MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.navigation);
        localDate = new LocalDate();
        monthlyFragment = MonthlyFragment.newInstance();
        dailyFragment = DailyFragment.newInstance();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_monthly:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, monthlyFragment);
                        fragmentTransaction.addToBackStack(null);
                        // Commit the transaction
                        fragmentTransaction.commit();

                        return true;
                    case R.id.navigation_weekly:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, WeeklyFragment.newInstance());
                        fragmentTransaction.addToBackStack(null);
                        // Commit the transaction
                        fragmentTransaction.commit();
                        return true;
                    case R.id.navigation_daily:

                        dailyFragment.setDateTime(localDate);
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container, dailyFragment);

                        fragmentTransaction.addToBackStack(null);
                        // Commit the transaction
                        fragmentTransaction.commit();
                        return true;
                }
//            transaction.replace(R.id.content, fragment).commit();
                return false;
            }
        });
        fragmentTransaction.add(R.id.container, monthlyFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onMonthlyFragmentInteraction() {

    }

    @Override
    public void onWeeklyFragmentInteraction() {

    }
    @Override
    public void onDailyFragmentInteraction() {

    }

    public static void changeFragmentDaily(LocalDate dateTime){
        localDate = dateTime;
        dailyFragment.setDateTime(localDate);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, dailyFragment);

        fragmentTransaction.addToBackStack(null);
        // Commit the transaction
        fragmentTransaction.commit();
    }
    public static void changeFragmentMonthly(){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, monthlyFragment);
        fragmentTransaction.addToBackStack(null);
        // Commit the transaction
        fragmentTransaction.commit();
    }
}
