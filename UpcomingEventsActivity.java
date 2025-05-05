package edu.ewubd.cse489120251;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UpcomingEventsActivity extends AppCompatActivity {
    private ArrayList<Event> eventsList = new ArrayList<>();
    private ListView listViewEvents;
    private CustomEventAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);
        listViewEvents = findViewById(R.id.listViewEvents);
        adapter = new CustomEventAdapter(this, eventsList);
        listViewEvents.setAdapter(adapter);

        Button btnExit = findViewById(R.id.btnExit);
        Button btnAddNew = findViewById(R.id.btnAddNew);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btnAddNew tapped");
                // Move to UpcomingEventsActivity
                Intent i = new Intent(UpcomingEventsActivity.this, NewEventActivity.class);
                startActivity(i);
            }
        });
        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = eventsList.get(position);
                Intent i = new Intent(UpcomingEventsActivity.this, NewEventActivity.class);
                i.putExtra("eventId", e.eventId);
                i.putExtra("title", e.title);
                i.putExtra("venue", e.venue);
                i.putExtra("datetime", e.datetime);
                i.putExtra("numParticipants", e.numParticipants);
                i.putExtra("description", e.description);
                startActivity(i);
            }
        });

        listViewEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Event e = eventsList.get(position);
                showConsentDialog(e);
                return true;
            }
        });
    }
    private void showConsentDialog(Event e){
        new AlertDialog.Builder(this)
                .setTitle("Consent Required")
                .setMessage("Do you agree to remove "+e.title+"?")
                .setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete from database
                        EventDB db = new EventDB(UpcomingEventsActivity.this);
                        db.deleteEvent(e.eventId);
                        db.close();
                        deleteDataFromRemoteDB(e.eventId);
                        eventsList.remove(e);   // delete from the memory
                        adapter.notifyDataSetChanged(); // redraw the list view
                        Toast.makeText(getApplicationContext(), "Event has been deleted successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }


    @Override
    public void onStart(){
        super.onStart();
        this.loadDataFromSQLite();
    }
    private void loadDataFromSQLite(){
        eventsList.clear();
        EventDB db = new EventDB(this);
        Cursor cur = db.selectEvents("SELECT * FROM events ORDER BY datetime DESC");
        if(cur != null){
            while(cur.moveToNext()){
                String eventId = cur.getString(0);
                String title = cur.getString(1);
                String venue = cur.getString(2);
                long datetime = cur.getLong(3);
                int numParticipants = cur.getInt(4);
                String description = cur.getString(5);
                Event e = new Event(eventId, title, venue, datetime, numParticipants, description);
                System.out.println(e);
                eventsList.add(e);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void loadDataFromRemoteServer(){
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("sid", "2021-3-60-000"));
            params.add(new BasicNameValuePair("semester", "2025-1"));
            params.add(new BasicNameValuePair("action", "restore"));
            //Handler h = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String dataFromServer = RemoteAccess.getInstance().makeHttpRequest(params);
                    if(dataFromServer != null){
                        try {
                            JSONObject json = new JSONObject(dataFromServer);
                            if(json.has("msg") && json.has("key-value")){
                                String msg = json.getString("msg");
                                if(msg != null && msg.equals("OK")){
                                    JSONArray rows = json.getJSONArray("key-value");

                                    // write code to store data in SQLite
                                    EventDB db = new EventDB(UpcomingEventsActivity.this);

                                    for(int i=0; i<rows.length(); i++){
                                        String eventID = rows.getJSONObject(i).getString("key");
                                        String value = rows.getJSONObject(i).getString("value");
                                        JSONObject eventJson = new JSONObject(value);
                                        String  title = eventJson.getString("title");
                                        String  venue = eventJson.getString("venue");
                                        long dateTime = eventJson.getLong("dateTime");
                                        int  numParticipants = eventJson.getInt("numParticipants");
                                        String  description = eventJson.getString("description");
                                        if(doesEventExist(eventID)){
                                            db.updateEvent(eventID, title, venue, dateTime, numParticipants, description);
                                        } else{
                                            db.insertEvent(eventID, title, venue, dateTime, numParticipants, description);
                                        }
                                    }
                                    db.close();
                                    loadDataFromSQLite();
                                }
//                                h.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(UpcomingEventsActivity.this, msg, Toast.LENGTH_LONG).show();
//                                    }
//                                });
                                return;
                            }
                        }catch (Exception e){}
                    }
                    System.out.println("Something went wrong");
                }
            }).start();
        }catch (Exception e){}
    }

    private boolean doesEventExist(String eventId){
        for (Event e: eventsList) {
            if(e.eventId.equals(eventId)){
                return true;
            }
        }
        return false;
    }

    private void deleteDataFromRemoteDB(String eventID){
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("sid", "2021-3-60-000"));
            params.add(new BasicNameValuePair("semester", "2025-1"));
            params.add(new BasicNameValuePair("key", eventID));
            params.add(new BasicNameValuePair("action", "remove"));

            Handler h = new Handler();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String dataFromServer = RemoteAccess.getInstance().makeHttpRequest(params);
                    if(dataFromServer != null){
                        try {
                            JSONObject json = new JSONObject(dataFromServer);
                            if(json.has("msg")){
                                String msg = json.getString("msg");
                                h.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(UpcomingEventsActivity.this, msg, Toast.LENGTH_LONG).show();
                                    }
                                });
                                return;
                            }
                        }catch (Exception e){}
                    }
                    System.out.println("Something went wrong");
                }
            }).start();
        }catch (Exception e){}
    }

}