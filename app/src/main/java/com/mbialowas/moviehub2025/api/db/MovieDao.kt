package com.mbialowas.moviehub2025.api.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mbialowas.moviehub2025.api.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMovies(movies: List<Movie>)
    // ROOM @annotations create SQL like statements on your behalf
    // ie. INSERT INTO Movie(id,name, description....)

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieById(id: Int): Movie?

    //@Query("DELETE FROM movies WHERE id = :id")
    @Delete
    fun deleteMovieById(movie: Movie)

    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<Movie>

    @Query("UPDATE movies SET title = :newTitle, overview = :newDescription WHERE id = :movieId")
    suspend fun updateMovie(movieId: Int, newTitle: String, newDescription: String)

}