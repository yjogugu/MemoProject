package com.taijoo.potfolioproject.util

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import com.taijoo.potfolioproject.R

class CurrentDayDecorator(dates: Collection<CalendarDay>) : DayViewDecorator {

    var dates: HashSet<CalendarDay> = HashSet(dates)

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {

        view.addSpan(DotSpan(10F,Color.parseColor("#F05548")))
    }

}