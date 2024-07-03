package com.example.icfealabanza.presentation.reuDetailScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.icfealabanza.data.repository.IcfeRepositoryImpl
import com.example.icfealabanza.domain.models.Reunion
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReuDetailScreenViewModel @Inject constructor(
    private val repositoryImpl: IcfeRepositoryImpl
): ViewModel() {

    var reu by mutableStateOf<Reunion?>(null)
        private set

    fun getReuById(reuId: String) {
        repositoryImpl.getReuById(
            id = reuId,
            onSuccess = { reu = it },
            onError = { Log.e("ERROR FIREBASE", it.message!!) }
        )
    }
}