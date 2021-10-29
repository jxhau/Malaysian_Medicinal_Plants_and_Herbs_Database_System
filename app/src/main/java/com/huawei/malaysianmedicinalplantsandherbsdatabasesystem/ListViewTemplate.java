package com.huawei.malaysianmedicinalplantsandherbsdatabasesystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class ListViewTemplate extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<GetDetails> getPlantslist;

    public ListViewTemplate(Context context, int layout, ArrayList<GetDetails> getPlantslist){
        this.context = context;
        this.layout = layout;
        this.getPlantslist = getPlantslist;
    }

    @Override
    public int getCount() {
        return getPlantslist.size();
    }

    @Override
    public Object getItem(int position) {
        return getPlantslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    // used to get next viewlist for next item
    private class ViewHolder{
        ImageView viewImage;
        TextView nameTxt, definitionTxt;
        ViewHolder(View v){
            viewImage = (ImageView) v.findViewById(R.id.ImageViewID);
            nameTxt = (TextView) v.findViewById(R.id.NameTextViewID);
            definitionTxt = (TextView) v.findViewById(R.id.DefinitionTextViewID);
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        if(row == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.list_view_design,null,true);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }else{
            holder = (ViewHolder)row.getTag();
        }
        // get and set the name from the GetPlants class
        GetDetails getPlants = getPlantslist.get(position);
        holder.nameTxt.setText(getPlants.getName());
        holder.definitionTxt.setText(getPlants.getDefinition());
        byte[] image = getPlants.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
        holder.viewImage.setImageBitmap(bitmap);

        return row;
    }

    // for searching
    public void update(ArrayList<GetDetails> results){
        getPlantslist = new ArrayList<>();
        getPlantslist.addAll(results);
        notifyDataSetChanged();
    }
}
