package com.quickresolve.User.Student;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.quickresolve.R;

public class StudentProfileActivity extends AppCompatActivity {

    TextView tvName, tvEnrollment, tvBranch, tvMobile;
    Button btnMyComplaints;
    FirebaseFirestore db;
    String uid;

    String enrollment = "423"; // from login / sharedPref

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);





                tvName = findViewById(R.id.tvName);
                tvEnrollment = findViewById(R.id.tvEnrollment);
                tvBranch = findViewById(R.id.tvBranch);
                tvMobile = findViewById(R.id.tvMobile);
                btnMyComplaints = findViewById(R.id.btnMyComplaints);
                uid = getIntent().getStringExtra("uid");

                db = FirebaseFirestore.getInstance();

                loadProfile();

                btnMyComplaints.setOnClickListener(v -> {
//                    Intent i = new Intent(this, MyComplaintsActivity.class);
//                    i.putExtra("enrollment", enrollment);
//                    startActivity(i);
                });
            }

    private void loadProfile() {
        db.collection("students")
                .document(uid)
                .addSnapshotListener((value, error) -> {
                    if (value != null && value.exists()) {
                        tvName.setText(value.getString("fullName"));
                        tvEnrollment.setText(value.getString("enrollment"));
                        tvMobile.setText(value.getString("mobile"));
//                        tvBranch.setText("Branch: " + value.getString("branch"));
                    }
                });
    }
}