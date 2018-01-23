package com.example.rakesh.saipalchatapp;

/**
 * Created by Rakesh on 6/2/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import morxander.zaman.ZamanTextView;


public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private List<Chat> mChats;

    public ChatRecyclerAdapter(List<Chat> chats) {
        mChats = chats;
        Log.d("Tag","constructors: ");
    }

    public void add(Chat chat) {
        Log.d("Tag","add methods: ");
        mChats.add(chat);

        notifyItemInserted(mChats.size() - 1);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("Tag","view type me 0: ");
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                Log.d("Tag","view type other: ");
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                Log.d("Tag","view type me: ");
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_others, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d("Tag","bind: ");
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            Log.d("idss",""+mChats.get(position).senderUid);
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        } else  {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    private void configureMyChatViewHolder(MyChatViewHolder myChatViewHolder, int position) {
        Log.d("Tag","viewholderfor set the value: my chat ");
        Chat chat = mChats.get(position);
////
//        String alphabet = chat.senderUid.substring(0, 1);

        myChatViewHolder.right_date.setTimeStamp(Long.parseLong(""+chat.timestamp));
//
        myChatViewHolder.txtChatMessage.setText(chat.message);
//        myChatViewHolder.txtUserAlphabet.setText(alphabet);
    }

    private void configureOtherChatViewHolder(OtherChatViewHolder otherChatViewHolder, int position) {
        Log.d("Tag","viewholderfor set the value:  other chat");
        Chat chat = mChats.get(position);
//
//        String alphabet = chat.receiver.substring(0, 1);
//
        otherChatViewHolder.txtChatMessage.setText(chat.message);
      //  otherChatViewHolder.txtUserAlphabet.setText(alphabet);
    }

    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("Tag","getitemviewtype");

        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            Log.d("Tag","getitemviewtype:  return me" + mChats.get(position).senderUid);

            return VIEW_TYPE_ME;
        } else {
            Log.d("Tag","getitemviewtype:  return other" +mChats.get(position).receiverUid);
            return VIEW_TYPE_OTHER;
        }
    }
    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet;
        ZamanTextView right_date;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            Log.d("Tag","me");

            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            right_date=(ZamanTextView)itemView.findViewById(R.id.right_date);
          //  txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage;
              ImageView img_user;

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            Log.d("Tag","other");
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
          //  img_user =  itemView.findViewById(R.id.circle_msg_sender);
        }
    }
}