package dev.chungjungsoo.gptmobile.presentation.ui.chat

import android.graphics.ImageDecoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import dev.chungjungsoo.gptmobile.R
import dev.chungjungsoo.gptmobile.presentation.theme.frostedBorderColor
import java.io.File
import kotlin.math.ceil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val THUMBNAIL_TARGET_PX = 384
private val IMAGE_EXTENSIONS = setOf("jpg", "jpeg", "png", "gif", "bmp", "webp", "heic", "heif")

@Composable
internal fun AttachmentPreview(
    filePath: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val file = File(filePath)
    val isImage = isImageAttachment(file)
    val preview by produceState<ImageBitmap?>(
        initialValue = null,
        key1 = filePath,
        key2 = file.lastModified()
    ) {
        value = if (isImage) {
            withContext(Dispatchers.IO) { decodeThumbnail(file) }
        } else {
            null
        }
    }
    val shape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.72f))
            .border(1.dp, frostedBorderColor(), shape)
    ) {
        if (preview != null) {
            Image(
                bitmap = preview!!,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = ImageVector.vectorResource(if (isImage) R.drawable.ic_image else R.drawable.ic_file),
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            )
        }
    }
}

internal fun isImageAttachment(file: File): Boolean = file.extension.lowercase() in IMAGE_EXTENSIONS

private fun decodeThumbnail(file: File): ImageBitmap? {
    if (!file.isFile) return null

    return runCatching {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(file)) { decoder, info, _ ->
            val largestDimension = maxOf(info.size.width, info.size.height)
            val sampleSize = ceil(largestDimension.toDouble() / THUMBNAIL_TARGET_PX)
                .toInt()
                .coerceAtLeast(1)
            decoder.setTargetSampleSize(sampleSize)
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        }.asImageBitmap()
    }.getOrNull()
}
