package edu.ewubd.cse489120251;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewEventActivity extends AppCompatActivity {
    private EditText etTitle, etVenue, etDateTime, etNumParticipants, etDescription;
    private RadioButton rdOnline, rdOffline;
    private String eventID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        etTitle = findViewById(R.id.etTitle);
        etVenue = findViewById(R.id.etVenue);
        etDateTime = findViewById(R.id.etDateTime);
        etNumParticipants = findViewById(R.id.etNumParticipants);
        etDescription = findViewById(R.id.etDescription);
        rdOnline = findViewById(R.id.rdOnline);
        rdOffline = findViewById(R.id.rdOffline);

        Intent i = this.getIntent();
        if(i!=null && i.hasExtra("eventId")){
            eventID = i.getStringExtra("eventId");
            String title  = i.getStringExtra("title");
            String venue  = i.getStringExtra("venue");
            long datetime  = i.getLongExtra("datetime", 0);
            int numParticipants  = i.getIntExtra("numParticipants", 0);
            String description  = i.getStringExtra("description");
            etTitle.setText(title);
            etVenue.setText(venue);
            try {
                etDateTime.setText(Util.getInstance().convertMillisecondsToDate(datetime, "dd-MM-yyyy HH:mm"));
            }catch (Exception e){
                e.printStackTrace();
            }
            etNumParticipants.setText(""+numParticipants);
            etDescription.setText(description);
        }
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnSave = findViewById(R.id.btnSave);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btnLogin tapped");
                String title = etTitle.getText().toString().trim();
                String venue = etVenue.getText().toString().trim();
                String dt = etDateTime.getText().toString().trim();
                String np = etNumParticipants.getText().toString().trim();
                int numParticipants = 0;
                try {
                    numParticipants = Integer.parseInt(np);
                }catch (Exception e){}
                String description = etDescription.getText().toString().trim();

                System.out.println("isOnline: "+rdOnline.isChecked());
                System.out.println("isOffline: "+rdOffline.isChecked());

                System.out.println("title: "+title);
                System.out.println("venue: "+venue);
                System.out.println("dt: "+dt);
                System.out.println("numParticipants: "+numParticipants);
                System.out.println("description: "+description);

                // check the data validity here
                long dateTime = 0;//System.currentTimeMillis();
                try{
                    dateTime = Util.getInstance().convertDateToMilliseconds(dt, "dd-MM-yyyy");
                }catch (Exception e){}

                if(title.length() < 8){
                    Toast.makeText(NewEventActivity.this, "Title must have 8 letters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(venue.length() < 8){
                    Toast.makeText(NewEventActivity.this, "Venue must have 8 letters", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check the data format here. take help from web
                    // hints: find code to covert a string-date to Date object
                    //          then, get the milliseconds value from the date object
                if(numParticipants <= 0){
                    Toast.makeText(NewEventActivity.this, "Invalid number of participants", Toast.LENGTH_SHORT).show();
                    return;
                }
                // write code to store data in SQLite
                EventDB db = new EventDB(NewEventActivity.this);
                if(eventID.isEmpty()){
                    eventID = title+System.nanoTime();
                    db.insertEvent(eventID, title, venue, dateTime, numParticipants, description);
                } else {
                    db.updateEvent(eventID, title, venue, dateTime, numParticipants, description);
                }
                db.close();

                storeDataToRemoteDB(eventID, title, venue, dateTime, numParticipants, description);
                finish();
            }
        });
    }
    private void storeDataToRemoteDB(String eventID,String title, String venue, long dateTime, int numParticipants, String description){
        try {
            JSONObject jo = new JSONObject();
            jo.put("title", title);
            jo.put("venue", venue);
            jo.put("dateTime", dateTime);
            jo.put("numParticipants", numParticipants);
            jo.put("description", description);
            String value = jo.toString();

            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("sid", "2021-3-60-000"));
            params.add(new BasicNameValuePair("semester", "2025-1"));
            params.add(new BasicNameValuePair("key", eventID));
            params.add(new BasicNameValuePair("value", value));
            params.add(new BasicNameValuePair("action", "backup"));

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
                                        Toast.makeText(NewEventActivity.this, msg, Toast.LENGTH_LONG).show();
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