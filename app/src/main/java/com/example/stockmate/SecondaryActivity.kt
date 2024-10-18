package com.example.stockmate

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class SecondaryActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var backButton: Button
    private lateinit var nextButton: Button
    private lateinit var tvCategory: TextView
    private lateinit var tvPageCount: TextView

    private var categoryArticlesList = mutableListOf<Article>()
    private var shopList = mutableSetOf<Article>()
    private var currentCategory: Int = ArticleType.MINERALES.id
    private val BACK_ACTION = 0
    private val NEXT_ACTION = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_secondary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configuration de l'Interface
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tvCategory = findViewById(R.id.tvCategory)
        tvPageCount = findViewById(R.id.tvPageCounter)

        //Initialisation de la list des Articles
        defineArticles(currentCategory)

        // Initialisation de l'adapter
        articleAdapter = ArticleAdapter(categoryArticlesList, ({ article ->
            article.counter++
        }), ({ a -> a.counter-- }))

        recyclerView.adapter = articleAdapter

        //Initialisation des boutons
        backButton = findViewById(R.id.backButton)
        nextButton = findViewById(R.id.nextButton)

        backButton.setOnClickListener {
            actualiseShopList()
            if (currentCategory == ArticleType.MINERALES.id) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)


            } else {
                actualiseArticles(currentCategory, BACK_ACTION)
                articleAdapter = articleAdapter.changeList(categoryArticlesList)
                recyclerView.adapter = articleAdapter
            }
        }

        nextButton.setOnClickListener {
            actualiseShopList()

            if (currentCategory == ArticleType.values().size - 1) {
                val intent = Intent(this, SummaryActivity::class.java)
                intent.putParcelableArrayListExtra("shopList", ArrayList<Article>(shopList))
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)


            } else {
                actualiseArticles(currentCategory, NEXT_ACTION)
                articleAdapter = articleAdapter.changeList(categoryArticlesList)
                recyclerView.adapter = articleAdapter

            }
        }


    }

    private fun defineArticles(currentType: Int) {
        val tempAllArticles = Article.getAllArticles()
            .filter { a -> a.type == currentType }.toMutableList()
        tempAllArticles.forEach { a ->
            a.counter = shopList.find { b -> b.title == a.title && b.type == a.type }?.counter ?: 0
        }

        categoryArticlesList = tempAllArticles
        currentCategory = currentType
        tvCategory.text = ArticleType.fromId(currentCategory).toString()
        tvPageCount.text = "${currentCategory + 1} / ${ArticleType.values().size.toString()} "
    }

    private fun actualiseArticles(currentType: Int, action: Int) {
        if (action == NEXT_ACTION) {
            defineArticles(currentType + 1)
        } else {
            defineArticles(currentType - 1)
        }
    }

    private fun actualiseShopList() {
        for (article in categoryArticlesList) {
            if (article.counter != 0) {
                val existingArticle = shopList.find { it.title == article.title }

                if (existingArticle == null) {
                    shopList.add(article)

                } else {
                    shopList.remove(existingArticle)
                    shopList.add(article)

                }
            }
        }
    }

}