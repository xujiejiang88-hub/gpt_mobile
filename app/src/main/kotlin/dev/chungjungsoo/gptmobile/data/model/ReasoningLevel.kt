package dev.chungjungsoo.gptmobile.data.model

enum class ReasoningLevel {
    LOW,
    MEDIUM,
    HIGH;

    fun apiValue(): String = name.lowercase()

    fun geminiBudget(): Int = when (this) {
        LOW -> 1_024
        MEDIUM -> 4_096
        HIGH -> 8_192
    }

    fun anthropicBudget(): Int = when (this) {
        LOW -> 4_096
        MEDIUM -> 10_000
        HIGH -> 16_000
    }
}
