package com.ratanapps.movieexplorer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.ratanapps.movieexplorer.MainActivity;
import com.ratanapps.movieexplorer.R;
import com.ratanapps.movieexplorer.activity.moviedetail;
import com.ratanapps.movieexplorer.extras.keys;
import com.ratanapps.movieexplorer.extras.moviedetailinfo;
import com.ratanapps.movieexplorer.extras.movieinfo;
import com.ratanapps.movieexplorer.network.VolleySingleton;
import com.ratanapps.movieexplorer.network.url;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivam on 25-Mar-17.
 */

public class rec_adapter extends RecyclerView.Adapter<rec_adapter.myviewholder>
{
    LayoutInflater layoutInflater=null;
    Context context=null;
    private List<moviedetailinfo> movieinfoList=new ArrayList<>();
    public rec_adapter(Context context)
    {
      this.context=context;
    }

    public void setMovieinfoList(List<moviedetailinfo> movieinfoList)
    {
        this.movieinfoList=movieinfoList;
        notifyItemRangeChanged(0,movieinfoList.size());
    }


    @Override
    public myviewholder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.rec_movie_item,parent,false);

        myviewholder holder=new myviewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final myviewholder holder, int position)
    {
       // get item from JSON and stick it in RecyclerView
        final moviedetailinfo movie=movieinfoList.get(position);
        holder.movie_title.setText(movie.getTitle());
        holder.movie_release.setText(movie.getReleased());
        holder.movie_director.setText(movie.getDirector());

         String image_URL=movie.getPoster();
        if(image_URL.equals(keys.NA))
           image_URL= url.THUMBNAIL_PLACEHOLDER_URL;

        holder.iv_thumbnail.layout(0,0,0,0);
        Glide.with(context).load(image_URL).into(holder.iv_thumbnail);

        final String temp_imageURL=image_URL;
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,moviedetail.class);
                intent.putExtra(moviedetail.MOVIE_DETAIL,movie);
                intent.putExtra(moviedetail.IMAGE_URL,temp_imageURL);

                ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation((MainActivity)context,holder.iv_thumbnail,"poster");
               // ActivityOptionsCompat.makeSceneTransitionAnimation()

                context.startActivity(intent,optionsCompat.toBundle());


            }
        });
    }

    @Override
    public int getItemCount()
    {
        return movieinfoList.size();
    }

    static class myviewholder extends RecyclerView.ViewHolder
    {
        View mView=null;
        ImageView iv_thumbnail=null;
        TextView movie_title=null;
        TextView movie_director=null;
        TextView movie_release=null;
        public myviewholder(View itemView)
        {
            super(itemView);
            mView=itemView;
            iv_thumbnail=(ImageView)itemView.findViewById(R.id.moview_thumbnail);
            movie_title=(TextView)itemView.findViewById(R.id.movieTitle);
            movie_director=(TextView)itemView.findViewById(R.id.movie_director);
            movie_release=(TextView)itemView.findViewById(R.id.movie_year);
        }
    }

}
