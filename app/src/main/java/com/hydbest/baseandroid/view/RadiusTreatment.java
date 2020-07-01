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

package com.hydbest.baseandroid.view;

import com.google.android.material.shape.EdgeTreatment;
import com.google.android.material.shape.MarkerEdgeTreatment;
import com.google.android.material.shape.ShapePath;

import androidx.annotation.NonNull;

/**
 * An edge treatment which draws triangles at the midpoint of an edge, facing into or out of the
 * shape.
 *
 * <p>If you want to draw a triangular shape below a circle (similar to a map marker), use {@link
 * MarkerEdgeTreatment} instead.
 */
public class RadiusTreatment extends EdgeTreatment {

    private final float size;

    /**
     * Instantiates a triangle treatment of the given size, which faces inward or outward relative to
     * the shape.
     *
     * @param size the length in pixels that the triangle extends into or out of the shape. The length
     *             of the side of the triangle coincident with the rest of the edge is 2 * size.
     */
    public RadiusTreatment(float size) {
        this.size = size;
    }

    @Override
    public void getEdgePath(float length, float center, float interpolation, @NonNull ShapePath shapePath) {
        shapePath.lineTo(center - (size * interpolation) - 40 , 0);
        shapePath.quadToPoint(center - (size * interpolation) - 20 , 0, center - (size * interpolation), 20);
        shapePath.quadToPoint(center - (size * interpolation) * 0.5f, size * interpolation, center, size * interpolation);
        shapePath.quadToPoint(center + (size * interpolation) * 0.5f, size * interpolation, center + (size * interpolation), 20);
        shapePath.quadToPoint(center + (size * interpolation) + 20, 0, center + (size * interpolation) + 40, 0);
        shapePath.lineTo(length, 0);
    }
}
