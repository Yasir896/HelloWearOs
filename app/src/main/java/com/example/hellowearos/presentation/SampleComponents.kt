package com.example.hellowearos.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.wear.compose.material.*


@Composable
fun CustomButton(
    onClick: () -> Unit,
    modifier: Modifier,
    iconModifier: Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        // Button
        Button(
            modifier = Modifier.size(ButtonDefaults.LargeButtonSize),
            onClick = onClick,
        ) {
            Icon(
                imageVector = Icons.Rounded.Phone,
                contentDescription = "triggers phone action",
                modifier = iconModifier
            )
        }
    }
}

@Composable
fun CustomText(modifier: Modifier) {
    Text(
        modifier = modifier,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colors.primary,
        text = "Hi Sherry!!"
    )
}

@Composable
fun CustomCard(
    onClick: () -> Unit,
    modifier: Modifier,
    iconModifier: Modifier
) {
    AppCard(
        modifier = modifier,
        appImage = {
            Icon(
                imageVector = Icons.Rounded.Message,
                contentDescription = "triggers open message action",
                modifier = iconModifier
            )
        },
        appName = { Text("Messages") },
        time = { Text("12m") },
        title = { Text("Kim Green") },
        onClick = onClick
    ) {
        Text("On my way!")
    }
}

@Composable
fun CustomChip(modifier: Modifier, iconModifier: Modifier) {
    Chip(
        modifier = modifier,
        onClick = { /* ... */ },
        label = {
            Text(
                text = "5 minute Meditation",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Rounded.SelfImprovement,
                contentDescription = "triggers meditation action",
                modifier = iconModifier
            )
        },
    )
}

@Composable
fun CustomToggleChip(modifier: Modifier) {
    var checked by remember { mutableStateOf(true) }

    ToggleChip(
        modifier = modifier,
        checked = checked,
        toggleControl = {
            Switch(
                checked = checked,
                modifier = Modifier.semantics {
                    this.contentDescription = if (checked) "On" else "Off"
                }
            )
        },
        onCheckedChange = {
            checked = it
        },
        label = {
            Text(
                text = "Sound",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}




