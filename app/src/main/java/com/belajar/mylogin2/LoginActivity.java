package com.belajar.mylogin2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPass;
    private Button btnMasuk;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser  firebaseUser;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.edit_login_email);
        etPass = findViewById(R.id.edit_login_password);
        btnMasuk = findViewById(R.id.btn_masuk);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null){
                    Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                }else{
                    Toast.makeText(LoginActivity.this, "Please Login !", Toast.LENGTH_SHORT).show();
                }

            }
        };

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();

                if( email.isEmpty()){
                    etEmail.setError("mohon masukan email");
                    etEmail.requestFocus();
                }else if(pass.isEmpty()){
                    etPass.setError("mohon masukan password");
                    etPass.requestFocus();
                } else if( email.isEmpty() && pass.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Data Tidak boleh kosong !", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pass.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                       if (!task.isSuccessful()){
                           Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                       }else{
                           Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                           startActivity(mainIntent);
                       }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    protected void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {

        Intent mainActivity = new Intent(Intent.ACTION_MAIN);
        mainActivity.addCategory(Intent.CATEGORY_HOME);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivity);
        finishAffinity();
    }


}
