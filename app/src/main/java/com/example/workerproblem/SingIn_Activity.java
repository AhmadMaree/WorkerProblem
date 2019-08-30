package com.example.workerproblem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SingIn_Activity extends AppCompatActivity {


    @BindView(R.id.txtEmail)
    TextView txtEmail;
    @BindView(R.id.txtPassword)
    TextView txtPassword;
    @BindView(R.id.btnSignIn)
    Button btnSignIn;
    @BindView(R.id.goReg)
    TextView goReg;

    private FirebaseAuth mAuth;
    String email = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singin);
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
        goReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);




            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = txtEmail.getText().toString();
                password = txtPassword.getText().toString();

                if (!email.isEmpty()) {
                    if (!password.isEmpty()) {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(SingIn_Activity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Intent intent = new Intent(getApplicationContext(), SnapActivity.class);
                                                    startActivity(intent);

                                                } else {

                                                    Toast.makeText(getApplicationContext(), R.string.LogIn_Faild, Toast.LENGTH_LONG).show();
                                                }
                                            }

                                        }
                                );


                    }
                } else
                    Toast.makeText(getApplicationContext(), R.string.EnterValidData, Toast.LENGTH_LONG).show();


            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser =mAuth.getCurrentUser();
        if (firebaseUser !=null){
            startActivity(new Intent(SingIn_Activity.this,SnapActivity.class));
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
