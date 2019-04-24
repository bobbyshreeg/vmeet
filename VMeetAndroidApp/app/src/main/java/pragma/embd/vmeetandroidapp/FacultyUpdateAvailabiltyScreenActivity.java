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

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class FacultyUpdateAvailabiltyScreenActivity extends Activity{

    EditText et_msg;
    Button btn_save;

    RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facultyupdateavailabiltyscreen);

        et_msg = (EditText) findViewById(R.id.et_msg);
        btn_save = (Button) findViewById(R.id.btn_save);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_msg.getText().toString().toString().trim().equals("")||
                        et_msg.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter details to Proceed", Toast.LENGTH_LONG).show();
                }
                else{
                    updateFacultyAvailability();

                }
            }
        });
    }

    void updateFacultyAvailability(){


        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());

        params = new RequestParams();

        params.put("database", "vmeetdb");
        params.put("tablename", "facultydetails");
        params.put("columns",
                "availability = '" + et_msg.getText().toString().trim() + "'");
        params.put("conditions", "empid = '" + FacultyMainScreenActivity.str_empid + "'");

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
                                        "Availabilty Updated Successfully",
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
                                        "Updating Availabilty Failed: "
                                                + obj.getString("error_msg"),
                                        Toast.LENGTH_LONG).show();
                            } else if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "connectionToDBFailed")) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Updating Availabilty Failed: "
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
