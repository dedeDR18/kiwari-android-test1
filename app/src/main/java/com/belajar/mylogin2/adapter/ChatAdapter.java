package com.belajar.mylogin2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.belajar.mylogin2.R;
import com.belajar.mylogin2.model.Chat;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<Chat> mChat;
    private String avatar;

    FirebaseUser firebaseUser;

    public ChatAdapter(Context context, List<Chat> mChat, String avatar) {
        this.context = context;
        this.mChat = mChat;
        this.avatar = avatar;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new ChatAdapter.ViewHolder(v);
        } else
        {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ChatAdapter.ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.show_date.setText(chat.getDate());
        holder.show_chat.setText(chat.getMessage());
        Glide.with(context).load("https://api.adorable.io/avatars/"+ avatar).into(holder.userAvatar);

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView show_chat, show_date;
        public ImageView userAvatar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_date = itemView.findViewById(R.id.chat_item_date);
            show_chat = itemView.findViewById(R.id.chat_item_message);
            userAvatar = itemView.findViewById(R.id.chat_item_avatar);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
