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

public class TaskFragment extends Fragment {

    private TextView tvGreeting;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        tvGreeting = view.findViewById(R.id.tv_greeting);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Get the user's ID
            String userId = user.getUid();
            // Retrieve the username from the database
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Get the User object
                        RegistrationActivity.User currentUser = dataSnapshot.getValue(RegistrationActivity.User.class);
                        if (currentUser != null) {
                            String userName = currentUser.name; // Get the user's name
                            tvGreeting.setText("Hello " + userName);
                        }
                    } else {
                        tvGreeting.setText("Hello User");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle possible errors
                    tvGreeting.setText("Hello User");
                }
            });
        } else {
            // Handle the case where the user is not logged in
            tvGreeting.setText("Hello User");
        }

        return view;
    }
}
