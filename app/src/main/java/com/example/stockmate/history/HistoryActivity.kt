package com.example.stockmate.history

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockmate.R
import com.google.android.material.button.MaterialButton

/** Lists past order lists and restocks, newest first. Tap an entry to see its items. */
class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvStatus: TextView
    private lateinit var btOrders: MaterialButton
    private lateinit var btRestocks: MaterialButton

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
        styleTab(btOrders, selected = kind == HistoryKind.ORDER)
        styleTab(btRestocks, selected = kind == HistoryKind.RESTOCK)

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

    /** Selected tab: filled accent. Unselected tab: transparent with an outline. */
    private fun styleTab(button: MaterialButton, selected: Boolean) {
        val fill = if (selected) getColor(R.color.sm_accent) else Color.TRANSPARENT
        button.backgroundTintList = ColorStateList.valueOf(fill)
        button.strokeWidth = if (selected) 0 else resources.getDimensionPixelSize(R.dimen.sm_stroke)
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
