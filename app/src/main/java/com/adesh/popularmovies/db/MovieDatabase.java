package com.adesh.popularmovies.db;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by adesh on 9/2/16.
 */
@Database(version = MovieDatabase.DATABASE_VERSION)
public class MovieDatabase {
    public static final int DATABASE_VERSION = 3;
    @Table(MovieColumns.class)
    public static final String TABLE = "movies";
}
