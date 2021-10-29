package com.huawei.malaysianmedicinalplantsandherbsdatabasesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLogin extends AppCompatActivity {
    Button loginButton;
    EditText idtext, pswordtext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        // configure action bar
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        actionBar.setHomeButtonEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(Color.parseColor("#009688"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        actionBar.setTitle("");

        idtext = findViewById(R.id.AdminID);
        pswordtext = findViewById(R.id.PasswordID);
        String id = "admin123";
        String password = "456";

        loginButton = findViewById(R.id.LoginButtonID);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idtext.getText().toString().equals("") || pswordtext.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Please insert Admin ID and Password.", Toast.LENGTH_SHORT).show();
                }
                else{
                    if (idtext.getText().toString().equals(id)){
                        if (pswordtext.getText().toString().equals(password)){
                            idtext.setText("");
                            pswordtext.setText("");
                            Toast.makeText(AdminLogin.this, "Login successful", Toast.LENGTH_SHORT).show();
                            openAdminSelectionActivity();
                        }else{
                            Toast.makeText(getApplicationContext(), "Password Incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Admin ID Incorrect.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    public void openAdminSelectionActivity(){
        Intent intent = new Intent (AdminLogin.this, AdminSelection.class);
        startActivity(intent);
    }
}