package com.puresound.backend.service.search;

import com.puresound.backend.dto.search.SearchSuggestionResponse;

public interface SearchSuggestionService {
    SearchSuggestionResponse getSuggestions(String keyword, Integer limit);
}
