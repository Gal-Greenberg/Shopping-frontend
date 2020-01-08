package com.example.shopping.Tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.shopping.User.NewUserForm;
import com.example.shopping.User.UserBoundary;
import com.example.shopping.User.UserRole;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class UserTasks extends AsyncTask<String, Void, Object> {

    private RestTemplate restTemplate;

    public UserTasks() {
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
        * params[5] - urlVariables selectedRole
        * params[6] - urlVariables userName
        * params[7] - urlVariables avatar
        */

        String[] urlVariables = new String[params.length - 3];
        for (int i = 0; i < urlVariables.length; i++) {
            urlVariables[i] = params[i + 3];
        }

        Object request = null;
        switch (params[0]) {
            case "signUp":
                if (params[5].matches("PLAYER"))
                    request = new NewUserForm(params[4], UserRole.PLAYER, params[6], params[7]);
                else
                    request = new NewUserForm(params[4], UserRole.MANAGER, params[6], params[7]);
                break;
            case "update":
                if (params[5].matches("PLAYER"))
                    request = new UserBoundary(null, UserRole.PLAYER, params[6], params[7]);
                else
                    request = new UserBoundary(null, UserRole.MANAGER, params[6], params[7]);
                break;
        }

        try {
            switch (params[1]) {
                case "get":
                    return restTemplate.getForObject(params[2], UserBoundary.class, urlVariables);
                case "post":
                    return restTemplate.postForObject(params[2], request, UserBoundary.class, urlVariables);
                case "put":
                    restTemplate.put(params[2], request, urlVariables);
                    return "put result succeeded";
            }
        }  catch (Exception e) {
            //TODO fix to return all message, return only 404
            Log.e("ExceptionUserTasks", e.getMessage());
            return e.getMessage();
        }
        return null;
    }
}
