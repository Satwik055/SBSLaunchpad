package com.satwik.sbslaunchpad.features.search.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.satwik.sbslaunchpad.data.post.model.Post
import com.satwik.sbslaunchpad.data.post.PostRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repository: PostRepository
) : ViewModel() {

    val searchState = TextFieldState()

    private val _searchResults = mutableStateOf<List<Post>>(emptyList())
    val searchResults: List<Post> get() = _searchResults.value

    private val _isSearching = mutableStateOf(false)
    val isSearching: Boolean get() = _isSearching.value

    init {
        viewModelScope.launch {
            snapshotFlow { searchState.text }
                .map { it.toString() }
                .distinctUntilChanged()
                .debounce(500L)
                .collectLatest { query ->
                    if (query.isBlank()) {
                        _searchResults.value = emptyList()
                        _isSearching.value = false
                    } else {
                        _isSearching.value = true
                        try {
                            _searchResults.value = repository.searchPost(query)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            _searchResults.value = emptyList()
                        } finally {
                            _isSearching.value = false
                        }
                    }
                }
        }
    }
}
