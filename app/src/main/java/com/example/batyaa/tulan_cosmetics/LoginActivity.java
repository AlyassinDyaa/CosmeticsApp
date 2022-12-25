package com.example.batyaa.tulan_cosmetics;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.batyaa.tulan_cosmetics.Admin.CatgeoryActivity;
import com.example.batyaa.tulan_cosmetics.Users.guestAboutUsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {


    private EditText userEmail, userPassword;
    private Button mButton;
    private ProgressBar mProgressBar;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;


    public EditText getUserPassword() {
        return userPassword;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        userEmail = (EditText) findViewById(R.id.loginEmail);
        userPassword = (EditText)findViewById(R.id.loginPassword);
        mButton = (Button) findViewById(R.id.loginBtn);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();


        mProgressBar.setVisibility(View.INVISIBLE);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mButton.setVisibility(View.INVISIBLE);

                final String mail = userEmail.getText().toString();
                final String password = userPassword.getText().toString();

                if(mail.isEmpty() || password.isEmpty())
                {
                    showMessage("Please Check Data entered");
                    mButton.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
                else if(mail.matches("tulan@gmail.com") && password.matches("123456") )
                {
                    signIn(mail, password);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mButton.setVisibility(View.VISIBLE);

                }
                else if(mail.toLowerCase().matches("tulan@gmail.com") && password.matches("123456") )
                {
                    signIn(mail, password);
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mButton.setVisibility(View.VISIBLE);

                }
                else
                {
                    showMessage("Please Check Data entered");
                    mButton.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }

            }
        });


    }


    private void signIn(String mail, String Password)
    {
        mAuth.signInWithEmailAndPassword(mail,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mButton.setVisibility(View.VISIBLE);
                    updateUI();
                }
                else
                {
                    showMessage(task.getException().getMessage());
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mButton.setVisibility(View.VISIBLE);
                }
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {
                    //user is already connected
                    //so we redirect user to Home
                    updateUI();
                }
                else if(user == null)
                {
                    updateGuestUI();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(),"WELCOME TO TULAN",Toast.LENGTH_SHORT).show();
    }

    private void updateGuestUI()
    {
        Intent intent = new Intent(LoginActivity.this,guestAboutUsActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateUI()
    {
        Intent intent = new Intent(LoginActivity.this,CatgeoryActivity.class);
        startActivity(intent);
        finish();
    }

    private void showMessage(String message)
    {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }


    public void MoveToQuestHome(View view)
    {
        Intent intent = new Intent(LoginActivity.this,  guestAboutUsActivity.class);
        startActivity(intent);
    }

/*
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            //user is already connected
            //so we redirect user to Home
            updateUI();
        }
        else if(user == null)
        {
            updateGuestUI();
        }

    }
*/

}
