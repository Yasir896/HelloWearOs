package com.example.hellowearos.presentation.tiles.messaging

import androidx.wear.tiles.ActionBuilders
import androidx.wear.tiles.ModifiersBuilders
import com.example.hellowearos.presentation.MainActivity
import com.example.hellowearos.presentation.model.Contact

internal fun launchActivityClickable(
    clickableId: String,
    androidActivity: ActionBuilders.AndroidActivity
) = ModifiersBuilders.Clickable.Builder()
    .setId(clickableId)
    .setOnClick(
        ActionBuilders.LaunchAction.Builder()
            .setAndroidActivity(androidActivity)
            .build()
    )
    .build()

internal fun openConversation(contact: Contact) = ActionBuilders.AndroidActivity.Builder()
    .setMessagingActivity()
    .addKeyToExtraMapping(
        MainActivity.EXTRA_JOURNEY,
        ActionBuilders.stringExtra(MainActivity.EXTRA_JOURNEY_CONVERSATION)
    )
    .addKeyToExtraMapping(
        MainActivity.EXTRA_CONVERSATION_CONTACT,
        ActionBuilders.stringExtra(contact.name)
    )
    .build()

internal fun openSearch() = ActionBuilders.AndroidActivity.Builder()
    .setMessagingActivity()
    .addKeyToExtraMapping(
        MainActivity.EXTRA_JOURNEY,
        ActionBuilders.stringExtra(MainActivity.EXTRA_JOURNEY_SEARCH)
    )
    .build()

internal fun openNewConversation() = ActionBuilders.AndroidActivity.Builder()
    .setMessagingActivity()
    .addKeyToExtraMapping(
        MainActivity.EXTRA_JOURNEY,
        ActionBuilders.stringExtra(MainActivity.EXTRA_JOURNEY_NEW)
    )
    .build()

internal fun ActionBuilders.AndroidActivity.Builder.setMessagingActivity(): ActionBuilders.AndroidActivity.Builder {
    return setPackageName("com.example.wear.tiles")
        .setClassName("com.example.wear.tiles.messaging.MainActivity")
}