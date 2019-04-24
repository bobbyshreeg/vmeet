package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class StudentRegistrationScreenActivity extends Activity {

    EditText et_usn, et_mobile_number, et_email_id, et_semester,
            et_username, et_password, et_confirm_password;
    Spinner sp_branch;
    Button btn_register_user;


    RequestParams params;

    String[] branch = {"CSE", "ECE", "TCE", "ME"};
    String str_branch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentregistration_screen);

        et_usn = (EditText) findViewById(R.id.et_usn);
        et_semester = (EditText) findViewById(R.id.et_semester);
      //  et_branch = (EditText) findViewById(R.id.et_branch);
        sp_branch = (Spinner) findViewById(R.id.sp_branch);
        et_mobile_number = (EditText) findViewById(R.id.et_mobile_number);
        et_email_id = (EditText) findViewById(R.id.et_email_id);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        sp_branch = (Spinner)findViewById(R.id.sp_branch);
        btn_register_user = (Button) findViewById(R.id.btn_register_user);



        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, branch);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_branch.setAdapter(adapter);



        sp_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                str_branch = parent.getItemAtPosition(position).toString().trim();

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

                str_branch = "CSE";

            }
        });


        btn_register_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(et_username.getText().toString().toString().trim().equals("")||
                        et_username.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter User Name", Toast.LENGTH_LONG).show();
                }
                else if(et_usn.getText().toString().toString().trim().equals("")||
                        et_usn.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter USN", Toast.LENGTH_LONG).show();
                }
                else if(!et_usn.getText().toString().toString().trim().contains("1KS")||
                        !et_usn.getText().toString().trim().contains("CS") ){

                    Toast.makeText(getApplicationContext(),"Please Enter USN in format '1KS(year)CS(num)", Toast.LENGTH_LONG).show();
                }
                else if(et_semester.getText().toString().toString().trim().equals("")||
                        et_semester.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Semester", Toast.LENGTH_LONG).show();
                }
               /* else if(et_branch.getText().toString().toString().trim().equals("")||
                        et_branch.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Branch", Toast.LENGTH_LONG).show();
                }*/
                else if(et_mobile_number.getText().toString().toString().trim().equals("")||
                        et_mobile_number.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Mobile Number", Toast.LENGTH_LONG).show();
                }

                else if(et_mobile_number.getText().toString().trim().length()!=10 ){

                    Toast.makeText(getApplicationContext(),"Please Enter 10 digit Mobile Number", Toast.LENGTH_LONG).show();
                }

                else if(et_email_id.getText().toString().toString().trim().equals("")||
                        et_email_id.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Email ID", Toast.LENGTH_LONG).show();
                }

                else  if(et_password.getText().toString().toString().trim().equals("")||
                        et_password.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Password", Toast.LENGTH_LONG).show();
                }
                else  if(et_confirm_password.getText().toString().toString().trim().equals("")||
                        et_confirm_password.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Confirm Password", Toast.LENGTH_LONG).show();
                }

                else if (!et_password.getText().toString().trim()
                        .equals(et_confirm_password.getText().toString().trim())) {

                    Toast.makeText(getApplicationContext(),
                            "Password Mismatch", Toast.LENGTH_SHORT)
                            .show();
                }
                else{

                    registerStudent();
                }


            }
        });
    }

    void registerStudent(){


        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());

        params = new RequestParams();

        params.put("database", "vmeetdb");
        params.put("tablename", "studentdetails");
        params.put("columnnames",
                "username, usn, semester, branch, emailid, phoneno, pwd");
        params.put("columnvalues", "'"
                + et_username.getText().toString().trim() + "','"
                + et_usn.getText().toString().trim() + "','"
                + et_semester.getText().toString().trim() + "','"
                + str_branch.trim() + "','"
                + et_email_id.getText().toString().trim() + "','"
                + et_mobile_number.getText().toString().trim() + "','"
                + et_password.getText().toString().trim() +  "'");



        callWebservice.callWebservice(params, "insert",
                new AsyncHttpResponseHandler() {


                    public void onSuccess(int statusCode, Header[] header, byte[] byteResponse) {

                        try {
                            // JSON Object

                            String response = new String(byteResponse);

                            JSONObject obj = new JSONObject(
                                    response);

                            if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "insertionSuccess")) {



                                addStudentCGPAdetails();



                            }
                            // Else display error message
                            else if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "insertionFailed")) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Registration Failed: "
                                                + obj.getString("error_msg"),
                                        Toast.LENGTH_LONG).show();
                            } else if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "connectionToDBFailed")) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Registration Failed: "
                                                + obj.getString("error_msg"),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {

                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occured [Server's JSON response might be invalid]!",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }
                    }

                    // When the response returned by REST has Http
                    // response code other than '200'
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
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
                        // When Http response code other than 404,
                        // 500
                        else {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                });



    }
    void addStudentCGPAdetails()
    {


        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());

        params = new RequestParams();

        params.put("database", "vmeetdb");
        params.put("tablename", "studentscgpadetails");
        params.put("columnnames",
                "studentusn, s1, s2, s3, s4, s5, s6, s7, s8, cgpa");
        params.put("columnvalues", "'"
                + et_usn.getText().toString().trim() + "','0','0','0','0','0','0','0','0','0'");



        callWebservice.callWebservice(params, "insert",
                new AsyncHttpResponseHandler() {


                    public void onSuccess(int statusCode, Header[] header, byte[] byteResponse) {

                        try {
                            // JSON Object

                            String response = new String(byteResponse);

                            JSONObject obj = new JSONObject(
                                    response);

                            if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "insertionSuccess")) {



                                Toast.makeText(
                                        getApplicationContext(),
                                        "Initialized Student CGPA data!",
                                        Toast.LENGTH_LONG).show();

                                Toast.makeText(
                                        getApplicationContext(),
                                        "You are successfully registered!",
                                        Toast.LENGTH_LONG).show();


                                finish();
                                Intent login_screen = new Intent(getApplicationContext(), StudentLoginScreenActivity.class);
                                startActivity(login_screen);

                            }
                            // Else display error message
                            else if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "insertionFailed")) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Operation Failed: "
                                                + obj.getString("error_msg"),
                                        Toast.LENGTH_LONG).show();
                            } else if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "connectionToDBFailed")) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Operation Failed: "
                                                + obj.getString("error_msg"),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {

                            Toast.makeText(
                                    getApplicationContext(),
                                    "Error Occured [Server's JSON response might be invalid]!",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }
                    }

                    // When the response returned by REST has Http
                    // response code other than '200'
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(),
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
                        // When Http response code other than 404,
                        // 500
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
        Intent goBackScreen = new Intent(getApplicationContext(), StudentLoginScreenActivity.class);
        startActivity(goBackScreen);
    }
}
