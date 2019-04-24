package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class StudentLoginScreenActivity extends Activity {

 //   EditText et_username;
    EditText et_usn, et_password;
    Button btn_login, btn_register, btn_forgot_password;

   String str_usertype;

    RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentlogin_screen);

    //    et_username = (EditText) findViewById(R.id.et_username);
        et_usn = (EditText) findViewById(R.id.et_usn);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_forgot_password = (Button) findViewById(R.id.btn_forgot_password);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(et_username.getText().toString().toString().trim().equals("")||
                        et_username.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Username", Toast.LENGTH_LONG).show();
                }
                else */
                if(et_usn.getText().toString().toString().trim().equals("")||
                        et_usn.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter USN", Toast.LENGTH_LONG).show();
                }
                else  if(et_password.getText().toString().toString().trim().equals("")||
                        et_password.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Password", Toast.LENGTH_LONG).show();
                }
                else{

                    validateStudent();
                }

            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent bb = new Intent(getApplicationContext(),OwnerRegistrationScreenActivity.class);
                // startActivity(bb);

                    finish();
                    Intent registerScreen = new Intent(getApplicationContext(), StudentRegistrationScreenActivity.class);
                    startActivity(registerScreen);

            }
        });

        btn_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent bb = new Intent(getApplicationContext(),OwnerRegistrationScreenActivity.class);
                // startActivity(bb);

                finish();
                Intent forgotPasswordScreen = new Intent(getApplicationContext(), ForgotPasswordScreenActivity.class);
                forgotPasswordScreen.putExtra("UserType", "Student");
                startActivity(forgotPasswordScreen);

            }
        });

    }

    void validateStudent(){


        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());
        params = new RequestParams();

        // Put Http parameter
        params.put("database", "vmeetdb");
        params.put("tablename", "studentdetails");
        params.put("columns", "Id, username, semester, emailid, phoneno, branch");
        params.put("conditions", "pwd='"
                + et_password.getText().toString().trim()+ "' AND usn = '" + et_usn.getText().toString().trim() + "'");

        callWebservice.callWebservice(params, "select",
                new AsyncHttpResponseHandler() {

                    public void onSuccess(int statusCode, Header[] header, byte[] byteResponse) {

                        try {



                            String response = new String(byteResponse);
                            // JSON Object
                            JSONObject obj = new JSONObject(response
                            );
                            // When the JSON response has status
                            // boolean value assigned with true
                            if (obj.getString("0").equals(
                                    "connectionToDBFailed")) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Login unsuccessful, connection to database failed",
                                        Toast.LENGTH_LONG)
                                        .show();

                            } else if (obj.getString("0")
                                    .equals("noDataExists")) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Login unsuccessful, Invalid credentials",
                                        Toast.LENGTH_LONG)
                                        .show();

                            }
                            // Else display error message
                            else {
                                // result array
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Login successful",
                                        Toast.LENGTH_LONG)
                                        .show();
                                JSONArray result = null;
                                result = (JSONArray) obj
                                        .get(String
                                                .valueOf("0"));
                                // send the id ,user name to
                                // next screen

                                finish();

                                    Intent studentMainscreen = new Intent(getApplicationContext(), StudentsMainScreenActivity.class);
                                studentMainscreen.putExtra("UserName", result.getString(1));
                                studentMainscreen.putExtra("USN", et_usn.getText().toString().trim());
                                studentMainscreen.putExtra("Id", result.getString(0));
                                studentMainscreen.putExtra("Semester", result.getString(2));
                                studentMainscreen.putExtra("EmailID", result.getString(3));
                                studentMainscreen.putExtra("PhoneNo", result.getString(4));
                                studentMainscreen.putExtra("Branch", result.getString(5));
                                    startActivity(studentMainscreen);
                                                                }
                        } catch (Exception e) {

                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occured [Server's JSON response might be invalid]!"
                                            + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }
                    }


                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                        Toast.makeText(getApplicationContext(),
                                "failed", Toast.LENGTH_LONG)
                                .show();

                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than
                        // 404, 500
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]",
                                    Toast.LENGTH_LONG).show();
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

