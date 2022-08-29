package com.example.barber.Adapter

import android.content.Context
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

class HairdresserListAdapter(listHairdresser : ArrayList<Hairdresser>, context : Context) :
    RecyclerView.Adapter<HairdresserListAdapter.HairdresserListViewHolder>() {
    private var list = listHairdresser
    private var mContext = context
    class HairdresserListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var imvHairdresser : ImageView
        var tvHairdresserName : TextView
        var tvHairdresserAge : TextView
        var tvHairdresserHairList : TextView
        init {
            imvHairdresser = itemView.findViewById(R.id.item_imv)
            tvHairdresserName = itemView.findViewById(R.id.item_tv_hairdresserName)
            tvHairdresserAge = itemView.findViewById(R.id.item_tv_hairdresserAge)
            tvHairdresserHairList = itemView.findViewById(R.id.item_tv_hairdresserHairList)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HairdresserListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hairdreser_list,parent,false)
        return HairdresserListViewHolder(view)
    }

    override fun onBindViewHolder(holder: HairdresserListViewHolder, position: Int) {
        val hairdresser = list.get(position)
        holder.tvHairdresserName.text = "Tên : " + hairdresser.tenThoCatToc
        holder.tvHairdresserAge.text = "Tuổi : " + hairdresser.tuoi
        holder.tvHairdresserHairList.text = "Kiểu tóc chính : " + hairdresser.kieuTocChinh
        Glide.with(mContext).load(hairdresser.img).into(holder.imvHairdresser)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}