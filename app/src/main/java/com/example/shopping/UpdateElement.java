package com.example.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopping.ElementBoundary.ElementBoundary;
import com.example.shopping.Tasks.ElementTasks;

public class UpdateElement extends ManagerActivity {

    TextView title;
    Spinner updateBySpinner;
    EditText name;
    Spinner typeSpinner;

    Button update;

    String selectedUpdateBy;
    String selectedType;
    int updateBySpinnerSelectedPosition;
    int typeSpinnerSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_element);

        title = findViewById(R.id.title);
        title.setText("Hello, " + loginUser.getUsername());

        updateBySpinner = findViewById(R.id.updateBy);
        name = findViewById(R.id.name);
        typeSpinner = findViewById(R.id.type);

        updateBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                selectedUpdateBy = item.toString();
                updateBySpinnerSelectedPosition = position;

                switch (updateBySpinnerSelectedPosition) {
                    case 0:
                        name.setVisibility(View.GONE);
                        typeSpinner.setVisibility(View.GONE);
                        break;
                    case 1:
                        name.setVisibility(View.VISIBLE);
                        typeSpinner.setVisibility(View.GONE);
                        break;
                    case 2:
                        name.setVisibility(View.GONE);
                        typeSpinner.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                selectedType = item.toString();
                typeSpinnerSelectedPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (updateBySpinnerSelectedPosition) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "please choose update by", Toast.LENGTH_LONG).show();
                        return;
                    case 1:
                        if (name.getText().toString().matches("")) {
                            Toast.makeText(getApplicationContext(), "please enter name", Toast.LENGTH_LONG).show();
                            return;
                        }
                        getElementByName();
                        break;
                    case 2:
                        if (typeSpinnerSelectedPosition == 0) {
                            Toast.makeText(getApplicationContext(), "please choose type", Toast.LENGTH_LONG).show();
                            return;
                        }
                        getElementsByType();
                        break;
                }
            }
        });
    }

    public void getElementByName() {
        ElementBoundary[] results;
        elementTasks = new ElementTasks();
        try {
            resultSearchUpdate = (ElementBoundary[]) elementTasks.execute(selectedType, "get",
                    BASE_URL + "/elements/{userDomain}/{userEmail}/byType/{type}", DOMAIN, stringEmail, selectedType).get();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (resultSearchUpdate.length == 0) {
            Toast.makeText(getApplicationContext(), "no element found with type: " + selectedType, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent;
        if (resultSearchUpdate.length == 1) {
            selectedUpdate = resultSearchUpdate[0];
            intent = new Intent(UpdateElement.this, CreateElement.class);
            intent.putExtras(new Bundle());
            startActivity(intent);
        }

        intent = new Intent(UpdateElement.this, ResultSearchUpdate.class);
        intent.putExtras(new Bundle());
        startActivity(intent);
    }

    public void getElementsByType() {

    }
}
