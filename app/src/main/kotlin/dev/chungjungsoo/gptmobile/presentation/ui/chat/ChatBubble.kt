package dev.chungjungsoo.gptmobile.presentation.ui.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.chungjungsoo.gptmobile.R
import dev.chungjungsoo.gptmobile.data.database.entity.AgentRun
import dev.chungjungsoo.gptmobile.data.database.entity.AssistantTimelineItem
import dev.chungjungsoo.gptmobile.data.database.entity.AssistantTimelineItemType
import dev.chungjungsoo.gptmobile.data.database.entity.ToolEvent
import dev.chungjungsoo.gptmobile.data.database.entity.hasUnavailableAssistantOrder
import dev.chungjungsoo.gptmobile.presentation.theme.FrostedSurface
import dev.chungjungsoo.gptmobile.presentation.theme.GPTMobileTheme
import dev.chungjungsoo.gptmobile.presentation.theme.frosted
import java.io.File

@Composable
fun UserChatBubble(
    modifier: Modifier = Modifier,
    text: String,
    files: List<String> = emptyList(),
    onLongPress: () -> Unit
) {
    val cardColor = CardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        disabledContentColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.38f),
        disabledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.38f)
    )

    Column(horizontalAlignment = Alignment.End) {
        Card(
            modifier = modifier
                .pointerInput(Unit) {
                    detectTapGestures(onLongPress = { onLongPress.invoke() })
            },
            shape = RoundedCornerShape(32.dp),
            colors = cardColor,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            ChatMarkdown(
                content = text,
                modifier = Modifier.padding(16.dp)
            )
        }
        MessageFileThumbnailRow(
            files = files,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun OpponentChatBubble(
    modifier: Modifier = Modifier,
    canRetry: Boolean,
    isLoading: Boolean,
    isError: Boolean = false,
    text: String,
    thoughts: String = "",
    timeline: List<AssistantTimelineItem> = emptyList(),
    attachments: List<String> = emptyList(),
    agentRun: AgentRun? = null,
    runNotices: List<ChatRunNotice> = emptyList(),
    toolEvents: List<ToolEvent> = emptyList(),
    contentIdentity: Any = text,
    canEdit: Boolean = false,
    revisionIndexLabel: String? = null,
    canShowPreviousRevision: Boolean = false,
    canShowNextRevision: Boolean = false,
    onCopyClick: () -> Unit = {},
    onSelectClick: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onShowPreviousRevision: () -> Unit = {},
    onShowNextRevision: () -> Unit = {}
) {
    val noticeMessages = visibleChatRunNotices(
        stored = runNotices,
        timelineNotices = timelineNoticeMessages(timeline),
        isRunActive = isLoading
    )
    val contentTimeline = timeline.filter { it.type != AssistantTimelineItemType.NOTICE }

    Column(modifier = modifier) {
        RunNoticeChips(
            notices = noticeMessages,
            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
        )
        AgentRunStatusBlock(
            run = agentRun,
            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
        )

        Column {
            val hasUnavailableOrder = hasUnavailableAssistantOrder(
                timeline = contentTimeline,
                content = text,
                thoughts = thoughts,
                hasToolEvents = toolEvents.isNotEmpty()
            )
            if (contentTimeline.isNotEmpty() && !hasUnavailableOrder) {
                FrostedSurface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    strong = true,
                    shadowElevation = 6.dp
                ) {
                    Column {
                        AssistantTimelineContent(
                            timeline = contentTimeline,
                            toolEvents = toolEvents,
                            isLoading = isLoading,
                            contentIdentity = contentIdentity
                        )
                        MessageFileThumbnailRow(
                            files = attachments,
                            usePrimaryColors = false,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            } else {
                LegacyAssistantContent(
                    text = text,
                    thoughts = thoughts,
                    toolEvents = toolEvents,
                    attachments = attachments,
                    isLoading = isLoading,
                    contentIdentity = contentIdentity,
                    showOrderNotice = hasUnavailableOrder
                )
            }

            if (!isLoading) {
                HorizontalDivider(
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(start = 4.dp, top = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isError) {
                        CopyTextIcon(onCopyClick)
                        SelectTextIcon(onSelectClick)
                        if (canEdit) {
                            EditTextIcon(onEditClick)
                        }
                    }
                    if (canRetry) {
                        RetryIcon(onRetryClick)
                    }
                    revisionIndexLabel?.let { label ->
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            modifier = Modifier.size(40.dp),
                            enabled = canShowPreviousRevision,
                            onClick = onShowPreviousRevision
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = stringResource(R.string.previous_revision)
                            )
                        }
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        IconButton(
                            modifier = Modifier.size(40.dp),
                            enabled = canShowNextRevision,
                            onClick = onShowNextRevision
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = stringResource(R.string.next_revision)
                            )
                        }
                    }
                }
                if (canRetry && toolEvents.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.retry_tools_warning),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 12.dp, top = 2.dp, end = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AssistantTimelineContent(
    timeline: List<AssistantTimelineItem>,
    toolEvents: List<ToolEvent>,
    isLoading: Boolean,
    contentIdentity: Any
) {
    val toolEventsBySequence = toolEvents.associateBy(ToolEvent::sequence)
    timeline.forEachIndexed { index, item ->
        when (item.type) {
            AssistantTimelineItemType.THINKING -> ThinkingBlock(
                modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
                thoughts = item.content,
                contentIdentity = "$contentIdentity:thinking:$index",
                isLoading = isLoading && index == timeline.lastIndex
            )

            AssistantTimelineItemType.TEXT -> {
                val displayText = if (isLoading && index == timeline.lastIndex) item.content + "●" else item.content
                ChatMarkdown(
                    content = displayText,
                    contentIdentity = "$contentIdentity:text:$index",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            AssistantTimelineItemType.TOOL ->
                item.toolSequence
                    ?.let(toolEventsBySequence::get)
                    ?.let { event ->
                        ToolTraceBlock(
                            events = listOf(event),
                            modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
                            contentIdentity = "$contentIdentity:tool:${event.sequence}"
                        )
                    }

            AssistantTimelineItemType.NOTICE -> Unit

            AssistantTimelineItemType.LEGACY_ORDER -> Unit
        }
    }
}

@Composable
private fun LegacyAssistantContent(
    text: String,
    thoughts: String,
    toolEvents: List<ToolEvent>,
    attachments: List<String>,
    isLoading: Boolean,
    contentIdentity: Any,
    showOrderNotice: Boolean
) {
    val isThinking = isLoading && thoughts.isNotBlank() && text.isBlank()
    if (showOrderNotice) {
        Text(
            text = stringResource(R.string.legacy_assistant_order_unavailable),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 12.dp, start = 16.dp, end = 16.dp)
        )
    }
    if (thoughts.isNotBlank()) {
        ThinkingBlock(
            modifier = Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp),
            thoughts = thoughts,
            contentIdentity = contentIdentity,
            isLoading = isThinking
        )
    }
    ToolTraceBlock(
        events = toolEvents,
        modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp),
        contentIdentity = contentIdentity
    )
    FrostedSurface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        strong = true,
        shadowElevation = 6.dp
    ) {
        Column {
            ChatMarkdown(
                content = if (isLoading) text + "●" else text,
                contentIdentity = contentIdentity,
                modifier = Modifier.padding(16.dp)
            )
            MessageFileThumbnailRow(
                files = attachments,
                usePrimaryColors = false,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun GPTMobileIcon(loading: Boolean) {
    Box(
        modifier = Modifier
            .padding(start = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .frosted(
                    shape = RoundedCornerShape(40.dp),
                    strong = true,
                    shadowElevation = 4.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp)
                )
            }
            Image(
                painter = painterResource(R.drawable.ic_gpt_mobile_no_padding),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun PlatformButton(
    isLoading: Boolean,
    name: String,
    selected: Boolean,
    onPlatformClick: () -> Unit
) {
    val buttonContent: @Composable RowScope.() -> Unit = {
        Spacer(modifier = Modifier.width(12.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = if (selected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        if (isLoading) Spacer(modifier = Modifier.width(4.dp))
    }

    TextButton(
        modifier = Modifier.widthIn(max = 160.dp),
        onClick = onPlatformClick,
        colors = if (selected) ButtonDefaults.filledTonalButtonColors() else ButtonDefaults.textButtonColors(),
        content = buttonContent
    )
}

@Composable
private fun CopyTextIcon(onCopyClick: () -> Unit) {
    IconButton(modifier = Modifier.size(40.dp), onClick = onCopyClick) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_copy),
            contentDescription = stringResource(R.string.copy_text)
        )
    }
}

@Composable
private fun SelectTextIcon(onSelectClick: () -> Unit) {
    IconButton(modifier = Modifier.size(40.dp), onClick = onSelectClick) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_select),
            contentDescription = stringResource(R.string.select_text)
        )
    }
}

@Composable
private fun RetryIcon(onRetryClick: () -> Unit) {
    IconButton(modifier = Modifier.size(40.dp), onClick = onRetryClick) {
        Icon(
            Icons.Rounded.Refresh,
            contentDescription = stringResource(R.string.retry)
        )
    }
}

@Composable
private fun EditTextIcon(onEditClick: () -> Unit) {
    IconButton(modifier = Modifier.size(40.dp), onClick = onEditClick) {
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = stringResource(R.string.edit)
        )
    }
}

@Preview
@Composable
fun UserChatBubblePreview() {
    val sampleText = """
        How can I print hello world
        in Python?
    """.trimIndent()
    GPTMobileTheme {
        UserChatBubble(text = sampleText, files = emptyList(), onLongPress = {})
    }
}

@Preview
@Composable
fun OpponentChatBubblePreview() {
    val sampleText = """
        # Demo
    
        Emphasis, aka italics, with *asterisks* or _underscores_. Strong emphasis, aka bold, with **asterisks** or __underscores__. Combined emphasis with **asterisks and _underscores_**. [Links with two blocks, text in square-brackets, destination is in parentheses.](https://www.example.com). Inline `code` has `back-ticks around` it.
    
        1. First ordered list item
        2. Another item
            * Unordered sub-list.
        3. And another item.
            You can have properly indented paragraphs within list items. Notice the blank line above, and the leading spaces (at least one, but we'll use three here to also align the raw Markdown).
    
        * Unordered list can use asterisks
        - Or minuses
        + Or pluses
    """.trimIndent()
    GPTMobileTheme {
        OpponentChatBubble(
            text = sampleText,
            canRetry = true,
            isLoading = false,
            revisionIndexLabel = "Revision 1/1",
            onCopyClick = {},
            onRetryClick = {}
        )
    }
}

@Composable
internal fun MessageFileThumbnailRow(
    files: List<String>,
    modifier: Modifier = Modifier,
    usePrimaryColors: Boolean = true
) {
    // Filter out empty strings and check if we have valid files
    val validFiles = files.filter { it.isNotEmpty() && it.isNotBlank() }

    if (validFiles.isEmpty()) {
        return
    }

    Row(
        modifier = modifier
            .wrapContentHeight()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        validFiles.forEach { filePath ->
            MessageFileThumbnail(
                filePath = filePath,
                usePrimaryColors = usePrimaryColors
            )
        }
    }
}

@Composable
private fun MessageFileThumbnail(
    filePath: String,
    usePrimaryColors: Boolean
) {
    val file = File(filePath)
    val containerColor = if (usePrimaryColors) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val contentColor = if (usePrimaryColors) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = Modifier.width(92.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AttachmentPreview(
            filePath = filePath,
            contentDescription = file.name,
            modifier = Modifier.size(width = 88.dp, height = 68.dp)
        )

        Text(
            text = file.name,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier
                .padding(top = 4.dp)
                .width(88.dp)
        )
    }
}
