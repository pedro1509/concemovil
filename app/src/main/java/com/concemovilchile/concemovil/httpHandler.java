package com.concemovilchile.concemovil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * Created by macbookpro on 11-06-15.
 */
public class httpHandler {
    public String get(String geturl){
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(geturl);

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String responseText = EntityUtils.toString(entity);
            return responseText;
        }catch (Exception e){
            return "Error";


        }

    }
}
