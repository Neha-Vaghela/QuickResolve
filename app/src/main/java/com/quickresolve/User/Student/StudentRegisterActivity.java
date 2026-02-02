package com.quickresolve.User.Student;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.quickresolve.R;

import java.util.HashMap;
import java.util.Map;

public class StudentRegisterActivity extends AppCompatActivity {
    MaterialButton btnRegister;
    TextInputEditText etName,etEnrollment,etMobile,etEmail,etPassword;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_register);

        btnRegister=findViewById(R.id.btnRegister);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        etName        = findViewById(R.id.etName);
        etEnrollment  = findViewById(R.id.etEnrollment);
        etMobile      = findViewById(R.id.etMobile);
        etEmail       = findViewById(R.id.etEmail);
        etPassword    = findViewById(R.id.etPassword);

        btnRegister.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String enrollment = etEnrollment.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            // ✅ Validation
            if(name.isEmpty() || enrollment.isEmpty() || mobile.isEmpty()
                    || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this,"Fill all fields",Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnSuccessListener(result -> {

                        String uid = auth.getCurrentUser().getUid();

                        Map<String, Object> student = new HashMap<>();
                        student.put("fullName", name);
                        student.put("enrollment", enrollment);
                        student.put("mobile", mobile);
                        student.put("email", email);

                        db.collection("students")
                                .document(uid)
                                .set(student)
                                .addOnSuccessListener(unused -> {
                                    // ✅ REAL success
                                    Toast.makeText(this,
                                            "Profile created successfully!",
                                            Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(this,
                                                "Firestore Error: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show()
                                );
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(this,
                                    "Auth Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show()
                    );
        });



    }
}