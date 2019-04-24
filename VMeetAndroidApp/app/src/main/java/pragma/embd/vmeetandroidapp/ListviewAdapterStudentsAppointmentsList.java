package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static pragma.embd.vmeetandroidapp.Constants.FIFTH_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.FIRST_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.FOURTH_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.SECOND_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.SEVENTH_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.SIXTH_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.THIRD_COLUMN;

public class ListviewAdapterStudentsAppointmentsList extends BaseAdapter
{
    public ArrayList<HashMap> list;
    Activity activity;
    HashMap map;
    static String temp=":";
    RequestParams params;

    public ListviewAdapterStudentsAppointmentsList(Activity activity, ArrayList<HashMap> list) {

        super();
        this.activity = activity;
        this.list = list;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public void toggleItem(int position) {
        HashMap map = list.get(position);

        this.notifyDataSetChanged();
    }
    private class ViewHolder {

        TextView tv_app_id, tv_faculty_name, tv_type1, tv_type2, tv_dates, tv_times, tv_status;
        Button btn_cancel_appointment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try{

            // TODO Auto-generated method stub
            View row = convertView;
            LinearLayout listLayout = new LinearLayout(activity);
            ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            listLayout.setOrientation(LinearLayout.HORIZONTAL);
            listLayout.setId(5000);
            final ListviewAdapterStudentsAppointmentsList.ViewHolder holder;
            LayoutInflater inflater =  activity.getLayoutInflater();

            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.listviewstudentsappointments_list,parent,false);
                holder = new ListviewAdapterStudentsAppointmentsList.ViewHolder();
                holder.tv_app_id = (TextView) convertView.findViewById(R.id.tv_app_id);
                holder.tv_faculty_name = (TextView) convertView.findViewById(R.id.tv_faculty_name);
                holder.tv_type1 = (TextView) convertView.findViewById(R.id.tv_type1);
                holder.tv_type2 = (TextView) convertView.findViewById(R.id.tv_type2);
                holder.tv_dates = (TextView) convertView.findViewById(R.id.tv_dates);
                holder.tv_times = (TextView) convertView.findViewById(R.id.tv_times);
                holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
                holder.btn_cancel_appointment = (Button) convertView.findViewById(R.id.btn_cancel_appointment);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ListviewAdapterStudentsAppointmentsList.ViewHolder) convertView.getTag();
            }



            map = list.get(position);
            holder.tv_app_id.setText((String)map.get(FIRST_COLUMN));
            holder.tv_faculty_name.setText("Name: " + (String)map.get(SECOND_COLUMN));
            holder.tv_type1.setText("Emp ID: " + (String)map.get(THIRD_COLUMN));
            holder.tv_type2.setText("Email :" + (String)map.get(FOURTH_COLUMN));
            holder.tv_dates.setText("Contact: " + (String)map.get(FIFTH_COLUMN));
            holder.tv_times.setText("Contact: " + (String)map.get(SIXTH_COLUMN));
            holder.tv_status.setText("Status: " + (String)map.get(SEVENTH_COLUMN));



            holder.btn_cancel_appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deleteAppointment(holder.tv_app_id.getText().toString().trim());
                }
            });





        }
        catch(Exception e){
            Toast.makeText(activity.getApplicationContext(), "error here "+ e.getMessage(),  Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }



void deleteAppointment(String appID){

    CallingWebservice callWebservice = new CallingWebservice(
            activity.getApplicationContext());

    params = new RequestParams();

    params.put("database", "vmeetdb");
    params.put("tablename", "studentappointmentdetails");
    params.put("conditions", "Id = '" + appID.trim() + "'");

    callWebservice.callWebservice(params, "delete",
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
                                        "deletionSuccess")) {


                            Toast.makeText(
                                    activity.getApplicationContext(),
                                    "Appointment Request deleted Successfully",
                                    Toast.LENGTH_LONG).show();

                            activity.finish();

                            Intent studentMainscreen = new Intent(activity.getApplicationContext(), StudentsMainScreenActivity.class);
                            studentMainscreen.putExtra("UserName", StudentsMainScreenActivity.str_UserName);
                            studentMainscreen.putExtra("Id", StudentsMainScreenActivity.str_id);
                            studentMainscreen.putExtra("USN", StudentsMainScreenActivity.str_usn);
                            studentMainscreen.putExtra("PhoneNo", StudentsMainScreenActivity.str_MobileNo);
                            studentMainscreen.putExtra("EmailID", StudentsMainScreenActivity.str_EmailId);
                            studentMainscreen.putExtra("Semester", StudentsMainScreenActivity.str_semester);
                            studentMainscreen.putExtra("Branch", StudentsMainScreenActivity.str_branch);
                            activity.startActivity(studentMainscreen);


                        }
                        // Else display error message
                        else if (obj.getString("response")
                                .equalsIgnoreCase(
                                        "deletionFailed")) {

                            Toast.makeText(
                                    activity.getApplicationContext(),
                                    "Updating Student CGPA Failed: "
                                            + obj.getString("error_msg"),
                                    Toast.LENGTH_LONG).show();
                        } else if (obj.getString("response")
                                .equalsIgnoreCase(
                                        "connectionToDBFailed")) {

                            Toast.makeText(
                                    activity.getApplicationContext(),
                                    "Updating Student CGPA Failed: "
                                            + obj.getString("error_msg"),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {

                        Toast.makeText(
                                activity.getApplicationContext(),
                                "Updating Students CGPA Failed: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();

                    }
                }

                // When the response returned by REST has Http
                // response code other than '200'
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {

                    Toast.makeText(activity.getApplicationContext(),
                            "failed", Toast.LENGTH_LONG)
                            .show();

                    // When Http response code is '404'
                    if (statusCode == 404) {
                        Toast.makeText(activity.getApplicationContext(),
                                "Requested resource not found",
                                Toast.LENGTH_LONG).show();
                    }
                    // When Http response code is '500'
                    else if (statusCode == 500) {
                        Toast.makeText(
                                activity.getApplicationContext(),
                                "Something went wrong at server end",
                                Toast.LENGTH_LONG).show();
                    }
                    // When Http response code other than 404,
                    // 500
                    else {
                        Toast.makeText(
                                activity.getApplicationContext(),
                                "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]",
                                Toast.LENGTH_LONG).show();
                    }
                }

            });



}

}
