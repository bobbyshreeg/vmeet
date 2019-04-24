package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FacultyCheckStudentsCGPAScreenActivity extends Activity {

    TextView tv_student_cgpa;
    EditText et_student_usn, et_s1, et_s2, et_s3, et_s4, et_s5, et_s6, et_s7, et_s8;
    Button btn_find, btn_cgpa;
    LinearLayout linear_layout;

    RequestParams params;

    String str_cgpa_id, str_cgpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facultycheckstudentscgpascreen);

        tv_student_cgpa = (TextView) findViewById(R.id.tv_student_cgpa);
        et_student_usn = (EditText) findViewById(R.id.et_student_usn);
        et_s1 = (EditText) findViewById(R.id.et_s1);
        et_s2 = (EditText) findViewById(R.id.et_s2);
        et_s3 = (EditText) findViewById(R.id.et_s3);
        et_s4 = (EditText) findViewById(R.id.et_s4);
        et_s5 = (EditText) findViewById(R.id.et_s5);
        et_s6 = (EditText) findViewById(R.id.et_s6);
        et_s7 = (EditText) findViewById(R.id.et_s7);
        et_s8 = (EditText) findViewById(R.id.et_s8);
        linear_layout = (LinearLayout)findViewById(R.id.linear_layout);
        btn_find = (Button) findViewById(R.id.btn_find);
        btn_cgpa = (Button) findViewById(R.id.btn_cgpa);

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_student_usn.getText().toString().toString().trim().equals("")||
                        et_student_usn.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Student USN", Toast.LENGTH_LONG).show();
                }
                else{

                    getStudentCGPADetails();

                }

            }
        });

        btn_cgpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_s1.getText().toString().toString().trim().equals("")||
                        et_s1.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Student SGPA for Semester 1", Toast.LENGTH_LONG).show();
                }
                else if(et_s2.getText().toString().toString().trim().equals("")||
                        et_s2.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Student SGPA for Semester 2", Toast.LENGTH_LONG).show();
                }
                else if(et_s3.getText().toString().toString().trim().equals("")||
                        et_s3.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Student SGPA for Semester 3", Toast.LENGTH_LONG).show();
                }
                else if(et_s4.getText().toString().toString().trim().equals("")||
                        et_s4.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Student SGPA for Semester 4", Toast.LENGTH_LONG).show();
                }
                else if(et_s5.getText().toString().toString().trim().equals("")||
                        et_s5.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Student SGPA for Semester 5", Toast.LENGTH_LONG).show();
                }
                else if(et_s6.getText().toString().toString().trim().equals("")||
                        et_s6.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Student SGPA for Semester 6", Toast.LENGTH_LONG).show();
                }
                else if(et_s7.getText().toString().toString().trim().equals("")||
                        et_s7.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Student SGPA for Semester 7", Toast.LENGTH_LONG).show();
                }
                else if(et_s8.getText().toString().toString().trim().equals("")||
                        et_s8.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Student SGPA for Semester 8", Toast.LENGTH_LONG).show();
                }
                else{

                    str_cgpa = String.valueOf(
                            (Double.valueOf(et_s1.getText().toString().trim()) +
                                    Double.valueOf(et_s2.getText().toString().trim()) +
                                    Double.valueOf(et_s3.getText().toString().trim()) +
                                    Double.valueOf(et_s4.getText().toString().trim()) +
                                    Double.valueOf(et_s5.getText().toString().trim()) +
                                    Double.valueOf(et_s6.getText().toString().trim()) +
                                    Double.valueOf(et_s7.getText().toString().trim()) +
                                    Double.valueOf(et_s8.getText().toString().trim()))/8);
              //      Toast.makeText(getApplicationContext(),"CGPA is: " + str_cgpa, Toast.LENGTH_LONG).show();
                    alert_box();

                }

            }
        });


    }

    public void alert_box(){

        try{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("CGPA");
            builder.setIcon(R.drawable.infoicon);

            builder.setMessage("Your CGPA is: " + str_cgpa);


            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    dialog.cancel();
                    updateStudentSGPAAndCGPADetails();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        catch(Exception e){

            Toast.makeText(getApplicationContext(), "error is: " + e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    void getStudentCGPADetails(){


        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());
        params = new RequestParams();

        // Put Http parameter
        params.put("database", "vmeetdb");
        params.put("tablename", "studentscgpadetails");
        params.put("columns", "Id, s1, s2, s3, s4, s5, s6, s7, s8, cgpa");
        params.put("conditions", "studentusn='"
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
                                        "Failed to fetch Student Details, connection to database failed",
                                        Toast.LENGTH_LONG)
                                        .show();

                            } else if (obj.getString("0")
                                    .equals("noDataExists")) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Failed to fetch Student Details, Invalid USN",
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


                                str_cgpa_id = result.getString(0);
                                et_s1.setText(result.getString(1));
                                et_s2.setText(result.getString(2));
                                et_s3.setText(result.getString(3));
                                et_s4.setText(result.getString(4));
                                et_s5.setText(result.getString(5));
                                et_s6.setText(result.getString(6));
                                et_s7.setText(result.getString(7));
                                et_s8.setText(result.getString(8));
                                str_cgpa = result.getString(9);
                                tv_student_cgpa.setText("CGPA is: " + str_cgpa);
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

    void updateStudentSGPAAndCGPADetails(){

        tv_student_cgpa.setText("CGPA is: " + str_cgpa);
        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());

        params = new RequestParams();

        params.put("database", "vmeetdb");
        params.put("tablename", "studentscgpadetails");
        params.put("columns",
                "s1 = '" + et_s1.getText().toString().trim() +
                        "', s2 = '" + et_s2.getText().toString().trim() +
                        "', s3 = '" + et_s3.getText().toString().trim() +
                        "', s4 = '" + et_s4.getText().toString().trim() +
                        "', s5 = '" + et_s5.getText().toString().trim() +
                        "', s6 = '" + et_s6.getText().toString().trim() +
                        "', s7 = '" + et_s7.getText().toString().trim() +
                        "', s8 = '" + et_s8.getText().toString().trim() +
        "', cgpa = '" + str_cgpa + "'");
        params.put("conditions", "studentusn = '" + et_student_usn.getText().toString().trim() + "'");

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
                                        "Student CGPA Updated Successfully",
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
                                        "Updating Student CGPA Failed: "
                                                + obj.getString("error_msg"),
                                        Toast.LENGTH_LONG).show();
                            } else if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "connectionToDBFailed")) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Updating Student CGPA Failed: "
                                                + obj.getString("error_msg"),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {

                            Toast.makeText(
                                    getApplicationContext(),
                                    "Updating Students CGPA Failed: " + e.getMessage(),
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
