package besteburhan.artibir;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    EditText editTextName,editTextLastName,editTextEmail,editTextPassword;
    Button buttonSignUp;

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle("Kayıt Ol");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextEmail = (EditText) findViewById(R.id.editTextEMail);
        editTextPassword=(EditText) findViewById(R.id.editTextPassword);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);

        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSignUp:
                if(!isEditTextEmpty()){
                    final String inputName = editTextName.getText().toString().trim();
                    final String inputLastName = editTextLastName.getText().toString().trim();
                    final String inputEmail = editTextEmail.getText().toString().trim();
                    String inputPassword = editTextPassword.getText().toString().trim();
                    mAuth.createUserWithEmailAndPassword(inputEmail,inputPassword).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                DatabaseReference dbRef = database.getReference("ArtiBir/"+"Users");
                                dbRef.child(mAuth.getCurrentUser().getUid()).setValue(new UsersInformation(inputEmail,inputName+" "+inputLastName,"0",""));
                                Intent intent= new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                            else{
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();

                            }
                        }
                    });


                }


                break;

        }
    }

    private boolean isEditTextEmpty() {
        String inputName = editTextName.getText().toString();
        String inputLastName = editTextLastName.getText().toString();
        String inputEmail = editTextEmail.getText().toString();
        String inputPassword = editTextPassword.getText().toString();


        if (inputName == null || inputName.trim().equals("") || inputLastName == null || inputLastName.trim().equals("")
                || inputEmail == null || inputEmail.trim().equals("") || inputPassword == null || inputPassword.trim().equals("")) {
            Toast.makeText(SignUpActivity.this, "Tüm alanları doldurunuz!", Toast.LENGTH_LONG).show();
            return true;
        }


        return false;
    }
}
