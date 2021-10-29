package com.huawei.malaysianmedicinalplantsandherbsdatabasesystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class AdminHerbsView extends AppCompatActivity {

    public static Herbs herbs;

    android.widget.ListView listView;
    ArrayList<GetDetails> getDetailslist = new ArrayList<GetDetails>();
    ListViewTemplate templateHerbs;

    FloatingActionButton AddHerbsButton;
    final int REQUEST_CODE_GALLERY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_herbs_view);
        // configure action bar
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(Color.parseColor("#009688"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        actionBar.setTitle("Herbs");

        herbs  = new Herbs(AdminHerbsView.this, "PlantDB.sqlite",null,1);
        herbs.queryData();

        listView = findViewById(R.id.ListViewHerbsID);
        // implement the template with the list view design and
        templateHerbs = new ListViewTemplate(this, R.layout.list_view_design,getDetailslist);
        listView.setAdapter(templateHerbs);
        getNewestHerb();

        // add button
        AddHerbsButton = findViewById(R.id.AddHerbsButtonID);
        AddHerbsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminAddActivity();
            }
        });

        // show detail
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetails(AdminHerbsView.this, position);
            }
        });

        // long press to edit or delete
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                CharSequence[] items = {"Edit", "Delete"};
                new AlertDialog.Builder(AdminHerbsView.this)
                        .setTitle("Choose an action")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // update
                                if (which == 0){
                                    try {
                                        int herbId = getIdFromCursor(position);
                                        updateDialog(AdminHerbsView.this, herbId);
                                    }catch (Exception e){
                                        Log.e("cannot open update", e.getMessage());
                                    }
                                }
                                // delete
                                else{
                                    try {
                                        int herbId = getIdFromCursor(position);
                                        deleteDialog(herbId);
                                    }catch (Exception e){
                                        Log.e("cannot open delete", e.getMessage());
                                    }
                                }
                            }
                        }).show();

                return true;
            }
        });
    }

    // search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem actionMenuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) actionMenuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search by name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<GetDetails> results = new ArrayList<>();
                for (GetDetails x : getDetailslist){
                    if (x.getName().toLowerCase().contains(s) || x.getName().contains(s)){
                        results.add(x);
                    }
                }
                ((ListViewTemplate)listView.getAdapter()).update(results);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void openAdminAddActivity(){
        Intent intent = new Intent (AdminHerbsView.this,AdminAdd.class);
        startActivity(intent);
    }

    public void getNewestHerb(){
        // get data from sqlite (table Plants)
        Cursor cursor = AdminHerbsView.herbs.getData();
        getDetailslist.clear();
        // Herbs
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            byte[] image = cursor.getBlob(1);
            String name = cursor.getString(2);
            String definition = cursor.getString(3);
            String vernacular = cursor.getString(4);
            String color = cursor.getString(5);
            String odour = cursor.getString(6);
            String taste = cursor.getString(7);
            String morphology = cursor.getString(8);
            String medicinal = cursor.getString(9);
            String safety = cursor.getString(10);
            String storage = cursor.getString(11);
            getDetailslist.add(new GetDetails(image, name, definition, vernacular, color, odour, taste, morphology, medicinal, safety, storage, id));
        }
        templateHerbs.notifyDataSetChanged();
    }

    int getIdFromCursor(int position){
        //ArrayList<Integer> id = new ArrayList<>();
        Cursor herbCursor = herbs.getData();
        herbCursor.moveToPosition(position);
        return herbCursor.getInt(0);
    }

    private void showDetails(Activity activity, final int ID){
        final Dialog showDetailsDialog = new Dialog(activity);
        showDetailsDialog.setContentView(R.layout.activity_detail);
        showDetailsDialog.setTitle("Full Details");
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 1);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 1);

        byte[] image = getDetailslist.get(ID).getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);

        ImageView viewImage = (ImageView) showDetailsDialog.findViewById(R.id.viewImageID);
        viewImage.setImageBitmap(bitmap);

        TextView viewName = showDetailsDialog.findViewById(R.id.NameID);
        viewName.setText(getDetailslist.get(ID).getName());

        TextView viewDefinition = showDetailsDialog.findViewById(R.id.DefinitionID);
        viewDefinition.setText(getDetailslist.get(ID).getDefinition());

        TextView viewVer = showDetailsDialog.findViewById(R.id.VerID);
        viewVer.setText(getDetailslist.get(ID).getVernacular());

        TextView viewColor = showDetailsDialog.findViewById(R.id.ColorID);
        viewColor.setText(getDetailslist.get(ID).getColor());

        TextView viewOdour = showDetailsDialog.findViewById(R.id.OdourID);
        viewOdour.setText(getDetailslist.get(ID).getOdour());

        TextView viewTaste = showDetailsDialog.findViewById(R.id.TasteID);
        viewTaste.setText(getDetailslist.get(ID).getTaste());

        TextView viewMorph = showDetailsDialog.findViewById(R.id.MorphID);
        viewMorph.setText(getDetailslist.get(ID).getMorphology());

        TextView viewMedic = showDetailsDialog.findViewById(R.id.MedicID);
        viewMedic.setText(getDetailslist.get(ID).getMedicinal());

        TextView viewSafty = showDetailsDialog.findViewById(R.id.SafetyID);
        viewSafty.setText(getDetailslist.get(ID).getSafety());

        TextView viewStorage = showDetailsDialog.findViewById(R.id.StorageID);
        viewStorage.setText(getDetailslist.get(ID).getStorage());

        showDetailsDialog.getWindow().setLayout(width, height);
        showDetailsDialog.show();
    }

    ImageView image_input;
    private void updateDialog(Activity activity, final int id){
        final Dialog updateDialog = new Dialog(activity);
        updateDialog.setContentView(R.layout.activity_admin_add);
        updateDialog.setTitle("Edit");

        image_input = (ImageView) updateDialog.findViewById(R.id.ImageID);
        final EditText name_input = updateDialog.findViewById(R.id.PNameID);
        final EditText def_input = updateDialog.findViewById(R.id.PDefinitionID);
        final EditText ver_input = updateDialog.findViewById(R.id.PVerID);
        final EditText color_input = updateDialog.findViewById(R.id.PColorID);
        final EditText odour_input = updateDialog.findViewById(R.id.POdourID);
        final EditText taste_input = updateDialog.findViewById(R.id.PTasteID);
        final EditText morph_input = updateDialog.findViewById(R.id.PMorphID);
        final EditText medic_input = updateDialog.findViewById(R.id.PMedicID);
        final EditText safety_input = updateDialog.findViewById(R.id.PSafetyID);
        final EditText storage_input = updateDialog.findViewById(R.id.PStorageID);
        Button updatePlants = updateDialog.findViewById(R.id.SavePlantID);
        Button updateHerbs = updateDialog.findViewById(R.id.SaveHerbsID);

        updatePlants.setText("Update Plants");
        updateHerbs.setText("Update Herbs");
        updatePlants.setEnabled(false);

        image_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(AdminHerbsView.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });

        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 1);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 1);
        updateDialog.getWindow().setLayout(width, height);
        updateDialog.show();

        // update database: PLANTS
        updateHerbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    herbs.updateData(
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
                            storage_input.getText().toString().trim(),
                            id);

                    updateDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Herb ID " + id +" updated", Toast.LENGTH_SHORT).show();
                    getNewestHerb();
                }
                catch (Exception e){
                    Log.e("Update failed", e.getMessage());
                }
            }
        });
    }

    private void deleteDialog(final int id){
        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(AdminHerbsView.this);

        deleteDialog.setTitle("Warning").setMessage("Are you sure to delete this item?");
        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    herbs.deleteData(id);
                    getNewestHerb();
                    Toast.makeText(getApplicationContext(), "Herb ID " + id + " deleted", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.e("Failed to delete", e.getMessage());
                }
            }
        });

        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        deleteDialog.show();

    }

    private static byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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
}