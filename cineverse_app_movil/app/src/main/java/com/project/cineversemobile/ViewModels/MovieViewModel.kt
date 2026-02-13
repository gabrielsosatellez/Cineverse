package com.project.cineversemobile.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.cineversemobile.API.RetrofitInstance
import com.project.cineversemobile.Data.Movie
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la carga y gestión del listado de películas.
 * Se comunica con el backend mediante Retrofit y expone el estado a la interfaz.
 */
class MovieViewModel(application: Application) : AndroidViewModel(application) {

    // Lista de películas disponibles
    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    // Indica si se está realizando una petición al servidor
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // Mensaje de error en caso de fallo al cargar las películas
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    init {
        loadMovies()
    }

    /**
     * Obtiene el listado de películas desde el backend.
     * Actualiza el estado de carga y maneja posibles errores de red.
     */
    fun loadMovies() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _movies.value = RetrofitInstance.movieApi.getAllMovies()
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Error al cargar películas"
            } finally {
                _isLoading.value = false
            }
        }
    }
}