package com.example.taskmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory;

import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    private EditText etTaskDate;
    private EditText etStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FirebaseApp.initializeApp(this);
        // Load the initial fragment when the app starts
        if (savedInstanceState == null) {
            loadFragment(new TaskFragment());
        }

        // Set up the Floating Action Button (FAB)
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> openCreateTaskFragment());

        // Set up the bottom navigation view
        BottomNavigationView navView = findViewById(R.id.bottom_navigation);
        navView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            // Use if-else statements instead of switch-case
            if (itemId == R.id.navigation_dashboard) {
                selectedFragment = new TaskManagementFragment();
            } else if (itemId == R.id.navigation_notifications) {
                selectedFragment = new NotificationsFragment();
            } else if (itemId == R.id.navigation_profile) {
                // Open the ProfileActivity
                Intent intent = new Intent(HomeActivity.this, ProfileFragment.class);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.navigation_calendar) {
                // Open the calendar activity
                Intent intent = new Intent(HomeActivity.this, CalendarActivity.class);
                startActivity(intent);
                return true;
            } else {
                return false;
            }

            // Load the selected fragment
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(PlayIntegrityAppCheckProviderFactory.getInstance());
    }

    // Method to load the selected fragment
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }

    // Method to open the CreateTaskFragment
    private void openCreateTaskFragment() {
        Fragment fragment = new CreateTaskFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    // Method to show the DatePickerDialog
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    String date = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                    etTaskDate.setText(date);
                }, year, month, day);

        datePickerDialog.show();
    }

    // Method to show the TimePickerDialog
    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(HomeActivity.this,
                (view, hourOfDay, minute1) -> {
                    String time = String.format("%02d:%02d", hourOfDay, minute1);
                    etStartTime.setText(time);
                }, hour, minute, true);

        timePickerDialog.show();
    }
}
