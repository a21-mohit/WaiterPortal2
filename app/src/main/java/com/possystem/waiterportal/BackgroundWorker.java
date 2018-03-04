package com.possystem.waiterportal;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

public class BackgroundWorker extends AsyncTask<String,Void,ArrayList<String>> {

    Context context;
    BackgroundWorker(Context ctx) {
        context = ctx;
        }

        @Override
    protected ArrayList<String> doInBackground(String... params) {
        String type = params[0];
        if (params[0].contains("Table")){
            type = "send";
        }
        final String managerIP = "192.168.0.10";
        final int managerPort = 9765;
        final String restAPIip = "192.168.0.10";
        final String login_url = "http://"+restAPIip+"/restaurant/login.php";
        final String category_url="http://"+restAPIip+"/restaurant/category.php";
        final String menuitem_url="http://"+restAPIip+"/restaurant/menuitem.php";
        ArrayList<String> result;
        if(type.equals("login")) {
            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setConnectTimeout(3000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                result = new ArrayList<>();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result.add(line.trim());
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (SocketTimeoutException e) {
                return null;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(type.equals("send")){
            try {
                ArrayList<String> orderarray = new ArrayList<>( Arrays.asList( params ));
                SocketAddress sockaddr = new InetSocketAddress(managerIP, managerPort);
                Socket s = new Socket();
                int timeoutMs = 2000;
                s.connect(sockaddr, timeoutMs);
                OutputStreamWriter os = new OutputStreamWriter(s.getOutputStream());
                for (int i = 0; i < orderarray.size(); i++) {
                    os.write(orderarray.get(i) + "\n");
                }
                os.write("DONE\n");
                os.flush();
                s.close();
                result = new ArrayList<>();
                result.add("success");
                return result;
            } catch (IOException e) {
                result = new ArrayList<>();
                result.add("exception");
                return result;
                //e.printStackTrace();
            }
        }
            if(type.equals("category")) {
                try {
                    URL url = new URL(category_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoInput(true);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    result = new ArrayList<String>();
                    String jsonStr = bufferedReader.readLine();
                    if(jsonStr != null){
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray categories = jsonObj.getJSONArray("categories");
                        for (int i = 0; i < categories.length(); i++) {
                            result.add(categories.getString(i));
                        }
                    }
                    else {
                        throw new IOException();
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(type.equals("menuitem")) {
                try {
                    String category=params[1];
                    URL url = new URL(menuitem_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setConnectTimeout(3000);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("category","UTF-8")+"="+URLEncoder.encode(category,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    result = new ArrayList<String>();
                    String jsonStr = bufferedReader.readLine();
                    if(jsonStr != null){
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONArray menu = jsonObj.getJSONArray(category);
                        for (int i = 0; i < menu.length(); i++) {
                            result.add(menu.getString(i));
                        }
                    }
                    else {
                        throw new IOException();
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (SocketTimeoutException e) {
                    return null;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
