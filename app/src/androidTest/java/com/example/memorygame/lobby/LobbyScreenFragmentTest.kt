/*
 * Copyright 2020 aliceresponde. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.example.memorygame.lobby

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.memorygame.R
import com.example.memorygame.R.id.button_new_game
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class LobbyScreenFragmentTest {

    @Before
    fun setUp() {
        launchFragmentInContainer<LobbyScreenFragment>()
    }

    @Test
    fun testLobbyScreenIsDisplayed() {
        onView(withId(button_new_game)).check(matches(isDisplayed()))
    }

    @Test
    fun testDifficultyLevelBottomSheetIsDisplayed() {
        onView(withId(button_new_game)).perform(click())
        onView(withId(R.id.text_sheet_difficulty_selection_title)).check(matches(isDisplayed()))
    }

}