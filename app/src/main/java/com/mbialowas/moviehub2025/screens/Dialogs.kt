package com.mbialowas.moviehub2025.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mbialowas.moviehub2025.api.model.Movie

@Composable
fun DeleteMovieDialog(
    movie: Movie?,
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    if (movie != null) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Delete Movie") },
            text = { Text("Are you sure you want to delete ${movie.title}?") },
            confirmButton = {
                TextButton(onClick = { onConfirmDelete() })
                {
                    Text(text = "Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel", color = Color.Green)
                }

            }
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMovieDialog(
    movie: Movie?,
    onDismiss: () -> Unit,
    onConfirmEdit: (String, String) -> Unit // Pass new title & description
) {
    if (movie != null) {
        var newTitle by remember { mutableStateOf(movie.title) }
        var newDescription by remember { mutableStateOf(movie.overview) }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Edit Movie") },
            text = {
                Column {
                    Text(text = "Update movie details:")
                    Spacer(modifier = Modifier.height(8.dp))

                    // Title input field
                    newTitle?.let {
                        OutlinedTextField(
                            value = it,
                            onValueChange = { newTitle = it },
                            label = { Text("Title") }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Description input field
                    newDescription?.let {
                        OutlinedTextField(
                            value = it,
                            onValueChange = { newDescription = it },
                            label = { Text("Description") }
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { newTitle?.let { newDescription?.let { it1 ->
                    onConfirmEdit(it,
                        it1
                    )
                } } }) {
                    Text("Save", color = Color.Green)
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text("Cancel")
                }
            }
        )
    }
}


