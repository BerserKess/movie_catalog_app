package br.com.movie_catalog.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MovieDatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private  const val DATABASE_NAME = "movies.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_MOVIES = "movies"

        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_TRAILER = "trailer"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_YEAR = "year"
        private const val COLUMN_GENRE = "genre"
        private const val COLUMN_DURATION = "duration"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_MOVIES (
                 $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                 $COLUMN_TITLE TEXT,
                 $COLUMN_DESCRIPTION TEXT,
                 $COLUMN_TRAILER TEXT,
                 $COLUMN_IMAGE TEXT,
                 $COLUMN_YEAR INTEGER,
                 $COLUMN_GENRE INTEGER,
                 $COLUMN_DURATION INTEGER
            );
        """.trimIndent()

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIES")
        onCreate(db)
    }


}