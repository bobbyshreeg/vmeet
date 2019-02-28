package com.example.bindu_madhavi.authenticate;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class profregister extends AppCompatActivity {
    FirebaseAuth fb_auth;
    EditText prof_email,prof_fname,prof_lname, prof_pwd, prof_cpwd;
    Spinner prof_dept_drop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profregister);
        fb_auth = FirebaseAuth.getInstance();
        prof_fname = findViewById(R.id.id_prof_fname);
        prof_lname = findViewById(R.id.id_prof_lname);
        prof_email = findViewById(R.id.id_prof_email);
        prof_pwd = findViewById(R.id.id_prof_pwd);
        prof_cpwd = findViewById(R.id.id_prof_repwd);
        prof_dept_drop=(Spinner)findViewById(R.id.id_prof_dept);
        ArrayAdapter<String> UdeptAdapter = new ArrayAdapter<String>(profregister.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.deptname));
        UdeptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prof_dept_drop.setAdapter(UdeptAdapter);


    }
    public void register(View v) {
        String prof_mail = prof_email.getText().toString();
        String prof_pword = prof_pwd.getText().toString();
        String prof_cpword = prof_cpwd.getText().toString();
        if (TextUtils.isEmpty(prof_mail) || TextUtils.isEmpty(prof_pword) || TextUtils.isEmpty(prof_cpword)) {
            Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
        } else {
            if (!TextUtils.equals(prof_pword, prof_cpword)) {
                Toast.makeText(this, "passwords dont match", Toast.LENGTH_SHORT).show();
            }
            else {
                fb_auth.createUserWithEmailAndPassword(prof_mail, prof_pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(profregister.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            launch();
                        }
                    }
                });
            }

        }
    }
    public void launch() {
        startActivity(new Intent(this, MainActivity.class));
    }
}