package com.example.taskmanager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_screen);

        // Play success sound
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.success_sound);
        mediaPlayer.start();

        // Release the MediaPlayer resources once the sound is done playing
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);

        // Delay before finishing this activity and returning to the previous one
        new Handler().postDelayed(() -> {
            // Return to the previous activity
            Intent returnIntent = new Intent(SuccessActivity.this, MainActivity.class);
            returnIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(returnIntent);
            finish();
        }, 2000); // 2 seconds delay
    }
}
