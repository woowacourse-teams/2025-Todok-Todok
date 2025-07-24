package com.example.todoktodok.presentation.view.discussion.create.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.model.Note
import com.example.domain.repository.DiscussionRepository
import com.example.domain.repository.NoteRepository
import com.example.todoktodok.presentation.core.event.MutableSingleLiveData
import com.example.todoktodok.presentation.core.event.SingleLiveData
import com.example.todoktodok.presentation.view.discussion.create.DiscussionCreateUiEvent

class DiscussionCreateViewModel(
    private val discussionRepository: DiscussionRepository,
    private val notesRepository: NoteRepository,
) : ViewModel() {
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    private val _uiEvent = MutableSingleLiveData<DiscussionCreateUiEvent>()
    val uiEvent: SingleLiveData<DiscussionCreateUiEvent> = _uiEvent

    fun onUiEvent(uiEvent: DiscussionCreateUiEvent) {
        _uiEvent.postValue(uiEvent)
    }

    suspend fun loadNotes() {
        _notes.value = notesRepository.fetchNotesByBookId(null)
    }
}
