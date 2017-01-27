package degree.nano.ahmed.nanodegree;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import degree.nano.ahmed.nanodegree.Controller.StoreData;

public class LoginActivity extends AppCompatActivity {

    TextView tvSignUp;
    EditText edEmail,edPass;
    Button btnSignIn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        move();
        auth = FirebaseAuth.getInstance();
        declare();
        action();

    }
    private void declare(){
        edEmail = (EditText) findViewById(R.id.edEmail);
        edPass = (EditText) findViewById(R.id.edPass);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
    }
    private void action(){

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edEmail.getText().toString().equals("")
                        ||edPass.getText().toString().equals(""))

                {
                    Toast.makeText(LoginActivity.this,"Please Insert All field",Toast.LENGTH_LONG).show();
                }else {
                    auth.signInWithEmailAndPassword(edEmail.getText().toString(), edPass.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.

                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        if (edPass.getText().toString().length() < 6) {
    Toast.makeText(LoginActivity.this, "Password should more than 6 characters", Toast.LENGTH_LONG).show();

                                        } else {
                                            Toast.makeText(LoginActivity.this, ""+task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                            Log.d("uuu",task.getException().getLocalizedMessage());
                                        }
                                    } else {
                                      String userId = task.getResult().getUser().getUid();
                                        new StoreData(LoginActivity.this).setUserId(userId);

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void move(){
        if(!new StoreData(this).getUserId().equals("0")){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
