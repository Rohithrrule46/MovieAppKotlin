package onepiece.whitebeard.movieappkotlin.model.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Entity(tableName = "movies_db" )
data class PopularMovies(
    val adult: Boolean?=false,
    val backdrop_path: String?="",
    val genre_ids: List<Int>?= listOf(),
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    val original_language: String?="",
    val original_title: String?="",
    val overview: String?="",
    val popularity: Double?=0.0,
    val poster_path: String?="",
    val release_date: String?="",
    val title: String?="",
    val video: Boolean?=false,
    val vote_average: Double?=0.0,
    val vote_count: Int?=0
) : Serializable