package com.example.barber.Adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Api.ApiServices
import com.example.barber.Model.BarberMeeting
import com.example.barber.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class HistoryMeetingAdapter(listHistory : ArrayList<BarberMeeting>, cDay : String, cTime : String) :
    RecyclerView.Adapter<HistoryMeetingAdapter.HistoryMeetingViewHolder>() {
    private var arrayListHistoryMeeting = listHistory
    private lateinit var listener : HistoryMeetingAdapter.onItemClicklistener
    private var currentDay = cDay
    private var currentTime = cTime
    internal inner class HistoryMeetingViewHolder(itemView: View,litener : HistoryMeetingAdapter.onItemClicklistener ) : RecyclerView.ViewHolder(itemView){
        var tvDay : TextView
        var tvHour : TextView
        var tvNameHairdresser : TextView
        var tvAddressStore : TextView
        var cv : CardView
        init {
            tvDay = itemView.findViewById(R.id.history_tv_day)
            tvHour = itemView.findViewById(R.id.history_tv_hour)
            tvNameHairdresser = itemView.findViewById(R.id.history_tv_nameHairdresser)
            tvAddressStore = itemView.findViewById(R.id.history_tv_addressStore)
            cv = itemView.findViewById(R.id.item_layout)
            itemView.setOnClickListener{
                listener.onClickView(adapterPosition)
            }
        }
    }

    interface onItemClicklistener{
        fun onClickView(position : Int)
    }
    fun setOnClickListener(listener: HistoryMeetingAdapter.onItemClicklistener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryMeetingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history_meeting,parent,false)
        return HistoryMeetingViewHolder(view,listener)
    }

    override fun onBindViewHolder(holder: HistoryMeetingViewHolder, position: Int) {
        var barberMeeting : BarberMeeting = arrayListHistoryMeeting.get(position)
        holder.tvDay.text = "Ngày: " + barberMeeting.ngay
        holder.tvHour.text = "Giờ: " + barberMeeting.gio
        holder.tvNameHairdresser.text ="Tên thợ cắt tóc: " +  barberMeeting.tenThoCatToc
        holder.tvAddressStore.text ="Địa chỉ: " +  barberMeeting.diaChiCuaHang
        Log.e("tinh trang lich hen ",position.toString()+ " " + barberMeeting.tinhTrangLichHen)

        if(barberMeeting.tinhTrangLichHen.equals("false")){
            holder.cv.setCardBackgroundColor(Color.rgb(255,117,112))
            holder.cv.isEnabled = false
        }
        else {
            holder.cv.setCardBackgroundColor(Color.WHITE)
            holder.cv.isEnabled = true
        }
        if(currentDay.compareTo(barberMeeting.ngay) > 0){
            var action = "cancelMeeting"
            ApiServices.create().cancelMeeting(barberMeeting.maLichCatToc.toString(),action).enqueue(
                object : Callback<Boolean>{
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        var check = response.body()
                        if(check != null){
                            if(check == true){
                                holder.cv.setCardBackgroundColor(Color.rgb(255,117,112))
                                holder.cv.isEnabled = false
                            }
                        }
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {

                    }

                }
            )

        }

        if(currentDay.equals(barberMeeting.ngay)){
            var hourC = currentTime.substring(0,2).toInt()
            var minuC = currentTime.substring(3).toInt()
            var hourPos = barberMeeting.gio.toString().substring(0,2).toInt()
            var minuPos = barberMeeting.gio.substring(3).toInt()
            if(hourC > hourPos){
                var action = "cancelMeeting"
                ApiServices.create().cancelMeeting(barberMeeting.maLichCatToc.toString(),action).enqueue(
                    object : Callback<Boolean>{
                        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                            var check = response.body()
                            if(check != null){
                                if(check == true){
                                    holder.cv.setCardBackgroundColor(Color.rgb(255,117,112))
                                    holder.cv.isEnabled = false
                                }
                            }
                        }

                        override fun onFailure(call: Call<Boolean>, t: Throwable) {

                        }

                    }
                )
            }
            else if(hourC == hourPos){
                var check = minuC + 15
                if(check > minuPos){

                    var action = "cancelMeeting"
                    ApiServices.create().cancelMeeting(barberMeeting.maLichCatToc.toString(),action).enqueue(
                        object : Callback<Boolean>{
                            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                                var check = response.body()
                                if(check != null){
                                    if(check == true){
                                        holder.cv.setCardBackgroundColor(Color.rgb(255,117,112))
                                        holder.cv.isEnabled = false
                                    }
                                }
                            }

                            override fun onFailure(call: Call<Boolean>, t: Throwable) {

                            }

                        }
                    )
                }
            }
        }


        holder.cv.setOnClickListener{
            listener.onClickView(position)
        }
    }

    override fun getItemCount(): Int {
        return arrayListHistoryMeeting.size
    }

}