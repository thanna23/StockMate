package com.example.stockmate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SummaryOrderListAdapter(private var shopList: List<Article>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_CATEGORY = 0
    private val TYPE_ARTICLE = 1


    // Liste pour stocker les catégories et les articles
    private val categorizedShopList = mutableListOf<Any>()

    init {
        categorizeArticles()
    }

    // Méthode pour organiser les articles par catégories
    private fun categorizeArticles() {
        // Regrouper les articles par catégorie
        val groupedArticles = shopList.groupBy { it.type }

        for ((type, articles) in groupedArticles) {
            categorizedShopList.add(type)  // Ajout de la catégorie (int)
            categorizedShopList.addAll(articles)  // Ajout des articles sous cette catégorie
        }
    }

    // Crée la vue pour chaque type (catégorie ou article)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_CATEGORY) {
            val categoryView = LayoutInflater.from(parent.context)
                .inflate(R.layout.category_summary, parent, false)
            CategoryViewHolder(categoryView)
        } else {
            val articleView = LayoutInflater.from(parent.context)
                .inflate(R.layout.article_summary, parent, false)
            SummaryViewHolder(articleView)
        }
    }

    // Lie les données aux vues en fonction du type (catégorie ou article)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = categorizedShopList[position]

        if (holder is CategoryViewHolder && item is Int) {
            // Lier la catégorie (numéro de catégorie) au CategoryViewHolder
            holder.categoryTextView.text = ArticleType.fromId(item).toString()

        } else if (holder is SummaryViewHolder && item is Article) {
            // Lier l'article au SummaryViewHolder
            holder.titleTextView.text = item.title
        }
    }

    // Renvoie le nombre total d'éléments (catégories + articles)
    override fun getItemCount(): Int {
        return categorizedShopList.size
    }

    // Renvoie le type de vue (catégorie ou article) pour chaque élément
    override fun getItemViewType(position: Int): Int {
        return if (categorizedShopList[position] is Int) {
            TYPE_CATEGORY
        } else {
            TYPE_ARTICLE
        }
    }



    class CategoryViewHolder(categoryView: View) : RecyclerView.ViewHolder(categoryView) {
        val categoryTextView: TextView = categoryView.findViewById(R.id.category)
    }

    class SummaryViewHolder(summaryView: View) : RecyclerView.ViewHolder(summaryView) {
        val titleTextView: TextView = summaryView.findViewById(R.id.tvArticleTitle)
    }
}

