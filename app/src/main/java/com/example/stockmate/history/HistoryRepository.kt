package com.example.stockmate.history

import android.util.Log
import com.example.stockmate.Article
import com.example.stockmate.ArticleType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

/**
 * Persists finished order lists and restocks to Firestore and reads them back.
 *
 * Each session becomes one document in the "orders" or "restocks" collection:
 * {
 *   createdAt: <server timestamp>,
 *   uid: "<anonymous user id>",
 *   itemCount: 3,
 *   items: [ { title: "Coca", type: 0, category: "MINERALES", counter: 4 }, ... ]
 * }
 *
 * All access requires a signed-in user (see firestore.rules). The app signs in
 * anonymously on first use; Firebase persists that session on the device.
 */
object HistoryRepository {

    private const val TAG = "HistoryRepository"
    private const val HISTORY_LIMIT = 100L

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun saveOrder(articles: List<Article>, onResult: (success: Boolean) -> Unit = {}) =
        save(HistoryKind.ORDER, articles, onResult)

    fun saveRestock(articles: List<Article>, onResult: (success: Boolean) -> Unit = {}) =
        save(HistoryKind.RESTOCK, articles, onResult)

    /** Loads the most recent entries of [kind], newest first. [onResult] gets null on failure. */
    fun loadHistory(kind: HistoryKind, onResult: (List<HistoryEntry>?) -> Unit) {
        withUser(onFailure = { onResult(null) }) {
            db.collection(kind.collection)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(HISTORY_LIMIT)
                .get()
                .addOnSuccessListener { snapshot ->
                    onResult(snapshot.documents.map { toEntry(kind, it) })
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to load ${kind.collection}", e)
                    onResult(null)
                }
        }
    }

    /**
     * Saves the session. Once signed in, Firestore caches writes locally, so this also
     * works offline: the document syncs when the device is back online. [onResult] is
     * called when the server acknowledges the write (or it fails).
     */
    private fun save(kind: HistoryKind, articles: List<Article>, onResult: (Boolean) -> Unit) {
        withUser(onFailure = { onResult(false) }) { uid ->
            val items = articles
                .sortedWith(compareBy({ it.type }, { it.title }))
                .map { article ->
                    mapOf(
                        "title" to article.title,
                        "type" to article.type,
                        "category" to (ArticleType.fromId(article.type)?.name ?: "UNKNOWN"),
                        "counter" to article.counter
                    )
                }

            val doc = hashMapOf(
                "createdAt" to FieldValue.serverTimestamp(),
                "uid" to uid,
                "itemCount" to items.size,
                "items" to items
            )

            db.collection(kind.collection)
                .add(doc)
                .addOnSuccessListener { ref ->
                    Log.d(TAG, "${kind.label} saved with id ${ref.id}")
                    onResult(true)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to save ${kind.label}", e)
                    onResult(false)
                }
        }
    }

    /** Runs [block] with the current user's uid, signing in anonymously first if needed. */
    private fun withUser(onFailure: () -> Unit, block: (uid: String) -> Unit) {
        val current = auth.currentUser
        if (current != null) {
            block(current.uid)
            return
        }
        auth.signInAnonymously()
            .addOnSuccessListener { result ->
                val uid = result.user?.uid
                if (uid == null) {
                    Log.e(TAG, "Anonymous sign-in returned no user")
                    onFailure()
                } else {
                    block(uid)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Anonymous sign-in failed", e)
                onFailure()
            }
    }

    private fun toEntry(kind: HistoryKind, doc: DocumentSnapshot): HistoryEntry {
        // ESTIMATE gives a local time for writes the server has not confirmed yet.
        val createdAt = doc.getTimestamp(
            "createdAt", DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
        )?.toDate()

        val rawItems = doc.get("items") as? List<*> ?: emptyList<Any>()
        val items = rawItems.mapNotNull { raw ->
            val map = raw as? Map<*, *> ?: return@mapNotNull null
            val title = map["title"] as? String ?: return@mapNotNull null
            val type = (map["type"] as? Number)?.toInt() ?: return@mapNotNull null
            val counter = (map["counter"] as? Number)?.toInt() ?: 0
            Article(title, counter, type)
        }
        return HistoryEntry(doc.id, kind, createdAt, items)
    }
}
