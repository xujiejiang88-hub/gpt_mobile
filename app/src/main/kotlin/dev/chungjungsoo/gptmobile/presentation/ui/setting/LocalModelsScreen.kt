package dev.chungjungsoo.gptmobile.presentation.ui.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.chungjungsoo.gptmobile.R
import dev.chungjungsoo.gptmobile.presentation.theme.frostedContainerColor
import dev.chungjungsoo.gptmobile.data.catalog.ModelCatalogParser
import dev.chungjungsoo.gptmobile.presentation.ui.localmodel.LocalModelDownloadDialogHost
import dev.chungjungsoo.gptmobile.presentation.ui.localmodel.LocalModelDownloadStatus
import dev.chungjungsoo.gptmobile.presentation.ui.localmodel.LocalModelRequirements
import dev.chungjungsoo.gptmobile.presentation.ui.localmodel.rememberLocalModelDownloader
import dev.chungjungsoo.gptmobile.util.pinnedExitUntilCollapsedScrollBehavior

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocalModelsScreen(
    modifier: Modifier = Modifier,
    viewModel: LocalModelsViewModel = hiltViewModel(),
    onNavigationClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    val scrollBehavior = pinnedExitUntilCollapsedScrollBehavior(
        canScroll = { scrollState.canScrollForward || scrollState.canScrollBackward }
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val requestDownload = rememberLocalModelDownloader(viewModel::onDownloadClick)

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LocalModelsTopBar(
                scrollBehavior = scrollBehavior,
                onNavigationClick = onNavigationClick
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            else -> {
                Column(
                    Modifier
                        .padding(innerPadding)
                        .verticalScroll(scrollState)
                ) {
                    HuggingFaceAccountSection(
                        hasToken = uiState.hasHuggingFaceToken,
                        onAddToken = viewModel::openAccessTokenDialog,
                        onRemoveToken = viewModel::removeHuggingFaceAccessToken
                    )
                    if (uiState.items.isEmpty()) {
                        Text(
                            text = stringResource(R.string.local_models_empty),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                        )
                    } else {
                        Text(
                            text = stringResource(
                                R.string.local_model_storage_used,
                                ModelCatalogParser.formatDownloadSize(uiState.totalStorageBytes)
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                        )
                        uiState.items.forEach { item ->
                            LocalModelItem(
                                item = item,
                                isCheckingAccess = uiState.checkingAccessEntryId == item.entry.id,
                                onDownload = { requestDownload(item.entry) },
                                onCancel = { viewModel.cancelDownload(item.entry) },
                                onDelete = { viewModel.onDeleteClick(item.entry) }
                            )
                        }
                    }
                }
            }
        }
    }

    LocalModelDownloadDialogHost(
        dialog = uiState.dialog,
        onConfirmRamWarning = viewModel::confirmRamWarning,
        onConfirmMeteredDownload = viewModel::confirmMeteredDownload,
        onConfirmDelete = viewModel::confirmDelete,
        onDismissDialog = viewModel::dismissDialog,
        onStartSignIn = viewModel::startHuggingFaceSignIn,
        onAuthActivityResult = viewModel::onAuthActivityResult,
        onLicenseTabClosed = viewModel::onLicenseTabClosed,
        onRetryAfterLicense = viewModel::retryAfterLicense,
        onEnterAccessToken = viewModel::openAccessTokenDialog,
        onSaveAccessToken = viewModel::saveHuggingFaceAccessToken
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LocalModelsTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigationClick: () -> Unit
) {
    LargeTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = frostedContainerColor(),
            titleContentColor = MaterialTheme.colorScheme.onBackground
        ),
        title = {
            Text(
                modifier = Modifier.padding(4.dp),
                text = stringResource(R.string.local_models),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(
                modifier = Modifier.padding(4.dp),
                onClick = onNavigationClick
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.go_back))
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
private fun HuggingFaceAccountSection(
    hasToken: Boolean,
    onAddToken: () -> Unit,
    onRemoveToken: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.huggingface_account),
            style = MaterialTheme.typography.titleMedium
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (hasToken) {
                    stringResource(R.string.huggingface_token_saved)
                } else {
                    stringResource(R.string.huggingface_add_access_token)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            if (hasToken) {
                TextButton(onClick = onRemoveToken) {
                    Text(stringResource(R.string.huggingface_remove_token))
                }
            } else {
                TextButton(onClick = onAddToken) {
                    Text(stringResource(R.string.huggingface_add_access_token))
                }
            }
        }
    }
}

@Composable
private fun LocalModelItem(
    item: LocalModelListItem,
    isCheckingAccess: Boolean,
    onDownload: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Text(
            text = item.entry.displayName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        LocalModelRequirements(item = item)
        LocalModelDownloadStatus(
            item = item,
            isCheckingAccess = isCheckingAccess,
            onDownload = onDownload,
            onCancel = onCancel,
            onDelete = onDelete
        )
    }
}
