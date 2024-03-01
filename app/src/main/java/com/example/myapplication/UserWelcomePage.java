package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import org.w3c.dom.Text;

public class UserWelcomePage extends AppCompatActivity {

    private TextView userNameText , usrEmailText;
    public Button logOutBtnUser ,editBtn, menuBtn;
    private FirebaseAuth authProfile;

    private String userName ,userEmail;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_welcome_page);

        menuBtn = (Button) findViewById(R.id.menu);
        editBtn = (Button) findViewById(R.id.edit);
        logOutBtnUser = (Button) findViewById(R.id.logOutBtn);
        userNameText = (TextView)  findViewById(R.id.userName);
        usrEmailText = (TextView) findViewById(R.id.userEmail);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if(firebaseUser == null){
            Toast.makeText(UserWelcomePage.this, "Something Went wrong! User details are not available in this moment.", Toast.LENGTH_SHORT).show();
        }else {
            showUserProfile(firebaseUser);
        }

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserWelcomePage.this,UserMenuPage.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showUserProfile(FirebaseUser firebaseUser){
        String userID = firebaseUser.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails =  snapshot.getValue(ReadWriteUserDetails.class);
                if(readWriteUserDetails != null){
                    userName = firebaseUser.getDisplayName();
                    userEmail = firebaseUser.getEmail();

                    userNameText.setText(userName);
                    usrEmailText.setText(userEmail);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserWelcomePage.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}