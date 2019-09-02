package com.example.workerproblem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.workerproblem.MainActivity.COFFE_MACHIEN;
import static com.example.workerproblem.MainActivity.COOLING_AIR_CONDITION;
import static com.example.workerproblem.MainActivity.ELECTRICAN;
import static com.example.workerproblem.MainActivity.MECHANICAL;
import static com.example.workerproblem.MainActivity.NETWORK;
import static com.example.workerproblem.MainActivity.PLUMBER;
import static com.example.workerproblem.MainActivity.POLICE;


public class ChooesActivity extends AppCompatActivity {


    @BindView(R.id.departmentName)
    Spinner spinnerType;
    @BindView(R.id.listview_emails)
    ListView listViewchooes;
    String message = "";
    String imageUrl = "";
    String ImageName = "";
    ArrayList<String> specialistType = new ArrayList<String>();
    ArrayList<String> emails=new ArrayList<String>();
    ArrayList<String> keys;
    String selectitemfromtype="";
    ArrayAdapter arrayAdapterForListOfSpecialist;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooes_activity);
        ButterKnife.bind(this);

        Intent intent=getIntent();

        if(intent.hasExtra(getString(R.string.ImageName))){
                ImageName=intent.getStringExtra(getString(R.string.ImageName));
        }
        if(intent.hasExtra(getString(R.string.message))){
                message=intent.getStringExtra(getString(R.string.message));
        }
        if(intent.hasExtra(getString(R.string.imageUrl))){
            imageUrl=intent.getStringExtra(getString(R.string.imageUrl));
        }

        if (specialistType.isEmpty()) {
            specialistType.add(MECHANICAL);
            specialistType.add(POLICE);
            specialistType.add(COOLING_AIR_CONDITION);
            specialistType.add(COFFE_MACHIEN);
            specialistType.add(NETWORK);
            specialistType.add(PLUMBER);
            specialistType.add(ELECTRICAN);
        }
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, specialistType);
        spinnerType.setAdapter(arrayAdapter2);
        spinnerType.setSelection(-1);


        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    emails=new ArrayList<String>();
                    keys=new ArrayList<String>();

                    selectitemfromtype=specialistType.get(i);
                    popData();

                arrayAdapterForListOfSpecialist = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, emails);
                listViewchooes.setAdapter(arrayAdapterForListOfSpecialist);
                arrayAdapterForListOfSpecialist.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        listViewchooes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String, String> snapMap = new HashMap<>();
                snapMap.put("from", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                snapMap.put("to", emails.get(i));
                snapMap.put("imageUrl", imageUrl);
                snapMap.put("ImageName", ImageName);
                snapMap.put("message", message);

                FirebaseDatabase.getInstance().getReference().child("users").child(keys.get(i)).child("snaps").push().setValue(snapMap);

                Intent intent = new Intent(getApplicationContext(), SnapActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


                Toast.makeText(ChooesActivity.this, emails.get(i), Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void popData() {

        FirebaseDatabase.getInstance().getReference().child(getString(R.string.users)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String email = (String) dataSnapshot.child(getString(R.string.email)).getValue();
                String tempUserSpicialist = (String) dataSnapshot.child(getString(R.string.SpecialistType)).getValue();

                if (tempUserSpicialist.equals(selectitemfromtype)) {
                    emails.add(email);
                    keys.add(dataSnapshot.getKey());


                }

                arrayAdapterForListOfSpecialist.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
