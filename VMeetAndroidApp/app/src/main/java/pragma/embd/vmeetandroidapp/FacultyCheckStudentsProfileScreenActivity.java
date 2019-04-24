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
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FacultyCheckStudentsProfileScreenActivity extends Activity {

    EditText et_student_usn, et_student_name, et_student_semester, et_student_branch, et_student_emailid, et_student_phoneno;
    Button btn_find, btn_update;

    RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facultycheckstudentsprofilescreen);

        et_student_usn = (EditText) findViewById(R.id.et_student_usn);
        et_student_name = (EditText) findViewById(R.id.et_student_name);
        et_student_semester = (EditText) findViewById(R.id.et_student_semester);
        et_student_branch = (EditText) findViewById(R.id.et_student_branch);
        et_student_emailid = (EditText) findViewById(R.id.et_student_emailid);
        et_student_phoneno = (EditText) findViewById(R.id.et_student_phoneno);
        btn_find = (Button) findViewById(R.id.btn_find);
        btn_update = (Button) findViewById(R.id.btn_update);

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_student_usn.getText().toString().toString().trim().equals("")||
                        et_student_usn.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Student USN", Toast.LENGTH_LONG).show();
                }
                else{

                    getStudentProfileDetails();

                }

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_student_semester.getText().toString().toString().trim().equals("")||
                        et_student_semester.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Student Semester", Toast.LENGTH_LONG).show();
                }
                else if(et_student_emailid.getText().toString().toString().trim().equals("")||
                        et_student_emailid.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Student Email ID", Toast.LENGTH_LONG).show();
                }
                else if(et_student_phoneno.getText().toString().toString().trim().equals("")||
                        et_student_phoneno.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Student Phone No", Toast.LENGTH_LONG).show();
                }
                else{

                    updateStudentProfileDetails();
                }

            }
        });


    }

    void getStudentProfileDetails(){


        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());
        params = new RequestParams();

        // Put Http parameter
        params.put("database", "vmeetdb");
        params.put("tablename", "studentdetails");
        params.put("columns", "username, semester, branch, emailid, phoneno");
        params.put("conditions", "usn='"
                + et_student_usn.getText().toString().trim()
                + "'");

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
                                        "Failed to fetch Students Profile, connection to database failed",
                                        Toast.LENGTH_LONG)
                                        .show();

                            } else if (obj.getString("0")
                                    .equals("noDataExists")) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Failed to fetch Students Profile, Invalid USN",
                                        Toast.LENGTH_LONG)
                                        .show();

                            }
                            // Else display error message
                            else {
                                // result array
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Operation Success",
                                        Toast.LENGTH_LONG)
                                        .show();
                                JSONArray result = null;
                                result = (JSONArray) obj
                                        .get(String
                                                .valueOf("0"));
                                // send the id ,user name to
                                // next screen


                                et_student_name.setText(result.getString(0));
                                et_student_semester.setText(result.getString(1));
                                et_student_branch.setText(result.getString(2));
                                et_student_emailid.setText(result.getString(3));
                                et_student_phoneno.setText(result.getString(4));
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

    void updateStudentProfileDetails(){
        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());

        params = new RequestParams();

        params.put("database", "vmeetdb");
        params.put("tablename", "studentdetails");
        params.put("columns",
                "semester = '" + et_student_semester.getText().toString().trim() +
                        "', emailid = '" + et_student_emailid.getText().toString().trim() +
                        "', phoneno = '" + et_student_phoneno.getText().toString().trim() + "'");
        params.put("conditions", "usn = '" + et_student_usn.getText().toString().trim() + "'");

        callWebservice.callWebservice(params, "update",
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] header, byte[] byteResponse) {

                        try {
                            String response = new String(byteResponse);
                            // JSON Object
                            JSONObject obj = new JSONObject(response
                            );

                            if (obj.getString("0")
                                    .equalsIgnoreCase(
                                            "updationSuccess")) {


                                Toast.makeText(
                                        getApplicationContext(),
                                        "Student Profile Updated Successfully",
                                        Toast.LENGTH_LONG).show();

                                finish();

                                Intent facultyMainscreen = new Intent(getApplicationContext(), FacultyMainScreenActivity.class);
                                facultyMainscreen.putExtra("UserName", FacultyMainScreenActivity.str_UserName);
                                facultyMainscreen.putExtra("EMPID", FacultyMainScreenActivity.str_empid);
                                facultyMainscreen.putExtra("Id", FacultyMainScreenActivity.str_id);
                                facultyMainscreen.putExtra("Branch", FacultyMainScreenActivity.str_branch);
                                facultyMainscreen.putExtra("EmailID", FacultyMainScreenActivity.str_EmailId);
                                facultyMainscreen.putExtra("PhoneNo", FacultyMainScreenActivity.str_MobileNo);
                                startActivity(facultyMainscreen);

                            }
                            // Else display error message
                            else if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "updationFailed")) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Updating Student Profile Failed: "
                                                + obj.getString("error_msg"),
                                        Toast.LENGTH_LONG).show();
                            } else if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "connectionToDBFailed")) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Updating Student Profile Failed: "
                                                + obj.getString("error_msg"),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {

                            Toast.makeText(
                                    getApplicationContext(),
                                    "Updating Students Attendance Failed: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }
                    }

                    // When the response returned by REST has Http
                    // response code other than '200'
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                        Toast.makeText(getApplicationContext(),
                                "failed", Toast.LENGTH_LONG)
                                .show();

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

    public void onBackPressed() {
        // TODO Auto-generated method stub

        super.onBackPressed();
        finish();

        Intent facultyMainscreen = new Intent(getApplicationContext(), FacultyMainScreenActivity.class);
        facultyMainscreen.putExtra("UserName", FacultyMainScreenActivity.str_UserName);
        facultyMainscreen.putExtra("EMPID", FacultyMainScreenActivity.str_empid);
        facultyMainscreen.putExtra("Id", FacultyMainScreenActivity.str_id);
        facultyMainscreen.putExtra("Branch", FacultyMainScreenActivity.str_branch);
        facultyMainscreen.putExtra("EmailID", FacultyMainScreenActivity.str_EmailId);
        facultyMainscreen.putExtra("PhoneNo", FacultyMainScreenActivity.str_MobileNo);
        startActivity(facultyMainscreen);



    }
}
