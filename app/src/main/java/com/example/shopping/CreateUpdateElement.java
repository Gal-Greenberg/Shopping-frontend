package com.example.shopping;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CreateUpdateElement extends MainActivity {

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
    ElementBoundary[] statesElementBoundary;

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
                Intent intent = new Intent(CreateUpdateElement.this, UserInfo.class);
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
                        creatingMall();
                        break;
                    case 3:
                        creatingStore();
                        break;
                }
                statesSpinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Object result = null;
        elementTasks = new ElementTasks();
        try {
            result = elementTasks.execute("states", "get", BASE_URL + "/elements/{userDomain}/{userEmail}/byType/{type}",
                    DOMAIN, loginUser.getUserId().getEmail(), "state").get();
        } catch (Exception e) {
            Log.e("ExceptionCreateElement", e.getMessage());
        }

        if (!isResultCorrect(result))
            return;
        statesElementBoundary = (ElementBoundary[]) result;

        statesArraySpinner = new ArrayList<>();
        statesArraySpinner.add(0, "Choose a state");
        for (int i = 0; i < statesElementBoundary.length; i++) {
            statesArraySpinner.add(i + 1, statesElementBoundary[i].getName());
        }

        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                statesArraySpinner);
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesSpinner.setAdapter(stringArrayAdapter);

        statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                stateSpinnerSelectedPosition = position;
                if (stateSpinnerSelectedPosition == 0) {
                    selectedState = null;
                }
                else
                    selectedState = statesElementBoundary[stateSpinnerSelectedPosition - 1];
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

                Object result = null;
                elementTasks = new ElementTasks();
                try {
                    switch (typeSpinnerSelectedPosition) {
                        case 1:
                            if (isCreating) {
                                result = creating(null, null, null, false);
                                break;
                            } else {
                                if (!isMutualFieldsChanged()) {
                                    Toast.makeText(getApplicationContext(), "the " + selectedType + " information didn't change", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                updating(null, null, null, false);
                            }
                            break;
                        case 2:
                            if (!isInputValidMall() || !isInputValidMallAndStore())
                                return;
                            if (isCreating) {
                                result = creating(city.getText().toString(), streetName.getText().toString(),
                                        streetNum.getText().toString(), true);
                                break;
                            } else {
                                if (!isMutualFieldsChanged() && !isMallFieldsChanged()) {
                                    Toast.makeText(getApplicationContext(), "the " + selectedType + " information didn't change", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                updating(city.getText().toString(), streetName.getText().toString(),
                                        streetNum.getText().toString(), true);
                            }
                            break;
                        case 3:
                            if (!isInputValidStore() || !isInputValidMallAndStore())
                                return;
                            if (isCreating) {
                                result = creating(mall.getText().toString(), floor.getText().toString(),
                                        category.getText().toString(), false);
                                break;
                            } else {
                                if (!isMutualFieldsChanged() && !isStoreFieldsChanged()) {
                                    Toast.makeText(getApplicationContext(), "the " + selectedType + " information didn't change", Toast.LENGTH_LONG).show();
                                    return;
                                }
                                updating(mall.getText().toString(), floor.getText().toString(),
                                        category.getText().toString(), false);
                            }
                            break;
                    }
                } catch (Exception e) {
                    Log.e("ExceptionCreateElement", e.getMessage());
                }

                if (isCreating && isResultCorrect(result))
                    actionSucceeded("creation");
            }
        });

        if (!isCreating) {
            fillingFieldsForUpdate();
        }
    }

    public boolean isMutualFieldsChanged() {
        if (name.getText().toString().matches(selectedUpdate.getName()) && selectedType.matches(selectedUpdate.getType())
                && ("" + active.isChecked()).matches(selectedUpdate.getActive())) {
            return false;
        }
        return true;
    }

    public boolean isMallFieldsChanged() {
        if (selectedState.getName().matches((String) selectedUpdate.getElementAttributes().get("state")) &&
                city.getText().toString().matches((String) selectedUpdate.getElementAttributes().get("city")) &&
                streetName.getText().toString().matches((String) selectedUpdate.getElementAttributes().get("streetName")) &&
                streetNum.getText().toString().matches((String) selectedUpdate.getElementAttributes().get("streetNum")))
            return false;
        return true;
    }

    public boolean isStoreFieldsChanged() {
        if (selectedState.getName().matches((String) selectedUpdate.getElementAttributes().get("state")) &&
                mall.getText().toString().matches((String) selectedUpdate.getElementAttributes().get("mall")) &&
                floor.getText().toString().matches((String) selectedUpdate.getElementAttributes().get("floor")) &&
                category.getText().toString().matches((String) selectedUpdate.getElementAttributes().get("category")))
            return false;
        return true;
    }

    public boolean isResultCorrect(Object result) {
        if (result.getClass() == String.class) {
            Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public Object creating(String elementAttribute1, String elementAttribute2, String elementAttribute3, boolean isMall) {
        elementTasks = new ElementTasks();
        try {
            if (isMall) {
                Address mall = getAddressMall();
                return elementTasks.execute("create", "post", BASE_URL + "/elements/{managerDomain}/{managerEmail}",
                        DOMAIN, loginUser.getUserId().getEmail(), name.getText().toString(), selectedType, "" + active.isChecked(),
                        (selectedState == null ? null : selectedState.getName()), elementAttribute1,
                        elementAttribute2, elementAttribute3, "", "" + mall.getLatitude(), "" + mall.getLongitude()).get();
            } else {
                return elementTasks.execute("create", "post", BASE_URL + "/elements/{managerDomain}/{managerEmail}",
                        DOMAIN, loginUser.getUserId().getEmail(), name.getText().toString(), selectedType, "" + active.isChecked(),
                        (selectedState == null ? null : selectedState.getName()), elementAttribute1,
                        elementAttribute2, elementAttribute3, "").get();
            }
        } catch (Exception e) {
            Log.e("ExceptionCreateElement", e.getMessage());
        }
        return null;
    }

    public void updating(String elementAttribute1, String elementAttribute2, String elementAttribute3, boolean isMall) {
        String result = null;
        elementTasks = new ElementTasks();
        try {
            if (isMall) {
                Address mall = getAddressMall();
                result = (String) elementTasks.execute("create", "post", BASE_URL + "/elements/{managerDomain}/{managerEmail}",
                        DOMAIN, loginUser.getUserId().getEmail(), name.getText().toString(), selectedType, "" + active.isChecked(),
                        (selectedState == null ? null : selectedState.getName()), elementAttribute1,
                        elementAttribute2, elementAttribute3, selectedUpdate.getElementId().getId(), "" + mall.getLatitude(),
                        "" + mall.getLongitude()).get();
            } else {
                result = (String) elementTasks.execute("update", "put",
                        BASE_URL + "/elements/{managerDomain}/{managerEmail}/{elementDomain}/{elementId}", DOMAIN,
                        loginUser.getUserId().getEmail(), name.getText().toString(), selectedType, "" + active.isChecked(),
                        (selectedState == null ? null : selectedState.getName()), elementAttribute1, elementAttribute2,
                        elementAttribute3, selectedUpdate.getElementId().getId()).get();
            }
        } catch (Exception e) {
            Log.e("ExceptionCreateElement", e.getMessage());
        }
        if (result.matches("put result succeeded")) {
            actionSucceeded("update");
            return;
        }
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
    }

    public Address getAddressMall() {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            List<Address> addresses = geocoder.getFromLocationName(city.getText().toString() + " " +
                    streetName.getText().toString() + " " + streetNum.getText().toString(), 1);

            if (addresses.size() > 0)
                return addresses.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void actionSucceeded(String action) {
        Toast.makeText(getApplicationContext(), "Successful element " + action, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(CreateUpdateElement.this, ManagerActivity.class);
        intent.putExtras(new Bundle());
        startActivity(intent);
    }

    public void fillingFieldsForUpdate() {
        create.setText("Update");
        name.setText(selectedUpdate.getName());

        if (selectedUpdate.getActive().matches("true")) {
            active.setChecked(true);
        }

        Map<String, Object> attributes = selectedUpdate.getElementAttributes();
        switch (selectedUpdate.getType()) {
            case "state":
                typeSpinnerSelectedPosition = 1;
                break;
            case "mall":
                typeSpinnerSelectedPosition = 2;
                creatingMall();
                statesSpinner.setVisibility(View.VISIBLE);

                fillingFieldsForMallAndStore((String) attributes.get("state"));
                city.setText(attributes.get("city").toString());
                streetName.setText(attributes.get("streetName").toString());
                streetNum.setText(attributes.get("streetNum").toString());
                break;
            case "store":
                typeSpinnerSelectedPosition = 3;
                creatingStore();
                statesSpinner.setVisibility(View.VISIBLE);

                fillingFieldsForMallAndStore((String) attributes.get("state"));
                mall.setText(attributes.get("mall").toString());
                floor.setText(attributes.get("floor").toString());
                category.setText(attributes.get("category").toString());
                break;
        }
        typeSpinner.setSelection(typeSpinnerSelectedPosition);
    }

    public void fillingFieldsForMallAndStore(String state) {
        stateSpinnerSelectedPosition = statesArraySpinner.indexOf(state);
        statesSpinner.setSelection(stateSpinnerSelectedPosition);
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

    public void creatingMall() {
        mall.setVisibility(View.GONE);
        floor.setVisibility(View.GONE);
        category.setVisibility(View.GONE);

        city.setVisibility(View.VISIBLE);
        streetName.setVisibility(View.VISIBLE);
        streetNum.setVisibility(View.VISIBLE);
    }

    public void creatingStore() {
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
