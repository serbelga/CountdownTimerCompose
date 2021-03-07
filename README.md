# Countdown Timer

![Workflow result](https://github.com/serbelga/CountdownTimerCompose/workflows/Check/badge.svg)

## :scroll: Description

An application built with Jetpack Compose that allows you to set a timer (minutes and seconds).

## :bulb: Motivation and Context

The countdown timer is made up by a selection screen where user can select a value for minutes and seconds. The 'Play FAB' is only visible if user select a countdown value greater than 0:00. This change in visibility is based on a 'fade in' and 'expand in' animation.

Once Play FAB is clicked, the countdown begins and an animated progress bar that fills the screen size will shrink with the progress. User can see the time remaining and every second the text is updated with an animation. The countdown can be also paused or restarted to set another value. 

Finally, when countdown reaches 0:00, an infinite animation is launched and user will see the ‘0:00’ with a blinking effect.

## :camera_flash: Screenshots

<img src="/results/screenshot_1.png" width="260">&emsp;<img src="/results/screenshot_2.png" width="260">

## License
```
Copyright 2020 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
