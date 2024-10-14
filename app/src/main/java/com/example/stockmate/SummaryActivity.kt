package com.example.stockmate

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class SummaryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sumAdapter: SummaryAdapter
    private lateinit var resetButton : Button
    private lateinit var endButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_summary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var shopList : ArrayList<Article>? = intent.getParcelableArrayListExtra("shopList")

        // Configuration de la RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (shopList != null) {
            shopList.groupBy { it.type }
            sumAdapter = SummaryAdapter(shopList.toList()){}
            recyclerView.adapter = sumAdapter
        }

        resetButton= findViewById(R.id.resetButton)
        resetButton.setOnClickListener {
            val intent = intent
            finish()
            intent.putParcelableArrayListExtra("shopList", ArrayList(shopList))
            startActivity(intent)
        }
        endButton= findViewById(R.id.nextButton)
        endButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
}
