package com.example.pos_utfpr_usingdb.database

import com.example.pos_utfpr_usingdb.entity.Cadastro
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

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

        // Atualiza o objeto cadastro com o ‘ID’ gerado ou existente
        cadastro.id = docRef.id

        val data = hashMapOf(
            "id" to cadastro.id,
            "nome" to cadastro.nome,
            "telefone" to cadastro.cellphone
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
                        id = document.getString("id") ?: document.id,
                        nome = document.getString("nome") ?: "",
                        cellphone = document.getString("telefone") ?: ""
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
                        id = document.getString("id") ?: document.id,
                        nome = document.getString("nome") ?: "",
                        cellphone = document.getString("telefone") ?: ""
                    )
                    onSuccess(cadastro)
                } else {
                    onSuccess(null)
                }
            }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
