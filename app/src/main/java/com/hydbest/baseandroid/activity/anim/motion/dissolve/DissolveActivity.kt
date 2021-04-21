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

package com.hydbest.baseandroid.activity.anim.motion.dissolve

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.observe
import androidx.transition.TransitionManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.activity.anim.motion.EdgeToEdge
import com.hydbest.baseandroid.activity.anim.motion.FAST_OUT_SLOW_IN

class DissolveActivity : AppCompatActivity() {

    private val viewModel: DissolveViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dissolve_activity)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val image: ImageView = findViewById(R.id.image)
        val card: CardView = findViewById(R.id.card)
        val next: Button = findViewById(R.id.next)

        setSupportActionBar(toolbar)
        EdgeToEdge.setUpRoot(findViewById(R.id.root))
        EdgeToEdge.setUpAppBar(findViewById(R.id.app_bar), toolbar)
        EdgeToEdge.setUpScrollingContent(findViewById(R.id.content))

        // This is the transition we use for dissolve effect of the image view.
        val dissolve = Dissolve().apply {
            addTarget(image)
            duration = 200L
            interpolator = FAST_OUT_SLOW_IN
        }
        viewModel.image.observe(this) { resId ->
            // This delays the dissolve to be invoked at the next draw frame.
            TransitionManager.beginDelayedTransition(card, dissolve)
            // Here, we are simply changing the image shown on the image view. The animation is
            // handled by the transition API.
            image.setImageResource(resId)
        }

        card.setOnClickListener { viewModel.nextImage() }
        next.setOnClickListener { viewModel.nextImage() }
    }
}
