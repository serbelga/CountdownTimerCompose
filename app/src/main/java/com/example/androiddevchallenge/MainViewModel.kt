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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var _countDownState: MutableLiveData<CountdownState> = MutableLiveData(CountdownState.IDLE)
    val countdownState: LiveData<CountdownState> = _countDownState

    private var job: Job? = null

    private var _totalMillis: MutableLiveData<Long> = MutableLiveData(0)
    val totalMillis: LiveData<Long> = _totalMillis

    private var _currentMillis: MutableLiveData<Long> = MutableLiveData(0)
    val currentMillis: LiveData<Long> = _currentMillis

    fun play(millis: Long) {
        if (millis <= 0) return
        _totalMillis.value = millis
        _currentMillis.value = millis
        resume()
    }

    fun restart() {
        job?.cancel()
        _currentMillis.value = 0
        _totalMillis.value = 0
        _countDownState.value = CountdownState.IDLE
    }

    fun resume() {
        job?.cancel()
        job = viewModelScope.launch {
            _countDownState.value = CountdownState.RUNNING
            while (currentMillis.value ?: 0 > 0) {
                delay(1000)
                if (countdownState.value == CountdownState.RUNNING) {
                    currentMillis.value?.let {
                        _currentMillis.value = it - 1000
                    }
                }
            }
            _countDownState.value = CountdownState.FINISH
        }
    }

    fun pause() {
        job?.cancel()
        _countDownState.value = CountdownState.PAUSED
    }
}
