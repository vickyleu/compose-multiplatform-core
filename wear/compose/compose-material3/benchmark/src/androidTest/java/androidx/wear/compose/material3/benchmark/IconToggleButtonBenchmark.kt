/*
 * Copyright 2024 The Android Open Source Project
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

package androidx.wear.compose.material3.benchmark

import androidx.compose.runtime.Composable
import androidx.compose.testutils.LayeredComposeTestCase
import androidx.compose.testutils.benchmark.ComposeBenchmarkRule
import androidx.compose.testutils.benchmark.benchmarkToFirstPixel
import androidx.test.filters.MediumTest
import androidx.wear.compose.material3.IconToggleButton
import androidx.wear.compose.material3.MaterialTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@MediumTest
@RunWith(Parameterized::class)
class IconToggleButtonBenchmark(private val type: IconToggleButtonType) {

    companion object {
        @Parameterized.Parameters(name = "{0}")
        @JvmStatic
        fun parameters() = IconToggleButtonType.values()
    }

    @get:Rule val benchmarkRule = ComposeBenchmarkRule()

    private val testCaseFactory = { IconToggleButtonTestCase(type) }

    @Test
    fun first_pixel() {
        benchmarkRule.benchmarkToFirstPixel(testCaseFactory)
    }
}

internal class IconToggleButtonTestCase(
    private val type: IconToggleButtonType,
) : LayeredComposeTestCase() {

    @Composable
    override fun MeasuredContent() {
        when (type) {
            IconToggleButtonType.IconToggleButtonOn -> {
                IconToggleButton(
                    checked = true,
                    onCheckedChange = {},
                ) {
                    StandardIcon()
                }
            }
            IconToggleButtonType.IconToggleButtonOff -> {
                IconToggleButton(
                    checked = false,
                    onCheckedChange = {},
                ) {
                    StandardIcon()
                }
            }
        }
    }

    @Composable
    override fun ContentWrappers(content: @Composable () -> Unit) {
        MaterialTheme { content() }
    }
}

enum class IconToggleButtonType {
    IconToggleButtonOn,
    IconToggleButtonOff
}
