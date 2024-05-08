package onepiece.whitebeard.movieappkotlin.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies

@Dao
interface MoviesDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertOrUpdateMovies(movies: PopularMovies)

    @Delete
    suspend fun deleteMovieFromDatabase(movies: PopularMovies)

    @Query("SELECT * FROM movies_db")
    fun getAllPopularMoviesFromDatabase(): LiveData<List<PopularMovies>>
}