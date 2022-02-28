package edu.sabanciuniv.egeerdoganhomework3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ProgressDialog prgDialog;
    RecyclerView newsRecView;
    List<NewsItem> data;
    NewsAdapter adp;
    Spinner spin;
    List<Spinner_category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRecView = findViewById(R.id.newsrec);
        spin = findViewById(R.id._dynamic);
        categories = new ArrayList<>();
        Spinner_category all = new Spinner_category("ALL", -1);
        categories.add(all);
        ArrayAdapter<Spinner_category> adp2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        spin.setAdapter(adp2);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner_category category = (Spinner_category) spin.getSelectedItem();
                NewsTask tsk_spin = new NewsTask();
                if(category.getId() == -1)
                {
                    NewsTask tsk = new NewsTask();
                    tsk.execute("http://94.138.207.51:8080/NewsApp/service/news/getall");
                }
                else{
                    String url = "http://94.138.207.51:8080/NewsApp/service/news/getbycategoryid/" + category.getId();
                    tsk_spin.execute(url);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setTitle("News");

        data = new ArrayList<>();
        adp = new NewsAdapter(data, this, new NewsAdapter.NewsItemClickListener() {
            @Override
            public void newItemClicked(NewsItem selectedNewsItem) {
                Toast.makeText(MainActivity.this, selectedNewsItem.getTitle(), Toast.LENGTH_SHORT).show();

                NewsItem news = new NewsItem(selectedNewsItem.getId(), selectedNewsItem.getTitle(),  selectedNewsItem.getText(), selectedNewsItem.getImagePath(), selectedNewsItem.getNewsDate());
                Intent i = new Intent(MainActivity.this, News_Details.class);
                i.putExtra("news", news);
                startActivity(i);
            }
        });
        newsRecView.setLayoutManager(new LinearLayoutManager(this));
        newsRecView.setAdapter(adp);

        NewsTask2 tsk2 = new NewsTask2();
        tsk2.execute("http://94.138.207.51:8080/NewsApp/service/news/getallnewscategories");
    }

    class NewsTask2 extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {

            String urlStr = strings[0];
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
            try {
                JSONObject obj = new JSONObject(s);

                if (obj.getInt("serviceMessageCode") == 1) {

                    JSONArray arr = obj.getJSONArray("items");

                    for (int i = 0; i < arr.length(); i++) {

                        JSONObject current = (JSONObject) arr.get(i);
                        Spinner_category item = new Spinner_category(current.getString("name"), current.getInt("id"));
                        categories.add(item);
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class NewsTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            prgDialog = new ProgressDialog(MainActivity.this);
            prgDialog.setTitle("Loading");
            prgDialog.setMessage("Please wait...");
            prgDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prgDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String urlStr = strings[0];
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

                    for (int i = 0; i < arr.length(); i++){

                        JSONObject current = (JSONObject) arr.get(i);

                        long date = current.getLong("date");
                        Date objDate = new Date(date);

                        NewsItem item = new NewsItem(current.getInt("id"),
                                current.getString("title"),
                                current.getString("text"),
                                current.getString("image"),
                                objDate
                                );
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
