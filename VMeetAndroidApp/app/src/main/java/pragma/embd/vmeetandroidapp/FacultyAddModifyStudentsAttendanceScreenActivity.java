package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class FacultyAddModifyStudentsAttendanceScreenActivity extends Activity{

    ListView listView1;
    Button btn_save_modify;

    public Activity act;
    ListAdapter adapter;
    RequestParams params;

    static String str_date, str_type, str_semester, str_branch, str_subject, str_SessionTimings;
    static String str_students_usn, str_attendance_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facultyaddmodifytudentsattendancescreen);

        listView1 = (ListView) findViewById(R.id.listView1);
        btn_save_modify = (Button) findViewById(R.id.btn_save_modify);

        act = this;

        str_type = getIntent().getStringExtra("Type");
        str_semester = getIntent().getStringExtra("Semester");
        str_branch = getIntent().getStringExtra("Branch");
        str_subject = getIntent().getStringExtra("Subject");
        str_SessionTimings = getIntent().getStringExtra("SessionTimings");
        str_date = getIntent().getStringExtra("Dates");

        if(str_type.equalsIgnoreCase("AddAttendance")){

            btn_save_modify.setText("SAVE");
            getStudentsList();
        }
        else if(str_type.equalsIgnoreCase("ModifyAttendance")){

            btn_save_modify.setText("MODIFY");
            getStudentsAttendanceInfo(str_date.trim());
         //   getStudentsList();


        }



        btn_save_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                alert_box();


            }
        });

    }

    void getStudentsAttendanceInfo(String attendanceDate){


        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());
        params = new RequestParams();

        // Put Http parameter
        params.put("database", "vmeetdb");
        params.put("tablename", "studentsattendancedetails");
        params.put("columns", "Id, studentsusn");
        params.put("conditions", "facultyid='"
                + FacultyMainScreenActivity.str_empid.trim()
                + "' AND dates='"
                + str_date.trim()+ "' AND semester = '" + str_semester.trim() + "' AND subjectname = '" + str_subject.trim() + "'");

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
                                        "Operation unsuccessful, No Data Exists",
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

                                str_attendance_id = result.getString(0);
                                str_students_usn = result.getString(1);

                                getStudentsList();

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

    public void alert_box(){

        try{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("CONFIRM");
            builder.setIcon(R.drawable.infoicon);
            if(str_type.equalsIgnoreCase("AddAttendance")) {
                builder.setMessage("Are You Sure To Add this Attendance Details?");
            }
            else if(str_type.equalsIgnoreCase("ModifyAttendance")){
                    builder.setMessage("Are You Sure To Modify this Attendance Details?");
                }

            //Message here
            // Set an EditText view to get user input
            builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    dialog.cancel();
                    if(str_type.equalsIgnoreCase("AddAttendance")) {
                        addStudentsAttendance();
                    }
                    else if(str_type.equalsIgnoreCase("ModifyAttendance")){
                        modifyStudentsAttendance();
                    }

                }
            });

            builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
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

    void addStudentsAttendance(){

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        int int_month = today.month + 1;

        String str_date = "" + today.monthDay + "/" + int_month + "/" + today.year;
        String str_time = today.format("%k:%M:%S");

        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());

        params = new RequestParams();

        params.put("database", "vmeetdb");
        params.put("tablename", "studentsattendancedetails");
        params.put("columnnames",
                "studentsusn, facultyid, dates, times, sessiontime, attendance, semester, subjectname");
        params.put("columnvalues", "'"
                + ListviewAdapterStudentsAttendanceList.temp.trim() + "','"
                + FacultyMainScreenActivity.str_empid.trim() + "','"
                + str_date.trim() + "','"
                + str_time.trim() + "','"
                + str_SessionTimings.trim() + "','PRESENT','"
                + str_semester.trim() + "','"
                + str_subject.trim() + "'");



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
                                        "Students Attendance Added Successfully!",
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
                                            "insertionFailed")) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Adding Students Attendance Failed: "
                                                + obj.getString("error_msg"),
                                        Toast.LENGTH_LONG).show();
                            } else if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "connectionToDBFailed")) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Adding Students Attendance Failed: "
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

    void modifyStudentsAttendance(){
        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());

        params = new RequestParams();

        params.put("database", "vmeetdb");
        params.put("tablename", "studentsattendancedetails");
        params.put("columns",
                "studentsusn = '" + ListviewAdapterStudentsAttendanceList.temp.trim() + "'");
        params.put("conditions", "Id = '" + str_attendance_id + "'");

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
                                        "Students Attendance Updated Successfully",
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
                                        "Updating Students Attendance Failed: "
                                                + obj.getString("error_msg"),
                                        Toast.LENGTH_LONG).show();
                            } else if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "connectionToDBFailed")) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Updating Students Attendance Failed: "
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


    void getStudentsList(){

        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());
        params = new RequestParams();

        // Put Http parameter
        params.put("database", "vmeetdb");
        params.put("tablename", "studentdetails");
        params.put("columns", "distinct username, usn");
        params.put("conditions", "semester = '" + str_semester.trim() + "' AND branch = '" + str_branch + "'");


        callWebservice.callWebservice(params, "select",
                new AsyncHttpResponseHandler() {

                    public void onSuccess(int statusCode, Header[] header, byte[] byteResponse) {

                        try {

                            String response = new String(byteResponse);
                            // JSON Object
                            JSONObject obj = new JSONObject(
                                    response);
                            // When the JSON response has status
                            // boolean value assigned with true
                            if (obj.getString("0").equals(
                                    "connectionToDBFailed")) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Not able to fetch details, connection to database failed",
                                        Toast.LENGTH_LONG)
                                        .show();

                            } else if (obj.getString("0")
                                    .equals("noDataExists")) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Not able to fetch details, no data exists",
                                        Toast.LENGTH_LONG)
                                        .show();

                            }
                            // Else display error message
                            else {

                                BeanDetails bean = new BeanDetails();
                                ArrayList<BeanDetails> empBean = new ArrayList<BeanDetails>();
                                ArrayList<HashMap> list = new ArrayList<HashMap>();

                                for (int js = 0; js <= obj.length() - 1; js++) {
                                    JSONArray result = (JSONArray) obj
                                            .get(String.valueOf(js));

                                    for (int m = 0; m <= result.length() - 1; m += 2) {
                                        bean.value1 = result.getString(m);
                                    }

                                    for (int n = 1; n <= result.length() - 1; n += 2) {
                                        bean.value2 = result.getString(n);
                                    }

                                    empBean.add(new BeanDetails(bean.value1, bean.value2));
                                }

                                if (empBean.size() > 0) {
                                    for (int i = 0; i < empBean.size(); i++) {

                                        BeanDetails data = empBean.get(i);

                                        HashMap hashMap = new HashMap();

                                        hashMap.put(Constants.FIRST_COLUMN,
                                                data.value1);
                                        hashMap.put(Constants.SECOND_COLUMN,
                                                data.value2);
                                        list.add(hashMap);
                                    }

                                    ListviewAdapterStudentsAttendanceList.temp = ":";
                                    adapter = new ListviewAdapterStudentsAttendanceList(
                                            act, list);
                                    listView1.setAdapter(adapter);
                                } else {

                                }


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

                    // When the response returned by REST has
                    // Http response code other than '200'
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
