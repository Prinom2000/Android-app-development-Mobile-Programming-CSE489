package edu.ewubd.cse489120251;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPass;
    private CheckBox cbRememberUser, cbRememberLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        cbRememberUser = findViewById(R.id.cbRememberUser);
        cbRememberLogin = findViewById(R.id.cbRememberLogin);

        Button btnNoAccount = findViewById(R.id.btnNoAccount);
        Button btnExit = findViewById(R.id.btnExit);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btnHaveAccount not implemented yet");
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
                finishAffinity();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("btnLogin tapped");
                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();

                System.out.println("email: "+email);
                System.out.println("pass: "+pass);

                System.out.println("isRememberUser: "+cbRememberUser.isChecked());
                System.out.println("isRememberLogin: "+cbRememberLogin.isChecked());

                // Move to UpcomingEventsActivity
                Intent i = new Intent(LoginActivity.this, UpcomingEventsActivity.class);
                i.putExtra("email", email);
                startActivity(i);
                finishAffinity();
            }
        });
    }
}