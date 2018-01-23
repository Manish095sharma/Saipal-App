package com.example.rakesh.saipalchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.LocaleDisplayNames;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setting extends AppCompatActivity implements View.OnClickListener {

    Toolbar mtoolbar;
   private static ActionBar actionBar;
   private CircleImageView circleImageView;
   private TextView mtextview_username,mtextview_status,
            mtextview_profilesetting,mtextview_accsetting,mtextview_privacysetting,mtextview_generalsetting;
   private static final int image_pick=1;
   private ProgressDialog progressDialog;
    DatabaseReference databaseReference;
    String currentuserId;
    private StorageReference mimage_storage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        idFind();
        setSupportActionBar(mtoolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Setting");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        progressDialog=new ProgressDialog(Setting.this);
        mimage_storage=FirebaseStorage.getInstance().getReference();
    //click circle image to change the image.
        circleImageView.setOnClickListener(this);
        mtextview_generalsetting.setOnClickListener(this);
        mtextview_privacysetting.setOnClickListener(this);
        mtextview_accsetting.setOnClickListener(this);
        mtextview_profilesetting.setOnClickListener(this);
        currentuserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        settingProfile();
    }

    private void settingProfile() {

         databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").
                child(currentuserId);
            databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uname=dataSnapshot.child("username").getValue().toString();
                String user_status=dataSnapshot.child("status").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();
                String thumb_image=dataSnapshot.child("thumbnail_image").getValue().toString();
                mtextview_username.setText(uname);
                mtextview_status.setText(user_status);

                if (image.equals(""))
                {

                }
                else {
                    Glide.with(Setting.this).load(image).placeholder(R.mipmap.default_user_icon_round).into(circleImageView);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void idFind()
    {
        mtoolbar= (Toolbar) findViewById(R.id.toolbar_setting);
        circleImageView= (CircleImageView) findViewById(R.id.profile_img);
        mtextview_username= (TextView) findViewById(R.id.txt_username);
        mtextview_status= (TextView) findViewById(R.id.txt_status);
        mtextview_profilesetting= (TextView) findViewById(R.id.txt_profilesetting);
        mtextview_accsetting= (TextView) findViewById(R.id.txt_Accountssetting);
        mtextview_privacysetting= (TextView) findViewById(R.id.txt_privacysetting);
        mtextview_generalsetting= (TextView) findViewById(R.id.txt_Generalsetting);
    }

    @Override
    public void onClick(View view) {

        int clickview_id=view.getId();

        switch (clickview_id)
        {
            case R.id.profile_img:
                changeImage();
        }


    }

    private void changeImage()
    {
        //get the image from gallery
        Intent galleryIntent =new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"select image"),image_pick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==image_pick && resultCode==RESULT_OK ) {
            Uri image = data.getData();
            //crop the image
            CropImage.activity(image)
                    .setAspectRatio(4, 3)
                    .setMinCropResultSize(500, 500)
                    .start(this);
        }
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                progressDialog.setTitle("upload image...");
                progressDialog.setMessage("please while upload image..........");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Uri resulturi = result.getUri();
                //store the image in storage of firebase
                final StorageReference image_file_path = mimage_storage.child("profile_images").child(currentuserId + ".jpg");

                image_file_path.putFile(resulturi).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            Log.d("sucess","sucess upload");
                            //download the image from storage firebase
                            String download_image_url=task.getResult().getDownloadUrl().toString();

                            //store the download image to user profile circle
                            databaseReference.child("image").setValue(download_image_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        settingProfile();
                                        progressDialog.dismiss();
                                        Log.d("sucess","download");
                                        Toast.makeText(Setting.this,"upload sucess ",Toast.LENGTH_SHORT).show();


                                    }
                                    else
                                    {
                                        Toast.makeText(Setting.this,"upload failed try again "+task.getException().getMessage()
                                                ,Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }

                                }
                            });


                        }
                        else
                            {
                                Toast.makeText(Setting.this,"upload failed try again "+task.getException().getMessage()
                                        ,Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                        }


                    }
                });

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }


        }



    }



}
