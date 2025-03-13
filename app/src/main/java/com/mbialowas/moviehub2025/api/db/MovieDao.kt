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
    fun insertAll(movies: List<Movie>)
    // ksp encounters @Insert
    // generate SQL
    // INSERT INTO movies (select * from movies)

    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieById(id: Int): Movie?

    // Deleting a movie
    // DELETE from movies where ID = :id
    @Delete
    suspend fun delete(movie: Movie)

    // edit a movie
    @Query("UPDATE movies SET title = :title, overview = :overview WHERE id = :movieID")
    fun updateMovie(movieID: Int, title: String, overview: String)

    // retrieve all movies
    @Query("SELECT * FROM movies")
    fun getAllMovies(): List<Movie>


}