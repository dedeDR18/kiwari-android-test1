package com.belajar.mylogin2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.belajar.mylogin2.adapter.FriendAdapter;
import com.belajar.mylogin2.model.User;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private CircleImageView avatar;
    private TextView name;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private Toolbar toolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment mFragment;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    mFragment = new userFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, mFragment, mFragment.getClass().getSimpleName()).commit();
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        avatar = findViewById(R.id.avatars);
        name = findViewById(R.id.name);
        toolbar = findViewById(R.id.toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_chat);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String id = firebaseUser.getUid();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                name.setText(user.getName());
                Log.d("url", "url " + user.getAvatar());
                System.out.println(user);
                Glide.with(MainActivity.this).load("https://api.adorable.io/avatars/" + user.getAvatar()).into(avatar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("error firebase", "Failed to read value.", databaseError.toException());
            }
        });

    }



    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(Intent.ACTION_MAIN);
        mainActivity.addCategory(Intent.CATEGORY_HOME);
        mainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainActivity);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.menu_logout){
            FirebaseAuth.getInstance().signOut();
             Intent gotoLoginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(gotoLoginIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
