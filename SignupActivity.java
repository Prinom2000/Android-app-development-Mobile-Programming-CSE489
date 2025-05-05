package edu.ewubd.cse489120251;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etPass, etConPass;
    private CheckBox cbRememberUser, cbRememberLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPass = findViewById(R.id.etPass);
        etConPass = findViewById(R.id.etConPass);

        cbRememberUser = findViewById(R.id.cbRememberUser);
        cbRememberLogin = findViewById(R.id.cbRememberLogin);

        Button btnHaveAccount = findViewById(R.id.btnHaveAccount);
        Button btnExit = findViewById(R.id.btnExit);
        Button btnSignup = findViewById(R.id.btnSignup);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btnHaveAccount not implemented yet");
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
                finishAffinity();
            }
        });
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btnSignup tapped");
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString();
                String pass = etPass.getText().toString();
                String conPass = etConPass.getText().toString();

                System.out.println("Name: "+name);
                System.out.println("email: "+email);
                System.out.println("phone: "+phone);
                System.out.println("pass: "+pass);
                System.out.println("conPass: "+conPass);
                System.out.println("isRememberUser: "+cbRememberUser.isChecked());
                System.out.println("isRememberLogin: "+cbRememberLogin.isChecked());


                // Move to UpcomingEventsActivity
                Intent i = new Intent(SignupActivity.this, UpcomingEventsActivity.class);
                i.putExtra("email", email);
                startActivity(i);
                finishAffinity();
            }
        });
    }
}