package com.example.vrp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button newButton,savedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newButton = findViewById(R.id.new_button);
        savedButton = findViewById(R.id.saved_button);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNew();
            }
        });

        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSaved();
            }
        });

        //addDataVRP();
    }

    private void openNew(){
        Intent intent = new Intent(this, NewProblemActivity.class);
        intent.putExtra("add",true);
        startActivity(intent);
    }

    private void openSaved(){
        Intent intent = new Intent(this,SavedProblemActivity.class);
        startActivity(intent);
    }

    private void addDataVRP(){
        List<VRPLocation> customers = new ArrayList<>();
        customers.add(new VRPLocation(1,-1, "", 1, 2, 5,false));
        customers.add(new VRPLocation(2,-1, "", 2, 4, 4,false));
        customers.add(new VRPLocation(3,-1, "", 3, 1, 2,false));
        customers.add(new VRPLocation(4,-1, "", 4, 3, 1,false));
        customers.add(new VRPLocation(5,-1, "", 5, 5, 3,false));

        VRPLocation deposits = new VRPLocation("", 0, 0,-1,true);
        int vehicleCapacity = 10;

        VRP vrp = new VRP(deposits, customers, vehicleCapacity);
        List<VRPRoute> routes = vrp.solveVRP();

        // Print the solution
        for (int i = 0; i < routes.size(); i++) {
            VRPRoute route = routes.get(i);
            System.out.println("Route " + (i + 1) + ": " + route.locations);
            for (int j = 0; j < route.locations.size(); j++) {
                System.out.println(route.locations.get(j).id);
            }
        }
    }
}