package com.example.colorsnap;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    public ArrayList<String> list;
    Context context;

    public MyAdapter(ArrayList<String> list) {this.list = list;}

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_color_scheme,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        String[]  results = (list.get(position).toString()).split(",");
//        if(results.length==1){ //length == 1 is temporary. Right now its only looking if a name is present
//            holder.nameView.setText(results[1]);
//        }
//        else{
//            holder.nameView.setText("null");
//        }

        holder.bindView(list.get(position).toString());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView nameView;
        public LinearLayout myLayout;
        private String colorSchemeName;

        Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            myLayout = (LinearLayout) itemView;
            nameView = (TextView) itemView.findViewById(R.id.textViewRowColorName);
            colorSchemeName = nameView.getText().toString();
            itemView.setOnClickListener(this);
            context = itemView.getContext();
        }

        public void bindView(String s){
            //Set's the sensor name for the recycler view. Since the sensor is known here, get the sensor type here too
            nameView.setText(s);
            colorSchemeName = s;
        }

        @Override
        public void onClick(View v) {
            try{
                //when tapped on a name on the recycler view, make a explicit intent which will go to the SensorDetailsActivity. Pass the sensor type's data to the next activity so the it knows what kind of sensor it is
                Intent i = new Intent(v.getContext(), ViewColorSchemeActivity.class);
                i.putExtra("SAVED_COLOR_SCHEME_NAME",colorSchemeName);
                v.getContext().startActivity(i);
            }
            catch(Exception e){
                Toast.makeText(v.getContext(), "Error has occured", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
