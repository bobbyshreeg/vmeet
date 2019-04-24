package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AdminCheckStudentFacultyProfileScreenActivity extends Activity {

    TextView tv_student_semester;
    EditText et_id, et_name, et_student_semester, et_branch, et_emailid, et_phoneno;
    Button btn_find;

    RequestParams params;

    String str_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admincheckstudentfacultyprofilescreen);

        tv_student_semester = (TextView) findViewById(R.id.tv_student_semester);
        et_id = (EditText) findViewById(R.id.et_id);
        et_name = (EditText) findViewById(R.id.et_name);
        et_student_semester = (EditText) findViewById(R.id.et_student_semester);
        et_branch = (EditText) findViewById(R.id.et_branch);
        et_emailid = (EditText) findViewById(R.id.et_emailid);
        et_phoneno = (EditText) findViewById(R.id.et_phoneno);
        btn_find = (Button) findViewById(R.id.btn_find);
     //   btn_update = (Button) findViewById(R.id.btn_update);


        str_type = getIntent().getStringExtra("Type");

        if(str_type.equalsIgnoreCase("Student")){

            et_id.setHint("Enter Student USN");
        }
        else if(str_type.equalsIgnoreCase("Faculty")){
            tv_student_semester.setVisibility(View.GONE);
            et_student_semester.setVisibility(View.GONE);
            et_id.setHint("Enter Faculty EMP ID");
        }

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_id.getText().toString().toString().trim().equals("")||
                        et_id.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Details to Proceed", Toast.LENGTH_LONG).show();
                }
                else{

                    getProfileDetails();

                }

            }
        });



    }

    void getProfileDetails(){


        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());
        params = new RequestParams();

        // Put Http parameter
        params.put("database", "vmeetdb");
        if(str_type.equalsIgnoreCase("Student")) {
            params.put("tablename", "studentdetails");
            params.put("columns", "username, branch, emailid, phoneno, semester");
            params.put("conditions", "usn='"
                    + et_id.getText().toString().trim()
                    + "'");
        }
        else if(str_type.equalsIgnoreCase("Faculty")) {
            params.put("tablename", "facultydetails");
            params.put("columns", "username, branch, emailid, phoneno, availability");
            params.put("conditions", "empid='"
                    + et_id.getText().toString().trim()
                    + "'");
        }

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


                                et_name.setText(result.getString(0));
                                et_student_semester.setText(result.getString(4));
                                et_branch.setText(result.getString(1));
                                et_emailid.setText(result.getString(2));
                                et_phoneno.setText(result.getString(3));
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



    public void onBackPressed() {
        // TODO Auto-generated method stub

        super.onBackPressed();
        finish();

        Intent adminScreen = new Intent(getApplicationContext(), AdminMainScreenActivity.class);
        startActivity(adminScreen);



    }
}
