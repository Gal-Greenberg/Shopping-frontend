package com.example.shopping;

import com.example.shopping.ElementBoundary.ElementBoundary;
import com.example.shopping.Tasks.ElementTasks;
import com.example.shopping.Tasks.UserTasks;
import com.example.shopping.User.UserBoundary;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String DOMAIN = "2020a.nofar"; //2020a.alik
    public static final String BASE_URL = "http://10.0.0.3:8080/collab";

    public static String stringEmail;
    public static UserTasks userTask;
    public static ElementTasks elementTasks;

    public static ElementBoundary selectedInfo;
    public static UserBoundary loginUser;

    public static int actionNumber;
    public static String selectedState;
    public static String[] selectedStores;
    public static ElementBoundary[] selectedMalls;

    public static ElementBoundary[] resultSearchUpdate;
    public static ElementBoundary selectedUpdate;
    public static boolean isCreating; //if false update

    public MainActivity() {
        userTask = new UserTasks();
        elementTasks = new ElementTasks();
    }

}
