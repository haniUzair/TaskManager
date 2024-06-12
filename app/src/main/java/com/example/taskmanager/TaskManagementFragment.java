package com.example.taskmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TaskManagementFragment extends Fragment {

    private LinearLayout taskList;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private ViewGroup successScreenContainer;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_management, container, false);

        taskList = view.findViewById(R.id.task_list);
        successScreenContainer = view.findViewById(R.id.success_screen_container);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");

        loadTasks();
        addStaticTasks(inflater); // Add static tasks

        return view;
    }

    private void loadTasks() {
        String userId = auth.getCurrentUser().getUid();

        databaseReference.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskList.removeAllViews();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Task task = snapshot.getValue(Task.class);
                    addTaskToUI(task);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    private void addTaskToUI(Task task) {
        View taskView = getLayoutInflater().inflate(R.layout.task_item, null);
        TextView taskTitle = taskView.findViewById(R.id.task_title);
        TextView taskDescription = taskView.findViewById(R.id.task_description);
        CheckBox taskCheckbox = taskView.findViewById(R.id.task_checkbox);

        taskTitle.setText(task.getTitle());
        taskDescription.setText(task.getDescription());

        taskCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                deleteTask(task.getId());
            }
        });

        taskList.addView(taskView);
    }

    private void deleteTask(String taskId) {
        databaseReference.child(taskId).removeValue().addOnSuccessListener(aVoid -> showSuccessScreen());
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
        }, 3000); // Show the success screen for 3 seconds
    }

    private void addStaticTasks(LayoutInflater inflater) {
        for (int i = 1; i <= 8; i++) {
            View taskView = inflater.inflate(R.layout.task_item, null);
            TextView taskTitle = taskView.findViewById(R.id.task_title);
            TextView taskDescription = taskView.findViewById(R.id.task_description);
            CheckBox taskCheckbox = taskView.findViewById(R.id.task_checkbox);

            taskTitle.setText("Task " + i);
            if (i == 1) {
                taskTitle.setTextColor(getResources().getColor(R.color.green));
            }

            if (i == 1 || i == 6 || i == 7 || i == 8) {
                taskDescription.setText("Study for the test");
            } else {
                taskDescription.setText("Go to the gym");
            }

            taskCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    showSuccessScreen();
                } else {
                    // Handle checkbox unchecked state if needed
                }
            });

            taskList.addView(taskView);
        }
    }
}
