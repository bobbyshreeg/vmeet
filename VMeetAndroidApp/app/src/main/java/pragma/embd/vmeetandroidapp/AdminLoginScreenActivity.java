package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

public class AdminLoginScreenActivity extends Activity {

    EditText et_username, et_password;
    Button btn_login;

   String str_usertype;

    RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminlogin_screen);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);




        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(et_username.getText().toString().toString().trim().equals("")||
                        et_username.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Username", Toast.LENGTH_LONG).show();
                }

                else  if(et_password.getText().toString().toString().trim().equals("")||
                        et_password.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Password", Toast.LENGTH_LONG).show();
                }
                else  if(et_username.getText().toString().toString().trim().equals("BinduM")||
                        et_password.getText().toString().toString().trim().equals("Bindu.123") ){

                    Intent adminScreen = new Intent(getApplicationContext(), AdminMainScreenActivity.class);
                    startActivity(adminScreen);
                }
                else{

                    Toast.makeText(getApplicationContext(),"Invalid Credentials", Toast.LENGTH_LONG).show();
                }

            }
        });



    }



    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        super.onBackPressed();
        finish();
        Intent goBackScreen = new Intent(getApplicationContext(), WelcomeScreenActivity.class);
        startActivity(goBackScreen);
    }
}

