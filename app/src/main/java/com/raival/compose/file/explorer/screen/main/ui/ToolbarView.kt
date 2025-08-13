package com.raival.compose.file.explorer.screen.main.ui

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesomeMotion
import androidx.compose.material.icons.rounded.EditAttributes
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raival.compose.file.explorer.App.Companion.globalClass
import com.raival.compose.file.explorer.R
import com.raival.compose.file.explorer.screen.main.tab.files.FilesTab
import com.raival.compose.file.explorer.screen.main.tab.home.HomeTab
import com.raival.compose.file.explorer.screen.preferences.PreferencesActivity

@Composable
fun Toolbar(
    title: String,
    subtitle: String,
    onToggleAppInfoDialog: (Boolean) -> Unit
) {
    val mainActivityManager = globalClass.mainActivityManager
    val context = LocalContext.current

    var showOptionsMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onToggleAppInfoDialog(true) }
        ) {
            Icon(imageVector = Icons.Rounded.Menu, contentDescription = null)
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 17.sp,
                maxLines = 1,
                lineHeight = 20.sp,
                overflow = TextOverflow.Ellipsis
            )
            AnimatedVisibility(visible = subtitle.isNotEmpty()) {
                Text(
                    modifier = Modifier.alpha(0.7f),
                    text = subtitle,
                    fontSize = 10.sp,
                    maxLines = 1,
                    lineHeight = 16.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Box {
            IconButton(
                onClick = { showOptionsMenu = true }
            ) {
                Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
            }

            DropdownMenu(
                expanded = showOptionsMenu,
                onDismissRequest = { showOptionsMenu = false },
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            ) {
                if (mainActivityManager.getActiveTab() is FilesTab) {
                    DropdownMenuItem(
                        text = {
                            Text(text = stringResource(R.string.toggle_hidden_files))
                        },
                        onClick = {
                            globalClass.preferencesManager.showHiddenFiles =
                                !globalClass.preferencesManager.showHiddenFiles
                            (mainActivityManager.getActiveTab() as FilesTab).onTabResumed()
                            showOptionsMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.EditAttributes,
                                contentDescription = null
                            )
                        }
                    )
                }

                if (mainActivityManager.getActiveTab() is HomeTab) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.customize_home_tab)) },
                        onClick = {
                            (mainActivityManager.getActiveTab() as HomeTab).showCustomizeHomeTabDialog =
                                true
                            showOptionsMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.EditAttributes,
                                contentDescription = null
                            )
                        }
                    )
                }

                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.manage_startup_tabs)) },
                    onClick = {
                        mainActivityManager.toggleStartupTabsDialog(true)
                        showOptionsMenu = false
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.AutoAwesomeMotion,
                            contentDescription = null
                        )
                    }
                )

                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.preferences)) },
                    onClick = {
                        context.startActivity(Intent(context, PreferencesActivity::class.java))
                        showOptionsMenu = false
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
                    }
                )
            }
        }
    }
}