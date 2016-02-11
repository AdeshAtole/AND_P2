package com.adesh.popularmovies.db;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by adesh on 9/2/16.
 */
public interface MovieColumns {
    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    String _ID = "_id";


    @DataType(DataType.Type.TEXT)
    @NotNull
    String TITLE = "title";

    @DataType(DataType.Type.BLOB)
    @NotNull
    String POSTER = "poster";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String PLOT = "plot";

    @DataType(DataType.Type.REAL)
    @NotNull
    String RATING = "rating";

    @DataType(DataType.Type.REAL)
    @NotNull
    String POPULARITY = "popularity";

    @DataType(DataType.Type.TEXT)
    @NotNull
    String RELEASE_DATE = "releaseDate";


}
