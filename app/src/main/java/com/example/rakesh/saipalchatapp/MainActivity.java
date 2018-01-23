package com.example.rakesh.saipalchatapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mindorks.placeholderview.PlaceHolderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
PlaceHolderView placeHolderView;
    String v;
    User user;
    Users users;
   // Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  mtoolbar= (Toolbar) findViewById(R.id.toolbar_chat);
        ///setSupportActionBar(mtoolbar);
        placeHolderView= (PlaceHolderView) findViewById(R.id.list_of_users);

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().
                getReference().child("Users");

   databaseReference.addValueEventListener(new ValueEventListener() {
       @Override
       public void onDataChange(DataSnapshot dataSnapshot) {
           user=new User();
           users=new Users();
           Iterator<DataSnapshot> d = dataSnapshot.getChildren().iterator();
           while (d.hasNext()) {

               DataSnapshot dataSnapshotChild = d.next();

               user = dataSnapshotChild.getValue(User.class);

               if (!user.email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                   Log.d("dataes", "" + user.userId);
                   Log.d("dataes", "" + user.getUsername());
                   Log.d("dataes", "" + user.email);
                   Log.d("dataes", "" + user.image);
                   placeHolderView.removeAllViews();
                   placeHolderView.addView(new UserListPlaceHolderView(MainActivity.this,
                           placeHolderView, user));

               }
               else
               {
                users.setUsername(user.getUsername());
                users.setEmailId(user.email);
                Log.d("current",""+users.getUsername());
               }

           }
       }

       @Override
       public void onCancelled(DatabaseError databaseError) {

       }
   });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_listing, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                break;

            case R.id.action_setting:
                startActivity(new Intent(MainActivity.this,Setting.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       FirebaseAuth.getInstance().signOut();
                        dialog.dismiss();
                       startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
