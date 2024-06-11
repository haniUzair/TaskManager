package com.example.taskmanager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class CreateTaskFragment extends Fragment {

    private EditText etTaskDate, etStartTime, etEndTime, etTitle, etDescription, etCategory;
    private ChipGroup chipGroup;
    private FrameLayout successScreenContainer;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);

        // Initialize UI components
        etTaskDate = view.findViewById(R.id.et_task_date);
        etStartTime = view.findViewById(R.id.et_start_time);
        etEndTime = view.findViewById(R.id.et_end_time);
        etTitle = view.findViewById(R.id.et_task_title);
        etDescription = view.findViewById(R.id.et_task_description);
        etCategory = view.findViewById(R.id.et_add_category);
        chipGroup = view.findViewById(R.id.chip_group);
        successScreenContainer = view.findViewById(R.id.success_screen_container);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");

        // Handle date picker
        etTaskDate.setOnClickListener(v -> showDatePickerDialog());

        // Handle time picker for start time
        etStartTime.setOnClickListener(v -> showTimePickerDialog(etStartTime));

        // Handle time picker for end time
        etEndTime.setOnClickListener(v -> showTimePickerDialog(etEndTime));

        // Handle add category
        Button btnAddCategory = view.findViewById(R.id.btn_add_category);
        btnAddCategory.setOnClickListener(v -> addCategory());

        // Handle task creation
        Button btnCreateTask = view.findViewById(R.id.btn_create_task);
        btnCreateTask.setOnClickListener(v -> createTask());

        // Add click listeners for TextViews in the GridLayout
        int[] textViewIds = {
                R.id.textview_faculty, R.id.textview_work, R.id.textview_designer,
                R.id.textview_backend, R.id.textview_uiux, R.id.textview_design,
                R.id.textview_library, R.id.textview_course, R.id.textview_frontend
        };

        for (int id : textViewIds) {
            TextView textView = view.findViewById(id);
            textView.setOnClickListener(v -> etCategory.append(textView.getText().toString() + " "));
        }

        return view;
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year1, month1, dayOfMonth) -> {
            String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            etTaskDate.setText(date);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), (view, hourOfDay, minute1) -> {
            String time = hourOfDay + ":" + (minute1 < 10 ? "0" + minute1 : minute1);
            editText.setText(time);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    private void addCategory() {
        String category = etCategory.getText().toString().trim();
        if (!category.isEmpty()) {
            Chip chip = new Chip(getActivity());
            chip.setText(category);
            chip.setCloseIconVisible(true);
            chip.setOnCloseIconClickListener(v -> chipGroup.removeView(chip));
            chipGroup.addView(chip);
            etCategory.setText("");
        } else {
            Toast.makeText(getActivity(), "Category cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private void createTask() {
        String title = etTitle.getText().toString().trim();
        String date = etTaskDate.getText().toString().trim();
        String startTime = etStartTime.getText().toString().trim();
        String endTime = etEndTime.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(date) && !TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            // Get the current user's ID
            String userId = auth.getCurrentUser().getUid();

            // Generate a unique task ID
            String taskId = databaseReference.push().getKey();

            // Create a new Task object
            Task task = new Task(taskId, title, date, startTime, endTime, description);

            // Add categories to the task
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                task.addCategory(chip.getText().toString());
            }

            // Add the task to Firebase Database
            databaseReference.child(taskId).setValue(task)
                    .addOnSuccessListener(aVoid -> showSuccessScreen())
                    .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to create task: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void showSuccessScreen() {
        // Inflate the success screen layout
        View successScreen = LayoutInflater.from(getContext()).inflate(R.layout.success_screentaskadded, null);
        successScreenContainer.addView(successScreen);

        // Play the success sound
        MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.success_sound);
        mediaPlayer.start();

        // Remove the success screen after a few seconds
        new Handler().postDelayed(() -> {
            successScreenContainer.removeView(successScreen);
            mediaPlayer.release();
            getFragmentManager().popBackStack();
        }, 3000); // Show the success screen for 3 seconds
    }
}
