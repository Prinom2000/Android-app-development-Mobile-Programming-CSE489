package edu.ewubd.cse4892021260098;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NewEventActivity extends AppCompatActivity {

    private EditText etTitle, etVenue, etDate, etNumofparticipation, etDescription;
    private RadioButton rdOnline, rdOffline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        etTitle= findViewById(R.id.etTille);
        etVenue= findViewById(R.id.etVenue);
        etDate= findViewById(R.id.etDate);
        etNumofparticipation= findViewById(R.id.etNumofparticipation);
        etDescription= findViewById(R.id.etDescription);

        rdOnline= findViewById(R.id.rdOnline);
        rdOffline= findViewById(R.id.rdOffline);

        Button btCancle = findViewById(R.id.btCancle);
        Button btSave = findViewById(R.id.btSave);

        btCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(NewEventActivity.this, UpcomingActivity.class);
                System.out.println("You cancled add new evant.....!");
                startActivity(i);

            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title= etTitle.getText().toString();
                String venue= etVenue.getText().toString();
                String date= etDate.getText().toString();
                String numofparticipation= etNumofparticipation.getText().toString();
                String description= etDescription.getText().toString();

                System.out.println("I love Coding.......!  👨‍💻");

                // send those information in database.
                System.out.println("isOnline: "+rdOnline.isChecked());
                System.out.println("isOffline: "+rdOffline.isChecked());

                System.out.println("title: "+title);
                System.out.println("venue: "+venue);
                System.out.println("dt: "+date);
                System.out.println("numParticipants: "+numofparticipation);
                System.out.println("description: "+description);
                // send those information in database.

                Intent i= new Intent(NewEventActivity.this, UpcomingActivity.class);
                System.out.println("You saved a new evant.....!");
                startActivity(i);





            }
        });


    }
}