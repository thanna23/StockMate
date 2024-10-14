package com.example.stockmate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArticleAdapter(private val articles: List<Article>, private val onClick: (Article) -> Unit,
                     private val onLongClick: (Article) -> Unit ) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvArticleTitle)
        val tvCounter: TextView = view.findViewById(R.id.tvArticleCounter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
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

    fun changeList(newArticles: List<Article>): ArticleAdapter {
        return ArticleAdapter(newArticles, this.onClick, this.onLongClick)
    }
}
