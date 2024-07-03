package com.example.icfealabanza.presentation.reusScreen

import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import com.example.icfealabanza.domain.models.Reunion
import com.example.icfealabanza.domain.use_cases.GetReusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReusViewModel @Inject constructor(
    private val getReusUseCase: GetReusUseCase,
    private val repositoryImpl: IcfeRepositoryImpl
): ViewModel() {

    var reus: MutableStateFlow<List<Reunion>?> = MutableStateFlow(null)
        private set

    var addModalOpen by mutableStateOf(false)
        private set

    private var snackBarText by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    fun toggleModal() {
        addModalOpen = !addModalOpen
    }
    fun getReus() {
        viewModelScope.launch {
            try {
                getReusUseCase(
                    onSuccess = { list ->
                        reus.value = list.reversed()
                    },
                    onError = { Log.e("Firebase error pero hizo consulta", it.message!!) }
                )
            } catch (e: Exception) {
                Log.e("ERROR FIREBASE", "ERROR FIREBASE")
            }
        }
    }



    fun saveReu(name: String, snackbarHostState: SnackbarHostState) {
        isLoading = true
        viewModelScope.launch {
           try {
                val result = repositoryImpl.saveReu(Reunion(name = name))
               snackBarText = if (result) {
                   "La reunion fue creada correctamente"
               } else {
                   "Hubo un error, intenta de nuevo"
               }
            } catch (e: Exception) {
                snackBarText = "Error de internet"
            }
            finally {
                Log.i("SNACKBAR", snackBarText)
                isLoading = false
                toggleModal()
                snackbarHostState.showSnackbar(snackBarText, "OK", duration = SnackbarDuration.Short)
            }
        }
    }
}