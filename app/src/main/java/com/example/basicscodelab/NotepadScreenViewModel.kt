package com.example.basicscodelab

import android.app.Application
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed class FontStyleAction {
        data class ToggleBold(val range: TextRange) : FontStyleAction()
        data class ToggleItalic(val range: TextRange) : FontStyleAction()
        data class ChangeSize(val range: TextRange, val newSizeSp: Float) : FontStyleAction()
    }

    val FIXED_FONT_FAMILY = FontFamily.SansSerif
    val DEFAULT_FONT_SIZE_SP = 16f.sp
    val DEFAULT_STYLE = SpanStyle(
        fontSize = 16.sp,
        fontFamily = FIXED_FONT_FAMILY,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal
    )

    private var saveJob: Job? = null
class notepadScreenViewModel(application: Application) : AndroidViewModel(application) {

        private val dataStoreManager = NotepadScreenData(application.applicationContext)

        private val _textState = MutableStateFlow(TextFieldValue())

    init {
        viewModelScope.launch {
            dataStoreManager.notepadTextFlow.collect { savedText ->
                // only apply if different from current text
                if (savedText.isNotEmpty() && savedText != _textState.value.text) {
                    val initialAnnotatedString = buildAnnotatedString {
                        append(savedText)
                        addStyle(DEFAULT_STYLE, 0, savedText.length)
                    }
                    _textState.value = _textState.value.copy(
                        annotatedString = initialAnnotatedString
                    )
                }
            }
        }
    }

        val textState: StateFlow<TextFieldValue> = _textState


        fun saveText() {
            viewModelScope.launch {
                val textToSave = _textState.value.text
                dataStoreManager.saveNotepadText(textToSave)
            }
        }
        private fun getSpanStyleAt(annotatedString: AnnotatedString, index: Int): SpanStyle {
            var baseStyle = DEFAULT_STYLE
            annotatedString.spanStyles.forEach { range ->
                if (index >= range.start && index < range.end) {
                    baseStyle = baseStyle.merge(range.item)
                }
            }
            return baseStyle
        }

        fun onTextChange(newValue: TextFieldValue) {
            // Build a new AnnotatedString from the new plain text.
            _textState.value = newValue.copy(
                annotatedString = buildAnnotatedString {
                    val old = _textState.value.annotatedString
                    val newText = newValue.text

                    append(newText)

                    // 1. Reapply all old styles that are still valid
                    old.spanStyles.forEach { range ->
                        val safeStart = range.start.coerceAtMost(newText.length)
                        val safeEnd = range.end.coerceAtMost(newText.length)
                        if (safeStart < safeEnd) {
                            addStyle(range.item, safeStart, safeEnd)
                        }
                    }

                    // 2. Optionally apply default style to newly added text
                    if (newText.length > old.text.length) {
                        val newPartStart = old.text.length
                        val newPartEnd = newText.length
                        addStyle(DEFAULT_STYLE, newPartStart, newPartEnd)
                    }
                }
            )

        }
        fun applyStyle(action: FontStyleAction) {
            _textState.update { currentValue ->
                val selection = when (action) {
                    is FontStyleAction.ToggleBold -> action.range
                    is FontStyleAction.ToggleItalic -> action.range
                    is FontStyleAction.ChangeSize -> action.range
                }

                if (selection.collapsed) currentValue

                val currentText = currentValue.annotatedString.text
                val currentStyles = currentValue.annotatedString.spanStyles

                val existingStyleAtStart = getSpanStyleAt(currentValue.annotatedString, selection.min)

                val styleAttributeToApply = when (action) {
                    is FontStyleAction.ToggleBold -> {
                        val shouldBeNormal = existingStyleAtStart.fontWeight == FontWeight.Bold
                        SpanStyle(fontWeight = if (shouldBeNormal) FontWeight.Normal else FontWeight.Bold)
                    }
                    is FontStyleAction.ToggleItalic -> {
                        val shouldBeNormal = existingStyleAtStart.fontStyle == FontStyle.Italic
                        SpanStyle(fontStyle = if (shouldBeNormal) FontStyle.Normal else FontStyle.Italic)
                    }
                    is FontStyleAction.ChangeSize -> {
                        SpanStyle(fontSize = action.newSizeSp.sp)
                    }
                }.copy(fontFamily = FIXED_FONT_FAMILY) // Always ensure the fixed font family

                val builder = AnnotatedString.Builder(currentText)

                buildAnnotatedString {
                    append(currentText)

                    val newRanges = mutableListOf<AnnotatedString.Range<SpanStyle>>()

                    currentStyles.forEach { range ->
                        val rangeStart = range.start
                        val rangeEnd = range.end

                        // Case 1: The current range is completely outside the selection
                        if (rangeEnd <= selection.min || rangeStart >= selection.max) {
                            newRanges.add(range)
                            return@forEach
                        }

                        if (rangeStart < selection.min) {
                            newRanges.add(AnnotatedString.Range(range.item, rangeStart, selection.min))
                        }

                        val overlapStart = maxOf(rangeStart, selection.min)
                        val overlapEnd = minOf(rangeEnd, selection.max)

                        val mergedStyle = range.item.merge(styleAttributeToApply)
                        newRanges.add(AnnotatedString.Range(mergedStyle, overlapStart, overlapEnd))

                        if (rangeEnd > selection.max) {
                            newRanges.add(AnnotatedString.Range(range.item, selection.max, rangeEnd))
                        }
                        newRanges.distinctBy { it.start to it.end }.forEach {
                            builder.addStyle(it.item, it.start, it.end)
                        }
                    }
                }

                builder.addStyle(styleAttributeToApply, selection.min, selection.max)

                val newestAnnotatedString = builder.toAnnotatedString()

                currentValue.copy(
                    annotatedString = newestAnnotatedString,
                    selection = TextRange(selection.min, selection.max)
                )
            }
        }
    }
