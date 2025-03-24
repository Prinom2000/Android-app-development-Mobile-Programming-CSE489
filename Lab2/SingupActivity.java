package edu.ewubd.cse4892021260098;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;



public class SingupActivity extends AppCompatActivity {

    private EditText  etName, etEmail, etPhone, etPass, etConPass;
    private CheckBox cbRememberUser, cbRememberLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button btnSingUp = findViewById(R.id.btnSingUP);
        Button btnExit = findViewById(R.id.btnExit);
        Button btnHaveAccout = findViewById(R.id.btnHaveAccount);


        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPass = findViewById(R.id.etPass);
        etConPass = findViewById(R.id.etConPass);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSingUp.setOnClickListener(new View.OnClickListener() { /////////////
            @Override
            public void onClick(View v) {
                System.out.println("btnSingUp tapped");

                String name= etName.getText().toString();
                String email= etEmail.getText().toString();
                String phone= etPhone.getText().toString();
                String pass= etPass.getText().toString();
                String conPass= etConPass.getText().toString();

                System.out.println("Name: "+name);  // to print in tarminal
                System.out.println("Email: "+email);
                System.out.println("Phone: "+phone);
                System.out.println("Password: "+pass);
                System.out.println("Con Password: "+conPass);

                Intent i= new Intent(SingupActivity.this, UpcomingActivity.class);
                i.putExtra("email", email);
                i.putExtra("phone", phone);
                startActivity(i);
                finishAffinity();



            }
        });
        btnHaveAccout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email= etEmail.getText().toString();
                String phone= etPhone.getText().toString();

                System.out.println("You click btnHaveAccout..........!");
                Intent i= new Intent(SingupActivity.this, LoginActivity.class);
                i.putExtra("email", email);
                i.putExtra("phone", phone);
                startActivity(i);
                finishAffinity();


            }
        });




    }
}