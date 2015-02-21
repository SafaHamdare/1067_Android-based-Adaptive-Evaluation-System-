package aes.example.com.aes;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suraj on 15-01-2015.
 */
public class ApiConnector {
    String result = "";
    HttpClient httpClient;
    HttpPost httpPost;
    String url = "";
    HttpResponse httpResponse;
    String responseText;

    public String registerUser(String name, String username, String password)
    {
        try {
            url = "http://adaptivetest.tk/aesmob/newuser.php";
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("name" , name));
            nameValuePairs.add(new BasicNameValuePair("Email_ID" , username));
            nameValuePairs.add(new BasicNameValuePair("Password" , password));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse = httpClient.execute(httpPost);
            responseText = EntityUtils.toString(httpResponse.getEntity());
            //responseText = responseText.substring(0,20);
            JSONObject jobject = new JSONObject(responseText);
            responseText = jobject.getString("message");
            return responseText;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getMessage();
        }


    }

    public String checkLogin(String email, String password)
    {
        try {
            url = "http://adaptivetest.tk/aesmob/login1.php";
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            nameValuePairs.add(new BasicNameValuePair("Email_ID" , email));
            nameValuePairs.add(new BasicNameValuePair("Password" , password));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse = httpClient.execute(httpPost);
            responseText = EntityUtils.toString(httpResponse.getEntity());
            //responseText = responseText.substring(0,20);
            JSONObject jobject = new JSONObject(responseText);
            responseText = jobject.getString("message");
            return responseText;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    public JSONObject fetchQuestion(String level, String email, String IsCorrect, String total, String test_ID)
    {
        JSONObject resultJson = new JSONObject();
        try {
            url = "http://adaptivetest.tk/aesmob/quants.php";
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

            nameValuePairs.add(new BasicNameValuePair("Email_ID" , email));
            nameValuePairs.add(new BasicNameValuePair("level" , level));
            nameValuePairs.add(new BasicNameValuePair("IsCorrect" , IsCorrect));
            nameValuePairs.add(new BasicNameValuePair("total" , total));
            nameValuePairs.add(new BasicNameValuePair("Test_ID", test_ID));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse = httpClient.execute(httpPost);
            responseText = EntityUtils.toString(httpResponse.getEntity());
            resultJson = new JSONObject(responseText);
            return resultJson;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return  resultJson;

    }

    public JSONArray getTestId(String email)
    {
        JSONArray resultJson = new JSONArray();
        try {
            url = "http://adaptivetest.tk/aesmob/report.php";
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);

            nameValuePairs.add(new BasicNameValuePair("Email_ID" , email));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse = httpClient.execute(httpPost);
            responseText = EntityUtils.toString(httpResponse.getEntity());
            Log.i("response" , responseText);
            resultJson = new JSONArray(responseText);

            return resultJson;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return  resultJson;

    }

    public JSONArray getReport(String email, String test_ID)
    {
        JSONArray resultJson = new JSONArray();
        try {
            url = "http://adaptivetest.tk/aesmob/report1.php";
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);

            nameValuePairs.add(new BasicNameValuePair("Email_ID" , email));
            nameValuePairs.add(new BasicNameValuePair("Test_ID" , test_ID));

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse = httpClient.execute(httpPost);
            responseText = EntityUtils.toString(httpResponse.getEntity());
            Log.i("response" , responseText);
            resultJson = new JSONArray(responseText);

            return resultJson;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return  resultJson;

    }
}
