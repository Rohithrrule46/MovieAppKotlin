package onepiece.whitebeard.movieappkotlin.dependencyinjection

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import onepiece.whitebeard.movieappkotlin.api.ApiInterface
import onepiece.whitebeard.movieappkotlin.api.RetrofitInstance
import onepiece.whitebeard.movieappkotlin.database.MoviesDao
import onepiece.whitebeard.movieappkotlin.database.MoviesDatabase
import onepiece.whitebeard.movieappkotlin.repository.FakeMoviesRepository
import onepiece.whitebeard.movieappkotlin.repository.MovieRepositoryProtocol
import onepiece.whitebeard.movieappkotlin.repository.MoviesRepositoryImpl
import javax.inject.Named
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideMovieRepository() = FakeMoviesRepository() as MovieRepositoryProtocol

    @Provides
    @Singleton
    fun provideMoviesDao(database: MoviesDatabase) = database.getMoviesDao()

    @Provides
    @Singleton
    fun provideRetrofitApi(): ApiInterface {
        return RetrofitInstance.api
    }

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext context: Context) =
        Room.inMemoryDatabaseBuilder(context, MoviesDatabase::class.java)
            .allowMainThreadQueries()
            .build()

}


