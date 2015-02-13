/*
 * Copyright (C) 2015 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fortysevendeg.android.scaladays.ui.commons

import android.support.v7.widget.Toolbar
import android.view.{View, ViewGroup, ContextThemeWrapper}
import com.fortysevendeg.android.scaladays.R
import macroid.FullDsl._
import macroid.LayoutBuildingMacros._
import macroid.{ActivityContext, AppContext, Ui}
import scala.language.postfixOps

trait ToolbarLayout
    extends ToolbarStyles {

  var toolBar = slot[Toolbar]

  def toolBarLayout(children: Ui[View]*)(implicit appContext: AppContext, activityContext: ActivityContext): Ui[Toolbar] =
    Ui {
      val darkToolBar = getToolbarThemeDarkActionBar
      children foreach (uiView => darkToolBar.addView(uiView.get))
      toolBar = Some(darkToolBar)
      darkToolBar
    } <~ toolbarStyle(56 dp)

  def expandedToolBarLayout(children: Ui[View]*)
      (height: Int)
      (implicit appContext: AppContext, activityContext: ActivityContext): Ui[Toolbar] =
    Ui {
      val darkToolBar = getToolbarThemeDarkActionBar
      children foreach (uiView => darkToolBar.addView(uiView.get))
      toolBar = Some(darkToolBar)
      darkToolBar
    } <~ toolbarStyle(height)


  private def getToolbarThemeDarkActionBar(implicit activityContext: ActivityContext) = {
    val contextTheme = new ContextThemeWrapper(activityContext.get, R.style.ThemeOverlay_AppCompat_Dark_ActionBar)
    val darkToolBar = new Toolbar(contextTheme)
    darkToolBar.setPopupTheme(R.style.ThemeOverlay_AppCompat_Light)
    darkToolBar
  }

}
