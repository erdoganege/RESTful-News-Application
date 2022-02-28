package edu.sabanciuniv.egeerdoganhomework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Adding_comment extends AppCompatActivity {

    ProgressDialog prgDialog2;
    EditText name;
    EditText message;
    String news_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_comment);
        setTitle("Post Comment");
        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);
        currentBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24px);
        name = findViewById(R.id.editText);
        message = findViewById(R.id.editText2);

        news_id = (String) getIntent().getSerializableExtra("news_id");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(Adding_comment.this, CommentSection.class);
            i.putExtra("news_id", news_id);
            startActivity(i);
            finish();
        }
        return true;
    }


    class JsonTask extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {

                StringBuilder stringBuilder = new StringBuilder();
                String urlStr = strings[0];
                String name = strings[1];
                String message = strings[2];
                String news_id = strings[3];

            JSONObject obj = new JSONObject();
            try {
                obj.put("name", name);
                obj.put("text", message);
                obj.put("news_id", news_id);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.connect();

                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(obj.toString());

                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    String line = "";
                    while((line = reader.readLine()) != null){
                        stringBuilder.append(line);
                    }
                }
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }


        @Override
        protected void onPostExecute(String s) {

            try {
                JSONObject inputObj = new JSONObject(s);

                if(inputObj.getInt("serviceMessageCode") == 0){
                    AlertDialog.Builder alert = new AlertDialog.Builder(Adding_comment.this);
                    alert.setMessage("WARNING");
                    alert.setPositiveButton("OKAY", null);
                    alert.show();
                }
                else{
                    prgDialog2 = new ProgressDialog(Adding_comment.this);
                    prgDialog2.setTitle("Loading");
                    prgDialog2.show();
                    Intent i = new Intent(Adding_comment.this, CommentSection.class);
                    i.putExtra("news_id", news_id);
                    startActivity(i);
                    finish();
                }

                String name = inputObj.getString("name");
                String message = inputObj.getString("text");
                int id = inputObj.getInt("news_id");
            }

            catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void ButtonClicked(View v){

        JsonTask tsk2 = new JsonTask();
        tsk2.execute("http://94.138.207.51:8080/NewsApp/service/news/savecomment", name.getText().toString(), message.getText().toString(), news_id);


        //CommentSection.CommentTask tsk = new CommentSection.CommentTask();
        //tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid", news_id);
    }

}
