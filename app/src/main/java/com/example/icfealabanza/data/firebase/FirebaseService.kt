package com.example.icfealabanza.data.firebase

import android.util.Log
import com.example.icfealabanza.domain.models.Reunion
import com.example.icfealabanza.domain.models.SongListItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseService @Inject constructor(private val rDatabase: FirebaseDatabase) {

    fun getReus(onSuccess: (List<Reunion>) -> Unit, onError: (Exception) -> Unit) {
        val ref = rDatabase.getReference("reuniones")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reuniones = mutableListOf<Reunion>()
                for (reuSnapshot in snapshot.children) {
                    val reu = reuSnapshot.getValue(Reunion::class.java)
                    if (reu?.id == null) reu?.id = reuSnapshot.key
                    if (reu != null) reuniones.add(reu)
                }
                onSuccess(reuniones)
            }
            override fun onCancelled(error: DatabaseError) {
                onError(Exception(error.message))
            }
        })

    }

    suspend fun saveReu(reu: Reunion): Boolean {
        val ref = rDatabase.reference.child("reuniones")
        val task = ref.push().setValue(reu)
        task.await()
        return task.isSuccessful
    }

    fun getReuById(id: String, onSuccess: (Reunion) -> Unit, onError: (Exception) -> Unit) {
        val ref = rDatabase.getReference("reuniones").child(id)
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reu = snapshot.getValue(Reunion::class.java)
                if (reu != null) onSuccess(reu)
                else onError(Exception("Reunion no encontrada"))
            }
            override fun onCancelled(error: DatabaseError) {
                onError(error.toException())
            }
        }
        ref.addValueEventListener(listener)
    }

    suspend fun deleteReu(id: String, onSuccess: (String) -> Unit, onError: (Exception) -> Unit) {
        val ref = rDatabase.getReference("reuniones").child(id)
        try {
            ref.removeValue().await()
            onSuccess("La reu se eliminó correctamente")
        } catch (e: Exception) {
            onError(e)
        }
    }

    fun addTrackToReu(idReu: String, track: SongListItem,
                      onErrorS: (String) -> Unit, onError: (Exception) -> Unit,
                      onSuccess: (String) -> Unit) {
        val ref = rDatabase.getReference("reuniones").child(idReu)
        val transactionHandler = object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val reu = currentData.getValue(Reunion::class.java) ?: return Transaction.success(currentData)
                if (reu.tracks.contains(track)) {
                    Log.e("YA EXISTE LA CANCON", "YA EXISTE LA CANCION EN LA REU")
                    return Transaction.abort()
                }
                else reu.tracks.add(track)
                currentData.value = reu
                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (error != null) {
                    onError(error.toException())
                    Log.e("FIREBASE ERROR", error.message)
                }
                else if (currentData == null) {
                    onErrorS("La reu no existe")
                    Log.e("NO EXISTE", "NO EXISTE REU")
                }
                else if (!committed) {
                    onErrorS("La cancion ya existe en la reu")
                }
                else onSuccess("La cancion fue agregada con exito")
            }

        }
        ref.runTransaction(transactionHandler)
    }

    fun deleteTrackFromReu(idReu: String, track: SongListItem, onSuccess: (String) -> Unit,
                           onErrorS: (String) -> Unit, onError: (Exception) -> Unit) {
        val ref = rDatabase.getReference("reuniones").child(idReu)
        val transactionHandler = object : Transaction.Handler {
            override fun doTransaction(currentData: MutableData): Transaction.Result {
                val reu = currentData.getValue(Reunion::class.java) ?: return Transaction.success(currentData)
                if (!reu.tracks.contains(track)) {
                    onErrorS("La cancion no esta en la reu")
                    return Transaction.abort()
                }
                reu.tracks.remove(track)
                currentData.value = reu
                return Transaction.success(currentData)
            }

            override fun onComplete(
                error: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                if (error != null) onError(error.toException())
                else if (currentData == null) onErrorS("La reu no existe")
                else onSuccess("La cancion fue eliminada de la reu")
            }
        }
        ref.runTransaction(transactionHandler)
    }

    fun getReusSingle(onSuccess: (List<Reunion>) -> Unit, onError: (Exception) -> Unit) {
       val ref = rDatabase.getReference("reuniones")
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reus = mutableListOf<Reunion>()
                for (childSnapshot in snapshot.children) {
                    val reu = childSnapshot.getValue(Reunion::class.java)
                    if (reu?.id == null) reu?.id = childSnapshot.key
                    if (reu != null) reus.add(reu)
                }
                onSuccess(reus)
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.toException())
            }
        }
        ref.addListenerForSingleValueEvent(listener)
    }
}