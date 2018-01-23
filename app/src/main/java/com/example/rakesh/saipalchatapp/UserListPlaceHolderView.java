package com.example.rakesh.saipalchatapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Position;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

/**
 * Created by Rakesh on 10/15/2017.
 */

@NonReusable
@Layout(R.layout.list_of_users)
public class UserListPlaceHolderView {



    @View(R.id.txt_username_list)
    private TextView  txt_user_List;

    @View(R.id.txt_user_last_msg)
    private TextView  txt_user_last_msg;
    @View(R.id.users_img_circle)
    private ImageView users_img_circle;
     User users;
    String username;
    PlaceHolderView placeholder;
    Context context;
    String currentusername;
    String currentuser_token;
    String uid;
    private PlaceHolderView placeHolderView;


     UserListPlaceHolderView(Context context,PlaceHolderView placeholder,User user) {
        // Log.d("errors","error");
        this.users = user;
        this.context = context;
        this.username=username;
         this.placeholder=placeholder;
         this.uid=uid;
    }



    @Resolve
    private void onResolved()
    {


            txt_user_List.setText(users.getUsername());

            Log.d("dabs",users.email);
            Log.d("dabs",users.image);
            if (users.image.equals(""))
            {

            }else
            {
                Glide.with(context).load(users.image).placeholder(R.mipmap.default_user_icon_round).fitCenter().into(users_img_circle);
            }




    }




    @Position
private int position;
    @Click(R.id.root_click)
    private void onClick()
    {


      //  Toast.makeText(context,"clicked"+username+" "+email,Toast.LENGTH_SHORT).show();
        context.startActivity(new Intent(context,ChatActivity.class)
                .putExtra("Receiver_userName",users.getUsername())
                .putExtra("receiver_userid",users.userId)
                .putExtra("receiverToken",users.firebaseToken));


          }



}
