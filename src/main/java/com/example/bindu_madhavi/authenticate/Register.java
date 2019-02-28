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

public class Register extends AppCompatActivity {
    FirebaseAuth fb_auth;
    EditText stud_fname,stud_lname,stud_email,stud_pwd,stud_cpwd;
    Spinner stud_dept_drop,stud_sem_drop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fb_auth = FirebaseAuth.getInstance();
        stud_fname = findViewById(R.id.id_stud_fname);
        stud_lname = findViewById(R.id.id_stud_lname);
        stud_email = findViewById(R.id.id_stud_email);
        stud_pwd = findViewById(R.id.id_stud_pwd);
        stud_cpwd = findViewById(R.id.id_stud_repwd);
        stud_dept_drop=(Spinner)findViewById(R.id.id_stud_dept);
        ArrayAdapter<String> UdeptAdapter = new ArrayAdapter<String>(Register.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.deptname));
        UdeptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stud_dept_drop.setAdapter(UdeptAdapter);

        stud_sem_drop=(Spinner)findViewById(R.id.id_stud_sem);
        ArrayAdapter<String> UsemAdapter = new ArrayAdapter<String>(Register.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Semnumber));
        UsemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stud_sem_drop.setAdapter(UsemAdapter);
    }
    public void register(View v) {
        String stud_mail = stud_email.getText().toString();
        String stud_pword = stud_pwd.getText().toString();
        String stud_cpword = stud_cpwd.getText().toString();
        if (TextUtils.isEmpty(stud_mail) || TextUtils.isEmpty(stud_pword) || TextUtils.isEmpty(stud_cpword)) {
            Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
        } else {
            if (!TextUtils.equals(stud_pword, stud_cpword)) {
                Toast.makeText(this, "passwords dont match", Toast.LENGTH_SHORT).show();
            }
            else {
                fb_auth.createUserWithEmailAndPassword(stud_mail,stud_pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this, "Failed", Toast.LENGTH_SHORT).show();
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