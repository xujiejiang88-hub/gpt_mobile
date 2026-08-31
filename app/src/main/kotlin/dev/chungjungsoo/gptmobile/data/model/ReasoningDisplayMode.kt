package dev.chungjungsoo.gptmobile.data.model

enum class ReasoningDisplayMode {
    OFF,
    STATUS,
    SUMMARY;

    companion object {
        fun getByValue(value: Int): ReasoningDisplayMode = entries.getOrNull(value) ?: OFF
    }
}

enum class ReasoningLanguage {
    CHINESE,
    ENGLISH;

    companion object {
        fun getByValue(value: Int): ReasoningLanguage = entries.getOrNull(value) ?: CHINESE
    }
}
