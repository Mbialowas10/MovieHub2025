package com.mbialowas.moviehub2025.api.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mbialowas.moviehub2025.api.model.Movie

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllMovies(movies: List<Movie>)
    // ROOM @annotations create SQL like statements on your behalf
    // ie. INSERT INTO Movie(id,name, description....)

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieById(id: Int): Movie?

    // DELETE FROM movies WHERE id = :id
    // if you prefer writing native SQL
    // @Query("DELETE FROM movies WHERE id = :id")
    // fun deleteMovieById(id: Int)
    @Delete
    fun deleteMovie(movie: Movie)

    // EDIT a movie
    @Query("UPDATE movies SET title = :title, overview = :overview WHERE id = :movieID")
    suspend fun updateMovie(movieID: Int, title: String, overview: String)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<Movie>

}