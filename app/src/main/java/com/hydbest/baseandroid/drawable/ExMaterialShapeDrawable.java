/*
 * Copyright 2017 The Android Open Source Project
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

package com.hydbest.baseandroid.drawable;

import android.annotation.SuppressLint;

import com.google.android.material.shape.MaterialShapeDrawable;

/**
 * Base drawable class for Material Shapes that handles shadows, elevation, scale and color for a
 * generated path.
 */
@SuppressLint("RestrictedApi")
public class ExMaterialShapeDrawable extends MaterialShapeDrawable {

    {
        setShadowCompatibilityMode(SHADOW_COMPAT_MODE_ALWAYS);
    }

    @Override
    public boolean requiresCompatShadow() {
        return true;
    }
}
