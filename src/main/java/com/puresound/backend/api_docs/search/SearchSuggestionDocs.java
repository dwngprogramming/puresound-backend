package com.puresound.backend.api_docs.search;

import com.puresound.backend.dto.ApiResponse;
import com.puresound.backend.dto.search.SearchSuggestionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Locale;

@Tag(name = "Search API", description = "API for search suggestions")
public interface SearchSuggestionDocs {

    @Operation(summary = "Get Search Suggestions",
            description = """
                    Retrieve grouped track, album, and artist suggestions for the given keyword.</br>
                    Default limit is `5` items per group and maximum limit is `10` items per group.</br>
                    """)
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "Get search suggestions successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Keyword is missing or blank")
    })
    ResponseEntity<ApiResponse<SearchSuggestionResponse>> getSuggestions(
            @Parameter(description = "Search keyword", example = "love") String keyword,
            @Parameter(description = "Maximum number of suggestions per group", example = "5") Integer limit,
            Locale locale
    );
}
