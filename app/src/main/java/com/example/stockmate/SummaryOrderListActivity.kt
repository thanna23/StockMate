package com.example.stockmate

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class SummaryOrderListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var sumAdapter: SummaryOrderListAdapter
    private lateinit var endButton: Button
    private lateinit var copyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_summary_order_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val shopList: ArrayList<Article>? = intent.getParcelableArrayListExtra("shopList")


        // Configuration de la RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (shopList != null) {
            shopList.groupBy { it.type }
            sumAdapter = SummaryOrderListAdapter(shopList.toList())
            recyclerView.adapter = sumAdapter
        }


        copyButton = findViewById(R.id.copyButton)
        copyButton.setOnClickListener {
            val groupedArticles = shopList?.groupBy { it.type }

            val stringBuilder = StringBuilder()

            for ((type, articles) in groupedArticles!!) {
                stringBuilder.append("${ArticleType.fromId(type)} :\n\n")

                val articleStringBuilder = StringBuilder()
                for (article in articles){
                    articleStringBuilder.append("${article.title} \n")
                }
                stringBuilder.append("$articleStringBuilder\n")
            }
            val text = stringBuilder.toString()
            copyTextToClipboard(text)
        }



        endButton = findViewById(R.id.endButton)
        endButton.setOnClickListener {

            val builder = AlertDialog.Builder(this, R.style.EndAlertDialogTheme)
            builder.setTitle("BRAVO !!")
            builder.setMessage("Congratulation you have finished the restock... Now get back to work !!")

            builder.setPositiveButton("Continue") { dialog, _ ->
                dialog.dismiss()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()

        }
    }


    private fun copyTextToClipboard(text: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        // Créer un ClipData contenant le texte à copier
        val clip = ClipData.newPlainText("label", text)

        // Copier le texte dans le presse-papiers
        clipboard.setPrimaryClip(clip)
    }


}