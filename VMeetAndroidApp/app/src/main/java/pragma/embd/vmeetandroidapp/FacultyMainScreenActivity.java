package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static pragma.embd.vmeetandroidapp.Constants.FIRST_COLUMN;

public class FacultyMainScreenActivity extends Activity{

    TextView tv_heading;
    ImageView img_attendance, img_scheduler, img_notes, img_students_profile, img_update_availability, img_view_appointments, img_student_cgpa, img_logout;
    static String str_UserName, str_id, str_empid, str_branch, str_EmailId, str_MobileNo;

    String[] semester = {"1", "2", "3", "4", "5", "6", "7", "8"};
    String str_semester, str_subject, str_date;

    RequestParams params;

    ArrayList<String> list;

    Spinner sp_semester, sp_subject, sp_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facultymain_screen);

        tv_heading = (TextView) findViewById(R.id.tv_heading);
        img_attendance = (ImageView) findViewById(R.id.img_attendance);
        img_scheduler = (ImageView) findViewById(R.id.img_scheduler);
        img_notes = (ImageView) findViewById(R.id.img_notes);
        img_students_profile = (ImageView) findViewById(R.id.img_students_profile);
        img_update_availability = (ImageView) findViewById(R.id.img_update_availability);
        img_view_appointments = (ImageView) findViewById(R.id.img_view_appointments);
        img_student_cgpa = (ImageView) findViewById(R.id.img_student_cgpa);
        img_logout = (ImageView) findViewById(R.id.img_logout);

        str_UserName = getIntent().getStringExtra("UserName");
        str_empid = getIntent().getStringExtra("EMPID");
        str_id = getIntent().getStringExtra("Id");
        str_MobileNo = getIntent().getStringExtra("PhoneNo");
        str_EmailId = getIntent().getStringExtra("EmailID");
        str_branch = getIntent().getStringExtra("Branch");

        tv_heading.setText("Welcome " + str_UserName.toUpperCase());

    //    getSubjectsForSemester("1");

        img_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                alert_box();
            }
        });

        img_students_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent studentsProfile = new Intent(getApplicationContext(), FacultyCheckStudentsProfileScreenActivity.class);
                startActivity(studentsProfile);
            }
        });

        img_scheduler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent schedulerScreen = new Intent(getApplicationContext(), FacultySchedulerlistScreenActivity.class);
                startActivity(schedulerScreen);
            }
        });

        img_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent listNotesScreen = new Intent(getApplicationContext(), FacultyNotesListScreenActivity.class);
                startActivity(listNotesScreen);
            }
        });

        img_update_availability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent facultyupdateAvailability = new Intent(getApplicationContext(), FacultyUpdateAvailabiltyScreenActivity.class);
                startActivity(facultyupdateAvailability);
            }
        });

        img_view_appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent facultyviewAppointments = new Intent(getApplicationContext(), FacultyViewAppointmentListScreenActivity.class);
                startActivity(facultyviewAppointments);
            }
        });

        img_student_cgpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent cgpaScreen = new Intent(getApplicationContext(), FacultyCheckStudentsCGPAScreenActivity.class);
                startActivity(cgpaScreen);
            }
        });

        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });



    }


    @Override
    protected Dialog onCreateDialog(int id) {
        AlertDialog dialogDetails = null;

        switch (id) {
            case 1:
                LayoutInflater inflater = LayoutInflater.from(this);
                View dialogview = inflater.inflate(R.layout.alertbox_add_attendance, null);
                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(this);
                dialogbuilder.setTitle("Add Attendance");
                dialogbuilder.setView(dialogview);
                dialogDetails = dialogbuilder.create();
                break;
            case 2:
                LayoutInflater inflater1 = LayoutInflater.from(this);
                View dialogview1 = inflater1.inflate(R.layout.alertbox_modify_attendance, null);
                AlertDialog.Builder dialogbuilder1 = new AlertDialog.Builder(this);
                dialogbuilder1.setTitle("Modify Attendance");
                dialogbuilder1.setView(dialogview1);
                dialogDetails = dialogbuilder1.create();
                break;
        }
        return dialogDetails;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {


        switch (id) {
            case 1:
                final AlertDialog alertDialog = (AlertDialog) dialog;
                final EditText et_session_timings = (EditText)alertDialog.findViewById(R.id.et_session_timings);
                sp_semester = (Spinner)alertDialog.findViewById(R.id.sp_semester);
                sp_subject = (Spinner)alertDialog.findViewById(R.id.sp_subject);
                Button btn_enter = (Button) alertDialog.findViewById(R.id.btn_enter);
                final Button btn_cancel = (Button) alertDialog.findViewById(R.id.btn_cancel);

                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, semester);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_semester.setAdapter(adapter);


          //      getSubjectsForSemester("1");
                sp_semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        // TODO Auto-generated method stub

                        str_semester = parent.getItemAtPosition(position).toString().trim();
                        getSubjectsForSemester(str_semester);

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                        str_semester = "1";
                        getSubjectsForSemester(str_semester);

                    }
                });



                sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        // TODO Auto-generated method stub

                        str_subject = parent.getItemAtPosition(position).toString().trim();


                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                        str_subject = "1";


                    }
                });


                btn_enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*String name = userName.getText().toString();
                        Toast.makeText(Activity.this, name,Toast.LENGTH_SHORT).show();*/
                        if(et_session_timings.getText().toString().toString().trim().equals("")||
                                et_session_timings.getText().toString().trim().length()==0 ){

                            Toast.makeText(getApplicationContext(),"Please Enter Session Timings", Toast.LENGTH_LONG).show();
                        }
                        else{

                            alertDialog.dismiss();
                            finish();
                            Intent studentsAttendance = new Intent(getApplicationContext(), FacultyAddModifyStudentsAttendanceScreenActivity.class);
                            studentsAttendance.putExtra("Type", "AddAttendance");
                            studentsAttendance.putExtra("Semester", str_semester);
                            studentsAttendance.putExtra("Branch", str_branch);
                            studentsAttendance.putExtra("Subject", str_subject);
                            studentsAttendance.putExtra("SessionTimings", et_session_timings.getText().toString().trim());
                            studentsAttendance.putExtra("Dates", "AA");
                            startActivity(studentsAttendance);
                        }
                    }
                    });


             btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
             break;

            case 2:
                final AlertDialog alertDialog1 = (AlertDialog) dialog;
                sp_semester = (Spinner)alertDialog1.findViewById(R.id.sp_semester);
                sp_subject = (Spinner)alertDialog1.findViewById(R.id.sp_subject);
                sp_date = (Spinner)alertDialog1.findViewById(R.id.sp_date);
                Button btn_get_date = (Button) alertDialog1.findViewById(R.id.btn_get_date);
                Button btn_modify_enter = (Button) alertDialog1.findViewById(R.id.btn_modify_enter);
                final Button btn_modify_cancel = (Button) alertDialog1.findViewById(R.id.btn_modify_cancel);

                ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, semester);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_semester.setAdapter(adapter1);


                getSubjectsForSemester("1");


                sp_semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        // TODO Auto-generated method stub

                        str_semester = parent.getItemAtPosition(position).toString().trim();
                        getSubjectsForSemester(str_semester);

                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                        str_semester = "1";
                        getSubjectsForSemester(str_semester);

                    }
                });



                sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        // TODO Auto-generated method stub

                        str_subject = parent.getItemAtPosition(position).toString().trim();


                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                        str_subject = "1";
                        getSubjectsForSemester("1");


                    }
                });

                btn_get_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        getDatesForAttendance();
                    }
                });

                sp_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        // TODO Auto-generated method stub

                        str_date = parent.getItemAtPosition(position).toString().trim();


                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                      //  str_semester = "1";


                    }
                });


                btn_modify_enter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*String name = userName.getText().toString();
                        Toast.makeText(Activity.this, name,Toast.LENGTH_SHORT).show();*/

                            alertDialog1.dismiss();
                            finish();
                            Intent studentsAttendance = new Intent(getApplicationContext(), FacultyAddModifyStudentsAttendanceScreenActivity.class);
                            studentsAttendance.putExtra("Type", "ModifyAttendance");
                            studentsAttendance.putExtra("Semester", str_semester);
                            studentsAttendance.putExtra("Branch", str_branch);
                            studentsAttendance.putExtra("Subject", str_subject);
                            studentsAttendance.putExtra("SessionTimings", "AA");
                            studentsAttendance.putExtra("Dates", str_date);
                            startActivity(studentsAttendance);

                    }
                });


                btn_modify_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                    }
                });
                break;
                }
        }

        void getDatesForAttendance(){

            CallingWebservice callWebservice = new CallingWebservice(
                    getApplicationContext());
            params = new RequestParams();

            // Put Http parameter
            params.put("database", "vmeetdb");
            params.put("tablename", "studentsattendancedetails");
            params.put("columns", "distinct dates");
            params.put("conditions", "facultyid = '" + str_empid.trim() + "' AND semester='" + str_semester.trim() + "' AND subjectname = '" + str_subject.trim() + "'");


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
                                    list = new ArrayList<String>();
                                    BeanDetails bean = new BeanDetails();
                                    ArrayList<BeanDetails> empBean = new ArrayList<BeanDetails>();


                                    for (int js = 0; js <= obj.length() - 1; js++) {
                                        JSONArray result = (JSONArray) obj
                                                .get(String.valueOf(js));

                                        for (int m = 0; m <= result.length() - 1; m += 1) {
                                            if(js==0)
                                                str_date = result.getString(m);

                                            bean.value1 = result.getString(m);
                                        }

                             /*   for (int n = 1; n <= result.length() - 1; n += 1) {
                                    bean.value2 = result.getString(n);
                                }
*/

                                        empBean.add(new BeanDetails(bean.value1));
                                    }

                                    if (empBean.size() > 0) {
                                        for (int i = 0; i < empBean.size(); i++) {

                                            BeanDetails data = empBean.get(i);

                                            HashMap hashMap = new HashMap();

                                            hashMap.put(FIRST_COLUMN,
                                                    data.value1);

                                            list.add((String)hashMap.get(FIRST_COLUMN));
                                        }

                                        ArrayAdapter adapter1 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, list);
                                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        sp_date.setAdapter(adapter1);

                               /* adapter = new ListviewAdapterViewCustomerCropBidsList(
                                        act, list);
                                listView1.setAdapter(adapter);*/
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


    public void alert_box(){

        try{

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("OPTIONS");
            builder.setIcon(R.drawable.infoicon);

            builder.setMessage("What you want to Do?");

            //Message here
            // Set an EditText view to get user input
            builder.setNegativeButton("MODIFY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    dialog.cancel();
                    showDialog(2);

                }
            });

            builder.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    dialog.cancel();
                }
            });

            builder.setNeutralButton("ADD", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    dialog.cancel();
                    showDialog(1);
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        catch(Exception e){

            Toast.makeText(getApplicationContext(), "error is: " + e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

void getSubjectsForSemester(String str_semester){

    CallingWebservice callWebservice = new CallingWebservice(
            getApplicationContext());
    params = new RequestParams();

    // Put Http parameter
    params.put("database", "vmeetdb");
    params.put("tablename", "branchsubjectdetails");
    params.put("columns", "distinct subjectname");
    params.put("conditions", "branchname = '" + str_branch.trim() + "' AND semester='" + str_semester.trim() + "'");


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
                            list = new ArrayList<String>();
                            BeanDetails bean = new BeanDetails();
                            ArrayList<BeanDetails> empBean = new ArrayList<BeanDetails>();


                            for (int js = 0; js <= obj.length() - 1; js++) {
                                JSONArray result = (JSONArray) obj
                                        .get(String.valueOf(js));



                                for (int m = 0; m <= result.length() - 1; m += 1) {
                                //    Toast.makeText(getApplicationContext(), "m value is: " + m, Toast.LENGTH_SHORT).show();
                                    if(js==0) {
                                        str_subject = result.getString(m);
                                 //       Toast.makeText(getApplicationContext(), "Subject in if is: " + str_subject, Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                //        Toast.makeText(getApplicationContext(), "Subject in else is: " + str_subject, Toast.LENGTH_SHORT).show();
                                    }

                                    bean.value1 = result.getString(m);
                                }

                             /*   for (int n = 1; n <= result.length() - 1; n += 1) {
                                    bean.value2 = result.getString(n);
                                }
*/

                                empBean.add(new BeanDetails(bean.value1));
                            }

                            if (empBean.size() > 0) {
                                for (int i = 0; i < empBean.size(); i++) {

                                    BeanDetails data = empBean.get(i);

                                    HashMap hashMap = new HashMap();

                                    hashMap.put(FIRST_COLUMN,
                                            data.value1);

                                    list.add((String)hashMap.get(FIRST_COLUMN));
                                }

                                ArrayAdapter adapter1 = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, list);
                                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sp_subject.setAdapter(adapter1);

                               /* adapter = new ListviewAdapterViewCustomerCropBidsList(
                                        act, list);
                                listView1.setAdapter(adapter);*/
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

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        super.onBackPressed();
        finish();
        Intent goBackScreen = new Intent(getApplicationContext(), FacultyLoginScreenActivity.class);
        startActivity(goBackScreen);
    }
}
