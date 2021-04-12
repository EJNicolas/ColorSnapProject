package com.example.colorsnap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
        private LinearLayout schemeColor1, schemeColor2, schemeColor3, schemeColor4, schemeColor5;
        Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //initialize variables
            myLayout = (LinearLayout) itemView;
            nameView = (TextView) itemView.findViewById(R.id.textViewRowColorName);
            buttonDeleteColorScheme = (Button) itemView.findViewById(R.id.buttonDeleteColorScheme);
            //Need these linear layouts to show the colors of the color scheme
            schemeColor1 = (LinearLayout) itemView.findViewById(R.id.schemeColor1);
            schemeColor2 = (LinearLayout) itemView.findViewById(R.id.schemeColor2);
            schemeColor3 = (LinearLayout) itemView.findViewById(R.id.schemeColor3);
            schemeColor4 = (LinearLayout) itemView.findViewById(R.id.schemeColor4);
            schemeColor5 = (LinearLayout) itemView.findViewById(R.id.schemeColor5);
            colorSchemeName = nameView.getText().toString();
            itemView.setOnClickListener(this);
            buttonDeleteColorScheme.setOnClickListener(this);
            context = itemView.getContext();
        }

        public void bindView(String s){
            //Set's the sensor name for the recycler view. Since the sensor is known here, get the sensor type here too
            nameView.setText(s);
            String[] colors = new String[5];
            colorSchemeName = s;
            //Gets the colors of a color scheme
            Cursor cursor;
            cursor = Constants.dbColorSchemes.getData(colorSchemeName);
            int colorColumn1 = cursor.getColumnIndex(Constants.COLOR1);
            int colorColumn2 = cursor.getColumnIndex(Constants.COLOR2);
            int colorColumn3 = cursor.getColumnIndex(Constants.COLOR3);
            int colorColumn4 = cursor.getColumnIndex(Constants.COLOR4);
            int colorColumn5 = cursor.getColumnIndex(Constants.COLOR5);

            cursor.moveToFirst();
            colors[0] = cursor.getString(colorColumn1);
            colors[1] = cursor.getString(colorColumn2);
            colors[2] = cursor.getString(colorColumn3);
            colors[3] = cursor.getString(colorColumn4);
            colors[4] = cursor.getString(colorColumn5);

            //Needed to set the layoout's weight through code. Referenced from https://stackoverflow.com/questions/4641072/how-to-set-layout-weight-attribute-dynamically-from-code
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f
            );

            //If the color at a column isnt null, set the LinearLayout's weight to 1 and set the background color to it
            //Because we are using weights, the layout's size will change according to how many colors there are in a color scheme
            if(!colors[0].equals("null")){
                schemeColor1.setLayoutParams(param);
                schemeColor1.setBackgroundColor(Color.parseColor("#" + colors[0].toString()));
            }
            if(!colors[1].equals("null")){
                schemeColor2.setLayoutParams(param);
                schemeColor2.setBackgroundColor(Color.parseColor("#" + colors[1].toString()));
            }
            if(!colors[2].equals("null")){
                schemeColor3.setLayoutParams(param);
                schemeColor3.setBackgroundColor(Color.parseColor("#" + colors[2].toString()));
            }
            if(!colors[3].equals("null")){
                schemeColor4.setLayoutParams(param);
                schemeColor4.setBackgroundColor(Color.parseColor("#" + colors[3].toString()));
            }
            if(!colors[4].equals("null")){
                schemeColor5.setLayoutParams(param);
                schemeColor5.setBackgroundColor(Color.parseColor("#" + colors[4].toString()));
            }
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
