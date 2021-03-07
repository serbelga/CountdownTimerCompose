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
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.androiddevchallenge.screen.CountdownScreen
import com.example.androiddevchallenge.screen.SelectionScreen
import com.example.androiddevchallenge.ui.theme.CountdownTimerTheme

class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountdownTimerTheme {
                val countdownState by mainViewModel.countdownState.observeAsState()
                val currentMillis by mainViewModel.currentMillis.observeAsState()
                val totalMillis by mainViewModel.totalMillis.observeAsState()
                Surface {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .height(56.dp)
                            .fillMaxWidth()
                            .zIndex(1f)
                    ) {
                        Icon(Icons.Rounded.HourglassEmpty, contentDescription = "")
                        Text(text = "Timer", style = MaterialTheme.typography.h6, modifier = Modifier.padding(start = 8.dp))
                    }
                    Crossfade(
                        targetState = countdownState
                    ) { state ->
                        when (state) {
                            CountdownState.IDLE -> SelectionScreen(
                                onPlayClick = {
                                    mainViewModel.play(it)
                                }
                            )
                            CountdownState.RUNNING, CountdownState.PAUSED, CountdownState.FINISH ->
                                CountdownScreen(
                                    currentMillis, totalMillis, state,
                                    onRestartClick = {
                                        mainViewModel.restart()
                                    },
                                    onResumeClick = {
                                        mainViewModel.resume()
                                    },
                                    onPauseClick = {
                                        mainViewModel.pause()
                                    }
                                )
                        }
                    }
                }
            }
        }
    }
}
