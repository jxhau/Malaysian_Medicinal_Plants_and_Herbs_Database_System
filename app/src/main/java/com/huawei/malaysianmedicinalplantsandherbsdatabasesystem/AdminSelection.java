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
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class AdminSelection extends AppCompatActivity {
    Button PlantsButton, HerbsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_selection);
        // configure action bar
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        actionBar.setHomeButtonEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(Color.parseColor("#009688"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        actionBar.setTitle("");

        ImageView imageView = findViewById(R.id.imageViewMainAdmin);
        Glide.with(this).load(R.drawable.growing).into(imageView);

        PlantsButton = findViewById(R.id.PlantsButtonID);
        PlantsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminPlantsViewActivity();
            }
        });
        HerbsButton = findViewById(R.id.HerbsButtonID);
        HerbsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminHerbsViewActivity();
            }
        });
    }

    public void openAdminPlantsViewActivity(){
        Intent intent = new Intent (AdminSelection.this, AdminPlantsView.class);
        startActivity(intent);
    }

    public void openAdminHerbsViewActivity(){
        Intent intent = new Intent (AdminSelection.this,AdminHerbsView.class);
        startActivity(intent);
    }
}