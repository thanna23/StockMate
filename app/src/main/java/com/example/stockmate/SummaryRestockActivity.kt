package com.example.stockmate

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class SummaryRestockActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mainSumAdapter: SummaryAdapter
    private lateinit var initialSumAdapter: SummaryAdapter
    private lateinit var currentSumAdapter: SummaryAdapter
    private lateinit var resetButton: Button
    private lateinit var endButton: Button
    private lateinit var initialListButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_summary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val shopList: ArrayList<Article>? = intent.getParcelableArrayListExtra("shopList")

        fun updateSumAdapter(newSumAdapter: SummaryAdapter) {
            recyclerView.adapter = newSumAdapter
            currentSumAdapter = newSumAdapter
        }

        // Configuration de la RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (shopList != null) {
            shopList.groupBy { it.type }

            initialSumAdapter = SummaryAdapter(shopList.toList())
            mainSumAdapter = SummaryAdapter(shopList.toList())
            updateSumAdapter(mainSumAdapter)
        }

        resetButton = findViewById(R.id.resetButton)
        resetButton.setOnClickListener {
            if (shopList != null) {
                updateSumAdapter(SummaryAdapter(shopList.toList()))
            }
        }

        initialListButton = findViewById(R.id.initialListButton)
        initialListButton.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    recyclerView.adapter = initialSumAdapter
                    true
                }

                MotionEvent.ACTION_UP -> {
                    v.performClick()
                    recyclerView.adapter = currentSumAdapter

                    true
                }

                else -> false
            }
        }

        endButton = findViewById(R.id.endButton)
        endButton.setOnClickListener {

            //Création du message de fin
            val builder = AlertDialog.Builder(this, R.style.EndAlertDialogTheme)
            builder.setTitle("BRAVO !!")
            builder.setMessage("Congratulation you have finished the restock... Now get back to work !!")

            // Ajouter du bouton "continue"
            builder.setPositiveButton("Continue") { dialog, _ ->
                dialog.dismiss()

                //Ouverture de la main activity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            // Création et affichage la boîte de dialogue
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }


    }
}
