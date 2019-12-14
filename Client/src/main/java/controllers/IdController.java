package controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.Id;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class IdController {
    private ArrayList<Id> idList = new ArrayList<>();
    private static IdController INSTANCE = new IdController();
    private static HttpURLConnection connection;
    private String httpUrl = "http://zipcode.rocks:8085/ids";



    public ArrayList<Id> getIds() {
        try{
            //url connections
            URL url =  new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();

            //request setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            //getting connection status
            int status = connection.getResponseCode();
            String line;
            String responseContent = "";
            BufferedReader reader;

            if(status > 299){
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while((line = reader.readLine()) != null){
                    responseContent += line;
                }
                reader.close();
            }
            else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    responseContent += line;
                }
                //converting json to java object
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                Id[] ids = objectMapper.readValue(responseContent, Id[].class);
                idList.addAll(Arrays.asList(ids));
                reader.close();
            }

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            connection.disconnect();
        }

        return this.idList;
    }

    public void postId(String name, String github) {
        try{
            URL url = new URL(httpUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");


            //json String // Object
            JSONObject user = new JSONObject();
            user.put("userid", "-");
            user.put("name", name);
            user.put("github", github);
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.write(user.toString().getBytes());
            outputStream.flush();

            Integer responseCode = connection.getResponseCode();

            BufferedReader bufferedReader;

            // Creates a reader buffer and POST actually posts it
            if (responseCode > 199 && responseCode < 300) {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            bufferedReader.close();

        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

    public Id putId(Id id) {
        return null;
    }
    public static IdController getInstance(){ return INSTANCE;}

}