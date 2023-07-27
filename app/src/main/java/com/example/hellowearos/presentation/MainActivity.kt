/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.hellowearos.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.example.hellowearos.R
import com.example.hellowearos.presentation.theme.HelloWearOsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*setContent {
            WearApp("Android")
        }*/

        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                when (intent.extras?.getString(EXTRA_JOURNEY)) {
                    EXTRA_JOURNEY_CONVERSATION -> {
                        val contact = intent.extras?.getString(EXTRA_CONVERSATION_CONTACT)!!
                        Text("Conversation: $contact")
                    }

                    EXTRA_JOURNEY_NEW -> {
                        Text("New conversation")
                    }

                    EXTRA_JOURNEY_SEARCH -> {
                        Text("Search for a conversation")
                    }

                    else -> Text("Opened from app launcher")
                }
            }
        }
    }

    companion object {
        internal const val EXTRA_JOURNEY = "journey"
        internal const val EXTRA_JOURNEY_CONVERSATION = "journey:conversation"
        internal const val EXTRA_JOURNEY_SEARCH = "journey:search"
        internal const val EXTRA_JOURNEY_NEW = "journey:new"
        internal const val EXTRA_CONVERSATION_CONTACT = "conversation:contact"
    }
}

@Composable
fun WearApp() {
    HelloWearOsTheme {
        val listState = rememberScalingLazyListState()

        Scaffold(
            timeText = {
                TimeText(modifier = Modifier.scrollAway(listState))
            },
            vignette = {
                // Only show a Vignette for scrollable screens. This code lab only has one screen,
                // which is scrollable, so we show it all the time.
                Vignette(vignettePosition = VignettePosition.TopAndBottom)
            },
            positionIndicator = {
                PositionIndicator(
                    scalingLazyListState = listState
                )
            }) {

            val contentModifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            val iconModifier = Modifier
                .size(24.dp)
                .wrapContentSize(align = Alignment.Center)

            ScalingLazyColumn(
                modifier = Modifier.fillMaxSize(),
                autoCentering = AutoCenteringParams(itemIndex = 0),
                state = listState
            ) {
                item {
                    CustomButton(
                        onClick = { },
                        contentModifier,
                        iconModifier
                    )
                }
                item { CustomText(contentModifier) }
                item {
                    CustomCard(
                        onClick = { },
                        contentModifier,
                        iconModifier
                    )
                }

                item { CustomChip(contentModifier, iconModifier) }
                item { CustomToggleChip(contentModifier) }
            }
        }
    }

}


@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}