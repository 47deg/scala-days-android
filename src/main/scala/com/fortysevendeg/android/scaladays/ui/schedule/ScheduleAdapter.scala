/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may
 *  not use this file except in compliance with the License. You may obtain
 *  a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.fortysevendeg.android.scaladays.ui.schedule

import android.support.v7.widget.RecyclerView
import android.view.View.OnClickListener
import android.view.{View, ViewGroup}
import com.fortysevendeg.android.scaladays.model.{Speaker, Event}
import com.fortysevendeg.android.scaladays.ui.commons.DateTimeTextViewTweaks._
import com.fortysevendeg.macroid.extras.TextTweaks._
import com.fortysevendeg.macroid.extras.ViewTweaks._
import com.fortysevendeg.macroid.extras.ViewGroupTweaks._
import macroid.FullDsl._
import macroid.{ActivityContext, AppContext}
import ScheduleAdapter._

class ScheduleAdapter(timeZone: String, scheduleItems: Seq[ScheduleItem], listener: RecyclerClickListener)
    (implicit context: ActivityContext, appContext: AppContext)
    extends RecyclerView.Adapter[RecyclerView.ViewHolder] {

  val recyclerClickListener = listener

  override def onCreateViewHolder(parentViewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder = {
    viewType match {
      case viewType if viewType == itemViewTypeTalk =>
        val adapter = new ScheduleLayoutAdapter()
        adapter.content.setOnClickListener(new OnClickListener {
          override def onClick(v: View): Unit = recyclerClickListener.onClick(scheduleItems(v.getTag.asInstanceOf[Int]))
        })
        new ViewHolderScheduleAdapter(adapter)
      case viewType if viewType == itemViewTypeHeader =>
        val adapter = new HeaderLayoutAdapter()
        adapter.content.setOnClickListener(new OnClickListener {
          override def onClick(v: View): Unit = recyclerClickListener.onClick(scheduleItems(v.getTag.asInstanceOf[Int]))
        })
        new ViewHolderHeaderAdapter(adapter)
    }
  }

  override def getItemCount: Int = scheduleItems.size

  override def onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int): Unit = {
    val scheduleItem = scheduleItems(position)
    getItemViewType(position) match {
      case viewType if viewType == itemViewTypeTalk =>
        val vh = viewHolder.asInstanceOf[ViewHolderScheduleAdapter]
        scheduleItem.event map {
          event =>
            vh.content.setTag(position)
            if (event.speakers.size == 0) {
              runUi(vh.speakerContent <~ vGone)
            } else {
              runUi(vh.speakerContent <~ vVisible <~ vgRemoveAllViews)
              event.speakers.map(
                speaker => {
                  val speakerLayout = new SpeakersLayout(speaker)
                  runUi((vh.speakerContent <~ vgAddView(speakerLayout.content)))
                }
              )
            }
            runUi(
              (vh.hour <~ tvDateTimeHourMinute(event.startTime, timeZone)) ~
                  (vh.name <~ tvText(event.title)) ~
                  (vh.room <~ event.track.map(track => tvText(track.name) + vVisible).getOrElse(vGone))
            )
        }
      case viewType if viewType == itemViewTypeHeader =>
        val vh = viewHolder.asInstanceOf[ViewHolderHeaderAdapter]
        runUi(
          vh.headerName <~ scheduleItem.header.map(tvText(_) + vVisible).getOrElse(vGone)
        )
    }
  }

  override def getItemViewType(position: Int): Int = if (scheduleItems(position).isHeader) itemViewTypeHeader else itemViewTypeTalk


}

object ScheduleAdapter {
  val itemViewTypeHeader = 0
  val itemViewTypeTalk = 1
}

trait RecyclerClickListener {
  def onClick(scheduleItem: ScheduleItem)
}

