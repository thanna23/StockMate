package com.example.stockmate.orderlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockmate.Article
import com.example.stockmate.R

class ArticleOrderListAdapter(
    private val articles: List<Article>,
    private val onClick: (Article) -> Unit
) :
    RecyclerView.Adapter<ArticleOrderListAdapter.ArticleOrderListViewHolder>() {

    class ArticleOrderListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvArticleTitle)
        val orderValue: CheckBox = view.findViewById(R.id.orderValueCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleOrderListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.article_order_list, parent, false)
        return ArticleOrderListViewHolder(view)
    }

    override fun getItemCount() = articles.size

    override fun onBindViewHolder(holder: ArticleOrderListViewHolder, position: Int) {
        val article = articles[position]
        holder.tvTitle.text = article.title

        holder.orderValue.setOnCheckedChangeListener(null)
        holder.orderValue.isChecked = article.orderValue
        holder.orderValue.setOnCheckedChangeListener { _, _ ->
            onClick(article)
        }
    }


    fun changeList(newArticles: List<Article>): ArticleOrderListAdapter {
        return ArticleOrderListAdapter(newArticles, this.onClick)
    }
}