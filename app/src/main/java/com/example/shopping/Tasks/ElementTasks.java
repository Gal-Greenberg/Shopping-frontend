package com.example.shopping.Tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.example.shopping.ElementBoundary.ElementBoundary;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class ElementTasks extends AsyncTask<String, Void, Object[]> {

    private RestTemplate restTemplate;

    public ElementTasks() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
    }

    @Override
    protected Object[] doInBackground(String... params) {
        /*
         * params[0] - function name
         * params[1] - restTemplate function name (get/post/put)
         * params[2] - url
         * params[3] - urlVariables DOMAIN
         * params[4] - urlVariables stringEmail
         * params[5] - urlVariables name
         * params[6] - urlVariables selectedType
         * params[7] - urlVariables active
         * params[8] - urlVariables selectedState
         * params[9] - urlVariables location
         * params[10] - urlVariables mall
         * params[11] - urlVariables floor
         * params[12] - urlVariables category
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

                if (params[6].matches("mall")) {
                    elementAttributes.put("state", params[8]);
                    elementAttributes.put("location", params[9]);

                    //get parent by name
                    parentElement = restTemplate.getForObject("/elements/{userDomain}/{userEmail}/byName/{name}",
                            ElementBoundary[].class, params[3], params[4], params[8])[0];
                }
                if (params[6].matches("store")) {
                    elementAttributes.put("mall", params[10]);
                    elementAttributes.put("floor", params[11]);
                    elementAttributes.put("category", params[12]);
                    elementAttributes.put("likes", 0);

                    //get parent by name
                    parentElement = restTemplate.getForObject("/elements/{userDomain}/{userEmail}/byName/{name}",
                            ElementBoundary[].class, params[3], params[4], params[8])[0];
                }

                if (parentElement == null) {
                    throw new RuntimeException("no parent element found: floor/state");
                }

                request = new ElementBoundary(null, params[5], params[6], params[7], null,
                        null, parentElement.getParentElement(), elementAttributes);
                break;
        }

        try {
            switch (params[1]) {
                case "get":
                    return restTemplate.getForObject(params[2], ElementBoundary[].class, urlVariables);
                case "post":
                    return restTemplate.postForObject(params[2], request, ElementBoundary[].class, urlVariables);
                case "put":
                    restTemplate.put(params[2], request, urlVariables);
                    String[] succeeded = {"put succeeded"};
                    return succeeded;
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
