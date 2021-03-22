package com.example.colorsnap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
//Adapter class to display color scheme names as seen in ColorSchemesActivity.
//This class reuses/repurposes code from Unit 6 PlantDataBaseRecyclerView and EJ Nicolas's Assignment 3
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    //Create variables
    public ArrayList<String> list;
    Context context;

    public MyAdapter(ArrayList<String> list) {this.list = list;}

    //Creates a view holder using the row_color_scheme layout
    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_color_scheme,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        //Calls a method to set the view's content. Will need to be edited to display the colors within a color scheme
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

    //Class for the ViewHolder which is each individual object on the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //Create variables
        public TextView nameView;
        private Button buttonDeleteColorScheme;
        public LinearLayout myLayout;
        private String colorSchemeName;
        Context context;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //initialize variables
            myLayout = (LinearLayout) itemView;
            nameView = (TextView) itemView.findViewById(R.id.textViewRowColorName);
            buttonDeleteColorScheme = (Button) itemView.findViewById(R.id.buttonDeleteColorScheme);
            colorSchemeName = nameView.getText().toString();
            itemView.setOnClickListener(this);
            buttonDeleteColorScheme.setOnClickListener(this);
            context = itemView.getContext();
        }

        public void bindView(String s){
            //Set's the sensor name for the recycler view. Since the sensor is known here, get the sensor type here too
            nameView.setText(s);
            colorSchemeName = s;
        }

        @Override
        public void onClick(View v) {
            //When a name is tapped, start the ViewColorSchemeActivity, passing the color scheme's name as an identifier.
            if(nameView.isPressed()){
                try{
                    Intent i = new Intent(v.getContext(), ViewColorSchemeActivity.class);
                    i.putExtra("SAVED_COLOR_SCHEME_NAME",colorSchemeName);
                    v.getContext().startActivity(i);
                }
                catch(Exception e){
                    Toast.makeText(v.getContext(), "Error has occured", Toast.LENGTH_SHORT).show();
                }
            }
            else if(buttonDeleteColorScheme.isPressed()){
                //Creating alert dialogue referenced from https://stackoverflow.com/questions/26097513/android-simple-alert-dialog
                //WHen the user presses the delete button, an alert will pop up which the user needs to confirm to delete the color scheme
                AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                alertDialog.setMessage("Delete Color Scheme?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Constants.dbColorSchemes.deleteRow(colorSchemeName);
                                Toast.makeText(v.getContext(), colorSchemeName + " deleted", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Intent i = new Intent(v.getContext(), ColorSchemesActivity.class);
                                v.getContext().startActivity(i);
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });;
                alertDialog.show();
            }
        }
    }
}
