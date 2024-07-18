package onepiece.whitebeard.movieappkotlin.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import onepiece.whitebeard.movieappkotlin.model.responses.PopularMovies

@Dao
interface MoviesDao {

    @Upsert
    suspend fun insertOrUpdateMovies(movies: PopularMovies)

    @Delete
    suspend fun deleteMovieFromDatabase(movies: PopularMovies)

    @Query("SELECT * FROM movies_db")
    fun getAllPopularMoviesFromDatabase(): LiveData<List<PopularMovies>>
}