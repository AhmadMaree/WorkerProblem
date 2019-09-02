package com.example.workerproblem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowSnapsActivity extends AppCompatActivity {

    public interface FirebaseCallBack {
        public void onCallBack(ArrayList<String> emilsFrom);

    }

    public static final String EMAILS = "emails";
    @BindView(R.id.lst)
    ListView snapsListView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> emails = new ArrayList<>();
    ArrayList<DataSnapshot> snaps = new ArrayList<>();
    FirebaseUser user;
    private String providerId;
    private String uid;
    private String name;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showallactivity);
        ButterKnife.bind(this);
        //---

        final FirebaseCallBack firebaseCallBack = new FirebaseCallBack() {
            @Override
            public void onCallBack(ArrayList<String> emilsFrom) {
                startWidgetService(emilsFrom);

            }
        };


        //--


        snapsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataSnapshot snap = snaps.get(position);
                Intent intent = new Intent(getApplicationContext(), ViewSnapActivity.class);
                intent.putExtra(getString(R.string.ImageName), (String) snap.child(getString(R.string.ImageName)).getValue());
                intent.putExtra(getString(R.string.imageUrl), (String) snap.child(getString(R.string.imageUrl)).getValue());
                intent.putExtra(getString(R.string.from), (String) snap.child("from").getValue());
                intent.putExtra(getString(R.string.SnapKey), snap.getKey());
                intent.putExtra(getString(R.string.Message), (String) snap.child(getString(R.string.message)).getValue());
                startActivity(intent);
            }
        });


        FirebaseDatabase.getInstance().getReference().child(getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("snaps").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String email = (String) dataSnapshot.child("from").getValue();
                emails.add(email);
                snaps.add(dataSnapshot);
                arrayAdapter.notifyDataSetChanged();

                firebaseCallBack.onCallBack(emails);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                int i;
                for (i = 0; i < snaps.size(); i++) {
                    if (snaps.get(i).getKey().equals(dataSnapshot.getKey()))
                        emails.remove(i);
                }

                arrayAdapter.notifyDataSetChanged();
                firebaseCallBack.onCallBack(emails);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        /////////////----------


        arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, emails);
        snapsListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();


    }


    void startWidgetService(ArrayList<String>emilsFrom) {
        System.out.println(emails.isEmpty());
        Intent i = new Intent(this, WidgetUpdateService.class);
        i.putStringArrayListExtra(Intent.EXTRA_TEXT, emilsFrom);
        i.setAction(WidgetUpdateService.WIDGET_UPDATE_ACTION);
        startService(i);
    }

    public void pupulateData() {
        FirebaseDatabase.getInstance().getReference().child(getString(R.string.users)).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("snaps").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String email = (String) dataSnapshot.child("from").getValue();
                emails.add(email);
                snaps.add(dataSnapshot);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int indx = 0;
                for (DataSnapshot snap : snaps) {
                    if (snap.getKey() == dataSnapshot.getKey()) {
                        emails.remove(indx);
                        snaps.remove(indx);
                    }
                    indx++;
                }
                arrayAdapter.notifyDataSetChanged();

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
