package com.example.workerproblem;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewSnapActivity extends AppCompatActivity {


    @BindView(R.id.imageView2)
    ImageView imageView;
    @BindView(R.id.txtMessage)
    TextView txtMessage;
    @BindView(R.id.chksolved)
    CheckBox didYouSolveIt;



    Boolean valDidYouSolveIt = false;
    String ImageKey = "";
    String ImageName = "";
    String imageUrl = "";
    String from = "";
    private String message = "";
    public Bitmap myBitmap;
    String SnapKey;
    FirebaseAuth mAth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewsnap_activity);
        ButterKnife.bind(this);
        mAth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        if (intent.hasExtra(getString(R.string.imageUrl))) {
            imageUrl = intent.getStringExtra(getString(R.string.imageUrl));
        }
        if (intent.hasExtra("from")) {
            from = intent.getStringExtra("from");
        }
        if (intent.hasExtra(getString(R.string.SnapKey))) {
            SnapKey = intent.getStringExtra(getString(R.string.SnapKey));
        }
        if (intent.hasExtra(getString(R.string.Message))) {
            message = intent.getStringExtra(getString(R.string.Message));
        }
        if (intent.hasExtra(getString(R.string.ImageName))) {
            ImageName = intent.getStringExtra(getString(R.string.ImageName));
        }

        txtMessage.setText(from + ":" + intent.getStringExtra(getString(R.string.Message)));
        didYouSolveIt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {


                    FirebaseDatabase.getInstance().getReference().child(getString(R.string.users)).child(mAth.getCurrentUser().getUid()).child("snaps").child(SnapKey).removeValue();
                    FirebaseStorage.getInstance().getReference().child(getString(R.string.images)).child(ImageName).delete();


                }


            }
        });
        ImageDownloader task = new ImageDownloader();

        Bitmap myImage;
        try {
            myImage = task.execute(imageUrl).get();
            imageView.setImageBitmap(myImage);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                myBitmap = BitmapFactory.decodeStream(inputStream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return myBitmap;


        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
