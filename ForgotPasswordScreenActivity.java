package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ForgotPasswordScreenActivity extends Activity {

    EditText et_username;
    Button btn_get_password, btn_login;

   RequestParams params;

  String str_usertype, str_password;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_screen);

        et_username = (EditText) findViewById(R.id.et_username);
        btn_get_password = (Button) findViewById(R.id.btn_get_password);
        btn_login = (Button) findViewById(R.id.btn_login);

        str_usertype = getIntent().getStringExtra("UserType");


        btn_get_password.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        if(et_username.getText().toString().toString().trim().equals("")||
                                et_username.getText().toString().trim().length()==0 ){

                                Toast.makeText(getApplicationContext(),"Please Enter User Name", Toast.LENGTH_LONG).show();
                        }
                        else{

                                getPassword();
                        }
                }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {

                if(str_usertype.equalsIgnoreCase("Student")) {
                        Intent i = new Intent(getApplicationContext(),
                                StudentLoginScreenActivity.class);
                        i.putExtra("UserType", "Driver");
                        startActivity(i);
                }
                else if(str_usertype.equalsIgnoreCase("Faculty")) {
                        Intent i = new Intent(getApplicationContext(),
                                FacultyLoginScreenActivity.class);
                        i.putExtra("UserType", "Driver");
                        startActivity(i);
                }
        }
        });



        }

        void getPassword(){


        CallingWebservice callWebservice = new CallingWebservice(
        getApplicationContext());
        params = new RequestParams();

        // Put Http parameter
        params.put("database", "vmeetdb");
        if(str_usertype.equalsIgnoreCase("Student"))
        params.put("tablename", "studentdetails");
        else if(str_usertype.equalsIgnoreCase("Faculty"))
                        params.put("tablename", "facultydetails");

        params.put("columns", "Id, pwd");
        params.put("conditions", "username='"
        + et_username.getText().toString().trim() + "'");

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
        "Operation unsuccessful, Invalid Username",
        Toast.LENGTH_LONG)
        .show();

        }
        // Else display error message
        else {
        // result array
        Toast.makeText(
        getApplicationContext(),
        "Valid Username",
        Toast.LENGTH_SHORT)
        .show();
        JSONArray result = null;
        result = (JSONArray) obj
        .get(String
        .valueOf("0"));
        // send the id ,user name to
        // next screen
                str_password = result.getString(1);
                alert_box();

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

                        builder.setTitle("Confidential");
                        builder.setIcon(R.drawable.infoicon);

                        builder.setMessage("Your Password is:: " + str_password);

                        //Message here
                        // Set an EditText view to get user input


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
                if(str_usertype.equalsIgnoreCase("Student")) {
                        Intent i = new Intent(getApplicationContext(),
                                StudentLoginScreenActivity.class);
                        i.putExtra("UserType", "Driver");
                        startActivity(i);
                }
                else if(str_usertype.equalsIgnoreCase("Faculty")) {
                        Intent i = new Intent(getApplicationContext(),
                                FacultyLoginScreenActivity.class);
                        i.putExtra("UserType", "Driver");
                        startActivity(i);
                }
        }
}

