package edu.sabanciuniv.egeerdoganhomework3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class News_Details extends AppCompatActivity {

    TextView title;
    TextView date;
    ImageView image;
    TextView text;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news__details);
        setTitle("News Details");
        ActionBar currentBar = getSupportActionBar();
        currentBar.setHomeButtonEnabled(true);
        currentBar.setDisplayHomeAsUpEnabled(true);
        currentBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_24px);

        title = findViewById(R.id.titleee);
        date = findViewById(R.id.dateee);
        image = findViewById(R.id.imageee);
        text = findViewById(R.id.detailed_texttt);



        NewsItem news = (NewsItem)getIntent().getSerializableExtra("news");
        if(news.getBitmap() ==null){

            new ImageDowloadTask(image).execute(news);
        }
        else{
            image.setImageBitmap(news.getBitmap());
        }

        id = String.valueOf(news.getId());
        title.setText(news.getTitle());
        date.setText(new SimpleDateFormat("dd/MM/yyy").format(news.getNewsDate()));
        text.setText(news.getText());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        else if(item.getItemId() == R.id.comments)
        {

            ////ArrayList<CommentItem> comments = (ArrayList<CommentItem>) NewsItem.getCommentsByNewsId(id);
           Intent i = new Intent(News_Details.this, CommentSection.class);
           i.putExtra("news_id", id);
           startActivity(i);
            return true;
        }
        return true;
    }

}
