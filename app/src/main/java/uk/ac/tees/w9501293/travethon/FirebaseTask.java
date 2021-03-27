package uk.ac.tees.w9501293.travethon;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.tees.w9501293.travethon.room.users.Users;

public class FirebaseTask {

    private static final String TAG = "FirebaseTask";
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void addUser(final String uid, final String username, final String email, final String phone,
                               final String age, final String profilePicture, final addUserListener addUserListener){
        final Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("username", username);
        user.put("uid", uid);
        user.put("profilePicture",profilePicture);
        user.put("phone",phone);
        user.put("age",age);

// Add a new document with a generated ID
        db.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Users users = new Users();
                        users.setProfilePicture(profilePicture);
                        users.setPhone(phone);
                        users.setUid(uid);
                        users.setEmail(email);
                        users.setAge(age);
                        users.setUsername(username);
                        addUserListener.onAdded(users);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        addUserListener.onFailed(e.getMessage());
                    }
                });
    }

    public static void getUsers(final getUsersListener getUsersListener){
        final ArrayList<Users> usersArrayList = new ArrayList<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Users user = new Users();
                                user.setUsername(document.getString("username"));
                                user.setAge(document.getString("age"));
                                user.setEmail(document.getString("email"));
                                user.setUid(document.getString("uid"));
                                user.setPhone(document.getString("phone"));
                                user.setProfilePicture(document.getString("profilePicture"));
                                usersArrayList.add(user);
                            }
                            getUsersListener.onFetched(usersArrayList);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public interface addUserListener{
        void onAdded(Users user);
        void onFailed(String message);
    }
    public interface getUsersListener{
        void onFetched(List<Users> users);
        void onFailed(String message);
    }
}
