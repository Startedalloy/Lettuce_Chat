package com.example.lettucechat.feature.chat.data.repository
import com.example.lettucechat.feature.chat.data.mapper.toDomain
import com.example.lettucechat.feature.chat.data.model.MessageDto
import com.example.lettucechat.feature.chat.domain.model.Message
import com.example.lettucechat.feature.chat.domain.repository.ChatRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChatRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ChatRepository {

    private val collection = firestore.collection(CHAT_COLLECTION)

    override fun observer(): Flow<List<Message>> = callbackFlow {
        val listener = collection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val messages = snapshot?.documents?.map { doc ->
                    doc.toObject(MessageDto::class.java)?.toDomain(doc.id)
                }.orEmpty().filterNotNull()

                trySend(messages)
            }

        awaitClose { listener.remove() }
    }

    override suspend fun sendMessage(text: String) {
        val senderId = auth.currentUser?.uid ?: throw IllegalStateException("User not logged in")
        val message = MessageDto(
            senderId = senderId,
            text = text.trim(),
            timestamp = System.currentTimeMillis()
        )
        collection.add(message).await()
    }

    override suspend fun editMessage(text: String, newText: String) {
        val querySnapshot = collection.whereEqualTo("text", text).limit(1).get().await()
        val doc = querySnapshot.documents.firstOrNull() ?: return
        doc.reference.update("text", newText.trim()).await()
    }

    override suspend fun deleteMessage(text: String) {
        val querySnapshot = collection.whereEqualTo("text", text).limit(1).get().await()
        val doc = querySnapshot.documents.firstOrNull() ?: return
        doc.reference.delete().await()
    }

    private companion object {
        const val CHAT_COLLECTION = "messages"
    }
}