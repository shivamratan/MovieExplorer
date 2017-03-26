package com.ratanapps.movieexplorer.network;

/**
 * Created by Shivam on 25-Mar-17.
 */

public interface url
{
    public final static String OMDB_LINK="http://www.omdbapi.com/";
    public final static String THUMBNAIL_PLACEHOLDER_URL="http://www.imdb.com/images/nopicture/medium/film.png";

    //parameter of base Url
    public final static String TYPE_MOVIE="movie";
    public final static String TYPE_SERIES="series";
    public final static String TYPE_EPISODE="episode";


    //label of base url
    public final static String LABEL_TYPE_MOVIE="Movie";
    public final static String LABEL_TYPE_SERIES="Series";
    public final static String LABEL_TYPE_EPISODE="Episode";

    //flag for debugging purpose of the project
    public final static boolean DEBUG_FLAG=false;
}

