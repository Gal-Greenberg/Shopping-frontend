package com.example.shopping;

import android.os.AsyncTask;

import com.example.shopping.User.UserBoundary;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class Task extends AsyncTask<String, Void, UserBoundary> {

    UserBoundary result;

    @Override
    protected UserBoundary doInBackground(String... params) {
        try {
            String url = "http://192.168.1.108:8080/collab/users/login/{domain}/{email}";

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

            result = restTemplate.getForObject(url, UserBoundary.class, "2020a.nofar",
                    "gal@gmail.com");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

//    @Override
//    protected void onPostExecute(UserBoundary result) {
//        super.onPostExecute(result);
//        this.result = result;
//    }
}
