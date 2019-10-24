package com.example.mob_lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    public EditText emailID;
    public EditText password;
    public EditText username;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    public int passMinLenght = 8;
    public String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.editText2);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.editText);
        password = findViewById(R.id.editText3);
        btnSignUp = findViewById(R.id.button);
        tvSignIn = findViewById(R.id.textView);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailID.getText().toString();
                String pwd = password.getText().toString();
                String user = username.getText().toString();

                        /*Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        intent.putExtra("username", user);
                        startActivity(intent);
                        finish();*/

                if(email.isEmpty()){
                    emailID.setError("Please enter your email");
                    emailID.requestFocus();
                } else if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                } else if(user.isEmpty()){
                    username.setError("Please enter your username");
                    username.requestFocus();
                } else if(user.isEmpty() && email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(MainActivity.this, "Fields are empty!",Toast.LENGTH_SHORT).show();
                } else if(password.length() < passMinLenght){  // Validation of password
                    password.setText("");
                    password.setError("Minimal lengt of password should be 8!");
                } else if(!emailID.getText().toString().trim().matches(emailPattern)){   // Validation of email
                    emailID.setText("");
                    emailID.setError("Please re-enter your email correctly");
                } else if(!(user.isEmpty() && email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Sign Up Unsuccesful. Please try again.",Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(MainActivity.this, HomeActivity.class ));
                            }
                        }
                    });
                } else{
                    Toast.makeText(MainActivity.this, "Error Occured!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

            }
    public void onBackPressed(){
        moveTaskToBack(true);
    }
}
