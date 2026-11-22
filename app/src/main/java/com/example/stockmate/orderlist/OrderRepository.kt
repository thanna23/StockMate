package com.example.stockmate.orderlist

import android.util.Log
import com.example.stockmate.Article
import com.example.stockmate.ArticleType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Persists order lists to Firestore.
 *
 * Each order becomes one document in the "orders" collection:
 * {
 *   createdAt: <server timestamp>,
 *   uid: "<anonymous user id>",
 *   itemCount: 3,
 *   items: [ { title: "Coca", type: 0, category: "MINERALES" }, ... ]
 * }
 *
 * Writes require a signed-in user (see firestore.rules). The app signs in
 * anonymously on first save; Firebase persists that session on the device.
 */
object OrderRepository {

    private const val TAG = "OrderRepository"
    private const val COLLECTION_ORDERS = "orders"

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    /**
     * Saves the order. Firestore caches writes locally, so once signed in this also
     * works offline: the document syncs when the device is back online. [onResult]
     * is called when the server acknowledges the write (or it fails).
     */
    fun saveOrder(articles: List<Article>, onResult: (success: Boolean) -> Unit = {}) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            writeOrder(currentUser.uid, articles, onResult)
            return
        }
        auth.signInAnonymously()
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    Log.e(TAG, "Anonymous sign-in returned no user")
                    onResult(false)
                } else {
                    writeOrder(uid, articles, onResult)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Anonymous sign-in failed", e)
                onResult(false)
            }
    }

    private fun writeOrder(uid: String, articles: List<Article>, onResult: (Boolean) -> Unit) {
        val items = articles
            .sortedWith(compareBy({ it.type }, { it.title }))
            .map { article ->
                mapOf(
                    "title" to article.title,
                    "type" to article.type,
                    "category" to (ArticleType.fromId(article.type)?.name ?: "UNKNOWN")
                )
            }

        val order = hashMapOf(
            "createdAt" to FieldValue.serverTimestamp(),
            "uid" to uid,
            "itemCount" to items.size,
            "items" to items
        )

        db.collection(COLLECTION_ORDERS)
            .add(order)
            .addOnSuccessListener { ref ->
                Log.d(TAG, "Order saved with id ${ref.id}")
                onResult(true)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to save order", e)
                onResult(false)
            }
    }
}
