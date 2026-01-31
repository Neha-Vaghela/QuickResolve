package com.quickresolve;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class StudentComplaintActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etEnrollment, etMobile, etOtherBranch, etDescription;
    private TextInputLayout tilOtherBranch;

    private RadioGroup rgBranch;
    private RadioButton rbOther;
    private Spinner spinnerCategory;
    private MaterialButton btnSubmit, btnAttachImage;

    private Uri imageUri; // optional image
    private FirebaseFirestore db;
    private StorageReference storageRef;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_complaint);

        // Firebase instances
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        // Initialize views
        etFullName = findViewById(R.id.etFullName);
        etEnrollment = findViewById(R.id.etEnrollment);
        etMobile = findViewById(R.id.etMobile);
        etOtherBranch = findViewById(R.id.etOtherBranch);
        tilOtherBranch = findViewById(R.id.tilOtherBranch);
        etDescription = findViewById(R.id.etDescribeProblem);

        rgBranch = findViewById(R.id.rgBranch);
        rbOther = findViewById(R.id.rbOther);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSubmit = findViewById(R.id.btnSubmitComplaint);
        btnAttachImage = findViewById(R.id.btnAttachImage);

        FirebaseDatabase.getInstance().getReference().child("Test").child("Test another").setValue("NEha");



    // Set default branch selection
        rgBranch.check(R.id.rbICT);
        submitDefaultComplaint();

        // Spinner setup
        String[] categories = {"Infrastructure", "Faculty", "Labs", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Show Other Branch field only if "Other" is selected
        rgBranch.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == rbOther.getId()) {
                tilOtherBranch.setVisibility(android.view.View.VISIBLE);
            } else {
                tilOtherBranch.setVisibility(android.view.View.GONE);
            }
        });

        // Image picker placeholder
        btnAttachImage.setOnClickListener(v ->
                Toast.makeText(this, "Image picker not implemented yet", Toast.LENGTH_SHORT).show()
        );

        // Submit button
        btnSubmit.setOnClickListener(v -> {
            Log.d("ComplaintActivity", "Submit clicked");
            submitComplaint();
        });
    }

    private void submitComplaint() {
        // Get input values safely
        String fullName = etFullName.getText() != null ? etFullName.getText().toString().trim() : "";
        String enrollment = etEnrollment.getText() != null ? etEnrollment.getText().toString().trim() : "";
        String mobile = etMobile.getText() != null ? etMobile.getText().toString().trim() : "";
        String description = etDescription.getText() != null ? etDescription.getText().toString().trim() : "";
        String otherBranch = etOtherBranch.getText() != null ? etOtherBranch.getText().toString().trim() : "";

        // Branch selection
        int selectedBranchId = rgBranch.getCheckedRadioButtonId();
        String branch = "";
        if (selectedBranchId != -1) {
            RadioButton selectedBranchButton = findViewById(selectedBranchId);
            branch = selectedBranchButton.getText().toString();
            if (branch.equals("Other")) {
                branch = otherBranch;
            }
        } else {
            Toast.makeText(this, "Please select a branch", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate required fields
        if (fullName.isEmpty() || enrollment.isEmpty() || mobile.isEmpty() || branch.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String category = spinnerCategory.getSelectedItem() != null ?
                spinnerCategory.getSelectedItem().toString() : "Other";

        // Prepare data map
        Map<String, Object> complaintData = new HashMap<>();
        complaintData.put("fullName", fullName);
        complaintData.put("enrollment", enrollment);
        complaintData.put("mobile", mobile);
        complaintData.put("branch", branch);
        complaintData.put("category", category);
        complaintData.put("description", description);
        complaintData.put("timestamp", System.currentTimeMillis());

        // Upload image if selected
        if (imageUri != null) {
            StorageReference imgRef = storageRef.child("complaint_images/" + System.currentTimeMillis() + ".jpg");
            imgRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                complaintData.put("imageUrl", uri.toString());
                                saveToFirestore(complaintData);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                                Log.e("Firestore", "Image URL error", e);
                            }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        Log.e("Firestore", "Image upload error", e);
                    });
        } else {
            saveToFirestore(complaintData);
        }
    }

    private void saveToFirestore(Map<String, Object> data) {
        db.collection("complaints")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Complaint submitted successfully", Toast.LENGTH_SHORT).show();
                    Log.d("Firestore", "Complaint added: " + documentReference.getId());

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to submit complaint", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Firestore add error", e);
                });
    }
    private void submitDefaultComplaint() {
        // Default / test data
        String fullName = "Test User";
        String enrollment = "123456";
        String mobile = "9876543210";
        String description = "This is a test complaint description.";
        String branch = "ICT"; // default branch
        String category = "Infrastructure"; // default category

        // Prepare data map
        Map<String, Object> complaintData = new HashMap<>();
        complaintData.put("fullName", fullName);
        complaintData.put("enrollment", enrollment);
        complaintData.put("mobile", mobile);
        complaintData.put("branch", branch);
        complaintData.put("category", category);
        complaintData.put("description", description);
        complaintData.put("timestamp", System.currentTimeMillis());

        // If you want to test with an image, assign a Uri to imageUri here
        // imageUri = ...

        // Upload image if exists
        if (imageUri != null) {
            StorageReference imgRef = storageRef.child("complaint_images/" + System.currentTimeMillis() + ".jpg");
            imgRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imgRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                complaintData.put("imageUrl", uri.toString());
                                saveToFirestore(complaintData);
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                            }))
                    .addOnFailureListener(e -> Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show());
        } else {
            saveToFirestore(complaintData);
        }
    }

// Call this method wherever you want to submit default data
// For example, in onCreate for testing:
// submitDefaultComplaint();

}
