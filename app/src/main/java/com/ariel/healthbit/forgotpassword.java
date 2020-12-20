package com.ariel.healthbit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class forgotpassword<toolbar> extends AppCompatActivity
{
    public static boolean emailvalidity(String email) //check email validity
    {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    Toolbar toolbar;
    EditText email;
    Button reset;
    TextView login;
    FirebaseAuth auth;
    ProgressBar prog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        auth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbarPASSWORD);
        setSupportActionBar(toolbar);

        email=(EditText)findViewById(R.id.forgotpassword_email);
        reset=(Button)findViewById(R.id.forgotpassword_reset);
        prog=(ProgressBar)findViewById(R.id.forgotpassword_progressBar);
        reset.setOnClickListener(new View.OnClickListener() {
        public void onClick(View view)
        {
            prog.setVisibility(View.VISIBLE);
            String TextEmail=email.getText().toString();

            if(TextEmail.isEmpty())
            {
                email.setError("Email is required!");
                prog.setVisibility(View.GONE);
                return;
            }
            if(!emailvalidity(TextEmail))
            {
                email.setError("email is invalid");
                prog.setVisibility(View.GONE);
                return;
            }
            auth.fetchSignInMethodsForEmail(TextEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) //check if email input is registered.
                {
                    boolean check=!task.getResult().getSignInMethods().isEmpty();
                    if(!check)
                    {
                        email.setError("Email is not registered!");
                        prog.setVisibility(View.GONE);
                        return;
                    }
                }

            });
            auth.sendPasswordResetEmail(TextEmail)

                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(forgotpassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            }
                            else
                                {
                                Toast.makeText(forgotpassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }

                            prog.setVisibility(View.GONE);
                        }
                    });

        }

    });



        login=(TextView)findViewById(R.id.forgotpassword_login);
        login.setPaintFlags(login.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //set under line
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                Intent myIntent = new Intent(getApplicationContext(), signin.class);
                startActivity(myIntent);
            }

        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}