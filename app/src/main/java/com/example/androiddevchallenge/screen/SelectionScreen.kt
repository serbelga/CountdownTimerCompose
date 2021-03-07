/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SelectionScreen(onPlayClick: (Long) -> Unit) {
    var minuteTenDigit by remember { mutableStateOf(0) }
    var minuteUnitDigit by remember { mutableStateOf(0) }
    var secondTenDigit by remember { mutableStateOf(0) }
    var secondUnitDigit by remember { mutableStateOf(0) }
    val playVisible =
        minuteTenDigit > 0 || minuteUnitDigit > 0 || secondTenDigit > 0 || secondUnitDigit > 0
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 240.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(120.dp)
            ) {
                SingleSelector(minuteTenDigit, last = 5) { minuteTenDigit = it }
                SingleSelector(minuteUnitDigit, last = 9) { minuteUnitDigit = it }
                Text(
                    text = ":",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp, end = 8.dp)
                )
                SingleSelector(secondTenDigit, last = 5) { secondTenDigit = it }
                SingleSelector(secondUnitDigit, last = 9) { secondUnitDigit = it }
            }
            AnimatedVisibility(
                visible = playVisible,
                enter = expandIn(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessHigh
                    )
                ) + fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        val minutesInMillis = (minuteTenDigit * 10 + minuteUnitDigit) * 60 * 1000
                        val secondsInMillis = (secondTenDigit * 10 + secondUnitDigit) * 1000
                        val millis = minutesInMillis + secondsInMillis
                        onPlayClick(millis.toLong())
                    },
                    modifier = Modifier.padding(top = 128.dp, bottom = 24.dp)
                ) {
                    Icon(Icons.Outlined.PlayArrow, contentDescription = "Play")
                }
            }
        }
    }
}

@Composable
fun SingleSelector(
    digit: Int,
    last: Int,
    modifier: Modifier = Modifier,
    onValueChange: (Int) -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = {
                if (digit < last) {
                    onValueChange(digit + 1)
                }
            },
            modifier = Modifier.alpha(if (digit < last) 1f else 0f)
        ) {
            Icon(Icons.Rounded.KeyboardArrowUp, contentDescription = "Up")
        }
        Text(
            text = "$digit",
            style = MaterialTheme.typography.h5
        )
        IconButton(
            onClick = {
                if (digit > 0) {
                    onValueChange(digit - 1)
                }
            },
            modifier = Modifier.alpha(if (digit > 0) 1f else 0f)
        ) {
            Icon(Icons.Rounded.KeyboardArrowDown, contentDescription = "Down")
        }
    }
}
