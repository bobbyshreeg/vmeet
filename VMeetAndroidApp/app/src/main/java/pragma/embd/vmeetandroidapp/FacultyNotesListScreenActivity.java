package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class FacultyNotesListScreenActivity extends Activity {


    Button btn_add_notes;
    ListView listView1;

    public Activity act;
    ListAdapter adapter;

    RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facultynoteslistscreen);

        btn_add_notes = (Button) findViewById(R.id.btn_add_notes);
        listView1 = (ListView) findViewById(R.id.listView1);

        act = this;
        getFacultyNotes();

        btn_add_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent notesScreen = new Intent(getApplicationContext(), FacultyAddNotesScreenActivity.class);
                startActivity(notesScreen);
            }
        });
    }

    void getFacultyNotes(){

        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());
        params = new RequestParams();

        // Put Http parameter
        params.put("database", "vmeetdb");
        params.put("tablename", "facultynotesdetails");
        params.put("columns", "Id, semester, subjectname, notestitle, notesdetails");
        params.put("conditions", "facultyid = '" + FacultyMainScreenActivity.str_empid.trim() + "' order by Id");

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

                                    for (int m = 0; m <= result.length() - 1; m += 5) {
                                        bean.value1 = result.getString(m);
                                    }

                                    for (int n = 1; n <= result.length() - 1; n += 5) {
                                        bean.value2 = result.getString(n);
                                    }

                                    for (int o = 2; o <= result.length() - 1; o += 5) {
                                        bean.value3 = result.getString(o);
                                    }

                                    for (int p = 3; p <= result.length() - 1; p += 5) {
                                        bean.value4 = result.getString(p);
                                    }

                                    for (int q = 4; q <= result.length() - 1; q += 5) {
                                        bean.value5 = result.getString(q);
                                    }




                                    empBean.add(new BeanDetails(bean.value1, bean.value2, bean.value3, bean.value4, bean.value5));
                                }

                                if (empBean.size() > 0) {
                                    for (int i = 0; i < empBean.size(); i++) {

                                        BeanDetails data = empBean.get(i);

                                        HashMap hashMap = new HashMap();

                                        hashMap.put(Constants.FIRST_COLUMN,
                                                data.value1);
                                        hashMap.put(Constants.SECOND_COLUMN,
                                                data.value2);
                                        hashMap.put(Constants.THIRD_COLUMN,
                                                data.value3);
                                        hashMap.put(Constants.FOURTH_COLUMN,
                                                data.value4);
                                        hashMap.put(Constants.FIFTH_COLUMN,
                                                data.value5);
                                        list.add(hashMap);
                                    }

                                    adapter = new ListviewAdapterFacultyNotesList(
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
