package com.example.barber.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Model.Post
import com.example.barber.R

internal class PostNoImgAdapter(listPost : ArrayList<Post>,typePost : String)
    : RecyclerView.Adapter<PostNoImgAdapter.PostNoImgViewHolder>() {
    private var list = listPost
    private var type = typePost
    private lateinit var listener: onItemClicklistener
    internal inner class PostNoImgViewHolder(itemView : View, listener: onItemClicklistener) : RecyclerView.ViewHolder(itemView){
        var cv : CardView
        var tvTile : TextView
        var tvDes : TextView
        init {
            cv = itemView.findViewById(R.id.cvItem_title)
            tvTile = itemView.findViewById(R.id.item_tv_title)
            tvDes = itemView.findViewById(R.id.item_tv_content)
            itemView.setOnClickListener{
                listener.onClickView(adapterPosition)
            }
        }
    }

    interface onItemClicklistener{
        fun onClickView(position : Int)
    }
    fun setOnClickListener(listener: PostNoImgAdapter.onItemClicklistener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostNoImgViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false)
        return PostNoImgViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: PostNoImgViewHolder, position: Int) {
        var post = list.get(position)
        holder.tvTile.text = post.tieuDe.toString()
        var des = post.noiDung.substring(0,50)
        holder.tvDes.text = des + " ...."
        holder.cv.setOnClickListener{
            listener.onClickView(position)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}