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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static pragma.embd.vmeetandroidapp.Constants.FIRST_COLUMN;

public class FacultyAddNotesScreenActivity extends Activity {

    Spinner sp_semester, sp_subject;
    EditText et_notes_title, et_notes_details;
    Button btn_save;

    String[] semester = {"1", "2", "3", "4", "5", "6", "7", "8"};
    String str_semester, str_subject;

    RequestParams params;

    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facultyaddnotesscreen);

        sp_semester = (Spinner) findViewById(R.id.sp_semester);
        sp_subject = (Spinner) findViewById(R.id.sp_subject);
        et_notes_title = (EditText) findViewById(R.id.et_notes_title);
        et_notes_details = (EditText) findViewById(R.id.et_notes_details);
        btn_save = (Button) findViewById(R.id.btn_save);


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, semester);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_semester.setAdapter(adapter);

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

                //    str_subject = "1";



            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_notes_title.getText().toString().toString().trim().equals("")||
                        et_notes_title.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Notes Title", Toast.LENGTH_LONG).show();
                }
                else  if(et_notes_details.getText().toString().toString().trim().equals("")||
                        et_notes_details.getText().toString().trim().length()==0 ){

                    Toast.makeText(getApplicationContext(),"Please Enter Notes Details", Toast.LENGTH_LONG).show();
                }
                else {
                    addNotesForFaculty();
                }
            }
        });

    }

    void addNotesForFaculty(){


        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());

        params = new RequestParams();

        params.put("database", "vmeetdb");
        params.put("tablename", "facultynotesdetails");
        params.put("columnnames",
                "facultyid, semester, branchname, subjectname, notestitle, notesdetails");
        params.put("columnvalues", "'"
                + FacultyMainScreenActivity.str_empid.trim() + "','"
                + str_semester.trim() + "','"
                + FacultyMainScreenActivity.str_branch.trim() + "','"
                + str_subject.trim() + "','"
                + et_notes_title.getText().toString().trim() + "','"
                + et_notes_details.getText().toString().trim() + "'");



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
                                        "Notes Added successfully!",
                                        Toast.LENGTH_LONG).show();

                                finish();
                                Intent listNotesScreen = new Intent(getApplicationContext(), FacultyNotesListScreenActivity.class);
                                startActivity(listNotesScreen);

                            }
                            // Else display error message
                            else if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "insertionFailed")) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Adding Scheduler Failed: "
                                                + obj.getString("error_msg"),
                                        Toast.LENGTH_LONG).show();
                            } else if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "connectionToDBFailed")) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Adding Scheduler Failed: "
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

    void getSubjectsForSemester(String str_semester){

        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());
        params = new RequestParams();

        // Put Http parameter
        params.put("database", "vmeetdb");
        params.put("tablename", "branchsubjectdetails");
        params.put("columns", "distinct subjectname");
        params.put("conditions", "branchname = '" + FacultyMainScreenActivity.str_branch + "' AND semester='" + str_semester.trim() + "'");


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
