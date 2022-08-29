package com.example.barber.Adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.barber.Interface.ClickItemHairdresserListener
import com.example.barber.Model.Hairdresser
import com.example.barber.R
import java.net.URL

class HairdresserBarberMeetingAdapter(arrayListHairdresser : ArrayList<Hairdresser>, context : Context) :
    RecyclerView.Adapter<HairdresserBarberMeetingAdapter.HairdresserViewHolder>() {
    private var listHairdresser : ArrayList<Hairdresser> = arrayListHairdresser
    private var mContext = context
    private lateinit var listener : ClickItemHairdresserListener
    class HairdresserViewHolder(itemView: View, listener : ClickItemHairdresserListener) : RecyclerView.ViewHolder(itemView){
        var imvHairdresser : ImageView
        var tvHairdresserName : TextView
        init {
            imvHairdresser = itemView.findViewById(R.id.item_imv)
            tvHairdresserName = itemView.findViewById(R.id.item_tv_hairdresserName)
            itemView.setOnClickListener{
                listener.onItemHairdresserClick(adapterPosition)
            }
        }
    }

    fun setOnClickListener(listener: ClickItemHairdresserListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HairdresserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_haidresser_meeting_barber,parent,false)
        return HairdresserViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: HairdresserViewHolder, position: Int) {
        val hairdresser = listHairdresser.get(position)
        holder.tvHairdresserName.text = hairdresser.tenThoCatToc
        Glide.with(mContext).load(hairdresser.img).into(holder.imvHairdresser)
    }

    override fun getItemCount(): Int {
        return listHairdresser.size
    }
}