package uk.ac.tees.w9501293.travethon.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.ac.tees.w9501293.travethon.FirebaseTask;
import uk.ac.tees.w9501293.travethon.MainActivity;
import uk.ac.tees.w9501293.travethon.R;
import uk.ac.tees.w9501293.travethon.room.users.Users;
import uk.ac.tees.w9501293.travethon.room.users.UsersTask;
import uk.ac.tees.w9501293.travethon.utils.Constants;


public class RegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match

    private FirebaseAuth mAuth;
    private OnFragmentInteractionListener mListener;
    private static final String TAG = "RegisterFragment";
    EditText emailEditText, passwordEditText;
    private Button loginButton,registerButton;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;
    private ProgressDialog progressDialog;
    private EditText usernameEditText;
    private FirebaseUser firebaseUser;


    public RegisterFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        loginButton = root.findViewById(R.id.login_button);
        registerButton = root.findViewById(R.id.register_button);
        emailEditText = root.findViewById(R.id.email_edittext);
        passwordEditText = root.findViewById(R.id.password_edittext);
        usernameEditText = root.findViewById(R.id.username);

        signInButton = root.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                getActivity().startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailString = emailEditText.getText().toString();
                final String passwordString = passwordEditText.getText().toString();
                final String usernameString = usernameEditText.getText().toString();
                if (usernameString.equals("")){
                    usernameEditText.setError("Enter username");
                    return;
                }
                if (emailString.equals("")){
                    emailEditText.setError("Enter email");
                    return;
                }
                if (passwordString.equals("")){
                    passwordEditText.setError("Enter password");
                    return;
                }
                progressDialog.show();


                mAuth.createUserWithEmailAndPassword(emailString, passwordString)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    firebaseUser = mAuth.getCurrentUser();
                                    FirebaseTask.addUser(firebaseUser.getUid(), usernameString, emailString, "Not available"
                                            , "Not available", "Not available", new FirebaseTask.addUserListener() {
                                                @Override
                                                public void onAdded(Users user) {

                                                    new UsersTask().addUser(getContext(),user);
                                                    progressDialog.dismiss();
                                                    updateUI(firebaseUser);
                                                }

                                                @Override
                                                public void onFailed(String message) {

                                                    progressDialog.dismiss();
                                                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getActivity(), "Authentication failed. " + task.getException().toString(),
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    updateUI(null);
                                }
                            }
                        });

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLoginPressed();
            }
        });
        return root;
    }





    private void updateUI(FirebaseUser user) {
        if (user!=null){
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onLoginPressed();
    }
}
