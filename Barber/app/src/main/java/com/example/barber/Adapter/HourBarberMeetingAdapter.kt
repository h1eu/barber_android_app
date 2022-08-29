package com.example.barber.Adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Interface.ClickItemHourListener
import com.example.barber.R
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HourBarberMeetingAdapter(hourArraylist : ArrayList<String>, hourUsed : ArrayList<String>, currentDay : String, currentTime : String, daySelected : String) :
    RecyclerView.Adapter<HourBarberMeetingAdapter.hourViewHolder>() {
    private lateinit var listener : ClickItemHourListener
    private var  listHour = hourArraylist
    private var  listHourUsed = hourUsed
    private var cDay = currentDay
    private var cTime = currentTime
    private var daySelec = daySelected


    class hourViewHolder(itemView : View, listener: ClickItemHourListener) : RecyclerView.ViewHolder(itemView) {
        var tvHour : TextView
        var cv : CardView
        init {
            tvHour = itemView.findViewById(R.id.tv_hour)
            cv = itemView.findViewById(R.id.hour_cv)
            itemView.setOnClickListener{
                listener.onItemHourClick(adapterPosition, tvHour.text.toString())
            }
        }
    }

    fun setOnClickListener(listener: ClickItemHourListener){
        this.listener = listener
    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): hourViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_hour_barber_meeting,parent,false)
        return hourViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: hourViewHolder, position: Int) {
        var hour : String = listHour.get(position)
        holder.tvHour.text = hour
        if(cDay.equals(daySelec)){
            var hourC = cTime.substring(0,2).toInt()
            var minuC = cTime.substring(3).toInt()
            var hourPick = holder.tvHour.text.toString().substring(0,2).toInt()
            var minuPick = holder.tvHour.text.toString().substring(3).toInt()
            if(hourC > hourPick){
                holder.tvHour.setBackgroundColor(Color.rgb(255,117,112))
                holder.cv.isEnabled = false
            }
            else if(hourC == hourPick){
                var check = minuC + 15
                if(check > minuPick){
                    holder.tvHour.setBackgroundColor(Color.rgb(255,117,112))
                    holder.cv.isEnabled = false
                }
            }
        }
        if(listHourUsed.size > 0){
            for(i in 0 until listHourUsed.size){
                if (holder.tvHour.text.equals(listHourUsed.get(i))){
                    holder.tvHour.setBackgroundColor(Color.rgb(255,117,112))
                    holder.cv.isEnabled = false
                }
            }
        }
        if(position == listHour.size - 1 ){
            listHourUsed.clear()
        }
    }

    override fun getItemCount(): Int {
        return listHour.size
    }








}