package com.example.hellowearos.presentation.tiles.messaging

import androidx.lifecycle.lifecycleScope
import androidx.wear.tiles.RequestBuilders
import androidx.wear.tiles.ResourceBuilders
import androidx.wear.tiles.TileBuilders
import coil.imageLoader
import com.example.hellowearos.presentation.repository.MessagingRepo
import com.google.android.horologist.tiles.CoroutinesTileService
import com.google.android.horologist.tiles.ExperimentalHorologistTilesApi
import kotlinx.coroutines.flow.*

@ExperimentalHorologistTilesApi
class MessagingTileService: CoroutinesTileService() {

    private lateinit var repo: MessagingRepo
    private lateinit var renderer: MessagingTileRenderer
    private lateinit var tileStateFlow: StateFlow<MessagingTileState?>

    override fun onCreate() {
        super.onCreate()
        repo = MessagingRepo(this)
        renderer = MessagingTileRenderer(this)
        tileStateFlow = repo.getFavoriteContacts()
            .map { contacts -> MessagingTileState(contacts) }
            .stateIn(
                lifecycleScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    }


    override suspend fun tileRequest(requestParams: RequestBuilders.TileRequest): TileBuilders.Tile {
        val tileState = latestTileState()
        return renderer.renderTimeline(tileState, requestParams)
    }



    private suspend fun latestTileState(): MessagingTileState {
        var tileState = tileStateFlow.filterNotNull().first()

        // see `refreshData()` docs for more information
        if (tileState.contacts.isEmpty()) {
            refreshData()
            tileState = tileStateFlow.filterNotNull().first()
        }
        return tileState
    }

    private suspend fun refreshData() {
        repo.updateContacts(MessagingRepo.knownContacts)
    }


    override suspend fun resourcesRequest(requestParams: RequestBuilders.ResourcesRequest): ResourceBuilders.Resources {
        // Since we know there's only 2 very small avatars, we'll fetch them
        // as part of this resource request.
        val avatars = imageLoader.fetchAvatarsFromNetwork(
            context = this@MessagingTileService,
            requestParams = requestParams,
            tileState = latestTileState()
        )
        // then pass the bitmaps to the renderer to transform them to ImageResources
        return renderer.produceRequestedResources(avatars, requestParams)
    }
}

