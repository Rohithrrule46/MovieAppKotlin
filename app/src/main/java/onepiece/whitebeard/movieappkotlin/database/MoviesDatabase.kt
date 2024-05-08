package onepiece.whitebeard.movieappkotlin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies

@Database(
    entities = [PopularMovies::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun getMoviesDao(): MoviesDao
}