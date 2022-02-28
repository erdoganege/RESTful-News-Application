package edu.sabanciuniv.egeerdoganhomework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CommentSection extends AppCompatActivity {

    ProgressDialog prgDialog;
    RecyclerView recViewComments;
    static List<CommentItem> data;
    @SuppressLint("StaticFieldLeak")
    static AdapterRecComment adp;
    String news_id;
    CommentTask tsk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_section);
        setTitle("Comments");
        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);
        currentBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24px);

        recViewComments = findViewById(R.id.recViewComments);
        news_id = (String) getIntent().getSerializableExtra("news_id");

        data = new ArrayList<>();

        adp = new AdapterRecComment(this, data);

        recViewComments.setLayoutManager(new LinearLayoutManager(this));

        recViewComments.setAdapter(adp);

        tsk = new CommentTask();
        tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getcommentsbynewsid", news_id);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comment_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        else if(item.getItemId() == R.id.add_comment)
        {
           Intent i = new Intent(CommentSection.this, Adding_comment.class);
           i.putExtra("news_id", news_id);
           startActivity(i);
           finish();
        }
        return true;
    }

    class CommentTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            prgDialog = new ProgressDialog(CommentSection.this);
            prgDialog.setTitle("Loading");
            prgDialog.setMessage("Please wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String id = strings[1];
            String urlStr = strings[0] + "/" + id;
            StringBuilder buffer = new StringBuilder();

            try {
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            data.clear();

            try {
                JSONObject obj = new JSONObject(s);

                if(obj.getInt("serviceMessageCode") == 1){
                    JSONArray arr = obj.getJSONArray("items");

                    for(int i = 0; i < arr.length(); i++){

                        JSONObject current = (JSONObject) arr.get(i);

                        CommentItem item = new CommentItem(current.getInt("id"), current.getString("name"), current.getString("text"));
                        data.add(item);
                    }
                }
                else{

                }
                adp.notifyDataSetChanged();
                prgDialog.dismiss();
            }

            catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
    }

