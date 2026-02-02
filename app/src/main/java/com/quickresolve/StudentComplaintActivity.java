package com.quickresolve;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StudentComplaintActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEnrollment, etMobile, etOtherBranch, etDescription;
    private TextInputLayout tilOtherBranch;
    private RadioGroup rgBranch;
    private RadioButton rbOther;
    private Spinner spinnerCategory;
    private MaterialButton btnSubmit;

    private FirebaseFirestore db;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_complaint);

        // Firebase
        db = FirebaseFirestore.getInstance();

        // Views
        etFullName = findViewById(R.id.etFullName);
        etEnrollment = findViewById(R.id.etEnrollment);
        etMobile = findViewById(R.id.etMobile);
        etOtherBranch = findViewById(R.id.etOtherBranch);
        etDescription = findViewById(R.id.etDescribeProblem);
        tilOtherBranch = findViewById(R.id.tilOtherBranch);

        rgBranch = findViewById(R.id.rgBranch);
        rbOther = findViewById(R.id.rbOther);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSubmit = findViewById(R.id.btnSubmitComplaint);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(v -> finish());

        // Default branch
        rgBranch.check(R.id.rbICT);

        // Spinner
        String[] categories = {"Infrastructure", "Faculty", "Labs", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Show other branch
        rgBranch.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == rbOther.getId()) {
                tilOtherBranch.setVisibility(View.VISIBLE);
            } else {
                tilOtherBranch.setVisibility(View.GONE);
            }
        });

        // Submit
        btnSubmit.setOnClickListener(v -> submitComplaint());
    }

    private void submitComplaint() {

        String fullName = etFullName.getText().toString().trim();
        String enrollment = etEnrollment.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        int selectedBranchId = rgBranch.getCheckedRadioButtonId();
        if (selectedBranchId == -1) {
            Toast.makeText(this, "Select branch", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton rb = findViewById(selectedBranchId);
        String branch = rb.getText().toString();
        if (branch.equals("Other")) {
            branch = etOtherBranch.getText().toString().trim();
        }

        if (fullName.isEmpty() || enrollment.isEmpty() ||
                mobile.isEmpty() || branch.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> complaint = new HashMap<>();
        complaint.put("uid", uid);
        complaint.put("fullName", fullName);
        complaint.put("enrollment", enrollment);
        complaint.put("mobile", mobile);
        complaint.put("branch", branch);
        complaint.put("category", spinnerCategory.getSelectedItem().toString());
        complaint.put("description", description);
        complaint.put("timestamp", System.currentTimeMillis());

        db.collection("complaints")
                .add(complaint)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "Complaint submitted", Toast.LENGTH_SHORT).show();
                    Log.d("Firestore", "Saved: " + doc.getId());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Submission failed", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error", e);
                });
    }
}
