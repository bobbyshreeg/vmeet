package pragma.embd.vmeetandroidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ListviewAdapterFacultyNotesList extends BaseAdapter
{
    public ArrayList<HashMap> list;
    Activity activity;
    HashMap map;
    static String temp=":";
    RequestParams params;

    public ListviewAdapterFacultyNotesList(Activity activity, ArrayList<HashMap> list) {

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

        TextView tv_notes_id, tv_semester, tv_subject_name, tv_notes_title, tv_notes_details;
        Button btn_view_notes, btn_delete;
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
            final ListviewAdapterFacultyNotesList.ViewHolder holder;
            LayoutInflater inflater =  activity.getLayoutInflater();

            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.listviewfacultynotes_list,parent,false);
                holder = new ListviewAdapterFacultyNotesList.ViewHolder();
                holder.tv_notes_id = (TextView) convertView.findViewById(R.id.tv_notes_id);
                holder.tv_semester = (TextView) convertView.findViewById(R.id.tv_semester);
                holder.tv_subject_name = (TextView) convertView.findViewById(R.id.tv_subject_name);
                holder.tv_notes_title = (TextView) convertView.findViewById(R.id.tv_notes_title);
                holder.tv_notes_details = (TextView) convertView.findViewById(R.id.tv_notes_details);
                holder.btn_view_notes = (Button) convertView.findViewById(R.id.btn_view_notes);
                holder.btn_delete = (Button) convertView.findViewById(R.id.btn_delete);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ListviewAdapterFacultyNotesList.ViewHolder) convertView.getTag();
            }



            map = list.get(position);
            holder.tv_notes_id.setText((String)map.get(FIRST_COLUMN));
            holder.tv_semester.setText((String)map.get(SECOND_COLUMN));
            holder.tv_subject_name.setText((String)map.get(THIRD_COLUMN));
            holder.tv_notes_title.setText((String)map.get(FOURTH_COLUMN));
            holder.tv_notes_details.setText((String)map.get(FIFTH_COLUMN));



            holder.btn_view_notes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alert_box(holder.tv_notes_title.getText().toString().trim(), holder.tv_notes_details.getText().toString().trim());
                }
            });


            holder.btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deleteFacultyNotes(holder.tv_notes_id.getText().toString().trim());
                }
            });


        }
        catch(Exception e){
            Toast.makeText(activity.getApplicationContext(), "error here "+ e.getMessage(),  Toast.LENGTH_SHORT).show();
        }

        return convertView;
    }


    void deleteFacultyNotes(String notesID){
        CallingWebservice callWebservice = new CallingWebservice(
                activity.getApplicationContext());

        params = new RequestParams();

        params.put("database", "vmeetdb");
        params.put("tablename", "facultynotesdetails");
        params.put("conditions", "Id = '" + notesID.trim() + "'");

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
                                        "Faculty Notes Deleted Successfully",
                                        Toast.LENGTH_LONG).show();


                                activity.finish();

                                Intent facultyMainscreen = new Intent(activity.getApplicationContext(), FacultyMainScreenActivity.class);
                                facultyMainscreen.putExtra("UserName", FacultyMainScreenActivity.str_UserName);
                                facultyMainscreen.putExtra("EMPID", FacultyMainScreenActivity.str_empid);
                                facultyMainscreen.putExtra("Id", FacultyMainScreenActivity.str_id);
                                facultyMainscreen.putExtra("Branch", FacultyMainScreenActivity.str_branch);
                                facultyMainscreen.putExtra("EmailID", FacultyMainScreenActivity.str_EmailId);
                                facultyMainscreen.putExtra("PhoneNo", FacultyMainScreenActivity.str_MobileNo);
                                activity.startActivity(facultyMainscreen);





                            }
                            // Else display error message
                            else if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "deletionFailed")) {

                                Toast.makeText(
                                        activity.getApplicationContext(),
                                        "Deleting Faculty Schedule Failed: "
                                                + obj.getString("error_msg"),
                                        Toast.LENGTH_LONG).show();
                            } else if (obj.getString("response")
                                    .equalsIgnoreCase(
                                            "connectionToDBFailed")) {

                                Toast.makeText(
                                        activity.getApplicationContext(),
                                        "Deleting Faculty Schedule Failed: "
                                                + obj.getString("error_msg"),
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {

                            Toast.makeText(
                                    activity.getApplicationContext(),
                                    "Deleting Faculty Schedule Failed: " + e.getMessage(),
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

    public void alert_box(String notesTitle, String notesDetails){

        try{

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

            builder.setTitle(notesTitle.toUpperCase());
            builder.setIcon(R.drawable.infoicon);

            builder.setMessage(notesDetails);


            builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        catch(Exception e){

            Toast.makeText(activity.getApplicationContext(), "error is: " + e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }
}
