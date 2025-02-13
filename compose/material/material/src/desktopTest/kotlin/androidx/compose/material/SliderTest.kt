/*
 * Copyright 2022 The Android Open Source Project
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

package androidx.compose.material

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertRangeInfoEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.roundToInt
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(InternalComposeUiApi::class)
class SliderTest {

    @get:Rule
    val rule = createComposeRule()

    @Before
    fun before() {
        rule.mainClock.autoAdvance = true
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun `slider(0 steps, ltr) changes values when arrows pressed`() {
        val state = mutableStateOf(0.5f)
        var sliderFocused = false
        rule.setContent {
            Slider(
                value = state.value,
                onValueChange = { state.value = it },
                valueRange = 0f..1f,
                modifier = Modifier.onFocusChanged {
                    sliderFocused = it.isFocused
                }
            )
        }

        // Press tab to focus on Slider
        rule.onRoot().performKeyPress(KeyEvent(Key.Tab, KeyEventType.KeyDown))
        rule.onRoot().performKeyPress(KeyEvent(Key.Tab, KeyEventType.KeyUp))
        rule.runOnIdle {
            Assert.assertTrue(sliderFocused)
        }

        repeat(3) {
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionRight, KeyEventType.KeyDown))
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionRight, KeyEventType.KeyUp))
            rule.runOnIdle {
                Assert.assertEquals((0.50f + (1 + it) / 100f).round2decPlaces(), (state.value).round2decPlaces())
            }
        }

        repeat(3) {
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionLeft, KeyEventType.KeyDown))
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionLeft, KeyEventType.KeyUp))
            rule.runOnIdle {
                Assert.assertEquals((0.53f - (1 + it) / 100f).round2decPlaces(), (state.value).round2decPlaces())
            }
        }

        repeat(3) {
            rule.onRoot().performKeyPress(KeyEvent(Key.PageDown, KeyEventType.KeyDown))
            rule.onRoot().performKeyPress(KeyEvent(Key.PageDown, KeyEventType.KeyUp))
            rule.runOnIdle {
                Assert.assertEquals((0.50f + (1 + it) / 10f).round2decPlaces(), (state.value).round2decPlaces())
            }
        }

        repeat(3) {
            rule.onRoot().performKeyPress(KeyEvent(Key.PageUp, KeyEventType.KeyDown))
            rule.onRoot().performKeyPress(KeyEvent(Key.PageUp, KeyEventType.KeyUp))
            rule.runOnIdle {
                Assert.assertEquals((0.80f - (1 + it) / 10f).round2decPlaces(), (state.value).round2decPlaces())
            }
        }

        repeat(3) {
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionUp, KeyEventType.KeyDown))
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionUp, KeyEventType.KeyUp))
            rule.runOnIdle {
                Assert.assertEquals((0.50f + (1 + it) / 100f).round2decPlaces(), (state.value).round2decPlaces())
            }
        }

        repeat(3) {
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionDown, KeyEventType.KeyDown))
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionDown, KeyEventType.KeyUp))
            rule.runOnIdle {
                Assert.assertEquals((0.53f - (1 + it) / 100f).round2decPlaces(), (state.value).round2decPlaces())
            }
        }

        rule.onRoot().performKeyPress(KeyEvent(Key.MoveEnd, KeyEventType.KeyDown))
        rule.onRoot().performKeyPress(KeyEvent(Key.MoveEnd, KeyEventType.KeyUp))
        rule.runOnIdle {
            Assert.assertEquals(1f, state.value)
        }

        rule.onRoot().performKeyPress(KeyEvent(Key.Home, KeyEventType.KeyDown))
        rule.onRoot().performKeyPress(KeyEvent(Key.Home, KeyEventType.KeyUp))
        rule.runOnIdle {
            Assert.assertEquals(0f, state.value)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun `slider(0 steps, rtl) changes values when arrows pressed`() {
        val state = mutableStateOf(0.5f)
        var sliderFocused = false
        rule.setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                Slider(
                    value = state.value,
                    onValueChange = { state.value = it },
                    valueRange = 0f..1f,
                    modifier = Modifier.onFocusChanged {
                        sliderFocused = it.isFocused
                    }
                )
            }
        }

        // Press tab to focus on Slider
        rule.onRoot().performKeyPress(KeyEvent(Key.Tab, KeyEventType.KeyDown))
        rule.onRoot().performKeyPress(KeyEvent(Key.Tab, KeyEventType.KeyUp))
        rule.runOnIdle {
            Assert.assertTrue(sliderFocused)
        }

        repeat(3) {
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionRight, KeyEventType.KeyDown))
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionRight, KeyEventType.KeyUp))
            rule.runOnIdle {
                Assert.assertEquals((0.50f - (1 + it) / 100f).round2decPlaces(), (state.value).round2decPlaces())
            }
        }

        repeat(3) {
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionLeft, KeyEventType.KeyDown))
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionLeft, KeyEventType.KeyUp))
            rule.runOnIdle {
                Assert.assertEquals((0.47f + (1 + it) / 100f).round2decPlaces(), (state.value).round2decPlaces())
            }
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Test
    fun `slider(29 steps, ltr) changes values when arrows pressed`() {
        val state = mutableStateOf(15f)
        var sliderFocused = false
        rule.setContent {
            Slider(
                value = state.value,
                steps = 29,
                onValueChange = { state.value = it },
                valueRange = 0f..30f,
                modifier = Modifier.onFocusChanged {
                    sliderFocused = it.isFocused
                }
            )
        }

        // Press tab to focus on Slider
        rule.onRoot().performKeyPress(KeyEvent(Key.Tab, KeyEventType.KeyDown))
        rule.onRoot().performKeyPress(KeyEvent(Key.Tab, KeyEventType.KeyUp))
        rule.runOnIdle {
            Assert.assertTrue(sliderFocused)
        }

        repeat(3) {
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionRight, KeyEventType.KeyDown))
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionRight, KeyEventType.KeyUp))
            rule.runOnIdle {
                Assert.assertEquals((15f + (1f + it)), (state.value))
            }
        }

        repeat(3) {
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionLeft, KeyEventType.KeyDown))
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionLeft, KeyEventType.KeyUp))
            rule.runOnIdle {
                Assert.assertEquals((18f - (1 + it)), state.value)
            }
        }

        rule.runOnIdle {
            state.value = 0f
        }

        val page = ((29 + 1) / 10).coerceIn(1, 10) // same logic as in Slider slideOnKeyEvents

        repeat(3) {
            rule.onRoot().performKeyPress(KeyEvent(Key.PageDown, KeyEventType.KeyDown))
            rule.onRoot().performKeyPress(KeyEvent(Key.PageDown, KeyEventType.KeyUp))
            rule.runOnIdle {
                Assert.assertEquals((1f + it) * page, state.value)
            }
        }

        rule.runOnIdle {
            state.value = 30f
        }

        repeat(3) {
            rule.onRoot().performKeyPress(KeyEvent(Key.PageUp, KeyEventType.KeyDown))
            rule.onRoot().performKeyPress(KeyEvent(Key.PageUp, KeyEventType.KeyUp))
            rule.runOnIdle {
                Assert.assertEquals(30f - (1 + it) * page, state.value)
            }
        }

        rule.runOnIdle {
            state.value = 0f
        }

        repeat(3) {
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionUp, KeyEventType.KeyDown))
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionUp, KeyEventType.KeyUp))
            rule.runOnIdle {
                Assert.assertEquals(1f + it, state.value)
            }
        }

        repeat(3) {
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionDown, KeyEventType.KeyDown))
            rule.onRoot().performKeyPress(KeyEvent(Key.DirectionDown, KeyEventType.KeyUp))
            rule.runOnIdle {
                Assert.assertEquals(3f - (1f + it), state.value)
            }
        }

        rule.onRoot().performKeyPress(KeyEvent(Key.MoveEnd, KeyEventType.KeyDown))
        rule.onRoot().performKeyPress(KeyEvent(Key.MoveEnd, KeyEventType.KeyUp))
        rule.runOnIdle {
            Assert.assertEquals(30f, state.value)
        }

        rule.onRoot().performKeyPress(KeyEvent(Key.Home, KeyEventType.KeyDown))
        rule.onRoot().performKeyPress(KeyEvent(Key.Home, KeyEventType.KeyUp))
        rule.runOnIdle {
            Assert.assertEquals(0f, state.value)
        }
    }

    @Test
    fun `Slider should request focus on Tap`() {
        var hasFocus = false
        rule.mainClock.autoAdvance = false
        rule.setContent {
            Slider(
                value = 0.1f,
                onValueChange = {},
                modifier = Modifier.onFocusChanged {
                    hasFocus = it.isFocused
                }.testTag("slider")
            )
        }
        rule.mainClock.advanceTimeByFrame()

        rule.onNodeWithTag("slider").performTouchInput {
            down(Offset(10f, 5f))
            up()
        }
        rule.mainClock.advanceTimeByFrame()

        rule.runOnIdle {
            Assert.assertEquals(true, hasFocus)
        }
    }

    @Test
    fun sliderPosition_valueCoercion() {
        val state = mutableStateOf(0f)
        rule.setContent {
            Slider(
                modifier = Modifier.testTag("slider"),
                value = state.value,
                onValueChange = { state.value = it },
                valueRange = 0f..1f
            )
        }
        rule.runOnIdle {
            state.value = 2f
        }
        rule.onNodeWithTag("slider").assertRangeInfoEquals(ProgressBarRangeInfo(1f, 0f..1f, 0))
        rule.runOnIdle {
            state.value = -123145f
        }
        rule.onNodeWithTag("slider").assertRangeInfoEquals(ProgressBarRangeInfo(0f, 0f..1f, 0))
    }
}

private fun Float.round2decPlaces() = (this * 100).roundToInt() / 100f
