package com.example.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopping.Element.ElementBoundary;
import com.example.shopping.Tasks.ElementTasks;

import java.util.Arrays;

public class UpdateElement extends ManagerActivity {

    ImageButton user;

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

        user = findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateElement.this, UserInfo.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });

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
        elementTasks = new ElementTasks();
        Object result = null;
        try {
            result = elementTasks.execute(selectedType, "get", BASE_URL + "/elements/{userDomain}/{userEmail}/byName/{name}",
                    DOMAIN, loginUser.getUserId().getEmail(), name.getText().toString()).get();
        } catch (Exception e) {
            Log.e("ExceptionUpdateElement", e.getMessage());
        }
        if (!isResultValid(result))
            return;
        handleResultSearchUpdate("name", name.getText().toString());
    }

    public void getElementsByType() {
        elementTasks = new ElementTasks();
        Object result = null;
        try {
            result = elementTasks.execute(selectedType, "get", BASE_URL + "/elements/{userDomain}/{userEmail}/byType/{type}",
                    DOMAIN, loginUser.getUserId().getEmail(), selectedType).get();
        } catch (Exception e) {
            Log.e("ExceptionUpdateElement", e.getMessage());
        }
        if (!isResultValid(result))
            return;
        handleResultSearchUpdate("type", selectedType);
    }

    public boolean isResultValid(Object result) {
        if (result.getClass() == String.class) {
            Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
        resultSearchUpdate = Arrays.asList((ElementBoundary[]) result);
        return true;
    }

    public void handleResultSearchUpdate(String updateBy, String value) {
        if (resultSearchUpdate.size() == 0) {
            Toast.makeText(getApplicationContext(), "no element found with " + updateBy + ": " + value, Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent;
        if (resultSearchUpdate.size() == 1) {
            isCreating = false;
            selectedUpdate = resultSearchUpdate.get(0);
            intent = new Intent(UpdateElement.this, CreateElement.class);
            intent.putExtras(new Bundle());
            startActivity(intent);
        }

        intent = new Intent(UpdateElement.this, ResultSearchUpdate.class);
        intent.putExtras(new Bundle());
        startActivity(intent);
    }
}
