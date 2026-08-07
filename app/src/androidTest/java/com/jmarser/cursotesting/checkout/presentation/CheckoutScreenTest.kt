package com.jmarser.cursotesting.checkout.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Assert.*
import org.junit.Rule
import kotlin.test.Test

class CheckoutScreenTest {

    @get: Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private fun createCheckoutScreen(

    ){
        composeRule.setContent {

        }
    }

    @Test
    fun givenLoadingState_whenRendered_thenShowsProgress() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    fun givenIdleStateWithEmptyCart_whenRendered_thenConfirmButtonDisabled() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    fun givenIdleStateWithValidForm_whenRendered_thenConfirmButtonEnabled() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    fun givenIdleState_whenTypingInvalidEmail_thenConfirmButtonDisabled() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    fun givenSuccessState_whenRendered_thenShowsOrderConfirmation() {
        // GIVEN

        // WHEN

        // THEN
    }

    @Test
    fun givenErrorState_whenRetryClicked_thenInvokesRetryCallback() {
        // GIVEN

        // WHEN

        // THEN
    }

}