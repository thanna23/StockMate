package com.example.stockmate.history

/** The two kinds of finished sessions we keep a history of, and their Firestore collection. */
enum class HistoryKind(val collection: String, val label: String) {
    ORDER("orders", "Order list"),
    RESTOCK("restocks", "Restock")
}
