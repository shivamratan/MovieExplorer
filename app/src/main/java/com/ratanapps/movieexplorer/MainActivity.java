package com.ratanapps.movieexplorer;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ratanapps.movieexplorer.adapter.rec_adapter;
import com.ratanapps.movieexplorer.extras.keys;
import com.ratanapps.movieexplorer.extras.moviedetailinfo;
import com.ratanapps.movieexplorer.extras.movieinfo;
import com.ratanapps.movieexplorer.fragment.nowplaying;
import com.ratanapps.movieexplorer.fragment.popular;
import com.ratanapps.movieexplorer.fragment.toprated;
import com.ratanapps.movieexplorer.fragment.upcoming;
import com.ratanapps.movieexplorer.network.VolleySingleton;
import com.ratanapps.movieexplorer.network.url;
import com.ratanapps.movieexplorer.util.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle toggle=null;
    private DrawerLayout drawerLayout=null;
    private EditText et_movie_searcher=null;
    private ProgressBar progressBar=null;
    private RecyclerView recyclerView=null;
    private RequestQueue requestQueue=null;
    private rec_adapter recyclerAdapter=null;
    private String MOVIE_TYPE=url.TYPE_MOVIE;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar=(ProgressBar)findViewById(R.id.progress_spinner);
        et_movie_searcher=(EditText)findViewById(R.id.et_query);
        final ListView listView=(ListView)findViewById(R.id.mylistview);
        final Toolbar toolbar=(Toolbar)findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Movie Explorer");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout=(DrawerLayout)findViewById(R.id.activity_main);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        final FrameLayout fragmentContainer=(FrameLayout)findViewById(R.id.fragment_container);
        final LinearLayout searchPane=(LinearLayout)findViewById(R.id.searchBAR_layout);
        final FrameLayout groundFrame=(FrameLayout)findViewById(R.id.groundFrame);

        final FragmentManager fragmentManager=getSupportFragmentManager();

        listView.setSelection(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                listView.setSelection(position);
                switch (position)
                {
                    case 0:
                        toolbar.setTitle(R.string.app_name);
                        fragmentContainer.setVisibility(View.GONE);
                        searchPane.setVisibility(View.VISIBLE);
                        groundFrame.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        toolbar.setTitle("Top Rated Movies");
                        fragmentContainer.setVisibility(View.VISIBLE);
                        searchPane.setVisibility(View.GONE);
                        groundFrame.setVisibility(View.GONE);
                        FragmentTransaction transaction=fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragment_container,new toprated());
                        transaction.commit();
                        break;
                    case 2:
                        toolbar.setTitle("Upcoming Movies");
                        fragmentContainer.setVisibility(View.VISIBLE);
                        searchPane.setVisibility(View.GONE);
                        groundFrame.setVisibility(View.GONE);
                        FragmentTransaction transaction2=fragmentManager.beginTransaction();
                        transaction2.replace(R.id.fragment_container,new upcoming());
                        transaction2.commit();
                        break;
                    case 3:
                        toolbar.setTitle("Now Playing Movies");
                        fragmentContainer.setVisibility(View.VISIBLE);
                        searchPane.setVisibility(View.GONE);
                        groundFrame.setVisibility(View.GONE);
                        FragmentTransaction transaction3=fragmentManager.beginTransaction();
                        transaction3.replace(R.id.fragment_container,new nowplaying());
                        transaction3.commit();
                        break;
                    case 4:
                        toolbar.setTitle("Popular Movies");
                        fragmentContainer.setVisibility(View.VISIBLE);
                        searchPane.setVisibility(View.GONE);
                        groundFrame.setVisibility(View.GONE);
                        FragmentTransaction transaction4=fragmentManager.beginTransaction();
                        transaction4.replace(R.id.fragment_container,new popular());
                        transaction4.commit();
                        break;

                }

                drawerLayout.closeDrawer(GravityCompat.START);

            }
        });

       /* ImageButton btn_search=(ImageButton)findViewById(R.id.imageButton_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this,"search button is Clicked !!",Toast.LENGTH_SHORT).show();
            }
        });*/

        requestQueue = VolleySingleton.getInstance().getRequestQueue();

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerAdapter=new rec_adapter(MainActivity.this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setItemAnimator(null);
        StaggeredGridLayoutManager gridLayoutManager=new StaggeredGridLayoutManager(getResources().getInteger(R.integer.grid_column_count),StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(gridLayoutManager);



        final String type_spinner[]={url.LABEL_TYPE_MOVIE,url.LABEL_TYPE_SERIES};
        final String url_param_arr_type[]={url.TYPE_MOVIE,url.TYPE_SERIES};
        Spinner spinner = (Spinner)findViewById(R.id.spinner_movietype);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.custom_spinner_textview,type_spinner);
       // ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,type_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText(MainActivity.this,type_spinner[position]+"Selected !",Toast.LENGTH_LONG).show();
                MOVIE_TYPE=url_param_arr_type[position];

                switch (position)
                {
                    case 0:
                        et_movie_searcher.setHint("Search Movie");
                        break;
                    case 1:
                        et_movie_searcher.setHint("Search Series");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed()
    {
        if(drawerLayout!=null&&drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.option_menu,menu);

       /* String type_spinner[]={url.TYPE_MOVIE,url.TYPE_SERIES};
        MenuItem item=menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,type_spinner);
      //  ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,type_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_setting:
                Toast.makeText(this,"Insert setting item ",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_exit:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                                    builder.setMessage("Are you sure want to exit the app")
                                            .setTitle("Warning")
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            finish();
                                                        }
                                                    })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                     builder.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void searchMovie(View view)
    {

      if(CommonUtils.isNetworkAvailable(this)) {
          CommonUtils.hideKeyBoard(this);

          final String query = et_movie_searcher.getText().toString().trim();

          if(!query.isEmpty())
          {

              progressBar.setVisibility(View.VISIBLE);


              final String linkurl = url.OMDB_LINK + "/?type="+MOVIE_TYPE+"&s="+query;
              JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,linkurl, null, new Response.Listener<JSONObject>() {
                  @Override
                  public void onResponse(JSONObject response) {

                     // Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                      movieinfo.searchResult mysearchresult=parseJSONResponse(response);

                     if(mysearchresult!=null )
                     {
                         recyclerView.setVisibility(View.GONE);
                         new loadmovie_detailfromserverasync(mysearchresult).execute();
                     }
                     else
                     {
                         progressBar.setVisibility(View.GONE);
                         Snackbar.make(recyclerView,"Result Not Found !!!",Snackbar.LENGTH_LONG).show();
                     }




                      //for debugging purpose only
                      if (url.DEBUG_FLAG) {
                          StringBuilder builder = new StringBuilder();
                          List<movieinfo.Movie> collect = mysearchresult.getMoviecollection();
                          for (int i = 0; i < collect.size(); i++)
                          {
                              movieinfo.Movie temp=collect.get(i);
                              builder.append(temp.title+"\n");
                          }
                          Toast.makeText(MainActivity.this, builder.toString(), Toast.LENGTH_LONG).show();
                          Log.e("RESPONSE_JSON", response.toString());
                      }



                  }

                  //custom method for parsing JSON response from OMDB server
                  public movieinfo.searchResult parseJSONResponse(JSONObject jsonObject)
                  {

                      movieinfo.searchResult mysearchResult=null;
                      List<movieinfo.Movie> moviecollection=new ArrayList<>();

                      String response = keys.NA;
                      //  if(jsonObject.has(keys.primary.MOVIE_RESPONSE))
                      try {
                          response = jsonObject.getString(keys.primary.MOVIE_RESPONSE);
                      } catch (JSONException e) {
                          e.printStackTrace();
                      }


                      if (response.equals(movieinfo.responseFlag.FLAG_TRUE)) {
                      try {


                              JSONArray jsonArray = jsonObject.getJSONArray(keys.primary.SEARCH_OBJ);

                              for (int i = 0; i < jsonArray.length(); i++) {
                                  JSONObject movie_jsonObj = jsonArray.getJSONObject(i);

                                  String title = keys.NA;
                                  if (movie_jsonObj.has(keys.primary.MOVIE_TITLE))
                                      title = movie_jsonObj.getString(keys.primary.MOVIE_TITLE);

                                  String year = keys.NA;
                                  if (movie_jsonObj.has(keys.primary.MOVIE_RELEASE))
                                      year = movie_jsonObj.getString(keys.primary.MOVIE_RELEASE);

                                  String imdbID = keys.NA;
                                  if (movie_jsonObj.has(keys.primary.IMDB_ID))
                                      imdbID = movie_jsonObj.getString(keys.primary.IMDB_ID);

                                  String type = keys.NA;
                                  if (movie_jsonObj.has(keys.primary.MOVIE_TYPE))
                                      type = movie_jsonObj.getString(keys.primary.MOVIE_TYPE);

                                  String poster_link = keys.NA;
                                  if (movie_jsonObj.has(keys.primary.MOVIE_POSTER_LINK))
                                      poster_link = movie_jsonObj.getString(keys.primary.MOVIE_POSTER_LINK);

                                  if (url.DEBUG_FLAG)
                                      Log.e("movie_srch_content", title + "##" + year + "##" + imdbID + "##" + type + "##" + poster_link);

                                  moviecollection.add(new movieinfo.Movie(title, year, imdbID, type, poster_link));
                              }

                              String totalresult = keys.NA;
                              if (jsonObject.has(keys.primary.MOVIE_TOTAL_RESULT))
                                  totalresult = jsonObject.getString(keys.primary.MOVIE_TOTAL_RESULT);


                              mysearchResult = new movieinfo.searchResult(moviecollection, totalresult, response);

                              //loading movieDetails from OMDB server


                          }catch(JSONException e){
                              e.printStackTrace();
                          }
                      }

                      return mysearchResult;
                  }


              }, new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError error) {
                      Log.e("Volley_ERROR", error.toString());
                  }
              })
              {

                 /* @Override
                  protected Map<String, String> getParams() throws AuthFailureError
                  {
                      Map<String,String> params=new HashMap<>();
                      params.put("type","movie");
                      params.put("s",query);
                      return params;
                  }*/
              };

              requestQueue.add(jsonObjectRequest);
          }
          else
          {
              Snackbar.make(recyclerView,R.string.input_error,Snackbar.LENGTH_LONG).show();
          }
      }
      else
      {
          Snackbar.make(recyclerView,R.string.network_error,Snackbar.LENGTH_LONG).show();
      }

    }


    class loadmovie_detailfromserverasync extends AsyncTask<String,String,Void>
    {

        movieinfo.searchResult mysearchResult__=null;
        final List<moviedetailinfo> mymoviedetailCollection=new ArrayList<>();

        public loadmovie_detailfromserverasync(movieinfo.searchResult mysearchResult__)
        {
            this.mysearchResult__=mysearchResult__;
        }


        @Override
        protected Void doInBackground(String... params) {

            loadmovieDetailfromServer(mysearchResult__);

            while(mymoviedetailCollection.size()!=mysearchResult__.getMoviecollection().size());

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            recyclerAdapter.setMovieinfoList(mymoviedetailCollection);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

             if(url.DEBUG_FLAG)
             {
                 StringBuilder builder = new StringBuilder();
                for (int i = 0; i < mymoviedetailCollection.size(); i++) {
                   builder.append(mymoviedetailCollection.get(i).Director + "\n");
                 }
                 Toast.makeText(MainActivity.this, builder.toString(), Toast.LENGTH_LONG).show();
            }

               // Toast.makeText(MainActivity.this, "Task Completed !!", Toast.LENGTH_LONG).show();
          //  }
        }

        public void loadmovieDetailfromServer(movieinfo.searchResult mysearchResult)
        {

          List<movieinfo.Movie> moviecollection=mysearchResult.getMoviecollection();
          final JsonObjectRequest jsonObjectRequest[]=new JsonObjectRequest[moviecollection.size()];



       int size=moviecollection.size();
        for(int i=0;i<size;i++)
        {
            movieinfo.Movie movieitem=moviecollection.get(i);
            final String query_url=url.OMDB_LINK+"/?plot=short&i="+movieitem.imdbId;
            jsonObjectRequest[i]=new JsonObjectRequest(Request.Method.POST, query_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    if(response!=null) {
                        parsemovieDetailJSON(response);
                    }
                }

                public void parsemovieDetailJSON(JSONObject response)
                {


                    String key_arr[]={
                            keys.secondary.MOV_TITLE,
                            keys.secondary.MOV_year,
                            keys.secondary.MOV_rated,
                            keys.secondary.MOV_REL_DATE,
                            keys.secondary.MOV_LENGTH,
                            keys.secondary.MOV_GENRE,
                            keys.secondary.MOV_DIRECTOR,
                            keys.secondary.MOV_WRITER,
                            keys.secondary.MOV_ACTORS,
                            keys.secondary.MOV_PLOT,
                            keys.secondary.MOV_LANGUAGE,
                            keys.secondary.MOV_COUNTRY,
                            keys.secondary.MOV_AWARDS,
                            keys.secondary.MOV_POSTER_LINK,
                            keys.secondary.MOV_METASCORE,
                            keys.secondary.MOV_IMDB_RATING,
                            keys.secondary.MOV_IMDB_VOTES,
                            keys.secondary.MOV_IMDB_ID,
                            keys.secondary.MOV_TYPE,
                            keys.secondary.MOV_RESPONSE  };

                    String value_arr[]=new String[key_arr.length];

                    StringBuilder mybuilder=new StringBuilder();

                    try {

                       for(int j=0;j<key_arr.length;j++)
                       {
                           value_arr[j]=keys.NA;
                           if(response.has(key_arr[j]))
                               value_arr[j]=response.getString(key_arr[j]);

                          if(url.DEBUG_FLAG)
                           mybuilder.append(value_arr[j]+" ## ");
                       }

                        if(url.DEBUG_FLAG)
                        Log.e("SECONDARY_RESPONSE",mybuilder.toString());


                        mymoviedetailCollection.add(new moviedetailinfo(value_arr[0],value_arr[1],value_arr[2],value_arr[3],value_arr[4],
                                                                        value_arr[5],value_arr[6],value_arr[7],value_arr[8],value_arr[9],
                                                                        value_arr[10],value_arr[11],value_arr[12],value_arr[13],value_arr[14],
                                                                        value_arr[15],value_arr[16],value_arr[17],value_arr[18],value_arr[19]));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    error.printStackTrace();
                }
            });

            requestQueue.add(jsonObjectRequest[i]);

        }
    }

     }

}
