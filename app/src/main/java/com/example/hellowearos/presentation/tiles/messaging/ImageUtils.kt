package com.example.hellowearos.presentation.tiles.messaging

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.wear.tiles.RequestBuilders
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.hellowearos.presentation.model.Contact
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

internal suspend fun ImageLoader.fetchAvatarsFromNetwork(
    context: Context,
    requestParams: RequestBuilders.ResourcesRequest,
    tileState: MessagingTileState,
): Map<Contact, Bitmap> {
    val requestedAvatars: List<Contact> = if (requestParams.resourceIds.isEmpty()) {
        tileState.contacts
    } else {
        tileState.contacts.filter { contact ->
            requestParams.resourceIds.contains(contact.imageResourceId())
        }
    }

    val images = coroutineScope {
        requestedAvatars.map { contact ->
            async {
                val image = loadAvatar(context, contact)
                image?.let { contact to it }
            }
        }
    }.awaitAll().filterNotNull().toMap()

    return images
}

internal fun Contact.imageResourceId() = "contact:$id"

private suspend fun ImageLoader.loadAvatar(
    context: Context,
    contact: Contact,
    size: Int? = 64
): Bitmap? {
    val request = ImageRequest.Builder(context)
        .data(contact.avatarUrl)
        .apply {
            if (size != null) {
                size(size)
            }
        }
        .allowRgb565(true)
        .transformations(CircleCropTransformation())
        .allowHardware(false)
        .build()
    val response = execute(request)
    return (response.drawable as? BitmapDrawable)?.bitmap
}