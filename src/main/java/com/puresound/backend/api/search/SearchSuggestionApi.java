package com.puresound.backend.api.search;

import com.puresound.backend.api_docs.search.SearchSuggestionDocs;
import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.search.SearchSuggestionResponse;
import com.puresound.backend.exception.exts.BadRequestException;
import com.puresound.backend.service.search.SearchSuggestionService;
import com.puresound.backend.util.ApiResponseFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/search")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchSuggestionApi implements SearchSuggestionDocs {
    SearchSuggestionService searchSuggestionService;
    ApiResponseFactory apiResponseFactory;

    @GetMapping(value = "/suggestions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<SearchSuggestionResponse>> getSuggestions(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "5") Integer limit,
            Locale locale
    ) {
        if (keyword == null || keyword.isBlank()) {
            throw new BadRequestException(ApiMessage.INVALID_REQUEST, LogLevel.INFO);
        }

        SearchSuggestionResponse response = searchSuggestionService.getSuggestions(keyword, limit);
        return ResponseEntity.ok(apiResponseFactory.create(ApiMessage.GET_SEARCH_SUGGESTIONS_SUCCESS, response, locale));
    }
}
