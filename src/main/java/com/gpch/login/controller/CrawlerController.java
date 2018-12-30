package com.gpch.login.controller;

import jdk.nashorn.internal.parser.JSONParser;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CrawlerController {
    public String endpoint = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/";
    public String key = "b20ac434ba454114b26800aa119658b1";
    @RequestMapping(value = "/title", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Map<String, ? extends Object> getTitle(){
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            Document document = Jsoup.connect("http://ioe.go.vn/").get();
            Elements elements =  document.getElementsByClass("tin_box_right_text");
            for (Element element: elements){
                Map<String, Object> m = new HashMap<>();
                m.put("link", element.child(0).attr("href"));
                list.add(m);
            }
            result.put("links", list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.put("name", "Tu");
        return result;
    }

    @RequestMapping(value = "/m", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Map<String, ? extends Object> faceDetect(){
        Map<String, Object> result = new HashMap<>();
        HttpClient httpclient = HttpClients.createDefault();
        try
        {
            URIBuilder builder = new URIBuilder(endpoint + "detect");

            builder.setParameter("returnFaceId", "true");
            builder.setParameter("returnFaceLandmarks", "false");
            builder.setParameter("returnFaceAttributes", "age");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", key);


            // Request body
            StringEntity reqEntity = new StringEntity("{url:\"https://znews-photo.zadn.vn/w660/Uploaded/jopluat/2018_12_27/Quang_Hai_1_zing.jpg\"}");
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
            String output = "";
            output = bufferedReader.readLine();
            JSONArray jsonArray = new JSONArray(output);
            result.put("face", jsonArray.getJSONObject(0).get("faceId"));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
