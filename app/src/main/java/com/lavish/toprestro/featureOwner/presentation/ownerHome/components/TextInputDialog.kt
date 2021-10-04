package com.lavish.toprestro.featureOwner.presentation.ownerHome.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun TextInputDialog(
    dialogState: MutableState<Boolean>,
    title: String,
    icon: ImageVector? = null,
    painterIcon: Painter? = null,
    inputLabel: String,
    buttonText: String,
    onInput: (String) -> Unit
) {

    if (dialogState.value) {

        AlertDialog(
            onDismissRequest = { dialogState.value = false },
            title = null,
            text = null,
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
            shape = RoundedCornerShape(16.dp),

            //Main Layout
            buttons = {

                Column(modifier = Modifier.padding(16.dp)) {

                    //Heading
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = title
                            )
                        }

                        painterIcon?.let {
                            Icon(
                                painter = it,
                                contentDescription = title
                            )
                        }

                        Title(
                            text = title,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    //States
                    val input = rememberSaveable { mutableStateOf("") }
                    val error = remember { mutableStateOf(false) }

                    //Input
                    OutlinedTextField(
                        modifier = Modifier.padding(top = 8.dp),
                        value = input.value,
                        onValueChange = {
                            input.value = it
                            error.value = it.isBlank()
                        },
                        label = { Text(inputLabel) },
                        isError = error.value,
                    )

                    //Error
                    if (error.value) {
                        Text(
                            text = "Can't be left blank",
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }

                    //Submit button
                    Button(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .align(Alignment.End),
                        onClick = {
                            if(!input.value.isBlank()) {
                                onInput(input.value)
                                dialogState.value = false
                            }
                        }
                    ) {
                        Text(
                            text = buttonText,
                            style = MaterialTheme.typography.button
                        )
                    }
                }
            }
        )
    }
}