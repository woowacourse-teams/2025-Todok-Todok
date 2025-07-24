package com.example.todoktodok.presentation.view.discussion.create.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
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
    private val _selectedNote = MutableLiveData<Note>()
    val selectedNote: LiveData<Note> = _selectedNote

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    private val discussionTitle = MutableLiveData<String>()
    private val discussionOpinion = MutableLiveData<String>()

    val isCreateEnabled =
        MediatorLiveData<Boolean>().apply {
            val update = {
                val note = _selectedNote.value
                val title = discussionTitle.value
                val opinion = discussionOpinion.value
                value = note != null && !title.isNullOrBlank() && !opinion.isNullOrBlank()
            }
            addSource(selectedNote) { update() }
            addSource(discussionTitle) { update() }
            addSource(discussionOpinion) { update() }
        }

    private val _uiEvent = MutableSingleLiveData<DiscussionCreateUiEvent>()
    val uiEvent: SingleLiveData<DiscussionCreateUiEvent> = _uiEvent

    fun onUiEvent(uiEvent: DiscussionCreateUiEvent) {
        _uiEvent.postValue(uiEvent)
    }

    suspend fun loadNotes() {
        _notes.value = notesRepository.fetchNotesByBookId(null)
    }

    fun selectNote(note: Note) {
        _selectedNote.value = note
    }

    suspend fun saveNote(): Long {
        _selectedNote.value?.let {
            return discussionRepository.saveDiscussion(
                it.id,
                discussionTitle.value ?: "",
                discussionOpinion.value ?: "",
            )
        } ?: return -1
    }

    fun updateDiscussionTitle(text: String) {
        discussionTitle.value = text
    }

    fun updateDiscussionOpinion(text: String) {
        discussionOpinion.value = text
    }
}
