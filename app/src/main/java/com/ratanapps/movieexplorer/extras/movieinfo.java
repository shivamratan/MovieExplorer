package com.ratanapps.movieexplorer.extras;

import java.util.List;

/**
 * Created by Shivam on 25-Mar-17.
 */

public class movieinfo
{

    public interface responseFlag
    {
        public final static String FLAG_TRUE="True";
        public final static String FLAG_FALSE="False";
    }

    public static class Movie
    {
        public String title=null;
        public String year=null;
        public String imdbId=null;
        public String type=null;
        public String poster_link=null;

        public Movie(String title,String year,String imdbId,String type,String poster_link)
        {
            this.title=title;
            this.year=year;
            this.imdbId=imdbId;
            this.type=type;
            this.poster_link=poster_link;
        }
    }


    public static class searchResult
    {

        private List<Movie> moviecollection=null;
        private String totalResult=null;
        private String response=null;

        public searchResult(List<Movie> moviecollection,String totalResult,String response)
        {
            this.moviecollection=moviecollection;
            this.totalResult=totalResult;
            this.response=response;
        }


        public List<Movie> getMoviecollection() {
            return moviecollection;
        }

        public void setMoviecollection(List<Movie> moviecollection) {
            this.moviecollection = moviecollection;
        }

        public String getTotalResult() {
            return totalResult;
        }

        public void setTotalResult(String totalResult) {
            this.totalResult = totalResult;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }


    }
}


