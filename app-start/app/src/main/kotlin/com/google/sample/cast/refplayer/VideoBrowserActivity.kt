/*
 * Copyright (C) 2022 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.sample.cast.refplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.sample.cast.refplayer.settings.CastPreference

class VideoBrowserActivity : AppCompatActivity() {
    private var mToolbar: Toolbar? = null

    private var mCastContext: CastContext? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_browser)
        setupActionBar()

        //Cat context 를 초기화 한다.
        mCastContext = CastContext.getSharedInstance(this)
    }

    private fun setupActionBar() {
        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar
        mToolbar?.setTitle(R.string.app_name)
        setSupportActionBar(mToolbar)
    }

    private var mediaRouteMenuItem: MenuItem? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.browse, menu)

        //해당 하는 resouce 를 메뉴에 추가한다.
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(applicationContext, menu, R.id.media_route_menu_item)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i: Intent
        when (item.itemId) {
            R.id.action_settings -> {
                i = Intent(this@VideoBrowserActivity, CastPreference::class.java)
                startActivity(i)
            }
        }
        return true
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy is called")
        super.onDestroy()
    }

    companion object {
        private const val TAG = "VideoBrowserActivity"
    }
}