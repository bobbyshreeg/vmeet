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
import static pragma.embd.vmeetandroidapp.Constants.THIRD_COLUMN;

public class ListviewAdapterStudentSchedulerList extends BaseAdapter {
    public ArrayList<HashMap> list;
    Activity activity;
    HashMap map;
    static String temp = ":";
    RequestParams params;

    public ListviewAdapterStudentSchedulerList(Activity activity, ArrayList<HashMap> list) {

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

        TextView tv_id, tv_faculty_name, tv_subject_name, tv_days, tv_times;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {

            // TODO Auto-generated method stub
            View row = convertView;
            LinearLayout listLayout = new LinearLayout(activity);
            ViewGroup.LayoutParams lpView = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            listLayout.setOrientation(LinearLayout.HORIZONTAL);
            listLayout.setId(5000);
            final ListviewAdapterStudentSchedulerList.ViewHolder holder;
            LayoutInflater inflater = activity.getLayoutInflater();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.listviewstudentscheduler_list, parent, false);
                holder = new ListviewAdapterStudentSchedulerList.ViewHolder();
                holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
                holder.tv_faculty_name = (TextView) convertView.findViewById(R.id.tv_faculty_name);
                holder.tv_subject_name = (TextView) convertView.findViewById(R.id.tv_subject_name);
                holder.tv_days = (TextView) convertView.findViewById(R.id.tv_days);
                holder.tv_times = (TextView) convertView.findViewById(R.id.tv_times);

                convertView.setTag(holder);
            } else {
                holder = (ListviewAdapterStudentSchedulerList.ViewHolder) convertView.getTag();
            }


            map = list.get(position);
            holder.tv_id.setText((String) map.get(FIRST_COLUMN));
            holder.tv_days.setText((String) map.get(THIRD_COLUMN));
            holder.tv_faculty_name.setText("Semester: " + (String) map.get(SECOND_COLUMN));
            holder.tv_subject_name.setText("Subject: " + (String) map.get(FOURTH_COLUMN));
            holder.tv_times.setText((String) map.get(FIFTH_COLUMN));


        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), "error here " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }


}