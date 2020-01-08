package com.example.shopping.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shopping.Action.ActionBoundary;
import com.example.shopping.Action.Element;
import com.example.shopping.Action.User;
import com.example.shopping.Element.ElementId;
import com.example.shopping.User.UserId;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class ActionTasks extends AsyncTask<String, Void, Object> {

    private RestTemplate restTemplate;

    public ActionTasks() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
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
         * params[7] - elementAttributes
         */

        Map<String, Object> elementAttributes = new HashMap<>();
        switch (params[4]) {
            case "findStoresInState":
            case "findAllStoresInCategoryInMall":
                for (int i = 0; i < params.length - 7; i++) {
                    elementAttributes.put("" + i, params[i + 7]);
                }
                break;
//            case "findAllStoresInCategoryInMall":
//                break;
        }
        elementAttributes.put("page", params[5]);
        elementAttributes.put("size", params[6]);

        Object request = new ActionBoundary(null, new Element(new ElementId(params[1], params[3])),
                new User(new UserId(params[1], params[2])), params[4], null, elementAttributes);
        try {
            restTemplate.postForObject(params[0], request, Object.class);
        } catch (Exception e) {
            //TODO fix to return all message, return only 404
            Log.e("ExceptionElementTasks", e.getMessage());
            return e.getMessage();
        }

        return null;
    }
}
