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
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.CountdownState
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CountdownScreen(
    currentMillis: Long?,
    totalMillis: Long?,
    countdownState: CountdownState,
    onResumeClick: () -> Unit,
    onPauseClick: () -> Unit,
    onRestartClick: () -> Unit
) {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(currentMillis ?: 0)
    val seconds =
        TimeUnit.MILLISECONDS.toSeconds(currentMillis ?: 0) - TimeUnit.MINUTES.toSeconds(minutes)
    val minuteTenDigit = (minutes / 10).toInt()
    val minuteUnitDigit = (minutes % 10).toInt()
    val secondTenDigit = (seconds / 10).toInt()
    val secondUnitDigit = (seconds % 10).toInt()
    val height = animateDpAsState(
        targetValue = (
            computeProgressPercentage(
                currentMillis,
                totalMillis
            ) * LocalConfiguration.current.screenHeightDp
            ).toInt().dp,
        animationSpec = tween(300)
    ).value
    Box(
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
        )
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                if (countdownState == CountdownState.FINISH) {
                    BlinkTime(minuteTenDigit, minuteUnitDigit, secondTenDigit, secondUnitDigit)
                } else {
                    Time(minuteTenDigit, minuteUnitDigit, secondTenDigit, secondUnitDigit)
                }
                AnimatedVisibility(countdownState != CountdownState.FINISH) {
                    FloatingActionButton(
                        modifier = Modifier.padding(12.dp),
                        backgroundColor = MaterialTheme.colors.background,
                        onClick = {
                            if (countdownState == CountdownState.RUNNING) {
                                onPauseClick()
                            } else if (countdownState == CountdownState.PAUSED) {
                                onResumeClick()
                            }
                        }
                    ) {
                        if (countdownState == CountdownState.RUNNING) {
                            Icon(
                                Icons.Rounded.Pause,
                                contentDescription = "Pause"
                            )
                        } else if (countdownState == CountdownState.PAUSED) {
                            Icon(Icons.Outlined.PlayArrow, contentDescription = "Play")
                        }
                    }
                }
                TextButton(
                    onClick = { onRestartClick() }
                ) {
                    Icon(
                        Icons.Rounded.Refresh,
                        contentDescription = "Refresh",
                        tint = MaterialTheme.colors.onSurface
                    )
                    Text(
                        text = "Restart",
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Time(minuteTenDigit: Int, minuteUnitDigit: Int, secondTenDigit: Int, secondUnitDigit: Int) {
    Row {
        AnimatedNumber(minuteTenDigit)
        AnimatedNumber(minuteUnitDigit)
        Text(text = ":", style = MaterialTheme.typography.h4)
        AnimatedNumber(secondTenDigit)
        AnimatedNumber(secondUnitDigit)
    }
}

@Composable
fun AnimatedNumber(number: Int) {
    var numberOffsetY by remember { mutableStateOf(20f) }
    var previousNumberOffsetY by remember { mutableStateOf(0f) }
    var numberAlpha by remember { mutableStateOf(0f) }
    var previousNumberAlpha by remember { mutableStateOf(1f) }
    val previousNumber = if (number == 9) 0 else number + 1
    LaunchedEffect(number) {
        animate(
            initialValue = 20f,
            targetValue = 0f,
            animationSpec = tween(400)
        ) { animationValue, _ -> numberOffsetY = animationValue }
    }
    LaunchedEffect(number) {
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(400)
        ) { animationValue, _ -> numberAlpha = animationValue }
    }
    LaunchedEffect(number) {
        animate(
            initialValue = 1f,
            targetValue = 0f,
            animationSpec = tween(600)
        ) { animationValue, _ -> previousNumberAlpha = animationValue }
    }
    LaunchedEffect(number) {
        animate(
            initialValue = 0f,
            targetValue = -25f,
            animationSpec = tween(200)
        ) { animationValue, _ -> previousNumberOffsetY = animationValue }
    }
    Box {
        Text(
            text = "$previousNumber", style = MaterialTheme.typography.h4,
            modifier = Modifier
                .alpha(previousNumberAlpha)
                .offset(y = previousNumberOffsetY.toInt().dp)
        )
        Text(
            text = "$number", style = MaterialTheme.typography.h4,
            modifier = Modifier
                .alpha(numberAlpha)
                .offset(y = numberOffsetY.toInt().dp)
        )
    }
}

@Composable
fun BlinkTime(
    minuteTenDigit: Int,
    minuteUnitDigit: Int,
    secondTenDigit: Int,
    secondUnitDigit: Int
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )
    Row {
        Text(
            text = "$minuteTenDigit",
            modifier = Modifier.alpha(alpha),
            style = MaterialTheme.typography.h4
        )
        Text(
            text = "$minuteUnitDigit",
            modifier = Modifier.alpha(alpha),
            style = MaterialTheme.typography.h4
        )
        Text(text = ":", modifier = Modifier.alpha(alpha), style = MaterialTheme.typography.h4)
        Text(
            text = "$secondTenDigit",
            modifier = Modifier.alpha(alpha),
            style = MaterialTheme.typography.h4
        )
        Text(
            text = "$secondUnitDigit",
            modifier = Modifier.alpha(alpha),
            style = MaterialTheme.typography.h4
        )
    }
}

fun computeProgressPercentage(progress: Long?, totalProgress: Long?): Double {
    return progress?.toDouble()?.div(totalProgress?.toDouble() ?: 1.0) ?: 0.0
}
