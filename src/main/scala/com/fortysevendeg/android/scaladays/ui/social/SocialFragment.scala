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

package com.fortysevendeg.android.scaladays.ui.social

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.{LayoutInflater, View, ViewGroup}
import com.fortysevendeg.android.scaladays.R
import com.fortysevendeg.android.scaladays.model.TwitterMessage
import com.fortysevendeg.android.scaladays.modules.ComponentRegistryImpl
import com.fortysevendeg.android.scaladays.modules.twitter.SearchRequest
import com.fortysevendeg.android.scaladays.ui.commons.{ListLayout, LineItemDecorator, UiServices}
import com.fortysevendeg.macroid.extras.RecyclerViewTweaks._
import com.fortysevendeg.macroid.extras.ResourcesExtras._
import macroid.FullDsl._
import macroid.{AppContext, Contexts, Ui}

import scala.concurrent.ExecutionContext.Implicits.global
import com.fortysevendeg.android.scaladays.ui.commons.IntegerResults._

class SocialFragment
  extends Fragment
  with Contexts[Fragment]
  with ComponentRegistryImpl
  with UiServices {

  override implicit lazy val appContextProvider: AppContext = fragmentAppContext

  private var fragmentLayout: Option[ListLayout] = None

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val fLayout = new ListLayout
    fragmentLayout = Some(fLayout)
    runUi(
      (fLayout.recyclerView
        <~ rvLayoutManager(new LinearLayoutManager(appContextProvider.get))
        <~ rvAddItemDecoration(new LineItemDecorator())) ~
        (fLayout.reloadButton <~ On.click(Ui {
          search()
        })))
    fLayout.content
  }

  override def onViewCreated(view: View, savedInstanceState: Bundle): Unit = {
    super.onViewCreated(view, savedInstanceState)

    if (twitterServices.isConnected()) {
      search()
    } else {
      val intent = new Intent(getActivity, classOf[AuthorizationActivity])
      startActivityForResult(intent, authResult)
    }
  }

  override def onActivityResult(requestCode: Int, resultCode: Int, data: Intent): Unit = {
    super.onActivityResult(requestCode, resultCode, data)
    requestCode match {
      case request if request == authResult =>
        resultCode match {
          case Activity.RESULT_OK =>
            search()
          case Activity.RESULT_CANCELED =>
            fragmentLayout map (_.failed())
        }
    }
  }

  def search() = {
    fragmentLayout map (_.loading())
    val result = for {
      conference <- loadSelectedConference()
      searchResponse <- twitterServices.search(SearchRequest(conference.info.hashTag))
    } yield reloadList(searchResponse.messages)

    result.recover {
      case _ => fragmentLayout map (_.failed())
    }
  }

  def reloadList(messages: Seq[TwitterMessage]) = {
    messages.length match {
      case 0 => fragmentLayout map (_.empty())
      case _ =>
        val adapter = new SocialAdapter(messages, new RecyclerClickListener {
          override def onClick(message: TwitterMessage): Unit = {
            startActivity(new Intent(Intent.ACTION_VIEW,
              Uri.parse(resGetString(R.string.url_twitter_status, message.screenName, message.id.toString))))
          }
        })
        fragmentLayout map (_.adapter(adapter))
    }
  }

}