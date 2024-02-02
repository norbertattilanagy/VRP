package com.example.vrp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewProblemActivity extends AppCompatActivity {

    EditText problemNameEditText, vehicleCapacityEditText, depotXEditText, depotYEditText;
    Button addLocationButton, viewButton, deleteButton;
    LinearLayout linearLayout;
    List<VRPLocation> locationList;
    Boolean add;
    int problemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_problem);

        Intent intent = getIntent();//get submitted level id
        add = intent.getBooleanExtra("add", true);

        problemNameEditText = findViewById(R.id.problemNameEditText);
        vehicleCapacityEditText = findViewById(R.id.vehicleCapacityEditText);
        depotXEditText = findViewById(R.id.depotXEditText);
        depotYEditText = findViewById(R.id.depotYEditText);

        addLocationButton = findViewById(R.id.addLocation_button);
        viewButton = findViewById(R.id.view_button);
        deleteButton = findViewById(R.id.delete_button);
        linearLayout = findViewById(R.id.newProblemActivityLayout);

        locationList = new ArrayList<>();

        if (!add) {
            deleteButton.setVisibility(View.VISIBLE);

            problemId = SavedProblemActivity.problemData.get(SavedProblemActivity.listPosition).getId();
            problemNameEditText.setText(SavedProblemActivity.problemData.get(SavedProblemActivity.listPosition).getName());
            vehicleCapacityEditText.setText(Integer.toString(SavedProblemActivity.problemData.get(SavedProblemActivity.listPosition).getCapacity()));

            Database db = new Database(this);
            locationList = db.selectIdLocation(problemId);
            for (int i = 0; i < locationList.size(); i++) {
                if (locationList.get(i).getWarehouse()) {
                    depotXEditText.setText(Double.toString(locationList.get(i).getX()));
                    depotYEditText.setText(Double.toString(locationList.get(i).getY()));
                    locationList.remove(i);
                } else {
                    addLocationInList(locationList.get(i), i);
                }
            }
        }

        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditLocation(false, null);
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewProblem();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProblem();
            }
        });
    }

    private void addEditLocation(Boolean edit, View v1) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_location);

        EditText locationName = dialog.findViewById(R.id.locationNameEditText);
        EditText locationXEditText = dialog.findViewById(R.id.locationXEditText);
        EditText locationYEditText = dialog.findViewById(R.id.locationYEditText);
        EditText locationRequestEditText = dialog.findViewById(R.id.locationRequestEditText);

        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        Button addButton = dialog.findViewById(R.id.addButton);

        if (edit) {
            locationName.setText(locationList.get(v1.getId()).name);
            locationXEditText.setText(Double.toString(locationList.get(v1.getId()).x));
            locationYEditText.setText(Double.toString(locationList.get(v1.getId()).y));
            locationRequestEditText.setText(Integer.toString(locationList.get(v1.getId()).request));
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();//close dialog
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get location data
                String name = locationName.getText().toString();
                double x = Double.parseDouble(locationXEditText.getText().toString());
                double y = Double.parseDouble(locationYEditText.getText().toString());
                int request = Integer.parseInt(locationRequestEditText.getText().toString());

                if (edit) {
                    locationList.get(v1.getId()).name = name;
                    locationList.get(v1.getId()).x = x;
                    locationList.get(v1.getId()).y = y;
                    locationList.get(v1.getId()).request = request;

                    //update location list in view
                    TextView locationNameTextView2 = v1.findViewById(R.id.locationNameTextView2);
                    TextView locationRequestTextView2 = v1.findViewById(R.id.locationRequestTextView2);
                    TextView locationCoordinateTextView2 = v1.findViewById(R.id.locationCoordinateTextView2);

                    locationNameTextView2.setText(": " + locationList.get(v1.getId()).name);
                    locationRequestTextView2.setText(": " + locationList.get(v1.getId()).request);
                    locationCoordinateTextView2.setText(": " + locationList.get(v1.getId()).x + "; " + locationList.get(v1.getId()).y);
                } else {
                    //add location data in list
                    VRPLocation location = new VRPLocation(name, x, y, request, false);
                    if (problemId != -1) {
                        location.setProblem_id(problemId);
                    }
                    locationList.add(location);
                    addLocationInList(location, locationList.size() - 1);
                }
                dialog.cancel();
            }
        });
        dialog.show();//open dialog
    }

    private void addLocationInList(VRPLocation location, int id) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.location_card, null);

        view.setId(id);

        //add location data in view
        TextView locationNameTextView2 = view.findViewById(R.id.locationNameTextView2);
        TextView locationRequestTextView2 = view.findViewById(R.id.locationRequestTextView2);
        TextView locationCoordinateTextView2 = view.findViewById(R.id.locationCoordinateTextView2);

        locationNameTextView2.setText(": " + location.name);
        locationRequestTextView2.setText(": " + location.request);
        locationCoordinateTextView2.setText(": " + location.x + "; " + location.y);

        linearLayout.addView(view, linearLayout.getChildCount() - 2);//add view before button
    }

    public void removeLocation(View view) {
        View v1 = (View) view.getParent().getParent();//location card view
        locationList.remove(v1.getId());//remove location from location list
        linearLayout.removeView((View) view.getParent().getParent());//remove view
    }

    public void editLocation(View view) {
        View v1 = (View) view.getParent().getParent();//location card view
        addEditLocation(true, v1);
    }

    public void viewProblem() {
        String problemName = problemNameEditText.getText().toString();
        int vehicleCapacity = Integer.parseInt(vehicleCapacityEditText.getText().toString());
        double depotX = Double.parseDouble(depotXEditText.getText().toString());
        double depotY = Double.parseDouble(depotYEditText.getText().toString());

        Intent intent = new Intent(this, ViewProblemActivity.class);
        //pass the values in ViewProblemActivity class
        intent.putExtra("problemName", problemName);
        intent.putExtra("vehicleCapacity", vehicleCapacity);
        intent.putExtra("depotX", depotX);
        intent.putExtra("depotY", depotY);
        intent.putExtra("locationList", (Serializable) locationList);
        intent.putExtra("add", add);
        startActivity(intent);
    }

    public void deleteProblem(){

        Context context = this;
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getResources().getString(R.string.delete));
        dialog.setMessage(getResources().getString(R.string.delete_problem)).setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Database db= new Database(context);
                db.deleteLocationProblemId(problemId);
                db.deleteProblem(Integer.toString(problemId));
                openMainActivity();
            }
        }).setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
            }
        });
        dialog.show();

    }

    private void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}