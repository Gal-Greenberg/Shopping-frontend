package com.example.shopping.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shopping.Element.Element;
import com.example.shopping.Element.ElementBoundary;
import com.example.shopping.MainActivity;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class ElementTasks extends AsyncTask<String, Void, Object> {

    private RestTemplate restTemplate;

    public ElementTasks() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
    }

    @Override
    protected Object doInBackground(String... params) {
        /*
         * params[0] - function name
         * params[1] - restTemplate function name (get/post/put)
         * params[2] - url
         * params[3] - urlVariables DOMAIN
         * params[4] - urlVariables stringEmail
         * params[5] - urlVariables name
         * params[6] - urlVariables selectedType / size
         * params[7] - urlVariables active / page
         * params[8] - urlVariables selectedState
         * params[9] - urlVariables city / mall
         * params[10] - urlVariables streetName / floor
         * params[11] - urlVariables streetNum / category
         * params[12] - urlVariables elementId selectedUpdate
         * params[13] - urlVariables lat
         * params[14] - urlVariables lng
         */

        String[] urlVariables = new String[params.length - 3];
        for (int i = 0; i < urlVariables.length; i++) {
            urlVariables[i] = params[i + 3];
        }

        Object request = null;
        switch (params[0]) {
            case "create":
            case "update":
                Map<String, Object> elementAttributes = new HashMap<>();
                ElementBoundary parentElement = null;

                elementAttributes.put("state", params[8]);
                if (params[6].matches("mall")) {
                    elementAttributes.put("city", params[9]);
                    elementAttributes.put("streetName", params[10]);
                    elementAttributes.put("streetNum", params[11]);
                    elementAttributes.put("lat", Double.parseDouble(params[13]));
                    elementAttributes.put("lng", Double.parseDouble(params[14]));

                    parentElement = getParentByName(params[3], params[4], params[8], "state");
                    if (parentElement == null)
                        return "no parent element found: mall";
                }
                if (params[6].matches("store")) {
                    elementAttributes.put("mall", params[9]);
                    elementAttributes.put("floor", params[10]);
                    elementAttributes.put("category", params[11]);
                    elementAttributes.put("likes", 0);

                    parentElement = getParentByName(params[3], params[4], params[9], "mall");
                    if (parentElement == null)
                        return "no parent element found: mall";
                }

                if (parentElement == null)
                    request = new ElementBoundary(null, params[5], params[6], params[7], null,
                            null, null, elementAttributes);
                else
                    request = new ElementBoundary(null, params[5], params[6], params[7], null,
                        null, new Element(parentElement.getElementId()), elementAttributes);
                urlVariables[2] = params[3];
                urlVariables[3] = params[12];
                break;
        }

        try {
            switch (params[1]) {
                case "get":
                    return restTemplate.getForObject(params[2], ElementBoundary[].class, urlVariables);
                case "post":
                    return restTemplate.postForObject(params[2], request, ElementBoundary.class, urlVariables);
                case "put":
                    restTemplate.put(params[2], request, urlVariables);
                    return "put result succeeded";
            }
        }  catch (Exception e) {
            Log.e("ExceptionElementTasks", e.getMessage());
            return e.getMessage();
        }
        return null;
    }

    public ElementBoundary getParentByName(String userDomain, String userEmail, String name, String parentType) {
        ElementBoundary[] parentElement = restTemplate.getForObject(MainActivity.BASE_URL + "/elements/{userDomain}/{userEmail}/byName/{name}",
                ElementBoundary[].class, userDomain, userEmail, name);
        if (parentElement.length == 0) {
            return null;
        }
        return parentElement[0];
    }
}
