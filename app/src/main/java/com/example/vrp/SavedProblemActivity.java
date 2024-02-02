package com.example.vrp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SavedProblemActivity extends AppCompatActivity {

    ListView listView;
    public static List<ProblemData> problemData;
    public static int listPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_problem);

        listView = findViewById(R.id.list);
        Database db = new Database(this);
        problemData = db.selectAllProblem();

        List<String> nameList = new ArrayList<>();
        for (int i = 0; i < problemData.size(); i++) {
            nameList.add(problemData.get(i).getName());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, nameList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                listPosition = position;
                viewProblem();
            }
        });
    }

    private void viewProblem() {
        Intent intent = new Intent(this, NewProblemActivity.class);
        intent.putExtra("add",false);
        startActivity(intent);
    }
}