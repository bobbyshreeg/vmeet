package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class StudentsNotesListScreenActivity extends Activity {


    ListView listView1;

    public Activity act;
    ListAdapter adapter;

    RequestParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentsnoteslistscreen);

        listView1 = (ListView) findViewById(R.id.listView1);

        act = this;
        getNotes();


    }

    void getNotes(){

        CallingWebservice callWebservice = new CallingWebservice(
                getApplicationContext());
        params = new RequestParams();

        // Put Http parameter
        params.put("database", "vmeetdb");
        params.put("tablename", "facultynotesdetails");
        params.put("columns", "Id, subjectname, notestitle, notesdetails");
        params.put("conditions", "semester = '" + StudentsMainScreenActivity.str_semester.trim() + "' AND branchname = '" + StudentsMainScreenActivity.str_branch.trim() + "' order by Id");

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

                                    for (int m = 0; m <= result.length() - 1; m += 4) {
                                        bean.value1 = result.getString(m);
                                    }

                                    for (int n = 1; n <= result.length() - 1; n += 4) {
                                        bean.value2 = result.getString(n);
                                    }

                                    for (int o = 2; o <= result.length() - 1; o += 4) {
                                        bean.value3 = result.getString(o);
                                    }

                                    for (int p = 3; p <= result.length() - 1; p += 4) {
                                        bean.value4 = result.getString(p);
                                    }

                                    empBean.add(new BeanDetails(bean.value1, bean.value2, bean.value3, bean.value4));
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
                                        list.add(hashMap);
                                    }

                                    adapter = new ListviewAdapterStudentsNotesList(
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
