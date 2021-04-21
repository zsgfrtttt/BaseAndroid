/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hydbest.baseandroid.activity.anim.motion.fadethrough

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.activity.anim.MEDIUM_COLLAPSE_DURATION
import com.hydbest.baseandroid.activity.anim.MEDIUM_EXPAND_DURATION
import com.hydbest.baseandroid.activity.anim.motion.EdgeToEdge
import com.hydbest.baseandroid.activity.anim.motion.fadeThrough

class FadeThroughActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fade_through_activity)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val card: CardView = findViewById(R.id.card)
        val contact: ConstraintLayout = findViewById(R.id.card_contact)
        val cheese: ConstraintLayout = findViewById(R.id.card_cheese)
        val toggle: Button = findViewById(R.id.toggle)
        val icon: ImageView = findViewById(R.id.contact_icon)

        // Set up the layout.
        setSupportActionBar(toolbar)
        EdgeToEdge.setUpRoot(findViewById(R.id.root))
        EdgeToEdge.setUpAppBar(findViewById(R.id.app_bar), toolbar)
        EdgeToEdge.setUpScrollingContent(findViewById(R.id.content))
        Glide.with(icon).load(R.drawable.cheese_2).apply(RequestOptions().transform(CircleCrop())).into(icon)

        // This is the transition we use for the fade-through effect.
        val fadeThrough = fadeThrough()

        toggle.setOnClickListener {
            // We are only toggling the visibilities of the card contents here.
            if (contact.isVisible) {
                // Delays the fade-through transition until the layout change below takes effect.
                TransitionManager.beginDelayedTransition(
                    card,
                    fadeThrough.setDuration(MEDIUM_EXPAND_DURATION)
                )
                contact.isVisible = false
                cheese.isVisible = true
            } else {
                TransitionManager.beginDelayedTransition(
                    card,
                    fadeThrough.setDuration(MEDIUM_COLLAPSE_DURATION)
                )
                contact.isVisible = true
                cheese.isVisible = false
            }
        }
    }

}
