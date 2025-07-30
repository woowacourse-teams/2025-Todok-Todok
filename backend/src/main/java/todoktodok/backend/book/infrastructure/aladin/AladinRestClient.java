package todoktodok.backend.book.infrastructure.aladin;

import java.net.URI;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriBuilder;

@Component
public class AladinRestClient {

    private static final int MAX_RESULT = 100;
    private static final int DOMESTIC_COMPUTER_CATEGORY_ID = 351;
    private static final String SEARCH_TYPE = "ISBN13";
    private static final String OUTPUT = "js";
    private static final String LATEST_VERSION = "20131101";

    private final String aladinItemSearchUri;
    private final String aladinApiKey;
    private final String aladinItemLookupUri;
    private final RestClient restClient;

    public AladinRestClient(
        @Value("${aladin.base-url}") final String aladinBaseUrl,
        @Value("${aladin.item-search-uri}") final String aladinItemSearchUri,
        @Value("${aladin.api-key}") final String aladinApiKey,
        @Value("${aladin.item-lookup-uri}") final String aladinItemLookupUri,
        final RestClient.Builder restClientBuilder
    ) {
        this.aladinItemSearchUri = aladinItemSearchUri;
        this.aladinApiKey = aladinApiKey;
        this.aladinItemLookupUri = aladinItemLookupUri;
        this.restClient = restClientBuilder.baseUrl(aladinBaseUrl).build();
    }

    public AladinItemResponses searchBooksByKeyword(final String searchBookKeyword) {
        try {
            return restClient.get()
                    .uri(createSearchBooksUri(searchBookKeyword))
                    .retrieve()
                    .body(AladinItemResponses.class);
        } catch (RestClientException e) {
            throw new RuntimeException("[ERROR] API 통신 중 문제 발생");
        }
    }

    public AladinItemResponses searchBookByIsbn(final String isbn) {
        try {
            return restClient.get()
                    .uri(createSearchBookUri(isbn))
                    .retrieve()
                    .body(AladinItemResponses.class);
        } catch (RestClientException e) {
            throw new RuntimeException("[ERROR] API 통신 중 문제 발생");
        }
    }

    private Function<UriBuilder, URI> createSearchBooksUri(final String searchBookKeyword) {
        return uriBuilder -> uriBuilder
                .path(aladinItemSearchUri)
                .queryParam("ttbkey", aladinApiKey)
                .queryParam("Query", searchBookKeyword)
                .queryParam("MaxResults", MAX_RESULT)
                .queryParam("CategoryId", DOMESTIC_COMPUTER_CATEGORY_ID)
                .queryParam("output", OUTPUT)
                .queryParam("Version", LATEST_VERSION)
                .build();
    }

    private Function<UriBuilder, URI> createSearchBookUri(final String isbn) {
        return uriBuilder -> uriBuilder
                .path(aladinItemLookupUri)
                .queryParam("ttbkey", aladinApiKey)
                .queryParam("itemIdType", SEARCH_TYPE)
                .queryParam("ItemId", isbn)
                .queryParam("output", OUTPUT)
                .queryParam("Version", LATEST_VERSION)
                .build();
    }
}
