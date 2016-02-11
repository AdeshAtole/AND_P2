package com.adesh.popularmovies.db;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by adesh on 10/2/16.
 */

@ContentProvider(database = MovieDatabase.class, authority = MoviesProvider.AUTHORITY)
public class MoviesProvider {
    public static final String AUTHORITY = "com.adesh.popularmovies";

    @TableEndpoint(table = MovieDatabase.TABLE)
    public static class Movies {
        @ContentUri(path = "movies", type = "vnd.android.cursor.dir/movie", defaultSort = MovieColumns.TITLE + " ASC")
        public static final Uri MOVIES = Uri.parse("content://" + AUTHORITY + "/movies");

        //        @InexactContentUri(path = "")

        @InexactContentUri(path = "movies/#", name = "MOVIE_ID", type = "vnd.android.cursor.item/movie", whereColumn = MovieColumns._ID, pathSegment = 1)
        public static Uri withId(long id) {
            return Uri.parse("content://" + AUTHORITY + "/movies/" + id);
        }
    }
}
