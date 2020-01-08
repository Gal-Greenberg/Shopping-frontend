package com.example.shopping;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopping.Element.ElementBoundary;
import com.example.shopping.Tasks.ElementTasks;

import java.util.ArrayList;
import java.util.Map;

public class CreateElement extends MainActivity {

    ImageButton user;

    TextView title;
    EditText name;
    Spinner typeSpinner;
    CheckBox active;

    Spinner statesSpinner;

    EditText city;
    EditText streetName;
    EditText streetNum;

    EditText mall;
    EditText floor;
    EditText category;

    Button create;

    ArrayList<String> statesArraySpinner;

    String selectedType;
    int typeSpinnerSelectedPosition;
    int stateSpinnerSelectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_element);

        user = findViewById(R.id.user);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateElement.this, UserInfo.class);
                intent.putExtras(new Bundle());
                startActivity(intent);
            }
        });

        title = findViewById(R.id.title);
        title.setText("Hello, " + loginUser.getUsername());

        name = findViewById(R.id.name);
        typeSpinner = findViewById(R.id.type);
        active = findViewById(R.id.active);

        statesSpinner = findViewById(R.id.states);

        city = findViewById(R.id.city);
        streetName = findViewById(R.id.streetName);
        streetNum = findViewById(R.id.streetNum);

        mall = findViewById(R.id.mall);
        floor = findViewById(R.id.floor);
        category = findViewById(R.id.category);

        create = findViewById(R.id.create);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                selectedType = item.toString();
                typeSpinnerSelectedPosition = position;

                switch (typeSpinnerSelectedPosition) {
                    case 0:
                    case 1:
                        statesSpinner.setVisibility(View.GONE);
                        city.setVisibility(View.GONE);
                        streetName.setVisibility(View.GONE);
                        streetNum.setVisibility(View.GONE);

                        mall.setVisibility(View.GONE);
                        floor.setVisibility(View.GONE);
                        category.setVisibility(View.GONE);
                        return;
                    case 2:
                        createMall();
                        break;
                    case 3:
                        createStore();
                        break;
                }
                mallAndStore();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "please enter name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (typeSpinnerSelectedPosition == 0) {
                    Toast.makeText(getApplicationContext(), "please choose type", Toast.LENGTH_LONG).show();
                    return;
                }

                if (typeSpinnerSelectedPosition == 2 && !isInputValidMallAndStore() && !isInputValidMall())
                    return;
                if (typeSpinnerSelectedPosition == 3 && !isInputValidMallAndStore() && !isInputValidStore())
                    return;

                if (!isCreating) {
                    updating();
                }

                Object result = null;
                elementTasks = new ElementTasks();
                try {
                    result = elementTasks.execute("create", "post", BASE_URL + "/elements/{managerDomain}/{managerEmail}",
                            DOMAIN, loginUser.getUserId().getEmail(), name.getText().toString(), selectedType,
                            "" + active.isChecked(), selectedState, city.getText().toString(), streetName.getText().toString(),
                            streetNum.getText().toString(), mall.getText().toString(), floor.getText().toString(),
                            category.getText().toString()).get();
                } catch (Exception e) {
                    Log.e("ExceptionCreateElement", e.getMessage());
                }

                ElementBoundary[] resultElementBoundary = isResultAsElementBoundaryArr(result);
                if (resultElementBoundary == null)
                    return;
                actionSucceeded("creation");
            }
        });

        if (!isCreating) {
            fillingFieldsForUpdate();
        }
    }

    public ElementBoundary[] isResultAsElementBoundaryArr(Object result) {
        if (result.getClass() == String.class) {
            Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            return null;
        }
        return (ElementBoundary[]) result;
    }

    public void updating() {
        String result = null;
        elementTasks = new ElementTasks();
        try {
            result = (String) elementTasks.execute("update", "put",
                    BASE_URL + "/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}", DOMAIN,
                    loginUser.getUserId().getEmail(), DOMAIN, selectedUpdate.getElementId().getId(),
                    name.getText().toString(), selectedType, "" + active.isChecked(), selectedState,
                    city.getText().toString(), streetName.getText().toString(), streetNum.getText().toString(),
                    mall.getText().toString(), floor.getText().toString(), category.getText().toString()).get();
        } catch (Exception e) {
            Log.e("ExceptionCreateElement", e.getMessage());
        }
        if (result.matches("put result succeeded"))
            actionSucceeded("update");
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
    }

    public void actionSucceeded(String action) {
        Toast.makeText(getApplicationContext(), "Successful element " + action, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(CreateElement.this, ManagerActivity.class);
        intent.putExtras(new Bundle());
        startActivity(intent);
    }

    public void fillingFieldsForUpdate() {
        create.setText("Update");
        name.setText(selectedUpdate.getName());

        Map<String, Object> attributes = selectedUpdate.getElementAttributes();
        selectedType = selectedUpdate.getType();
        switch (selectedType) {
            case "state":
                typeSpinnerSelectedPosition = 1;
                break;
            case "mall":
                fillingFieldsMallAndStore(attributes);
                city.setText(attributes.get("city").toString());
                streetName.setText(attributes.get("streetName").toString());
                streetNum.setText(attributes.get("streetNum").toString());
                typeSpinnerSelectedPosition = 2;
                break;
            case "store":
                fillingFieldsMallAndStore(attributes);
                fillingFieldsStore(attributes);
                typeSpinnerSelectedPosition = 3;
                break;
        }
        typeSpinner.setSelection(typeSpinnerSelectedPosition);

        if (selectedUpdate.getActive().matches("true")) {
            active.setChecked(true);
        }
    }

    public void fillingFieldsMallAndStore(Map<String, Object> attributes) {
        stateSpinnerSelectedPosition = statesArraySpinner.indexOf(attributes.get("state"));
        selectedState = statesArraySpinner.get(stateSpinnerSelectedPosition);
        statesSpinner.setSelection(stateSpinnerSelectedPosition);
    }

    public void fillingFieldsStore(Map<String, Object> attributes) {
        mall.setText(attributes.get("mall").toString());
        floor.setText(attributes.get("floor").toString());
        category.setText(attributes.get("category").toString());
    }

    public boolean isInputValidMallAndStore() {
        if (stateSpinnerSelectedPosition == 0) {
            Toast.makeText(getApplicationContext(), "please choose state", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean isInputValidMall() {
        if (city.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "please enter city", Toast.LENGTH_LONG).show();
            return false;
        }
        if (streetName.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "please enter street name", Toast.LENGTH_LONG).show();
            return false;
        }
        if (streetNum.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "please enter street number", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean isInputValidStore() {
        if (mall.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "please enter mall", Toast.LENGTH_LONG).show();
            return false;
        }
        if (floor.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "please enter floor", Toast.LENGTH_LONG).show();
            return false;
        }
        if (category.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "please enter category", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void mallAndStore() {
        statesSpinner.setVisibility(View.VISIBLE);

        Object result = null;
        elementTasks = new ElementTasks();
        try {
            result = elementTasks.execute("states", "get", BASE_URL + "/elements/{userDomain}/{userEmail}/byType/{type}",
                    DOMAIN, loginUser.getUserId().getEmail(), "state").get();
        } catch (Exception e) {
            Log.e("ExceptionCreateElement", e.getMessage());
        }

        ElementBoundary[] resultElementBoundary = isResultAsElementBoundaryArr(result);
        if (resultElementBoundary == null)
            return;

        statesArraySpinner = new ArrayList<>();
        statesArraySpinner.add(0, "Choose a state");
        for (int i = 0; i < resultElementBoundary.length; i++) {
            statesArraySpinner.add(i + 1,resultElementBoundary[i].getName());
        }

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                statesArraySpinner);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesSpinner.setAdapter(stringArrayAdapter);

        statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Object item = adapterView.getItemAtPosition(position);
                selectedState = item.toString();
                stateSpinnerSelectedPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void createMall() {
        mall.setVisibility(View.GONE);
        floor.setVisibility(View.GONE);
        category.setVisibility(View.GONE);

        city.setVisibility(View.VISIBLE);
        streetName.setVisibility(View.VISIBLE);
        streetNum.setVisibility(View.VISIBLE);
    }

    public void createStore() {
        statesSpinner.setVisibility(View.GONE);
        city.setVisibility(View.GONE);
        streetName.setVisibility(View.GONE);
        streetNum.setVisibility(View.GONE);

        mall.setVisibility(View.VISIBLE);
        floor.setVisibility(View.VISIBLE);
        category.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
