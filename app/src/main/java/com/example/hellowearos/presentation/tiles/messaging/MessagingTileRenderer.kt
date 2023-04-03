@file:OptIn(ExperimentalHorologistTilesApi::class)

package com.example.hellowearos.presentation.tiles.messaging

import android.content.Context
import android.graphics.Bitmap
import androidx.wear.compose.material.CompactChip
import androidx.wear.tiles.DeviceParametersBuilders
import androidx.wear.tiles.LayoutElementBuilders
import androidx.wear.tiles.ModifiersBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.material.Button
import androidx.wear.tiles.material.ButtonColors
import androidx.wear.tiles.material.ChipColors
import androidx.wear.tiles.material.layouts.MultiButtonLayout
import androidx.wear.tiles.material.layouts.PrimaryLayout
import com.example.hellowearos.R
import com.example.hellowearos.presentation.model.Contact
import com.google.android.horologist.tiles.ExperimentalHorologistTilesApi
import com.google.android.horologist.tiles.images.drawableResToImageResource
import com.google.android.horologist.tiles.images.toImageResource
import com.google.android.horologist.tiles.render.SingleTileLayoutRenderer

@ExperimentalHorologistTilesApi
class MessagingTileRenderer(context: Context):
    SingleTileLayoutRenderer<MessagingTileState, Map<Contact, Bitmap>>(context) {
    override fun renderTile(
        state: MessagingTileState,
        deviceParameters: DeviceParametersBuilders.DeviceParameters
    ): LayoutElementBuilders.LayoutElement {
        return messagingTileLayout(
            context = context,
            deviceParameters = deviceParameters,
            state = state,
            contactClickableFactory = { contact ->
                launchActivityClickable(
                    clickableId = contact.id.toString(),
                    androidActivity = openConversation(contact)
                )
            },
            searchButtonClickable = launchActivityClickable("search_button", openSearch()),
            newButtonClickable = launchActivityClickable("new_button", openNewConversation())
        )
    }

    override fun ResourceBuilders.Resources.Builder.produceRequestedResources(
        resourceState: Map<Contact, Bitmap>,
        deviceParameters: DeviceParametersBuilders.DeviceParameters,
        resourceIds: MutableList<String>
    ) {
        addIdToImageMapping(ID_IC_SEARCH, drawableResToImageResource(R.drawable.ic_search_24))

        resourceState.forEach { (contact, bitmap) ->
            addIdToImageMapping(
                /* id = */ contact.imageResourceId(),
                /* image = */ bitmap.toImageResource()
            )
        }
    }

    companion object {
        internal const val ID_IC_SEARCH = "ic_search"
    }
}


private fun messagingTileLayout(
    context: Context,
    deviceParameters: DeviceParametersBuilders.DeviceParameters,
    state: MessagingTileState,
    contactClickableFactory: (Contact) -> ModifiersBuilders.Clickable,
    searchButtonClickable: ModifiersBuilders.Clickable,
    newButtonClickable: ModifiersBuilders.Clickable
) = PrimaryLayout.Builder(deviceParameters)
    .setContent(
        MultiButtonLayout.Builder()
            .apply {
                // In a PrimaryLayout with a compact chip at the bottom, we can fit 5 buttons.
                // We're only taking the first 4 contacts so that we can fit a Search button too.
                state.contacts.take(4).forEach { contact ->
                    addButtonContent(
                        contactLayout(
                            context = context,
                            contact = contact,
                            clickable = contactClickableFactory(contact)
                        )
                    )
                }
            }
            .addButtonContent(searchLayout(context, searchButtonClickable))
            .build()
    ).setPrimaryChipContent(
        androidx.wear.tiles.material.CompactChip.Builder(
            /* context = */ context,
            /* text = */ context.getString(R.string.tile_messaging_create_new),
            /* clickable = */ newButtonClickable,
            /* deviceParameters = */ deviceParameters
        )
            .setChipColors(ChipColors.primaryChipColors(MessagingTileTheme.colors))
            .build()
    ).build()


    @ExperimentalHorologistTilesApi
    private fun searchLayout(
    context: Context,
    clickable: ModifiersBuilders.Clickable,
) = Button.Builder(context, clickable)
    .setContentDescription(context.getString(R.string.tile_messaging_search))
    .setIconContent(MessagingTileRenderer.ID_IC_SEARCH)
    .setButtonColors(ButtonColors.secondaryButtonColors(MessagingTileTheme.colors))
    .build()

fun contactLayout(context: Context, contact: Contact, clickable: ModifiersBuilders.Clickable) =
    Button.Builder(context, clickable)
        .setContentDescription(contact.name)
        .apply {
            if (contact.avatarUrl != null) {
                setImageContent(contact.imageResourceId())
            } else {
                setTextContent(contact.initials)
                setButtonColors(ButtonColors.secondaryButtonColors(MessagingTileTheme.colors))
            }
        }
        .build()
