package com.example.workerproblem;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateSnaps extends AppCompatActivity {

    @BindView(R.id.send_button)
    Button sendbutton;
    @BindView(R.id.chooes_button)
    Button chooseImage;
    @BindView(R.id.text_massege)
    TextView massege;
    @BindView(R.id.image_view_p)
    ImageView imageView;

    String imageName = UUID.randomUUID().toString() + ".jpg";
    String Message;
    Uri downurl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creat_snap);
        ButterKnife.bind(this);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageView();
            }
        });


        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Bitmap bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] arbyte=byteArrayOutputStream.toByteArray();


                UploadTask uploadTask= FirebaseStorage.getInstance().getReference().child("images").child(imageName).putBytes(arbyte);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateSnaps.this,R.string.failedupload,Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Message=massege.getText().toString();
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downurl=uri;
                                toActivityData();
                            }
                        });
                    }
                });

            }
        });





    }

    private void toActivityData() {

        final Intent intent=new Intent(getApplicationContext(),ChooesActivity.class);
         intent.putExtra(getString(R.string.imageUrl), downurl.toString());
        intent.putExtra(getString(R.string.ImageName), imageName);
        intent.putExtra(getString(R.string.message), Message);
        startActivity(intent);
    }

    private void getImageView() {

        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            Uri image=data.getData();

            if(requestCode== 1 && resultCode==RESULT_OK && data !=null){
                try {
                    Bitmap bitmap=  MediaStore.Images.Media.getBitmap(this.getContentResolver(),image);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getImageView();
            }

        }
    }
}
