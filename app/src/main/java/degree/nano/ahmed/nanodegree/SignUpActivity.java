package degree.nano.ahmed.nanodegree;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import degree.nano.ahmed.nanodegree.Controller.StoreData;
import degree.nano.ahmed.nanodegree.model.UserModel;

public class SignUpActivity extends AppCompatActivity {

    EditText edUserName,edEmail,edPass,edConPass,edMobile;
    Button btnSignUp;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        declare();
        action();
    }

    private void declare(){
        edUserName = (EditText) findViewById(R.id.edUserName);
        edEmail = (EditText) findViewById(R.id.edEmail);
        edPass = (EditText) findViewById(R.id.edPass);
        edMobile = (EditText) findViewById(R.id.edMobile);
        edConPass = (EditText) findViewById(R.id.edConPass);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
    }
    private void action(){
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edUserName.getText().toString().equals("")||edEmail.getText().toString().equals("")||edMobile.getText().toString().equals("")
                        ||edPass.getText().toString().equals("")||edConPass.getText().toString().equals(""))

                {
                    Toast.makeText(SignUpActivity.this,"Please Insert All field",Toast.LENGTH_LONG).show();
                }else if(edPass.getText().toString().equals("edConPass.getText().toString()")){
                    Toast.makeText(SignUpActivity.this,"Password and confirm password not matched",Toast.LENGTH_LONG).show();

                }else {
                    auth.createUserWithEmailAndPassword(edEmail.getText().toString(), edPass.getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        createNewUser(task.getResult().getUser());

                                    }
                                }
                            });
                }
            }
        });
    }
    private void createNewUser(FirebaseUser userFromRegistration) {
        String username = edUserName.getText().toString();
        String email = userFromRegistration.getEmail();
        String userId = userFromRegistration.getUid();
        String mobile = edMobile.getText().toString();

        UserModel user = new UserModel(username, email,mobile);
        new StoreData(this).setUserId(userId);

        mDatabase.child("users").child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
