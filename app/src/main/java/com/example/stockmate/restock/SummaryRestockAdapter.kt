package com.example.stockmate.restock

import androidx.recyclerview.widget.RecyclerView
import com.example.stockmate.Article
import com.example.stockmate.ArticleType
import com.example.stockmate.SummaryAdapter

class SummaryRestockAdapter(shopList: List<Article>) : SummaryAdapter(shopList) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = categorizedShopList[position]

        if (holder is CategoryViewHolder && item is Int) {
            holder.categoryTextView.text = ArticleType.fromId(item).toString()

        } else if (holder is SummaryViewHolder && item is Article) {
            holder.titleTextView.text = item.title
            holder.counterTextView.text = item.counter.toString()

            holder.titleTextView.setOnClickListener {
                categorizedShopList.removeAt(position)
                notifyItemRangeRemoved(position, itemCount - position)
                notifyItemRemoved(position)
                if (categorizedShopList.count { it is Article && it.type == item.type } == 0) {
                    categorizedShopList.removeAt(position - 1)
                    notifyItemRangeRemoved(position - 1, itemCount - position - 1)
                    notifyItemRemoved(position - 1)
                }
            }
        }
    }
}
