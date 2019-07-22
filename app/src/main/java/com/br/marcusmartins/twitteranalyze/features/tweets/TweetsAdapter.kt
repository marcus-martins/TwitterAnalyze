package com.br.marcusmartins.twitteranalyze.features.tweets

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.br.marcusmartins.twitteranalyze.R
import com.br.marcusmartins.twitteranalyze.core.extension.inflate
import com.br.marcusmartins.twitteranalyze.features.tweets.entities.Tweets
import kotlinx.android.synthetic.main.card_tweets.view.*
import javax.inject.Inject
import kotlin.properties.Delegates

class TweetsAdapter
@Inject constructor() : RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    internal var collection: List<Tweets> by Delegates.observable(emptyList()) {
        _, _, _ -> notifyDataSetChanged()
    }

    internal var clickListener: (Tweets) -> Unit = { _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            parent.inflate(
                R.layout.card_tweets
            )
        )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
            viewHolder.bind(collection[position], clickListener)

    override fun getItemCount() = collection.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(tweets: Tweets, clickListener: (Tweets) -> Unit) {
            itemView.tweet_message.text = tweets.text
            itemView.tweet_date_value.text = tweets.created_at
            itemView.setOnClickListener { clickListener(tweets) }
        }
    }
}
