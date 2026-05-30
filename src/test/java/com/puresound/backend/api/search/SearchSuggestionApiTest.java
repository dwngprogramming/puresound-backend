package com.puresound.backend.api.search;

import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.search.SearchSuggestionResponse;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.service.search.SearchSuggestionService;
import com.puresound.backend.util.ApiResponseFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchSuggestionApiTest {

    @Mock
    SearchSuggestionService searchSuggestionService;

    @Mock
    ApiResponseFactory apiResponseFactory;

    @InjectMocks
    SearchSuggestionApi searchSuggestionApi;

    @Test
    void getSuggestionsRejectsBlankKeyword() {
        assertThatThrownBy(() -> searchSuggestionApi.getSuggestions("   ", 5, Locale.ENGLISH))
                .isInstanceOf(BadRequestException.class)
                .extracting("apiMessage")
                .isEqualTo(ApiMessage.INVALID_REQUEST);
    }

    @Test
    void getSuggestionsReturnsWrappedResponse() {
        SearchSuggestionResponse suggestions = SearchSuggestionResponse.of("love", List.of(), List.of(), List.of());
        ApiResponse<SearchSuggestionResponse> apiResponse = new ApiResponse<>(
                ApiMessage.GET_SEARCH_SUGGESTIONS_SUCCESS.name(),
                "Get search suggestions successfully",
                suggestions
        );

        when(searchSuggestionService.getSuggestions("love", 5)).thenReturn(suggestions);
        when(apiResponseFactory.create(ApiMessage.GET_SEARCH_SUGGESTIONS_SUCCESS, suggestions, Locale.ENGLISH))
                .thenReturn(apiResponse);

        ResponseEntity<ApiResponse<SearchSuggestionResponse>> response =
                searchSuggestionApi.getSuggestions("love", 5, Locale.ENGLISH);

        assertThat(response.getBody()).isEqualTo(apiResponse);
        verify(searchSuggestionService).getSuggestions("love", 5);
    }
}
