package dev.chungjungsoo.gptmobile.data.dto

import dev.chungjungsoo.gptmobile.data.model.ReasoningDisplayMode
import dev.chungjungsoo.gptmobile.data.model.ReasoningLanguage

data class InteractionSetting(
    val reasoningDisplayMode: ReasoningDisplayMode = ReasoningDisplayMode.OFF,
    val reasoningLanguage: ReasoningLanguage = ReasoningLanguage.CHINESE
)
