package com.example.vrp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewProblemActivity extends AppCompatActivity {

    static String problemName;
    static int vehicleCapacity;
    static double depotX, depotY;
    static List<VRPLocation> locationList;
    static List<VRPRoute> routes;
    Boolean add;
    TextView problemName_textView, capacity_textView, requested_textView;
    LinearLayout scrollLinearLayout;
    Button save_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_problem);

        Intent intent = getIntent();//get submitted level id
        problemName = intent.getStringExtra("problemName");
        vehicleCapacity = intent.getIntExtra("vehicleCapacity", -1);
        depotX = intent.getDoubleExtra("depotX", -1);
        depotY = intent.getDoubleExtra("depotY", -1);
        locationList = (List<VRPLocation>) intent.getSerializableExtra("locationList");
        add = intent.getBooleanExtra("add", true);

        problemName_textView = findViewById(R.id.problemName_textView);
        capacity_textView = findViewById(R.id.capacity_textView);
        requested_textView = findViewById(R.id.requested_textView);
        scrollLinearLayout = findViewById(R.id.scrollLinearLayout);
        save_button = findViewById(R.id.save_button);

        setText();
        calculateVRP();

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (add) {
                    saveProblem();
                } else {
                    updateProblem();
                }
            }
        });
    }

    private void calculateVRP() {
        VRPLocation deposit = new VRPLocation(getResources().getString((R.string.depot)), depotX, depotY, 0, true);

        VRP vrp = new VRP(deposit, locationList, vehicleCapacity);
        routes = vrp.solveVRP();

        // Print the solution
        for (int i = 0; i < routes.size(); i++) {
            VRPRoute route = routes.get(i);
            System.out.print("Route " + (i + 1) + ": ");
            for (int j = 0; j < route.locations.size(); j++) {
                System.out.print(route.locations.get(j).name + " - ");
            }
            System.out.println();
        }
    }

    private void setText() {
        if (!add)
            save_button.setText(getResources().getString(R.string.update));

        problemName_textView.setText(getResources().getString(R.string.problem_name) + ": " + problemName);
        capacity_textView.setText(getResources().getString(R.string.vehicle_capacity) + ": " + vehicleCapacity);
        requested_textView.setText(getResources().getString(R.string.requested_quantity) + ": ");

        for (int i = 0; i < locationList.size(); i++) {
            TextView textView = new TextView(this);
            textView.setTextSize(16f);
            textView.setText(locationList.get(i).name + ": " + locationList.get(i).request);
            scrollLinearLayout.addView(textView);
        }
    }

    private void saveProblem() {
        Database db = new Database(this);
        int problemId = db.addProblem(new ProblemData(vehicleCapacity, problemName));

        for (int i = 0; i < locationList.size(); i++) {
            locationList.get(i).setProblem_id(problemId);
            db.addLocation(locationList.get(i));
        }

        VRPLocation warehouse = new VRPLocation(problemId, getResources().getString((R.string.depot)), depotX, depotY, -1, true);
        db.addLocation(warehouse);

        Intent intent = new Intent(this, SavedProblemActivity.class);
        startActivity(intent);
    }

    private void updateProblem() {
        System.out.println("update");
        Database db = new Database(this);

        int problemId = db.addProblem(new ProblemData(vehicleCapacity, problemName));
        VRPLocation warehouse = new VRPLocation(problemId, getResources().getString((R.string.depot)), depotX, depotY, -1, true);
        locationList.add(warehouse);

        db.updateProblem(String.valueOf(locationList.get(0).getProblem_id()), String.valueOf(vehicleCapacity), problemName);
        db.updateLocation(locationList);
    }
}