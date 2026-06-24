package com.example.pos_utfpr_usingdb.database

import com.example.pos_utfpr_usingdb.entity.Cadastro
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

//Lógica de CRUD de database no Firebase
class DatabaseFirebaseHandler {
    private val db = Firebase.firestore

    fun save(
        cadastro: Cadastro,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val collection = db.collection("cadastro")

        // Se o ‘id’ estiver vazio, gera um novo DocumentReference com ‘ID’ automático
        val docRef = if (cadastro.id.isEmpty()) {
            val autoId = collection.document().id
            val shortId = if (autoId.length >= 4) autoId.substring(0, 4) else autoId
            collection.document(shortId)
        } else {
            collection.document(cadastro.id)
        }

        cadastro.id = docRef.id

        val data = hashMapOf(
            DB_CADASTRO_ID to cadastro.id,
            DB_CADASTRO_NOME to cadastro.nome,
            DB_CADASTRO_CELLPHONE to cadastro.cellphone
        )

        docRef.set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun list(onSuccess: (List<Cadastro>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("cadastro")
            .get()
            .addOnSuccessListener { result ->
                val list = result.map { document ->
                    Cadastro(
                        id = document.getString(DB_CADASTRO_ID) ?: document.id,
                        nome = document.getString(DB_CADASTRO_NOME) ?: "",
                        cellphone = document.getString(DB_CADASTRO_CELLPHONE) ?: ""
                    )
                }
                onSuccess(list)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun delete(
        id: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("cadastro")
            .document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun findById(id: String, onSuccess: (Cadastro?) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("cadastro")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val cadastro = Cadastro(
                        id = document.getString(
                            DB_CADASTRO_ID
                        ) ?: document.id,
                        nome = document.getString(DB_CADASTRO_NOME) ?: "",
                        cellphone = document.getString(DB_CADASTRO_CELLPHONE) ?: ""
                    )
                    onSuccess(cadastro)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    companion object {
        internal const val DB_CADASTRO_ID = "id"
        internal const val DB_CADASTRO_NOME = "nome"
        internal const val DB_CADASTRO_CELLPHONE = "telefone"
    }
}