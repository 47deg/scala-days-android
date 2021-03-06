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

import android.view.Gravity
import android.view.ViewGroup.LayoutParams._
import android.widget.ImageView.ScaleType
import android.widget._
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.ui.commons.AsyncImageTweaks._
import macroid.extras.FrameLayoutTweaks._
import macroid.extras.LinearLayoutTweaks._
import macroid.extras.ResourcesExtras._
import macroid.extras.TextViewTweaks._
import macroid.extras.ImageViewTweaks._
import macroid.extras.ViewTweaks._
import macroid.FullDsl._
import macroid.{ActivityContextWrapper, ContextWrapper, Tweak}

import scala.language.postfixOps

trait SpeakersLayoutStyles {

  def backgroundAvatar(implicit context: ActivityContextWrapper): Tweak[FrameLayout] =
    vWrapContent +
      vBackground(R.drawable.background_speaker_avatar_schedule) +
      vPaddings(resGetDimensionPixelSize(R.dimen.stroke_avatar_schedule))

  def avatarStyle(picture: Option[String])(implicit context: ActivityContextWrapper): Tweak[ImageView] = {
    val avatarSize = resGetDimensionPixelSize(R.dimen.size_schedule_avatar)
    lp[LinearLayout](avatarSize, avatarSize) +
      ivScaleType(ScaleType.CENTER_CROP) +
      (picture map {
        roundedImage(_, android.R.color.transparent, avatarSize, Some(R.drawable.placeholder_avatar_failed))
      } getOrElse ivSrc(R.drawable.placeholder_avatar_failed))
  }

  def speakerNameItemStyle(name: String)(implicit context: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      tvSizeResource(R.dimen.text_medium) +
      vPadding(0, 0, resGetDimensionPixelSize(R.dimen.padding_default_extra_small), 0) +
      tvColorResource(R.color.text_schedule_name) +
      tvText(name)

  def speakerTwitterItemStyle(twitter: Option[String])(implicit context: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      tvSizeResource(R.dimen.text_medium) +
      tvColorResource(R.color.text_schedule_twitter) +
      twitter.map(tvText(_) + vVisible).getOrElse(vGone)

  def itemSpeakerContentStyle(implicit context: ContextWrapper): Tweak[LinearLayout] =
    vMatchWidth +
      llHorizontal +
      vPadding(0, resGetDimensionPixelSize(R.dimen.padding_default_extra_small), 0, 0) +
      llGravity(Gravity.CENTER_VERTICAL)

  def itemSpeakerContentNamesStyle(implicit context: ContextWrapper): Tweak[LinearLayout] =
    vMatchWidth +
      llVertical +
      vPadding(paddingLeft = resGetDimensionPixelSize(R.dimen.padding_default))
}

trait AdapterStyles {

  def itemRootContentStyle(implicit context: ContextWrapper): Tweak[FrameLayout] =
    vMatchWidth +
      flForeground(resGetDrawable(R.drawable.foreground_list_dark))

  def tagFavoriteStyle(implicit context: ContextWrapper): Tweak[ImageView] =
    vWrapContent +
      ivSrc(R.drawable.shedule_tag_favorite) +
      flLayoutGravity(Gravity.RIGHT) +
      vPadding(paddingRight = resGetDimensionPixelSize(R.dimen.padding_default_small))

  def itemHourContentStyle(implicit context: ContextWrapper): Tweak[LinearLayout] =
    lp[LinearLayout](resGetDimensionPixelSize(R.dimen.width_schedule_hour), MATCH_PARENT) +
      llVertical +
      vBackgroundColorResource(R.color.background_list_schedule_hour) +
      llGravity(Gravity.CENTER_HORIZONTAL)

  def itemContentStyle(implicit context: ContextWrapper): Tweak[LinearLayout] =
    vMatchParent +
      llHorizontal

  def hourStyle(implicit context: ContextWrapper): Tweak[TextView] = {
    val padding = resGetDimensionPixelSize(R.dimen.padding_default_small)
    vWrapContent +
      tvSizeResource(R.dimen.text_small) +
      vPadding(0, padding, 0, padding) +
      tvColorResource(R.color.text_schedule_name) +
      tvBold
  }

  def voteActionStyle(implicit context: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      tvText(R.string.vote) +
      tvSizeResource(R.dimen.text_small) +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default_small), 0) +
      vBackgroundColorResource(R.color.primary) +
      tvColorResource(R.color.text_vote) +
      tvAllCaps() +
      vGone

  def voteStyle(implicit context: ContextWrapper): Tweak[ImageView] =
    vWrapContent +
      vGone

  def itemInfoContentStyle(implicit context: ContextWrapper): Tweak[LinearLayout] =
    vMatchWidth +
      llVertical +
      vPaddings(resGetDimensionPixelSize(R.dimen.padding_default), resGetDimensionPixelSize(R.dimen.padding_default_small)) +
      vBackgroundColorResource(R.color.background_list_schedule_info)

  val itemSpeakersContentStyle: Tweak[LinearLayout] =
    vMatchWidth +
      llVertical

  def roomItemStyle(implicit context: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      tvSizeResource(R.dimen.text_small) +
      tvColorResource(R.color.text_schedule_room) +
      vPadding(0, 0, 0, resGetDimensionPixelSize(R.dimen.padding_default_extra_small))

  def trackItemStyle(implicit context: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      tvSizeResource(R.dimen.text_small) +
      tvColorResource(R.color.text_schedule_room) +
      vPadding(0, 0, 0, resGetDimensionPixelSize(R.dimen.padding_default_extra_small))

  def nameItemStyle(implicit context: ContextWrapper): Tweak[TextView] =
    vWrapContent +
      tvSizeResource(R.dimen.text_medium) +
      tvColorResource(R.color.text_schedule_name) +
      tvBold

}
