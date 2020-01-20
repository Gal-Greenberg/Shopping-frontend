package com.example.shopping;

import com.example.shopping.Element.ElementBoundary;
import com.example.shopping.Tasks.ActionTasks;
import com.example.shopping.Tasks.ElementTasks;
import com.example.shopping.Tasks.UserTasks;
import com.example.shopping.User.UserBoundary;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String DOMAIN = "2020a.alik";
    public static final String BASE_URL = "http://ipBackend:8888/collab";
    public static final String PAGE_SIZE = "10";

    public static UserTasks userTask;
    public static ElementTasks elementTasks;
    public static ActionTasks actionTasks;

    public static UserBoundary loginUser;

    public static int actionNumber;
    public static List<String> selectedStores;
    public static List<String> selectedCategory;
    public static ElementBoundary selectedState;
    public static ElementBoundary selectedInfo;
    public static List<ElementBoundary> selectedMalls;

    public static List<ElementBoundary> resultSearchUpdate;
    public static ElementBoundary selectedUpdate;
    public static boolean isCreating; //if false update

    public MainActivity() {
        userTask = new UserTasks();
        elementTasks = new ElementTasks();
        actionTasks = new ActionTasks();
    }
}
