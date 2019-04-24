package pragma.embd.vmeetandroidapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

public class WelcomeScreenActivity extends Activity {


    Button btn_student, btn_admin, btn_faculty;

    private static final int MY_PERMISSIONS_REQUEST_NETWORK_PROVIDER =1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        btn_student = (Button) findViewById(R.id.btn_student);
        btn_admin = (Button) findViewById(R.id.btn_admin);
        btn_faculty = (Button) findViewById(R.id.btn_faculty);


        requestForPermissions();

        btn_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent login_screen = new Intent(getApplicationContext(),StudentLoginScreenActivity.class);
                startActivity(login_screen);



            }
        });

        btn_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent login_screen = new Intent(getApplicationContext(),AdminLoginScreenActivity.class);
                startActivity(login_screen);



            }
        });

        btn_faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent login_screen = new Intent(getApplicationContext(),FacultyLoginScreenActivity.class);
                startActivity(login_screen);

            }
        });



    }

    void requestForPermissions() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

                //       Toast.makeText(getApplicationContext(), "In first If", Toast.LENGTH_LONG).show();
            } else {
                // permission is already granted
                //       Toast.makeText(getApplicationContext(), "In Else", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_NETWORK_PROVIDER);


            }


        }
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        super.onBackPressed();
        finish();
    }
}
