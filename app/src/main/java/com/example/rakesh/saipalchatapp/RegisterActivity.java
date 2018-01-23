package com.example.rakesh.saipalchatapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


   // Toolbar mtoolbar;
    EditText musername,memail,mpassword,mcontact;
    Button mregister;
    ProgressDialog progressBar;
    FirebaseUser u;
    DatabaseReference database;
    String token;
    String name,contact,email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
      //  mtoolbar= (Toolbar) findViewById(R.id.reg_toolbar);
       /// setSupportActionBar(mtoolbar);
      //  mtoolbar.setTitle("saipalapp");
     //   mtoolbar.setLogo(R.drawable.saipal_logo);
        idFind();
        progressBar=new ProgressDialog(RegisterActivity.this);
        progressBar.setCancelable(false);
        progressBar.setTitle("Please wait.....");
        mregister.setOnClickListener(this);
         database= FirebaseDatabase.getInstance().getReference();




    }

    public void idFind()
    {
        musername= (EditText) findViewById(R.id.edit_reg_username);
        mcontact= (EditText) findViewById(R.id.edit_reg_contact);
        memail= (EditText) findViewById(R.id.edit_reg_email);
        mpassword= (EditText) findViewById(R.id.edit_reg_password);
        mregister= (Button) findViewById(R.id.btn_reg);
    }

    @Override
    public void onClick(View view) {
        name=musername.getText().toString();
        contact=mcontact.getText().toString();
        email=memail.getText().toString();
        password=mpassword.getText().toString();


        if (name.equals("") && email.equals("") && password.equals("") && contact.equals(""))
        {
            musername.setError("username required");
            mcontact.setError("contact required");
            memail.setError("email required");
            mpassword.setError("password required");
        }

        if (name.equals(""))
        {
            musername.setError("username required");
        }
        if (contact.equals(""))
        {
            mcontact.setError("contact required");
        }
        if (email.equals(""))
        {
            memail.setError("email required");
        }
        if (password.equals(""))
        {
            mpassword.setError("password required");
        }
        else {
            progressBar.show();
            createuserAccount();
        }



    }

    private void createuserAccount() {



        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            User user = new User(name, email);
                            String uid=task.getResult().getUser().getUid();
                            //   database.child("Users").push().setValue(user);
                            token=FirebaseInstanceId.getInstance().getToken();
                            User user1=new User(name,contact,email,uid,token,"","","Hi i am in saipal technology");
                            database.child("Users").child(uid).setValue(user1);
                            Log.d("TAG", "createUserWithEmail:success"+ task.isSuccessful()+"  b  "+token);
                            progressBar.dismiss();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                        } if (!task.isSuccessful()) {
                            Log.d("TAG",task.getException().getMessage());
                            progressBar.dismiss();

                        }
                    }});
    }


}
