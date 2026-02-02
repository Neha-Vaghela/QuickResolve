package com.quickresolve;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.quickresolve.User.Student.StudentProfileActivity;
import com.quickresolve.User.Student.StudentRegisterActivity;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    FirebaseAuth auth;
    TextInputEditText etEmail,etPassword;
    TextView create_acc;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        create_acc=findViewById(R.id.create_acc);
        auth = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String pass = etPassword.getText().toString();

                auth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(result -> {
                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            Intent i = new Intent(LoginActivity.this, StudentProfileActivity.class);
                            i.putExtra("uid", uid);
                            startActivity(i);

                            finish();
                        });
            }
        });

        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, StudentRegisterActivity.class));
            }
        });

    }
}