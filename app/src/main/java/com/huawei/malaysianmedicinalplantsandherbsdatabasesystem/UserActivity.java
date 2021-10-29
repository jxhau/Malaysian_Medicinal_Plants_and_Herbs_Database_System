package com.huawei.malaysianmedicinalplantsandherbsdatabasesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserActivity extends AppCompatActivity {
    public static Plants plants;
    public static Herbs herbs;

    android.widget.ListView listView;
    ArrayList<GetDetails> getDetailslist = new ArrayList<GetDetails>();
    ListViewTemplate templatePlants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // configure action bar
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24);
        upArrow.setColorFilter(Color.parseColor("#009688"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        actionBar.setTitle("Plants and Herbs");

        // get the plantDB for viewing when start app
        plants = new Plants(UserActivity.this, "PlantDB.sqlite",null,1);
        plants.queryData();
        herbs  = new Herbs(UserActivity.this, "PlantDB.sqlite",null,1);
        herbs.queryData();

        // Plants
        listView = findViewById(R.id.ListViewHerbsID);
        // implement the template with the list view design and the plant list
        templatePlants = new ListViewTemplate(this, R.layout.list_view_design,getDetailslist);
        listView.setAdapter(templatePlants);
        getNewestData();

        // press to see full details
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetails(UserActivity.this, position);
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

    public void getNewestData(){
        // get data from sqlite (table Plants)
        Cursor cursor = UserActivity.plants.getData();
        getDetailslist.clear();
        // Plants
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

        // Herbs
        Cursor cursor1 = UserActivity.herbs.getData();
        while (cursor1.moveToNext()) {
            int id = cursor1.getInt(0);
            byte[] image = cursor1.getBlob(1);
            String name = cursor1.getString(2);
            String definition = cursor1.getString(3);
            String vernacular = cursor1.getString(4);
            String color = cursor1.getString(5);
            String odour = cursor1.getString(6);
            String taste = cursor1.getString(7);
            String morphology = cursor1.getString(8);
            String medicinal = cursor1.getString(9);
            String safety = cursor1.getString(10);
            String storage = cursor1.getString(11);
            getDetailslist.add(new GetDetails(image, name, definition, vernacular, color, odour, taste, morphology, medicinal, safety, storage, id));
        }
        templatePlants.notifyDataSetChanged();
    }

    private static byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}