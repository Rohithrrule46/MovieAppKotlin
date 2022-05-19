package onepiece.whitebeard.movieappkotlin.dependencyinjection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import onepiece.whitebeard.movieappkotlin.api.ApiInterface
import onepiece.whitebeard.movieappkotlin.api.RetrofitInstance
import onepiece.whitebeard.movieappkotlin.database.MoviesDao
import onepiece.whitebeard.movieappkotlin.database.MoviesDatabase
import onepiece.whitebeard.movieappkotlin.repository.MovieRepositotyInterface
import onepiece.whitebeard.movieappkotlin.repository.MoviesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideMovieDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, MoviesDatabase::class.java, "movies_db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

    @Singleton
    @Provides
    fun provideMovieRepository(
        dao: MoviesDao,
        api: ApiInterface
    ) = MoviesRepository(dao, api) as MovieRepositotyInterface

    @Singleton
    @Provides
    fun provideMoviesDao(database: MoviesDatabase) = database.getMoviesDao()

    @Singleton
    @Provides
    fun provideRetrofitApi(): ApiInterface {
        return RetrofitInstance.api
    }
}