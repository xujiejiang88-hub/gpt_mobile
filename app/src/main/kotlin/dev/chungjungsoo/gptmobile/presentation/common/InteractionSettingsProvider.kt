package dev.chungjungsoo.gptmobile.presentation.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.chungjungsoo.gptmobile.data.dto.InteractionSetting
import dev.chungjungsoo.gptmobile.data.model.ReasoningDisplayMode
import dev.chungjungsoo.gptmobile.data.model.ReasoningLanguage
import dev.chungjungsoo.gptmobile.data.repository.SettingRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

val LocalInteractionSetting = compositionLocalOf { InteractionSetting() }
val LocalInteractionSettingsViewModel = compositionLocalOf<InteractionSettingsViewModel> {
    error("Interaction settings view model is not provided")
}

@HiltViewModel
class InteractionSettingsViewModel @Inject constructor(
    private val settingRepository: SettingRepository
) : ViewModel() {
    private val _settings = MutableStateFlow(InteractionSetting())
    val settings = _settings.asStateFlow()

    init {
        viewModelScope.launch { _settings.value = settingRepository.fetchInteractionSettings() }
    }

    fun updateDisplayMode(mode: ReasoningDisplayMode) = update { it.copy(reasoningDisplayMode = mode) }

    fun updateLanguage(language: ReasoningLanguage) = update { it.copy(reasoningLanguage = language) }

    private fun update(transform: (InteractionSetting) -> InteractionSetting) {
        _settings.value = transform(_settings.value)
        viewModelScope.launch { settingRepository.updateInteractionSettings(_settings.value) }
    }
}

@Composable
fun InteractionSettingsProvider(
    viewModel: InteractionSettingsViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val settings = viewModel.settings.collectAsStateWithLifecycle().value
    CompositionLocalProvider(
        LocalInteractionSetting provides settings,
        LocalInteractionSettingsViewModel provides viewModel,
        content = content
    )
}
