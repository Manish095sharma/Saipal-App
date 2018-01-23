package com.example.rakesh.saipalchatapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity implements  TextView.OnEditorActionListener {

    Chat chat;
    private static final String TAG = "Chatting_info";
    RecyclerView recyclerView;
    LinearLayout background_color;
    EditText msg;
    ImageView send_btn;
    String recever_uid, sender_uid;
    static ActionBar actionBar;
    String message;
    ChatRecyclerAdapter adapter;
    String receivertoken;
    String receiver_name;
    String sender_name, sender_token;
    FirebaseUser user;
    Users getusername;
   // Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
       // mtoolbar= (Toolbar) findViewById(R.id.toolbar_chat);
      //  setSupportActionBar(mtoolbar);
        actionBar = getSupportActionBar();
        receiver_name = getIntent().getExtras().getString("Receiver_userName");
        actionBar.setTitle(receiver_name);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // hide the keyboard when open the start app which has edittext.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_chat);
        send_btn= (ImageView) findViewById(R.id.sendbtn);
        background_color=(LinearLayout)findViewById(R.id.background_color);
        msg = (EditText) findViewById(R.id.edit_text_message);
        sender_token = FirebaseInstanceId.getInstance().getToken();
        recever_uid = getIntent().getExtras().getString("receiver_userid");
        sender_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user = FirebaseAuth.getInstance().getCurrentUser();
        receivertoken = getIntent().getExtras().getString("receiverToken");
        getusername=new Users();
       // getcurrentUsername();
        getMessageFromFirebaseUser(sender_uid, recever_uid);

        background_color.setBackgroundColor(Color.parseColor("#3CB371"));



        //Editor/keyboard send btn
        msg.setOnEditorActionListener(this);

        //send button click
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendmessage();
                msg.setText("");
            }
        });
        //for adjust the recyclerview/see the buttom part of recyclerview
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }



    private void sendmessage() {
        message = msg.getText().toString();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(ChatActivity.this, "type some text", Toast.LENGTH_SHORT).show();


        } else {
            int time = (int) (System.currentTimeMillis());
            Timestamp tsTemp = new Timestamp(time);
            String ts =  tsTemp.toString();

            String dateTime= DateFormat.getDateTimeInstance().format(new Date());
            chat = new Chat(sender_uid, recever_uid, receiver_name, message,""+ (System.currentTimeMillis()/1000) );
            final String room_type_1 = sender_uid + "_" + recever_uid;
            final String room_type_2 = recever_uid + "_" + sender_uid;

            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("chat_rooms").getRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Toast.makeText(ChatActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    if (dataSnapshot.hasChild(room_type_1)) {
                        Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_1 + " exists");
                        reference.child("chat_rooms").child(room_type_1).child(String.valueOf(chat.timestamp)).setValue(chat);
                        // getMessageFromFirebaseUser(sender_uid, recever_uid);
                    } else if (dataSnapshot.hasChild(room_type_2)) {
                        Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_2 + " exist");
                        reference.child("chat_rooms").child(room_type_2).child(String.valueOf(chat.timestamp)).setValue(chat);
                        //  getMessageFromFirebaseUser(sender_uid, recever_uid);
                    } else {
                        Log.e(TAG, "sendMessageToFirebaseUser: success");
                        reference.child("chat_rooms").child(room_type_1).child(String.valueOf(chat.timestamp)).setValue(chat);
                        getMessageFromFirebaseUser(sender_uid, recever_uid);
                    }


                    FirebaseDatabase.getInstance().getReference().child("Users").child(sender_uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String username=   dataSnapshot.child("username").getValue().toString();
                            sendPushNotificationToReceiver(username,
                                    message,
                                    sender_uid,
                                    recever_uid,
                                    FirebaseInstanceId.getInstance().getToken().toString(),
                                    receivertoken);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void sendPushNotificationToReceiver(String sender, String message, String senderUid, String receveruid, String senderToken, String receivertoken) {
        Log.d("tokens", "receiver " + receivertoken);
        Log.d("tokens", "sende " + senderToken);
        Log.d("tokens", "sender " + sender);


        NotificationBulder.initialize()


                .username(sender)
                .senderuid(senderUid)
             //   .receiveruid(receveruid)
                .firebaseToken(senderToken)
                .receiverFirebaseToken(receivertoken)
                .message(message)
                .title(sender)
                .send();
    }

    private void getMessageFromFirebaseUser(String senderUid, String receiverUid) {

        Log.d("Tag", "sender " + senderUid + "  receiver  " + receiverUid);
        //  chat = new Chat(sendar_name,receiver_name,sender_uid, recever_uid, message, System.currentTimeMillis());

        final String room_type_1 = senderUid + "_" + receiverUid;
        final String room_type_2 = receiverUid + "_" + senderUid;
        Log.d("Tag", "" + room_type_1 + "" + room_type_2);

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("chat_rooms").getRef().addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "enter in database chat_rooms: " + dataSnapshot.getRef().toString());
                Log.e(TAG, "enter in database chat_rooms: " + dataSnapshot.getChildren().iterator().toString());
                Log.e(TAG, "enter in database chat_rooms: " + dataSnapshot.getChildrenCount());

                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_1 + " exists");
                    FirebaseDatabase.getInstance().getReference()
                            .child("chat_rooms")
                            .child(room_type_1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            Log.d("Tag", "" + chat.message);
                            if (adapter == null) {
                                adapter = new ChatRecyclerAdapter(new ArrayList<Chat>());
                                recyclerView.setAdapter(adapter);
                            }
                            adapter.add(chat);
                            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_2 + " exists");
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("chat_rooms")
                            .child(room_type_2).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            Chat chat = dataSnapshot.getValue(Chat.class);
                            Log.d("Tag", "" + chat.message);
                            if (adapter == null) {
                                adapter = new ChatRecyclerAdapter(new ArrayList<Chat>());
                                recyclerView.setAdapter(adapter);
                            }
                            adapter.add(chat);
                            recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                            //  mOnGetMessagesListener.onGetMessagesSuccess(chat);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //  mOnGetMessagesListener.onGetMessagesFailure("Unable to get message: " + databaseError.getMessage());
                        }
                    });
                } else {
                    Log.e(TAG, "getMessageFromFirebaseUser: no such room available");
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // keyboard for edit
    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_SEND) {
            sendmessage();
            msg.setText("");
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            return true;
        }
        return false;
    }




}
