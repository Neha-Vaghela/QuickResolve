package com.quickresolve; // Replace with your package name

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.quickresolve.User.Student.StudentProfileActivity;

public class
MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    // UI Elements
    private MaterialButton feedbackSubmitButton;
    private RatingBar feedbackRating;
    private TextInputEditText feedbackComment;
    ImageView complaint;

    MaterialButton btnExplore,com_list;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Links to your XML

        // Initialize Views
        drawerLayout = findViewById(R.id.drawer_layout);
        btnExplore = findViewById(R.id.btnExplore);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        feedbackSubmitButton = findViewById(R.id.feedback_submit_button);
        feedbackRating = findViewById(R.id.feedback_rating);
        feedbackComment = findViewById(R.id.feedback_comment);
        complaint = findViewById(R.id.complaint); // Add this BEFORE setOnClickListener
        com_list = findViewById(R.id.com_list);

        complaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, StudentComplaintActivity.class));
            }
        });

        com_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ComplaintListActivity.class));
            }
        });
        // Set up Toolbar
        setSupportActionBar(toolbar);

        // Set up Navigation Drawer Toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,  // Add these strings to strings.xml
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set Navigation Item Listener
        navigationView.setNavigationItemSelectedListener(this);


        feedbackSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle feedback submission
                float rating = feedbackRating.getRating();
                String comment = feedbackComment.getText().toString();
                // Process feedback (e.g., send to server)
                Toast.makeText(MainActivity.this, "Feedback submitted: " + rating + " stars, " + comment, Toast.LENGTH_SHORT).show();
                // Reset fields
                feedbackRating.setRating(0);
                feedbackComment.setText("");
            }
        });

        btnExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FeatureActivity.class));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.profile) {
            startActivity(new Intent(MainActivity.this, StudentProfileActivity.class));
        } else if (id == R.id.nav_home) {
            // Already on home
        } else if (id == R.id.nav_my_complaints) {
            // Navigate to complaints
            Toast.makeText(this, "My Complaints", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_explore_features) {
            Toast.makeText(this, "Move on ExploreFeaturesActivity ", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_feedback) {
            // Scroll to feedback or show dialog
            Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_help) {
            Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}