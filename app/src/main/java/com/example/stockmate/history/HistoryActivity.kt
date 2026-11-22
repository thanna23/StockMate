package com.example.stockmate.history

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockmate.R

/** Lists past order lists and restocks, newest first. Tap an entry to see its items. */
class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvStatus: TextView
    private lateinit var btOrders: Button
    private lateinit var btRestocks: Button

    private var currentKind = HistoryKind.ORDER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tvStatus = findViewById(R.id.tvStatus)
        btOrders = findViewById(R.id.btOrders)
        btRestocks = findViewById(R.id.btRestocks)

        btOrders.setOnClickListener { show(HistoryKind.ORDER) }
        btRestocks.setOnClickListener { show(HistoryKind.RESTOCK) }

        savedInstanceState?.getString(KEY_KIND)?.let { currentKind = HistoryKind.valueOf(it) }
        show(currentKind)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_KIND, currentKind.name)
    }

    private fun show(kind: HistoryKind) {
        currentKind = kind
        btOrders.alpha = if (kind == HistoryKind.ORDER) 1f else 0.5f
        btRestocks.alpha = if (kind == HistoryKind.RESTOCK) 1f else 0.5f

        recyclerView.adapter = null
        showStatus("Loading...")

        HistoryRepository.loadHistory(kind) { entries ->
            // Ignore results for a tab the user already switched away from.
            if (kind != currentKind || isDestroyed) return@loadHistory
            when {
                entries == null -> showStatus("Could not load history. Check your connection.")
                entries.isEmpty() -> showStatus("No ${kind.label.lowercase()} saved yet.")
                else -> {
                    tvStatus.visibility = View.GONE
                    recyclerView.adapter = HistoryAdapter(entries) { openEntry(it) }
                }
            }
        }
    }

    private fun showStatus(message: String) {
        tvStatus.text = message
        tvStatus.visibility = View.VISIBLE
    }

    private fun openEntry(entry: HistoryEntry) {
        val intent = Intent(this, HistoryDetailActivity::class.java)
            .putExtra(HistoryDetailActivity.EXTRA_KIND, entry.kind.label)
            .putExtra(HistoryDetailActivity.EXTRA_DATE, HistoryAdapter.formatDate(entry))
            .putParcelableArrayListExtra(HistoryDetailActivity.EXTRA_ITEMS, ArrayList(entry.items))
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    companion object {
        private const val KEY_KIND = "kind"
    }
}
