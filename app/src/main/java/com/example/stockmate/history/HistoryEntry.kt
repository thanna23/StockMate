package com.example.stockmate.history

import com.example.stockmate.Article
import java.util.Date

/** One saved order list or restock, as read back from Firestore. */
data class HistoryEntry(
    val id: String,
    val kind: HistoryKind,
    val createdAt: Date?,
    val items: List<Article>
)
