package com.huawei.malaysianmedicinalplantsandherbsdatabasesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AdminAdd extends AppCompatActivity {

    private static final int GET_FROM_GALLERY = 3;
    final int REQUEST_CODE_GALLERY = 1;

    public static Plants plants;
    public static Herbs herbs;

    ImageView image_input;
    EditText name_input, def_input, ver_input, color_input, odour_input,
            taste_input, morph_input, medic_input, safety_input, storage_input;
    Button savetoplants, savetoherbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add);

        // configure action bar
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        actionBar.setHomeButtonEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(Color.parseColor("#FF6200EE"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        actionBar.setTitle("");

        image_input = findViewById(R.id.ImageID);
        image_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check permission first
                ActivityCompat.requestPermissions(AdminAdd.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
            }
        });

        // imageView's defined ady
        name_input = findViewById(R.id.PNameID);
        def_input = findViewById(R.id.PDefinitionID);
        ver_input = findViewById(R.id.PVerID);
        color_input = findViewById(R.id.PColorID);
        odour_input = findViewById(R.id.POdourID);
        taste_input = findViewById(R.id.PTasteID);
        morph_input = findViewById(R.id.PMorphID);
        medic_input = findViewById(R.id.PMedicID);
        safety_input = findViewById(R.id.PSafetyID);
        storage_input = findViewById(R.id.PStorageID);

        // create plant database of savetoplant button
        plants = new Plants(AdminAdd.this, "PlantDB.sqlite",null,1);
        plants.queryData();

        savetoplants = findViewById(R.id.SavePlantID);
        savetoplants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    plants.insertData(
                            imageViewToByte(image_input),
                            name_input.getText().toString().trim(),
                            def_input.getText().toString().trim(),
                            ver_input.getText().toString().trim(),
                            color_input.getText().toString().trim(),
                            odour_input.getText().toString().trim(),
                            taste_input.getText().toString().trim(),
                            morph_input.getText().toString().trim(),
                            medic_input.getText().toString().trim(),
                            safety_input.getText().toString().trim(),
                            storage_input.getText().toString().trim()
                    );
                    Toast.makeText(getApplicationContext(), "Added to Plants successfully.", Toast.LENGTH_SHORT).show();
                    image_input.setImageResource(R.mipmap.ic_launcher);
                    name_input.setText("");
                    def_input.setText("");
                    ver_input.setText("");
                    color_input.setText("");
                    odour_input.setText("");
                    taste_input.setText("");
                    morph_input.setText("");
                    medic_input.setText("");
                    safety_input.setText("");
                    storage_input.setText("");

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Fail to add this item to Plants.", Toast.LENGTH_SHORT).show();
                }
                openAdminPlantsViewActivity();
            }
        });

        // create database to save herbs
        herbs  = new Herbs(AdminAdd.this, "PlantDB.sqlite",null,1);
        herbs.queryData();
        savetoherbs = findViewById(R.id.SaveHerbsID);
        savetoherbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    herbs.insertData(
                            imageViewToByte(image_input),
                            name_input.getText().toString().trim(),
                            def_input.getText().toString().trim(),
                            ver_input.getText().toString().trim(),
                            color_input.getText().toString().trim(),
                            odour_input.getText().toString().trim(),
                            taste_input.getText().toString().trim(),
                            morph_input.getText().toString().trim(),
                            medic_input.getText().toString().trim(),
                            safety_input.getText().toString().trim(),
                            storage_input.getText().toString().trim()
                    );
                    Toast.makeText(getApplicationContext(), "Added to Herbs successfully.", Toast.LENGTH_SHORT).show();
                    image_input.setImageResource(R.mipmap.ic_launcher);
                    name_input.setText("");
                    def_input.setText("");
                    ver_input.setText("");
                    color_input.setText("");
                    odour_input.setText("");
                    taste_input.setText("");
                    morph_input.setText("");
                    medic_input.setText("");
                    safety_input.setText("");
                    storage_input.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Fail to add this item to Herbs.", Toast.LENGTH_SHORT).show();
                }
                openAdminHerbsViewActivity();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                image_input.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void openAdminPlantsViewActivity(){
        Intent intent = new Intent (AdminAdd.this, AdminPlantsView.class);
        startActivity(intent);
    }

    public void openAdminHerbsViewActivity(){
        Intent intent = new Intent (AdminAdd.this,AdminHerbsView.class);
        startActivity(intent);
    }
}