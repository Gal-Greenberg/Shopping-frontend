package com.example.shopping.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shopping.Action.ActionBoundary;
import com.example.shopping.Action.Element;
import com.example.shopping.Action.User;
import com.example.shopping.Element.ElementBoundary;
import com.example.shopping.Element.ElementId;
import com.example.shopping.User.UserId;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class ActionTasks extends AsyncTask<String, Void, Object> {

    private RestTemplate restTemplate;
    private String[] selectedElementAttributes;

    public ActionTasks() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
    }

    public ActionTasks(String[] elementAttributes) {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        this.selectedElementAttributes = elementAttributes;
    }

    @Override
    protected Object doInBackground(String... params) {
        /*
         * params[0] - url
         * params[1] - DOMAIN
         * params[2] - user email
         * params[3] - Element id (String)
         * params[4] - action type
         * params[5] - page
         * params[6] - size
         * params[7] - Element id (String)
         */

        Object responseType;
        Map<String, Object> elementAttributes = new HashMap<>();
        switch (params[4]) {
            case "findStoresInState":
            case "findAllStoresInCategoryInMall":
                for (int i = 0; i < selectedElementAttributes.length; i++) {
                    elementAttributes.put("" + i, selectedElementAttributes[i]);
                }
                responseType = new ElementBoundary[0];
                break;
            case "distanceBetweenMalls":
                elementAttributes.put("id", params[7]);
                responseType = new HashMap<String, String>();
                break;
            case "findAllStoresInMall":
            case "findAllStoresByLikes":
                responseType = new ElementBoundary[0];
                break;
            default:
                responseType = new HashMap<String, String>();
                break;
        }
        elementAttributes.put("page", Integer.parseInt(params[5]));
        elementAttributes.put("size", Integer.parseInt(params[6]));

        Object request = new ActionBoundary(null, new Element(new ElementId(params[1], params[3])),
                new User(new UserId(params[1], params[2])), params[4], null, elementAttributes);
        try {
            return restTemplate.postForObject(params[0], request, responseType.getClass());
        } catch (Exception e) {
            Log.e("ExceptionActionTasks", e.getMessage());
            return e.getMessage();
        }
    }
}
