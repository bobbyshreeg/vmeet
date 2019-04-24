package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminMainScreenActivity extends Activity{


    ImageView img_students_profile, img_faculty_profile, img_view_appointments, img_logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminmain_screen);


        img_faculty_profile = (ImageView) findViewById(R.id.img_faculty_profile);
        img_students_profile = (ImageView) findViewById(R.id.img_students_profile);
        img_view_appointments = (ImageView) findViewById(R.id.img_view_appointments);
        img_logout = (ImageView) findViewById(R.id.img_logout);



        img_students_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent admincheckProfile = new Intent(getApplicationContext(), AdminCheckStudentFacultyProfileScreenActivity.class);
                admincheckProfile.putExtra("Type", "Student");
                startActivity(admincheckProfile);
            }
        });

        img_faculty_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent admincheckProfile = new Intent(getApplicationContext(), AdminCheckStudentFacultyProfileScreenActivity.class);
                admincheckProfile.putExtra("Type", "Faculty");
                startActivity(admincheckProfile);
            }
        });

        img_view_appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent viewAppointmentsList = new Intent(getApplicationContext(), AdminViewAppointmentListScreenActivity.class);
                startActivity(viewAppointmentsList);

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
    public void onBackPressed() {
        // TODO Auto-generated method stub

        super.onBackPressed();
        finish();
        Intent goBackScreen = new Intent(getApplicationContext(), AdminLoginScreenActivity.class);
        startActivity(goBackScreen);
    }
}
