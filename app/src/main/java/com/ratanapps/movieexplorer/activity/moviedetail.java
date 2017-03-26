package com.ratanapps.movieexplorer.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ratanapps.movieexplorer.R;
import com.ratanapps.movieexplorer.extras.moviedetailinfo;

public class moviedetail extends AppCompatActivity {


    public final static String MOVIE_DETAIL="movie_detail";
    public final static String IMAGE_URL="image_url";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviedetail);


        TextView tv_title=(TextView)findViewById(R.id.grid_title);
        TextView tv_releasedate=(TextView)findViewById(R.id.grid_released);
        TextView tv_length=(TextView)findViewById(R.id.grid_runtime);
        TextView tv_genre=(TextView)findViewById(R.id.grid_genre);
        TextView tv_director=(TextView)findViewById(R.id.grid_director);
        TextView tv_writer=(TextView)findViewById(R.id.grid_writers);
        TextView tv_actors=(TextView)findViewById(R.id.grid_actors);
        TextView tv_plot=(TextView)findViewById(R.id.grid_plot);
        TextView tv_language=(TextView)findViewById(R.id.grid_language);
        TextView tv_country=(TextView)findViewById(R.id.grid_country);
        TextView tv_awards=(TextView)findViewById(R.id.grid_awards);
        TextView tv_imdbrating=(TextView)findViewById(R.id.grid_imdbRating);
        TextView tv_imdbvotes=(TextView)findViewById(R.id.grid_imdbVotes);

        final moviedetailinfo movie=getIntent().getParcelableExtra(MOVIE_DETAIL);
        final String imageurl=getIntent().getStringExtra(IMAGE_URL);

        Glide.with(this).load(imageurl).into((ImageView)findViewById(R.id.main_backdrop));

        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setTitle(movie.Title);

        Toolbar toolbar=(Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_title.setText(movie.getTitle());
        tv_releasedate.setText(movie.getReleased());
        tv_length.setText(movie.getRuntime());
        tv_genre.setText(movie.getGenre());
        tv_director.setText(movie.getDirector());
        tv_writer.setText(movie.getWriter());
        tv_actors.setText(movie.getActors());
        tv_plot.setText(movie.getPlot());
        tv_language.setText(movie.getLanguage());
        tv_country.setText(movie.getCountry());
        tv_awards.setText(movie.getAwards());
        tv_imdbrating.setText(movie.getImdbRating());
        tv_imdbvotes.setText(movie.getImdbVotes());
    }




}
