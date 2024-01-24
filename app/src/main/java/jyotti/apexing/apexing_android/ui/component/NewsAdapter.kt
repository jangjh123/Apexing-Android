package jyotti.apexing.apexing_android.ui.component

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import jyotti.apexing.apexing_android.data.model.main.news.News
import jyotti.apexing.apexing_android.databinding.ItemNewsBinding
import jyotti.apexing.apexing_android.util.Utils.getThumbnail

class NewsAdapter(private inline val onClickNews: (String) -> Unit) : ListAdapter<News, RecyclerView.ViewHolder>(NewsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            val news = getItem(position)
            holder.bind(news)
        }
    }

    inner class ViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(news: News) {
            with(binding) {
                tvNewsTitle.text = news.title

                Glide.with(root)
                    .load(news.img)
                    .centerCrop()
                    .thumbnail(
                        getThumbnail(root.context, news.img)
                    )
                    .into(ivNewsImage)

                layoutNews.setOnClickListener {
                    onClickNews(news.link)
                }
            }
        }
    }
}

class NewsDiffUtil : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean = oldItem.link == newItem.link
    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean = oldItem == newItem
}