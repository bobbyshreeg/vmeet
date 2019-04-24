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

public class ListviewAdapterStudentsNotesList extends BaseAdapter
{
    public ArrayList<HashMap> list;
    Activity activity;
    HashMap map;
    static String temp=":";
    RequestParams params;

    public ListviewAdapterStudentsNotesList(Activity activity, ArrayList<HashMap> list) {

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

        TextView tv_notes_id, tv_subject_name, tv_notes_title, tv_notes_details;
        Button btn_view_notes;
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
            final ListviewAdapterStudentsNotesList.ViewHolder holder;
            LayoutInflater inflater =  activity.getLayoutInflater();

            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.listviewstudentsnotes_list,parent,false);
                holder = new ListviewAdapterStudentsNotesList.ViewHolder();
                holder.tv_notes_id = (TextView) convertView.findViewById(R.id.tv_notes_id);
                holder.tv_subject_name = (TextView) convertView.findViewById(R.id.tv_subject_name);
                holder.tv_notes_title = (TextView) convertView.findViewById(R.id.tv_notes_title);
                holder.tv_notes_details = (TextView) convertView.findViewById(R.id.tv_notes_details);
                holder.btn_view_notes = (Button) convertView.findViewById(R.id.btn_view_notes);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ListviewAdapterStudentsNotesList.ViewHolder) convertView.getTag();
            }



            map = list.get(position);
            holder.tv_notes_id.setText((String)map.get(FIRST_COLUMN));
            holder.tv_subject_name.setText((String)map.get(SECOND_COLUMN));
            holder.tv_notes_title.setText((String)map.get(THIRD_COLUMN));
            holder.tv_notes_details.setText((String)map.get(FOURTH_COLUMN));



            holder.btn_view_notes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    alert_box(holder.tv_notes_title.getText().toString().trim(), holder.tv_notes_details.getText().toString().trim());
                }
            });



        }
        catch(Exception e){
            Toast.makeText(activity.getApplicationContext(), "error here "+ e.getMessage(),  Toast.LENGTH_SHORT).show();
        }

        return convertView;
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
