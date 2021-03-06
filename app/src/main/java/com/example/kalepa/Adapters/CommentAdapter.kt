package com.example.kalepa.Adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import com.example.kalepa.Preferences.SharedApp
import com.example.kalepa.R
import com.example.kalepa.common.ItemAdapter
import com.example.kalepa.common.bindView
import com.example.kalepa.models.Comment
import com.example.kalepa.models.Notification

class CommentAdapter (
    val comment: Comment,
    val holded : (Comment) -> Boolean
) : ItemAdapter<CommentAdapter.ViewHolder>(R.layout.item_profile_comment) {
    override fun onCreateViewHolder(itemView: View) = ViewHolder(itemView)
    override fun ViewHolder.onBindViewHolder() { // 2
        usernametv.text = comment.user
        textView.text = comment.body
        if (SharedApp.prefs.isMod) {
            itemView.setOnLongClickListener {
                holded(comment)
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val usernametv by bindView<TextView>(R.id.m_comment_user)
        val textView by bindView<TextView>(R.id.m_comment_body)
    }
}