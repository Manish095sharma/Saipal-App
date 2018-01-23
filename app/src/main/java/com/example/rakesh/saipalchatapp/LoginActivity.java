package com.example.rakesh.saipalchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText Email,password;
    Button login,btn_for_reg;
   ProgressDialog progressDialog;
   ///Toolbar mtoolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       // mtoolbar= (Toolbar) findViewById(R.id.login_toolbar);
       //setSupportActionBar(mtoolbar);
       // mtoolbar.setTitle("Saipalapp");
      //  mtoolbar.setLogo(R.drawable.saipal_logo);


        progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("please wait.....");
        Email = (EditText) findViewById(R.id.edit_email);
        password= (EditText) findViewById(R.id.edit_password);
        btn_for_reg= (Button) findViewById(R.id.btn_for_reg);
        login= (Button) findViewById(R.id.btn_login);


        login.setOnClickListener(this);
        btn_for_reg.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        String email = Email.getText().toString();
        String pass = password.getText().toString();
        if (R.id.btn_login == id)
        {


            if (email.equalsIgnoreCase("") || pass.equalsIgnoreCase(""))
            {
                Toast.makeText(getApplicationContext(),"fields are empty plz enter",Toast.LENGTH_SHORT).show();

            }
            else login(email,pass);
        }
       else if (R.id.btn_for_reg==id)
        {
            Log.d("Tag","not reg clicked");
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
        }
    }

    private void login(String email,String pass) {

        progressDialog.show();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.d("TAG", "signin complete" + task.isSuccessful());
                            Toast.makeText(getApplicationContext(),"welcome",Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplication(), MainActivity.class));
                        }
                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            Log.d("TAG", task.getException().getMessage());
                            Toast.makeText(getApplicationContext(),""+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }

                });
    }

}
