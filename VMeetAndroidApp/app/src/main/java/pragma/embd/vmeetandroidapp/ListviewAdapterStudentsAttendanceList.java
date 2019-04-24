package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import static pragma.embd.vmeetandroidapp.Constants.FIRST_COLUMN;
import static pragma.embd.vmeetandroidapp.Constants.SECOND_COLUMN;

public class ListviewAdapterStudentsAttendanceList extends BaseAdapter
{
    public ArrayList<HashMap> list;
    Activity activity;
    HashMap map;
    static String temp=":";

    public ListviewAdapterStudentsAttendanceList(Activity activity, ArrayList<HashMap> list) {

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

        TextView tv_student_name, tv_student_usn;
        CheckBox checkBox1;
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
            final ViewHolder holder;
            LayoutInflater inflater =  activity.getLayoutInflater();

            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.listviewstudentsattendance_list,parent,false);
                holder = new ViewHolder();
                holder.tv_student_name = (TextView) convertView.findViewById(R.id.tv_student_name);
                holder.tv_student_usn = (TextView) convertView.findViewById(R.id.tv_student_usn);
                holder.checkBox1 = (CheckBox) convertView.findViewById(R.id.checkBox1);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }



            map = list.get(position);
            holder.tv_student_name.setText((String)map.get(FIRST_COLUMN));
            holder.tv_student_usn.setText((String)map.get(SECOND_COLUMN));

         /*   Toast.makeText(activity.getApplicationContext(), "in if:" + holder.tv_student_usn.getText().toString(),
                    Toast.LENGTH_SHORT).show();
*/

            //    str_farmer_id=(String)map.get(FIRST_COLUMN);

            if(FacultyAddModifyStudentsAttendanceScreenActivity.str_type.equalsIgnoreCase("ModifyAttendance")) {

                if (FacultyAddModifyStudentsAttendanceScreenActivity.str_students_usn.contains(holder.tv_student_usn.
                getText().toString().trim())){
                    temp=temp + holder.tv_student_usn.getText().toString()+ ":";
                   /* Toast.makeText(activity.getApplicationContext(), "in if:" + holder.tv_student_usn.getText().toString(),
                            Toast.LENGTH_SHORT).show();*/
                    holder.checkBox1.setChecked(true);


                    /*Toast.makeText(activity.getApplicationContext(), "temp is: " + temp,
                            Toast.LENGTH_SHORT).show();*/
                }
                else{
                    /*Toast.makeText(activity.getApplicationContext(), "in else:" + temp,  Toast.LENGTH_SHORT).show();
                    holder.checkBox1.setChecked(false);
                    temp = temp.replace(holder.tv_student_usn.getText().toString() + ":", "");*/

                }
            //    Toast.makeText(activity.getApplicationContext(), temp,  Toast.LENGTH_SHORT).show();

            }


            holder.checkBox1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    try{

                        Toast.makeText(activity.getApplicationContext(), "on button click:" + temp,  Toast.LENGTH_SHORT).show();

                        if(holder.checkBox1.isChecked()){
                            //			 Toast.makeText(activity.getApplicationContext(), "checked",  Toast.LENGTH_SHORT).show();

                            temp=temp + holder.tv_student_usn.getText().toString()+ ":";
                            			Toast.makeText(activity.getApplicationContext(), temp,  Toast.LENGTH_SHORT).show();
                        }
                        else{



                            temp = temp.replace(holder.tv_student_usn.getText().toString() + ":", "");
                            		 Toast.makeText(activity.getApplicationContext(),temp,  Toast.LENGTH_SHORT).show();
                        }



                    }
                    catch(Exception e){
                        Toast.makeText(activity.getApplicationContext(), "error "+ e.getMessage(),  Toast.LENGTH_SHORT).show();
                    }

                }
            });



        }
        catch(Exception e){
            Toast.makeText(activity.getApplicationContext(), "error here "+ e.getMessage(),  Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }



}
