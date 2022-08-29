package com.example.barber.Interface

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.barber.Adapter.HourBarberMeetingAdapter
import java.text.FieldPosition

interface ClickItemHourListener {
    fun onItemHourClick(position : Int, hour : String)


}