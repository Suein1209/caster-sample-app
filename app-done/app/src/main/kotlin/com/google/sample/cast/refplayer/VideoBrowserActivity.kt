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

import com.google.android.gms.cast.framework.CastButtonFactory
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastState
import com.google.android.gms.cast.framework.CastStateListener
import com.google.android.gms.cast.framework.IntroductoryOverlay
import com.google.sample.cast.refplayer.settings.CastPreference

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View

class VideoBrowserActivity : AppCompatActivity() {
    private val mIsHoneyCombOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
    private var mToolbar: androidx.appcompat.widget.Toolbar? = null
    private var mCastContext: CastContext? = null
    private var mediaRouteMenuItem: MenuItem? = null
    private var mIntroductoryOverlay: IntroductoryOverlay? = null
    private var mCastStateListener: CastStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_browser)
        setupActionBar()
        mCastStateListener = object : CastStateListener {
            override fun onCastStateChanged(newState: Int) {
                if (newState != CastState.NO_DEVICES_AVAILABLE) {
                    showIntroductoryOverlay()
                }
            }
        }
        mCastContext = CastContext.getSharedInstance(this)
    }

    override fun onResume() {
        super.onResume()
        mCastContext?.addCastStateListener(mCastStateListener!!)

    }

    override fun onPause() {
        super.onPause()
        mCastContext?.removeCastStateListener(mCastStateListener!!)

    }

    private fun setupActionBar() {
        mToolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar?
        mToolbar?.setTitle(R.string.app_name)
        setSupportActionBar(mToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.browse, menu)
        mediaRouteMenuItem = CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu,
                R.id.media_route_menu_item)
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

    private fun showIntroductoryOverlay() {
        mIntroductoryOverlay?.remove()
        if (mediaRouteMenuItem?.isVisible == true) {
            Looper.myLooper().run {
                mIntroductoryOverlay = com.google.android.gms.cast.framework.IntroductoryOverlay.Builder(
                        this@VideoBrowserActivity, mediaRouteMenuItem!!)
                        .setTitleText("Introducing Cast")
                        .setSingleTime()
                        .setOnOverlayDismissedListener(
                                object : IntroductoryOverlay.OnOverlayDismissedListener {
                                    override fun onOverlayDismissed() {
                                        mIntroductoryOverlay = null
                                    }
                                })
                        .build()
                mIntroductoryOverlay!!.show()
            }
        }
    }

    companion object {
        private const val TAG = "VideoBrowserActivity"
    }
}