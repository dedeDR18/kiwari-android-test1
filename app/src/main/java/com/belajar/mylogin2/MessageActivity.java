package com.belajar.mylogin2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.belajar.mylogin2.adapter.ChatAdapter;
import com.belajar.mylogin2.model.Chat;
import com.belajar.mylogin2.model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CircleImageView civAvatar;
    private TextView tvUsername;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private ImageButton btnSend;
    private EditText etMessageSend;
    private ChatAdapter chatAdapter;
    private List<Chat> mChat;
    private RecyclerView rvMessage;


    private Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        toolbar = findViewById(R.id.message_activity_toolbar);
        civAvatar = findViewById(R.id.message_activity_avatars);
        tvUsername = findViewById(R.id.message_activity_name);
        btnSend = findViewById(R.id.message_activity_ib_send);
        etMessageSend = findViewById(R.id.message_activity_et);
        rvMessage = findViewById(R.id.rvMessage);
        rvMessage.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rvMessage.setLayoutManager(linearLayoutManager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        intent = getIntent();
        final String userid = intent.getStringExtra("userid");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etMessageSend.getText().toString();
                if (!message.equals("")){
                    sendMessage(firebaseUser.getUid(), userid, message, getCurrentDate());
                } else {
                    Toast.makeText(MessageActivity.this, "Empty Message !", Toast.LENGTH_SHORT).show();
                }
                etMessageSend.setText("");
            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                tvUsername.setText(user.getName());
                Glide.with(MessageActivity.this).load("https://api.adorable.io/avatars/"+user.getAvatar()).into(civAvatar);
                readMessages(firebaseUser.getUid(), userid, user.getAvatar());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("error firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message, String date){
        reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("date", date);

        reference.child("Chats").push().setValue(hashMap);
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void readMessages(final String myid, final String userid, final String avatar){
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)){
                        mChat.add(chat);
                    }
                    chatAdapter = new ChatAdapter(MessageActivity.this, mChat, avatar);
                    rvMessage.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("error firebase", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
