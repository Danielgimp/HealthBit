package com.ariel.healthbit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class settingprofile extends AppCompatActivity
{

    Toolbar toolbar;
    EditText currentpass,newpass,confirmpass;
    Button delete,changepass;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settingprofile);
        toolbar=(Toolbar)findViewById(R.id.toolbarSettingProfile);
        setSupportActionBar(toolbar);
        currentpass=(EditText)findViewById(R.id.setting_currentpass);
        newpass=(EditText)findViewById(R.id.setting_newpass);
        confirmpass=(EditText)findViewById(R.id.setting_confirmnewpass);
        changepass=(Button)findViewById(R.id.setting_changepassword);
        delete=(Button)findViewById(R.id.setting_deleteaccount);

        changepass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                String TextCurrentpass=currentpass.getText().toString();
                String TextConfpass=confirmpass.getText().toString();
                String Textnewpass=newpass.getText().toString();
                //current password checks
                if (TextUtils.isEmpty(TextCurrentpass))
                {
                    currentpass.setError("password is required!");
                    return;
                }
                if (TextCurrentpass.length()<6)
                {
                    currentpass.setError("password must be at least 6 characters");
                    return;
                }

                //new password checks
                if (TextUtils.isEmpty(Textnewpass))
                {
                    newpass.setError("password is required!");
                    return;
                }
                if (Textnewpass.length()<6)
                {
                    newpass.setError("password must be at least 6 characters");
                    return;
                }
                //confirm password checks
                if (TextUtils.isEmpty(TextConfpass))
                {
                    confirmpass.setError("confirm your password!");
                    return;
                }
                if (!(Textnewpass.equals(TextConfpass)))
                {
                    confirmpass.setError("password and confirm password should match!");
                    return;
                }
                String email=auth.getCurrentUser().getEmail();
                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, TextCurrentpass);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    auth.getCurrentUser().updatePassword(Textnewpass).addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(settingprofile.this, "password changed", Toast.LENGTH_SHORT).show();
                                                Intent myIntent = new Intent(getApplicationContext(), MainProfile.class);
                                                startActivity(myIntent);
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    currentpass.setError("this password is incorrect ,please retype your current password.");
                                    return;
                                }
                            }
                        });

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view)
            {
                //show alert message: press yes- delete account, press no - cancel the alert dialog.
                AlertDialog.Builder builder = new AlertDialog.Builder(settingprofile.this);
                builder.setTitle("Are you sure you want to delete your account?");
                builder.setMessage("Deleting your account is permanent and will remove all content including profile settings.\n");
                builder.setCancelable(true);

                builder.setPositiveButton
                        (
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                String uid=auth.getCurrentUser().getUid();
                                auth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful())
                                        {
                                            FirebaseAuth.getInstance().signOut();
                                            DatabaseReference refDel= FirebaseDatabase.getInstance().getReference("users");
                                            refDel.child(uid).removeValue();
                                            Intent myIntent = new Intent(getApplicationContext(),MainActivity.class);
                                            startActivity(myIntent);

                                        }
                                    }
                                });
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert = builder.create();
                alert.show();
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) //toolbar definition
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    public void onBackPressed()
    {
        Intent myIntent = new Intent(getApplicationContext(), MainProfile.class);
        startActivity(myIntent);
    }
}