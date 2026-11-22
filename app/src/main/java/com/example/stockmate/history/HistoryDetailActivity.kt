package com.example.stockmate.history

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockmate.Article
import com.example.stockmate.R
import com.example.stockmate.SummaryAdapter

/** Shows the items of one past order list or restock, grouped by category. */
class HistoryDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.tvTitle).text = intent.getStringExtra(EXTRA_KIND)
        findViewById<TextView>(R.id.tvDate).text = intent.getStringExtra(EXTRA_DATE)

        val items: ArrayList<Article> =
            intent.getParcelableArrayListExtra(EXTRA_ITEMS) ?: arrayListOf()

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // SummaryAdapter shows the counter only when it is non-zero, so restocks show
        // quantities and order lists show just the names.
        recyclerView.adapter = SummaryAdapter(items)
    }

    companion object {
        const val EXTRA_KIND = "kind"
        const val EXTRA_DATE = "date"
        const val EXTRA_ITEMS = "items"
    }
}
