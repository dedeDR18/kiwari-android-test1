package com.belajar.mylogin2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.belajar.mylogin2.MessageActivity;
import com.belajar.mylogin2.R;
import com.belajar.mylogin2.model.User;
import com.bumptech.glide.Glide;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private Context context;
    private List<User> mUser;

    public FriendAdapter(Context context, List<User> mUser) {
        this.context = context;
        this.mUser = mUser;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.friend_item, parent, false);
        return new FriendAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = mUser.get(position);
        holder.username.setText(user.getName());
        Glide.with(context).load("https://api.adorable.io/avatars/"+user.getAvatar()).into(holder.userAvatar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView userAvatar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.tv_friendName);
            userAvatar = itemView.findViewById(R.id.iv_friendAvatar);
        }
    }
}
