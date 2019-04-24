package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class StudentRequestAppointmentsScreenActivity extends Activity{

    TextView tv_heading;
    RadioGroup radiogroup_appointment_type, radiogroup_appointment_occurance;
    RadioButton radiobutton_general, radiobutton_important, radiobutton_once, radiobutton_weekly;
    Spinner sp_days;
    DatePicker simpleDatePicker;
    TimePicker simpleTimePicker;
    Button btn_submit;

    String str_faculty_id, str_faculty_name, str_app_type, str_app_occurance, str_days, str_app_time, str_app_date;
    RequestParams params;

    String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentrequestappointmentsscreen);

        tv_heading = (TextView) findViewById(R.id.tv_heading);
        radiogroup_appointment_type = (RadioGroup) findViewById(R.id.radiogroup_appointment_type);
        radiogroup_appointment_occurance = (RadioGroup) findViewById(R.id.radiogroup_appointment_occurance);
        radiobutton_general = (RadioButton) findViewById(R.id.radiobutton_general);
        radiobutton_important = (RadioButton) findViewById(R.id.radiobutton_important);
        radiobutton_once = (RadioButton) findViewById(R.id.radiobutton_once);
        radiobutton_weekly = (RadioButton) findViewById(R.id.radiobutton_weekly);
        sp_days = (Spinner)findViewById(R.id.sp_days);
        simpleTimePicker = (TimePicker)findViewById(R.id.simpleTimePicker);
        simpleDatePicker = (DatePicker) findViewById(R.id.simpleDatePicker);
        btn_submit = (Button)findViewById(R.id.btn_submit);


        str_faculty_id = getIntent().getStringExtra("FacultyID");
        str_faculty_name = getIntent().getStringExtra("FacultyName");

        tv_heading.setText("Faculty Name: " + str_faculty_name.toUpperCase());

        str_app_type = "General";
        str_app_occurance = "Once";

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_days.setAdapter(adapter);

        simpleTimePicker.setIs24HourView(true);
        simpleDatePicker.setSpinnersShown(false);

        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();

        int int_month = today.month + 1;

        str_app_date = "" + today.monthDay + "/" + int_month + "/" + today.year;
        str_app_time = today.format("%k:%M");



        simpleTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // display a toast with changed values of time picker
         //       Toast.makeText(getApplicationContext(), hourOfDay + "  " + minute, Toast.LENGTH_SHORT).show();

                String hour = String.valueOf(hourOfDay);
                if(hour.length()==1)
                    hour = "0" + hour;

                String minutes = String.valueOf(minute);
                if(minutes.length()==1)
                    minutes = "0" + minutes;

                str_app_time = hour + ":" + minutes;

            }
        });

        sp_days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                str_days = parent.getItemAtPosition(position).toString().trim();

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

                str_days = "MONDAY";

            }
        });

        radiogroup_appointment_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                //		 Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_LONG).show();
                int selectedId = radiogroup_appointment_type.getCheckedRadioButtonId();

                if (selectedId == R.id.radiobutton_general) {
                    str_app_type = "General";
                } else if (selectedId == R.id.radiobutton_important) {
                    str_app_type = "Important";

                }

            }
        });


        radiogroup_appointment_occurance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub

                //		 Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_LONG).show();
                int selectedId = radiogroup_appointment_occurance.getCheckedRadioButtonId();

                if (selectedId == R.id.radiobutton_once) {
                    str_app_occurance = "Once";
                    simpleTimePicker.setVisibility(View.VISIBLE);
                    simpleDatePicker.setVisibility(View.VISIBLE);
                    sp_days.setVisibility(View.GONE);
                } else if (selectedId == R.id.radiobutton_weekly) {
                    str_app_occurance = "Weekly";
                    sp_days.setVisibility(View.VISIBLE);
                    simpleTimePicker.setVisibility(View.VISIBLE);
                    simpleDatePicker.setVisibility(View.GONE);

                }

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String day = "" + simpleDatePicker.getDayOfMonth();
                String month = "" + (simpleDatePicker.getMonth() + 1);
                String year = "" + simpleDatePicker.getYear();

                str_app_date = day + "/" + month + "/" + year;

                CallingWebservice callWebservice = new CallingWebservice(
                        getApplicationContext());

                params = new RequestParams();

                params.put("database", "vmeetdb");
                params.put("tablename", "studentappointmentdetails");
                params.put("columnnames",
                        "studentusn, facultyid, type1, type2, dates, times");

                if(str_app_occurance.equalsIgnoreCase("Once")) {
                    params.put("columnvalues", "'"
                            + StudentsMainScreenActivity.str_usn.trim() + "','"
                            + str_faculty_id.trim() + "','"
                            + str_app_type.trim() + "','"
                            + str_app_occurance.trim() + "','"
                            + str_app_date.trim() + "','"
                            + str_app_time.trim() + "'");

                }

                if(str_app_occurance.equalsIgnoreCase("Weekly")) {
                    params.put("columnvalues", "'"
                            + StudentsMainScreenActivity.str_usn.trim() + "','"
                            + str_faculty_id.trim() + "','"
                            + str_app_type.trim() + "','"
                            + str_app_occurance.trim() + "','"
                            + str_days.trim() + "','"
                            + str_app_time.trim() + "'");

                }



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


                                        Toast.makeText(getApplicationContext(), "Appoitment Request submitted successfully", Toast.LENGTH_SHORT).show();

                                        finish();

                                        Intent studentMainscreen = new Intent(getApplicationContext(), StudentsMainScreenActivity.class);
                                        studentMainscreen.putExtra("UserName", StudentsMainScreenActivity.str_UserName);
                                        studentMainscreen.putExtra("Id", StudentsMainScreenActivity.str_id);
                                        studentMainscreen.putExtra("USN", StudentsMainScreenActivity.str_usn);
                                        studentMainscreen.putExtra("PhoneNo", StudentsMainScreenActivity.str_MobileNo);
                                        studentMainscreen.putExtra("EmailID", StudentsMainScreenActivity.str_EmailId);
                                        studentMainscreen.putExtra("Semester", StudentsMainScreenActivity.str_semester);
                                        studentMainscreen.putExtra("Branch", StudentsMainScreenActivity.str_branch);
                                        startActivity(studentMainscreen);


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
        });
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub

        super.onBackPressed();
        finish();

        Intent studentMainscreen = new Intent(getApplicationContext(), StudentsMainScreenActivity.class);
        studentMainscreen.putExtra("UserName", StudentsMainScreenActivity.str_UserName);
        studentMainscreen.putExtra("Id", StudentsMainScreenActivity.str_id);
        studentMainscreen.putExtra("USN", StudentsMainScreenActivity.str_usn);
        studentMainscreen.putExtra("PhoneNo", StudentsMainScreenActivity.str_MobileNo);
        studentMainscreen.putExtra("EmailID", StudentsMainScreenActivity.str_EmailId);
        studentMainscreen.putExtra("Semester", StudentsMainScreenActivity.str_semester);
        studentMainscreen.putExtra("Branch", StudentsMainScreenActivity.str_branch);
        startActivity(studentMainscreen);



    }
}
