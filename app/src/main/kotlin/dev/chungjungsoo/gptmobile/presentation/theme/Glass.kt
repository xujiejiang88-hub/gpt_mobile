package dev.chungjungsoo.gptmobile.presentation.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val DARK_THRESHOLD = 0.35f

@Composable
fun frostedContainerColor(strong: Boolean = false): Color {
    val isDark = MaterialTheme.colorScheme.background.luminance() < DARK_THRESHOLD
    return when {
        isDark && strong -> MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.94f)
        isDark -> MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.78f)
        strong -> Color.White.copy(alpha = 0.82f)
        else -> Color.White.copy(alpha = 0.72f)
    }
}

@Composable
fun frostedBorderColor(): Color {
    return if (MaterialTheme.colorScheme.background.luminance() < DARK_THRESHOLD) {
        Color.White.copy(alpha = 0.16f)
    } else {
        Color.White.copy(alpha = 0.88f)
    }
}

/**
 * Renders the glass fill and its border in the same draw modifier.  This avoids the nested
 * Material container background that produced a second rectangular layer on older devices.
 */
private fun Modifier.singlePaneGlass(
    shape: Shape,
    fill: Color,
    border: Color
): Modifier = this
    .clip(shape)
    .drawWithCache {
        val outline = shape.createOutline(size, layoutDirection, this)
        val stroke = Stroke(width = 1.dp.toPx())
        onDrawBehind {
            drawOutline(outline = outline, color = fill)
            drawOutline(outline = outline, color = border, style = stroke)
        }
    }

@Composable
fun FrostedSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(18.dp),
    strong: Boolean = false,
    @Suppress("UNUSED_PARAMETER") shadowElevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    val fill = frostedContainerColor(strong)
    val border = frostedBorderColor()
    Box(
        modifier = modifier
            .singlePaneGlass(shape, fill, border)
    ) {
        content()
    }
}

@Composable
fun Modifier.frosted(
    shape: Shape = RoundedCornerShape(18.dp),
    strong: Boolean = false,
    @Suppress("UNUSED_PARAMETER") shadowElevation: Dp = 0.dp
): Modifier = singlePaneGlass(
    shape = shape,
    fill = frostedContainerColor(strong),
    border = frostedBorderColor()
)

@Composable
fun FrostedIconContainer(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(14.dp),
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.frosted(
            shape = shape,
            strong = true,
            shadowElevation = 4.dp
        )
    ) {
        content()
    }
}
