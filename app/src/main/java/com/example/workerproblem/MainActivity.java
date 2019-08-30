package com.example.workerproblem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.android.gms.common.util.CollectionUtils.mapOf;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.emailName)
    TextView email_name;
    @BindView(R.id.btnGo)
    Button btnGo;
    @BindView(R.id.goSignIn)
    TextView goSignIn;
    @BindView(R.id.inputPassword)
    TextView txtPassword;
    @BindView(R.id.spinner)
    Spinner spinnerSpecialist;
    @BindView(R.id.checkBox)
    CheckBox ifSpecialist;





    private FirebaseAuth mAuth;
    Boolean valueCheckBox;
    ArrayList<String> listvalueCheckBox = new ArrayList<String>();
    public static String ELECTRICAN = "Electrician";
    public static String PLUMBER = "Plumber";
    public static String NETWORK = "network";
    public static String COFFE_MACHIEN = "Coffee Machine";
    public static String MECHANICAL = "Mechanical";
    public static String POLICE = "police";
    public static String Selectedlist = "";


    String email = "";
    String password = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singup_activity);

        ButterKnife.bind(this);
        mAuth=FirebaseAuth.getInstance();

        valueCheckBox=ifSpecialist.isChecked();
        ifSpecialist.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b== true){
                    spinnerSpecialist.setVisibility(View.VISIBLE);
                    if (listvalueCheckBox.isEmpty()) {
                        listvalueCheckBox.add(MECHANICAL);
                        listvalueCheckBox.add(POLICE);
                        listvalueCheckBox.add(COFFE_MACHIEN);
                        listvalueCheckBox.add(NETWORK);
                        listvalueCheckBox.add(PLUMBER);
                        listvalueCheckBox.add(ELECTRICAN);
                    }

                    ArrayAdapter arrayAdapter=new ArrayAdapter(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,listvalueCheckBox);
                    spinnerSpecialist.setAdapter(arrayAdapter);
                } else {

                    spinnerSpecialist.setVisibility(View.INVISIBLE);
                }





            }
        });




        goSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),SingIn_Activity.class);
                startActivity(intent);
            }
        });


        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                valueCheckBox=ifSpecialist.isChecked();
                if(valueCheckBox == true){
                    Selectedlist= spinnerSpecialist.getSelectedItem().toString();

                }else{

                    Selectedlist="";
                }

                email=email_name.getText().toString();
                password=txtPassword.getText().toString();
                setUesr(email,password);

            }
        });







    }

    private void setUesr(final String email, final String password) {
        if(email.isEmpty() || password.isEmpty()){

            Toast.makeText(MainActivity.this,R.string.EnterValidData,
                    Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,R.string.User_existed,Toast.LENGTH_SHORT).show();
                                }else {
                                    mAuth.createUserWithEmailAndPassword(email,password)
                                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("sucess", "createUserWithEmail:success");
                                                        register(task.getResult().getUser().getUid());
                                                        login();
                                                    }else {
                                                        Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                                    }

                                                }
                                            });
                                }






                        }
                    });
        }



    }
    public void login() {

        Intent intent = new Intent(this, SnapActivity.class);
        startActivity(intent);
    }

    private void register(String uid) {


        Map<String,String> snapMap= new HashMap<>();
        snapMap.put("email",email_name.getText().toString());
        snapMap.put("Password", txtPassword.getText().toString());
        snapMap.put("ifSpecialist", valueCheckBox.toString());
        snapMap.put("SpecialistType",Selectedlist);

        FirebaseDatabase.getInstance().getReference().child("users").
                child(uid).setValue(snapMap);

    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}