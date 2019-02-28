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

public class MainActivity extends AppCompatActivity {

    EditText email,passwd;
    FirebaseAuth firebaseAuth;
    Spinner usertype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser()!=null){
            Toast.makeText(this, "User already logged in!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,Main2Activity.class));
        }
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.email);
        passwd=findViewById(R.id.pwd);
        usertype=(Spinner)findViewById(R.id.utype);
        ArrayAdapter<String> UtypeAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Usertype));
        UtypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usertype.setAdapter(UtypeAdapter);

    }
    public void login(View v){
        String mail=email.getText().toString();
        String pword=passwd.getText().toString();
        if (TextUtils.isEmpty(mail)||TextUtils.isEmpty(pword)){
            Toast.makeText(this, "Empty username or password", Toast.LENGTH_SHORT).show();
        }
        else{
            firebaseAuth.signInWithEmailAndPassword(mail,pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            );
        }
    }
    public void signup(View v){
        startActivity(new Intent(this,Main2Activity.class));
    }
}
