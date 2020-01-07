package com.example.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shopping.ElementBoundary.ElementBoundary;
import com.example.shopping.Tasks.ElementTasks;

import java.util.ArrayList;
import java.util.List;

public class ChooseAction extends MainActivity {

    Spinner spinner;
    ListView listView;
    Button shopping;

    int spinnerSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_action);

        listView = findViewById(R.id.listView);

        final ArrayList<String> allActions = new ArrayList<>();
        allActions.add("Finding all stores you want to go in the closest mall"); //0
        allActions.add("Finding all stores in specific mall"); //1
        allActions.add("Finding all stores in specific category in specific mall"); //2
        allActions.add("Finding distance between shopping malls"); //3
        allActions.add("Search the store with the most likes in specific mall"); //4

        final ListViewAdapter listViewAdapter = new ListViewAdapter(this, allActions, false, 1);
        listView.setAdapter(listViewAdapter);

        ElementBoundary[] result;
        elementTasks = new ElementTasks();
        try {
            result = (ElementBoundary[]) elementTasks.execute("states", "get",
                    BASE_URL + "/elements/{userDomain}/{userEmail}/byType/{type}", DOMAIN, stringEmail, "state").get();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String[] arraySpinner = new String[result.length + 1];
        arraySpinner[0] = "Choose a state";
        for (int i = 0; i < result.length; i++) {
            arraySpinner[i + 1] = result[i].getName();
        }

        spinner = findViewById(R.id.states);
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                arraySpinner);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(stringArrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                selectedState = item.toString();
                spinnerSelectedPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        shopping = findViewById(R.id.shopping);
        shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Integer> positionCheckedBox = listViewAdapter.getCheckedBox();
                if (positionCheckedBox.size() == 0) {
                    Toast.makeText(getApplicationContext(), "please choose action", Toast.LENGTH_LONG).show();
                    return;
                }
                if (spinnerSelectedPosition == 0) {
                    Toast.makeText(getApplicationContext(), "please choose state", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent;
                actionNumber = positionCheckedBox.get(0);
                switch (actionNumber) {
                    case 0:
                        intent = new Intent(ChooseAction.this, ChooseStores.class);
                        intent.putExtras(new Bundle());
                        startActivity(intent);
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        intent = new Intent(ChooseAction.this, ChooseMall.class);
                        intent.putExtras(new Bundle());
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}
