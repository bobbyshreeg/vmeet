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

import static pragma.embd.vmeetandroidapp.Constants.EIGTH_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.FIFTH_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.FIRST_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.FOURTH_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.NINTH_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.SECOND_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.SEVENTH_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.SIXTH_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.TENTH_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.THIRD_COLUMN;

public class ListviewAdapterAdminViewAppointmentsList extends BaseAdapter
{
    public ArrayList<HashMap> list;
    Activity activity;
    HashMap map;
    static String temp=":";
    RequestParams params;

    public ListviewAdapterAdminViewAppointmentsList(Activity activity, ArrayList<HashMap> list) {

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

        TextView tv_app_id, tv_student_name, tv_student_usn, tv_type1, tv_type2, tv_dates, tv_times, tv_status, tv_faculty_name, tv_faculty_empid;

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
            final ListviewAdapterAdminViewAppointmentsList.ViewHolder holder;
            LayoutInflater inflater =  activity.getLayoutInflater();

            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.listviewadminviewappointments_list,parent,false);
                holder = new ListviewAdapterAdminViewAppointmentsList.ViewHolder();
                holder.tv_app_id = (TextView) convertView.findViewById(R.id.tv_app_id);
                holder.tv_student_name = (TextView) convertView.findViewById(R.id.tv_student_name);
                holder.tv_student_usn = (TextView) convertView.findViewById(R.id.tv_student_usn);
                holder.tv_type1 = (TextView) convertView.findViewById(R.id.tv_type1);
                holder.tv_type2 = (TextView) convertView.findViewById(R.id.tv_type2);
                holder.tv_dates = (TextView) convertView.findViewById(R.id.tv_dates);
                holder.tv_times = (TextView) convertView.findViewById(R.id.tv_times);
                holder.tv_faculty_name = (TextView) convertView.findViewById(R.id.tv_faculty_name);
                holder.tv_faculty_empid = (TextView) convertView.findViewById(R.id.tv_faculty_empid);
                holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);


                convertView.setTag(holder);
            }
            else
            {
                holder = (ListviewAdapterAdminViewAppointmentsList.ViewHolder) convertView.getTag();
            }



            map = list.get(position);
            holder.tv_app_id.setText((String)map.get(FIRST_COLUMN));
            holder.tv_student_name.setText("Student Name: " + (String)map.get(SECOND_COLUMN));
            holder.tv_student_usn.setText("USN: " + (String)map.get(THIRD_COLUMN));
            holder.tv_faculty_name.setText("Faculty Name: " + (String)map.get(FOURTH_COLUMN));
            holder.tv_faculty_empid.setText("EMP ID: " + (String)map.get(FIFTH_COLUMN));
            holder.tv_type1.setText("Type: " + (String)map.get(SIXTH_COLUMN));
            holder.tv_type2.setText("Occurance :" + (String)map.get(SEVENTH_COLUMN));
            holder.tv_dates.setText("Date: " + (String)map.get(EIGTH_COLUMN));
            holder.tv_times.setText("Time: " + (String)map.get(NINTH_COLUMN));
            holder.tv_status.setText("Status: " + (String)map.get(TENTH_COLUMN));









        }
        catch(Exception e){
            Toast.makeText(activity.getApplicationContext(), "error here "+ e.getMessage(),  Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }



}
