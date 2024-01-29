package jyotti.apexing.apexing_android.ui.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jyotti.apexing.apexing_android.data.model.main.news.News
import jyotti.apexing.apexing_android.databinding.ItemNewsBinding

class NewsAdapter(private inline val onClickNews: (String) -> Unit) : ListAdapter<News, RecyclerView.ViewHolder>(NewsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewsViewHolder(
            binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClickNews = onClickNews
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsViewHolder) {
            val news = getItem(position)
            holder.bind(news)
        }
    }

    class NewsViewHolder(
        private val binding: ItemNewsBinding,
        private val onClickNews: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: News) = with(binding) { this.news = news }

        fun onClickNewsItem(link: String) {
            onClickNews(link)
        }
    }
}

class NewsDiffUtil : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean = oldItem.link == newItem.link
    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean = oldItem == newItem
}