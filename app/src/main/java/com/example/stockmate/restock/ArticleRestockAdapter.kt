package com.example.stockmate.restock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockmate.Article
import com.example.stockmate.R

class ArticleRestockAdapter(private val articles: List<Article>, private val onClick: (Article) -> Unit,
                            private val onLongClick: (Article) -> Unit ) :
    RecyclerView.Adapter<ArticleRestockAdapter.ArticleRestockViewHolder>() {

    class ArticleRestockViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvArticleTitle)
        val tvCounter: TextView = view.findViewById(R.id.tvArticleCounter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleRestockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_restock, parent, false)
        return ArticleRestockViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleRestockViewHolder, position: Int) {
        val article = articles[position]
        holder.tvTitle.text = article.title
        holder.tvCounter.text = article.counter.toString()

        holder.view.setOnClickListener {
            onClick(article) // Lorsque l'article est cliqué
            notifyItemChanged(position) // Met à jour l'affichage
        }

        holder.view.setOnLongClickListener {
            onLongClick(article)
            notifyItemChanged(position)
            true
        }

    }

    override fun getItemCount() = articles.size

    fun changeList(newArticles: List<Article>): ArticleRestockAdapter {
        return ArticleRestockAdapter(newArticles, this.onClick, this.onLongClick)
    }
}
