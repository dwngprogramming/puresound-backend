package com.puresound.backend.service.location;

import com.fasterxml.jackson.databind.JsonNode;
import com.puresound.backend.constant.api.ApiMessage;
import com.puresound.backend.constant.api.LogLevel;
import com.puresound.backend.dto.location.LocationResponse;
import com.puresound.backend.dto.location.ReverseGeocodingRequest;
import com.puresound.backend.exception.InternalServerException;
import com.puresound.backend.exception.exts.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Locale;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Service
@Slf4j
public class DefaultLocationService implements LocationService {
    final RestClient restClient;

    @Value("${nominatim.url}")
    String url;

    @Override
    public LocationResponse reverseGeocode(ReverseGeocodingRequest request) {
        Locale locale = LocaleContextHolder.getLocale();
        String requestUrl = UriComponentsBuilder.fromUriString(url)
                .queryParam("lat", request.latitude())
                .queryParam("lon", request.longitude())
                .queryParam("format", "geojson")
                .queryParam("accept-language", locale.getLanguage())
                .toUriString();

        try {
            JsonNode jsonNode = restClient
                    .get()
                    .uri(requestUrl)
                    .retrieve()
                    .body(JsonNode.class);

            return extractLocation(jsonNode);

        } catch (RestClientException e) {
            throw new InternalServerException("Failed to reverse geocode location");
        } catch (Exception e) {
            throw new NotFoundException(ApiMessage.LOCATION_NOT_FOUND, LogLevel.INFO);
        }
    }

    private LocationResponse extractLocation(JsonNode root) {
        try {
            JsonNode address = root
                    .path("features")
                    .path(0)
                    .path("properties")
                    .path("address");

            String county = address.path("county").asText();
            String suburb = address.path("suburb").asText();

            String state = address.path("state").asText();
            String city = address.path("city").asText();

            String district = county.isEmpty() ? suburb : county;
            String province = state.isEmpty() ? city : state;
            String countryCode = address.path("country_code").asText();

            return new LocationResponse(
                    cleanName(district),
                    cleanName(province),
                    countryCode
            );
        } catch (Exception e) {
            throw new InternalServerException("Failed to parse location data");
        }
    }

    private String cleanName(String name) {
        if (name == null || name.isEmpty()) {
            return "Unknown";
        }
        return name
                // Remove Vietnamese prefixes
                .replaceAll("^(Tỉnh|Thành phố|Phường|Quận|Huyện|TP\\.)\\s+", "")
                // Remove English prefixes
                .replaceAll("^(Province of|City of|Municipality of|The\\s+)\\s*", "")
                // Remove English suffixes
                .replaceAll("\\s+(Province|City|Ward|District|County|Municipality|Governorate|Prefecture|Region)$", "")
                .trim();
    }
}

