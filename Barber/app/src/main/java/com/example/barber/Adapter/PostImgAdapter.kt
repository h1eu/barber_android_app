package com.example.barber.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.barber.Model.Post
import com.example.barber.R

internal class PostImgAdapter(listPost : ArrayList<Post>, typePost : String, context: Context)
    : RecyclerView.Adapter<PostImgAdapter.PostImgViewHolder>() {
    private var list = listPost
    private var type = typePost
    private var mContext = context
    private lateinit var listener: PostImgAdapter.onItemClicklistener
    internal inner class PostImgViewHolder(itemView : View,listener: onItemClicklistener) : RecyclerView.ViewHolder(itemView){
        var layout : ConstraintLayout
        var img : ImageView
        var tvTitle : TextView
        init {
            layout = itemView.findViewById(R.id.item_layout)
            img = itemView.findViewById(R.id.item_imv)
            tvTitle = itemView.findViewById(R.id.item_tv_titlePost)
        }
    }
    interface onItemClicklistener{
        fun onClickView(position : Int)
    }
    fun setOnClickListener(listener: PostImgAdapter.onItemClicklistener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostImgViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post_img,parent,false)
        return PostImgViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: PostImgViewHolder, position: Int) {
        var post = list.get(position)
        holder.tvTitle.text = post.tieuDe
        Glide.with(mContext).load(post.imgPost).into(holder.img)
        holder.layout.setOnClickListener{
            listener.onClickView(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}