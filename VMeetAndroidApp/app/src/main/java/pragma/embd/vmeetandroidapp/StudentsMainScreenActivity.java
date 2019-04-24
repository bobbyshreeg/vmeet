package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class StudentsMainScreenActivity extends Activity{

    TextView tv_heading;
    ImageView img_scheduler, img_notes, img_students_profile, img_faculty, img_appointments, img_student_cgpa, img_logout;
    static String str_UserName, str_id, str_usn, str_MobileNo, str_EmailId, str_semester, str_branch;

    RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentmain_screen);

        tv_heading = (TextView) findViewById(R.id.tv_heading);
     //   img_attendance = (ImageView) findViewById(R.id.img_attendance);
        img_scheduler = (ImageView) findViewById(R.id.img_scheduler);
        img_notes = (ImageView) findViewById(R.id.img_notes);
        img_students_profile = (ImageView) findViewById(R.id.img_students_profile);
        img_faculty = (ImageView) findViewById(R.id.img_faculty);
        img_appointments = (ImageView) findViewById(R.id.img_appointments);
        img_student_cgpa = (ImageView) findViewById(R.id.img_student_cgpa);
        img_logout = (ImageView) findViewById(R.id.img_logout);

        str_UserName = getIntent().getStringExtra("UserName");
        str_id = getIntent().getStringExtra("Id");
        str_usn = getIntent().getStringExtra("USN");
        str_MobileNo = getIntent().getStringExtra("PhoneNo");
        str_EmailId = getIntent().getStringExtra("EmailID");
        str_semester = getIntent().getStringExtra("Semester");
        str_branch = getIntent().getStringExtra("Branch");

        tv_heading.setText("Welcome " + str_UserName.toUpperCase());

       /* img_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });*/

        img_scheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent schedulerScreen = new Intent(getApplicationContext(), StudentSchedulerlistScreenActivity.class);
                startActivity(schedulerScreen);

            }
        });

        img_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent listNotesScreen = new Intent(getApplicationContext(), StudentsNotesListScreenActivity.class);
                startActivity(listNotesScreen);
            }
        });

        img_students_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent studentsProfile = new Intent(getApplicationContext(), StudentsProfileScreenActivity.class);
                startActivity(studentsProfile);
            }
        });

        img_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent facaultyListScreen = new Intent(getApplicationContext(), FacultyAppointmentListScreenActivity.class);
                startActivity(facaultyListScreen);
            }
        });

        img_appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent myAppointmentsList = new Intent(getApplicationContext(), StudentsAppointmentListScreenActivity.class);
                startActivity(myAppointmentsList);

            }
        });
        img_student_cgpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getMyCGPA();
            }
        });

        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }

    void getMyCGPA(){


        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());
        params = new RequestParams();

        // Put Http parameter
        params.put("database", "vmeetdb");
        params.put("tablename", "studentscgpadetails");
        params.put("columns", "Id, cgpa");
        params.put("conditions", "studentusn='"
                + str_usn.trim() + "'");

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
                                        "Operation unsuccessful, connection to database failed",
                                        Toast.LENGTH_LONG)
                                        .show();

                            } else if (obj.getString("0")
                                    .equals("noDataExists")) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Operation unsuccessful, Invalid credentials",
                                        Toast.LENGTH_LONG)
                                        .show();

                            }
                            // Else display error message
                            else {
                                // result array
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Operation successful",
                                        Toast.LENGTH_LONG)
                                        .show();
                                JSONArray result = null;
                                result = (JSONArray) obj
                                        .get(String
                                                .valueOf("0"));
                                // send the id ,user name to
                                // next screen

                        alert_box(result.getString(1));
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

    public void alert_box(String str_cgpa){

        try{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("CGPA");
            builder.setIcon(R.drawable.infoicon);

            builder.setMessage("Your CGPA is: " + str_cgpa);


            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        catch(Exception e){

            Toast.makeText(getApplicationContext(), "error is: " + e.getMessage() , Toast.LENGTH_SHORT).show();
        }
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
