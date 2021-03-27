package uk.ac.tees.w9501293.travethon;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.ac.tees.w9501293.travethon.room.users.Users;
import uk.ac.tees.w9501293.travethon.room.users.UsersTask;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    TextView name,gmail,phone,age;
    ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        name = findViewById(R.id.name);
        gmail = findViewById(R.id.gmail);
        phone = findViewById(R.id.phone);
        age = findViewById(R.id.age);
        profilePicture = findViewById(R.id.profile_picture);
        new UsersTask().getUser(getApplicationContext(), currentUser.getUid(), new UsersTask.getUserListener() {
            @Override
            public void onFetched(Users user) {
                if (user != null){
                    name.setText(user.getUsername());
                    gmail.setText(user.getEmail());
                    phone.setText(user.getPhone());
                    age.setText(user.getAge());
                    if (!user.getProfilePicture().equals("Not available")){
                        Glide.with(getApplicationContext()).load(user.getProfilePicture()).into(profilePicture);
                    }
                }

            }
        });
    }

}
