package dev.chungjungsoo.gptmobile.presentation.ui.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.chungjungsoo.gptmobile.R
import dev.chungjungsoo.gptmobile.data.database.entity.AgentRun
import dev.chungjungsoo.gptmobile.data.database.entity.AgentRunStatus
import dev.chungjungsoo.gptmobile.data.database.entity.AgentRunTerminalError
import dev.chungjungsoo.gptmobile.presentation.theme.FrostedSurface

@Composable
fun RunNoticeChips(notices: List<String>, modifier: Modifier = Modifier) {
    if (notices.isEmpty()) return
    Column(modifier = modifier) {
        notices.forEach { notice ->
            androidx.compose.foundation.layout.Box(modifier = Modifier.padding(bottom = 8.dp)) {
                FrostedSurface(
                    modifier = Modifier.semantics { contentDescription = notice },
                    shadowElevation = 3.dp
                ) {
                    Text(
                        text = notice,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AgentRunStatusBlock(run: AgentRun?, modifier: Modifier = Modifier) {
    if (run == null ||
        run.status == AgentRunStatus.COMPLETED ||
        run.status == AgentRunStatus.QUEUED ||
        run.status == AgentRunStatus.RUNNING
    ) {
        return
    }

    val status = when (run.status) {
        AgentRunStatus.CANCELED -> stringResource(R.string.agent_run_canceled)
        AgentRunStatus.INTERRUPTED -> stringResource(R.string.agent_run_interrupted)
        else -> stringResource(R.string.agent_run_failed)
    }
    val duration = agentRunDurationSeconds(run)
        ?.let { " · ${pluralStringResource(R.plurals.duration_seconds, it.toInt(), it)}" }
        .orEmpty()
    val terminalError = run.terminalError
        ?.takeIf { it.isNotBlank() }
        ?.let { agentRunTerminalErrorText(it) }

    androidx.compose.foundation.layout.Box(modifier = modifier) {
        FrostedSurface(
            modifier = Modifier.semantics { contentDescription = listOfNotNull(status + duration, terminalError).joinToString(". ") },
            shadowElevation = 3.dp
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = status + duration,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            terminalError?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            }
        }
    }
}

@Composable
private fun agentRunTerminalErrorText(error: String): String = when (error) {
    AgentRunTerminalError.SERVICE_STOPPED -> stringResource(R.string.agent_run_error_service_stopped)
    else -> error
}

internal fun agentRunDurationSeconds(run: AgentRun): Long? {
    val startedAt = run.startedAt ?: return null
    val completedAt = run.completedAt ?: return null
    return (completedAt - startedAt).coerceAtLeast(0)
}
