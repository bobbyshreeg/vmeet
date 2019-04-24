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

import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;

import static pragma.embd.vmeetandroidapp.Constants.FIFTH_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.FIRST_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.FOURTH_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.SECOND_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.SIXTH_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.THIRD_COLUMN;

public class ListviewAdapterFacultyAppointmentsList extends BaseAdapter
{
    public ArrayList<HashMap> list;
    Activity activity;
    HashMap map;
    static String str_faculty_empid, str_faculty_name;
    RequestParams params;

    public ListviewAdapterFacultyAppointmentsList(Activity activity, ArrayList<HashMap> list) {

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

        TextView tv_faculty_id, tv_faculty_name, tv_faculty_empid, tv_faculty_emailid, tv_faculty_phoneno, tv_faculty_availability;
        Button btn_request_appointment;
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
            final ListviewAdapterFacultyAppointmentsList.ViewHolder holder;
            LayoutInflater inflater =  activity.getLayoutInflater();

            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.listviewfacultyappointments_list,parent,false);
                holder = new ListviewAdapterFacultyAppointmentsList.ViewHolder();
                holder.tv_faculty_id = (TextView) convertView.findViewById(R.id.tv_faculty_id);
                holder.tv_faculty_name = (TextView) convertView.findViewById(R.id.tv_faculty_name);
                holder.tv_faculty_empid = (TextView) convertView.findViewById(R.id.tv_faculty_empid);
                holder.tv_faculty_emailid = (TextView) convertView.findViewById(R.id.tv_faculty_emailid);
                holder.tv_faculty_phoneno = (TextView) convertView.findViewById(R.id.tv_faculty_phoneno);
                holder.tv_faculty_availability = (TextView) convertView.findViewById(R.id.tv_faculty_availability);
                holder.btn_request_appointment = (Button) convertView.findViewById(R.id.btn_request_appointment);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ListviewAdapterFacultyAppointmentsList.ViewHolder) convertView.getTag();
            }



            map = list.get(position);
            holder.tv_faculty_id.setText((String)map.get(FIRST_COLUMN));
            holder.tv_faculty_name.setText("Name: " + (String)map.get(SECOND_COLUMN));
            str_faculty_name = (String)map.get(SECOND_COLUMN);
            holder.tv_faculty_empid.setText("Emp ID: " + (String)map.get(THIRD_COLUMN));
            str_faculty_empid = (String)map.get(THIRD_COLUMN);
            holder.tv_faculty_emailid.setText("Email :" + (String)map.get(FOURTH_COLUMN));
            holder.tv_faculty_phoneno.setText("Contact: " + (String)map.get(FIFTH_COLUMN));
            holder.tv_faculty_availability.setText("Availability: " + (String)map.get(SIXTH_COLUMN));



            holder.btn_request_appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    activity.finish();
                    Intent requestAppointment = new Intent(activity.getApplicationContext(), StudentRequestAppointmentsScreenActivity.class);
                    requestAppointment.putExtra("FacultyID", str_faculty_empid.trim());
                    requestAppointment.putExtra("FacultyName", str_faculty_name.trim());
                    activity.startActivity(requestAppointment);
                }
            });





        }
        catch(Exception e){
            Toast.makeText(activity.getApplicationContext(), "error here "+ e.getMessage(),  Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }





}
