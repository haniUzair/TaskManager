package com.example.taskmanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;

public class TaskFragment extends Fragment {

    private ViewGroup tasksParent;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        tasksParent = view.findViewById(R.id.ll_tasks); // Corrected ID here
        databaseReference = FirebaseDatabase.getInstance().getReference("Tasks");

        // Get the tasks from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tasksParent.removeAllViews(); // Clear previous tasks

                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    // Inflate the item_task.xml layout
                    View taskView = inflater.inflate(R.layout.item_task, null);

                    // Get task data from the snapshot
                    String taskTitle = taskSnapshot.child("title").getValue(String.class);
                    String taskDate = taskSnapshot.child("date").getValue(String.class);
                    String taskStartTime = taskSnapshot.child("startTime").getValue(String.class);
                    String taskEndTime = taskSnapshot.child("endTime").getValue(String.class);
                    String taskDescription = taskSnapshot.child("description").getValue(String.class);

                    // Populate the views in the item_task layout
                    TextView tvTaskTitle = taskView.findViewById(R.id.tv_task_title);
                    TextView tvTaskDate = taskView.findViewById(R.id.tv_task_date);
                    TextView tvTaskTime = taskView.findViewById(R.id.tv_task_time);
                    TextView tvTaskDescription = taskView.findViewById(R.id.tv_task_description);

                    tvTaskTitle.setText(taskTitle);
                    tvTaskDate.setText(taskDate);
                    tvTaskTime.setText(taskStartTime + " - " + taskEndTime);
                    tvTaskDescription.setText(taskDescription);

                    // Add the inflated task view to the tasksParent
                    tasksParent.addView(taskView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        });

        // Get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView tvGreeting = view.findViewById(R.id.tv_greeting);
        TextView tvDate = view.findViewById(R.id.tv_date);

        if (user != null) {
            String userId = user.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId); // Adjusted database reference here
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userName = dataSnapshot.child("name").getValue(String.class);
                        if (tvGreeting != null) {
                            tvGreeting.setText("Hello " + userName);
                        }
                    } else {
                        if (tvGreeting != null) {
                            tvGreeting.setText("Hello User");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                    if (tvGreeting != null) {
                        tvGreeting.setText("Hello User");
                    }
                }
            });
        } else {
            if (tvGreeting != null) {
                tvGreeting.setText("Hello User");
            }
        }
        String currentDate = DateFormat.getDateInstance().format(new Date());
        tvDate.setText(currentDate);
        return view;
    }
}




