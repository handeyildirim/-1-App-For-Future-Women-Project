package besteburhan.artibir;

import android.app.ActionBar;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static besteburhan.artibir.R.id.editTextPassword;
import static besteburhan.artibir.R.id.imageView;
import static besteburhan.artibir.R.id.start;
import static com.bumptech.glide.Glide.with;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView iVLoginScreen;
    EditText editTextEmail,editTextPassword;
    TextView textViewForgetPass;
    Button buttonSignIn,buttonSignUp;


    String facebookName;
    String facebookEmail ;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    FirebaseDatabase database;

    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        iVLoginScreen = (ImageView) findViewById(R.id.imageViewLoginScreen);
        editTextEmail = (EditText) findViewById(R.id.editTextEMail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        textViewForgetPass = (TextView) findViewById(R.id.textViewForgetPassword);
        Glide.with(this).load("http://i68.tinypic.com/2nvba5w.jpg").into(iVLoginScreen);


        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();


        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));
        callbackManager= CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }
            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Giriş iptal edildi.",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException error) {

            }
        });




        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Intent intent =new Intent(MainActivity.this,SecondActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };



    }
    private void handleFacebookAccessToken(final AccessToken accessToken) {

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            GraphRequest request = GraphRequest.newMeRequest(
                                    accessToken,
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(
                                                JSONObject object,
                                                GraphResponse response) {
                                            try {
                                                facebookName = object.getString("name");
                                                facebookEmail = object.getString("email");

                                                DatabaseReference dbRef =database.getReference("ArtiBir");
                                                dbRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(new UsersInformation(facebookEmail,facebookName,"",""));



                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,link,email");
                            request.setParameters(parameters);
                            request.executeAsync();



                        } else {

                            Toast.makeText(MainActivity.this, "Giriş başarısız.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });
    }



    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSignUp:
                Intent intent= new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);

                break;
            case R.id.buttonSignIn:
                String inputEmail ;
                String inputPassword;
                inputEmail = editTextEmail.getText().toString();
                inputPassword= editTextPassword.getText().toString();
                if(inputEmail == null || inputEmail.trim().equals("") || inputPassword == null || inputPassword.trim().equals("")) {
                    Toast.makeText(getApplicationContext(),"Lütfen e-posta adresinizi ve şifrenizi giriniz.",Toast.LENGTH_LONG).show();
                }
                else{
                    mAuth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth.addAuthStateListener(mAuthStateListener);
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                }
                break;
        }

    }
}
