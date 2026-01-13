package com.conrad.weather

import com.conrad.weather.ui.common.components.SearchTextField

import android.view.KeyEvent.KEYCODE_ENTER

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performKeyPress
import androidx.compose.ui.test.performTextInput

import org.junit.Rule
import org.junit.Test
import org.junit.Assert

class SearchTextFieldTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val onSearchQueryChange = SearchTracker()
    private val onSearchTriggered = SearchTracker()

    @Test
    fun searchTextField_updatesOnQueryChange() {
        // Given
        composeTestRule.setContent {
            SearchTextField(
                searchQuery = "",
                onSearchQueryChange = onSearchQueryChange::onTriggered,
                onSearchTriggered = { }
            )
        }

        // When
        composeTestRule.onNodeWithTag("SearchTextField").performTextInput("Test")

        // Then
        Assert.assertTrue(onSearchQueryChange.wasCalled)
        Assert.assertEquals("Test", onSearchQueryChange.calledWithQuery)
    }

    @Test
    fun searchTextField_triggersSearchOnEnter() {
        // Given
        composeTestRule.setContent {
            SearchTextField(
                searchQuery = "Test",
                onSearchQueryChange = { },
                onSearchTriggered = onSearchTriggered::onTriggered
            )
        }

        // When
        composeTestRule.onNodeWithTag("SearchTextField").performClick()
        composeTestRule.onNodeWithTag("SearchTextField").assertIsFocused().performKeyPress(
            KeyEvent(NativeKeyEvent(KEYCODE_ENTER, KEYCODE_ENTER))
        )

        // Then
        Assert.assertTrue(onSearchTriggered.wasCalled)
        Assert.assertEquals("Test", onSearchTriggered.calledWithQuery)
    }

    @Test
    fun searchTextField_triggersSearchOnSearchAction() {
        // Given
        composeTestRule.setContent {
            SearchTextField(
                searchQuery = "Test",
                onSearchQueryChange = { },
                onSearchTriggered = onSearchTriggered::onTriggered
            )
        }

        // When
        composeTestRule.onNodeWithTag("SearchTextField").performImeAction()

        // Then
        Assert.assertTrue(onSearchTriggered.wasCalled)
        Assert.assertEquals("Test", onSearchTriggered.calledWithQuery)
    }

    @Test
    fun searchTextField_clearsQueryOnClickOfClearIcon() {
        // Given
        composeTestRule.setContent {
            SearchTextField(
                searchQuery = "Test",
                onSearchQueryChange = onSearchQueryChange::onTriggered,
                onSearchTriggered = { }
            )
        }

        // When
        composeTestRule.onNodeWithContentDescription("Clear").performClick()

        // Then
        Assert.assertTrue(onSearchQueryChange.wasCalled)
        Assert.assertEquals("", onSearchQueryChange.calledWithQuery)
    }

    @Test
    fun searchTextField_doesNotTriggerSearchIfQueryIsTooShort() {
        // Given
        composeTestRule.setContent {
            SearchTextField(
                searchQuery = "Te",
                onSearchQueryChange = { },
                onSearchTriggered = onSearchTriggered::onTriggered
            )
        }

        // When
        composeTestRule.onNodeWithTag("SearchTextField").performClick()
        composeTestRule.onNodeWithTag("SearchTextField").assertIsFocused().performKeyPress(
            KeyEvent(NativeKeyEvent(KEYCODE_ENTER, KEYCODE_ENTER))
        )

        // Then
        Assert.assertFalse(onSearchTriggered.wasCalled)
    }
}

private class SearchTracker {
    var wasCalled = false
    var calledWithQuery: String? = null

    fun onTriggered(query: String) {
        wasCalled = true
        calledWithQuery = query
    }
}