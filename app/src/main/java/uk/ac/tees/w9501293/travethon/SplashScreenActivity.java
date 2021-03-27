package uk.ac.tees.w9501293.travethon;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import uk.ac.tees.w9501293.travethon.room.users.Users;
import uk.ac.tees.w9501293.travethon.room.users.UsersTask;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseTask.getUsers(new FirebaseTask.getUsersListener() {
            @Override
            public void onFetched(List<Users> users) {
                if (users != null){
                    new UsersTask().addUsers(SplashScreenActivity.this,users);
                    startActivity(new Intent(SplashScreenActivity.this, AuthenticationActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailed(String message) {

            }
        });
    }
}
