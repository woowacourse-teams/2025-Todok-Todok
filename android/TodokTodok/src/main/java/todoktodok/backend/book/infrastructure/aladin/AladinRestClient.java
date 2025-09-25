package todoktodok.backend.book.infrastructure.aladin;

import java.net.URI;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import todoktodok.backend.book.infrastructure.aladin.exception.AladinApiException;

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
        return getAladinItemResponses(
                createSearchBooksUri(searchBookKeyword),
                String.format("searchBookKeyword= %s", searchBookKeyword)
        );
    }

    public AladinItemResponses searchBookByIsbn(final String isbn) {
        return getAladinItemResponses(
                createSearchBookUri(isbn),
                String.format("isbn= %s", isbn)
        );
    }

    public AladinItemResponses searchBooksByKeywordWithPaging(
            final String searchBookKeyword,
            final int page,
            final int size
    ) {
        return getAladinItemResponses(
            createSearchBooksUriByPaging(searchBookKeyword, page, size),
            String.format("searchBookKeyword= %s, page= %d, size= %d", searchBookKeyword, page, size)
        );
    }

    private Function<UriBuilder, URI> createSearchBooksUri(final String searchBookKeyword) {
        return uriBuilder -> uriBuilder
                .path(aladinItemSearchUri)
                .queryParam("ttbkey", aladinApiKey)
                .queryParam("Query", searchBookKeyword)
                .queryParam("MaxResults", MAX_RESULT)
                .queryParam("CategoryId", DOMESTIC_COMPUTER_CATEGORY_ID)
                .queryParam("Output", OUTPUT)
                .queryParam("Version", LATEST_VERSION)
                .build();
    }

    private Function<UriBuilder, URI> createSearchBookUri(final String isbn) {
        return uriBuilder -> uriBuilder
                .path(aladinItemLookupUri)
                .queryParam("ttbkey", aladinApiKey)
                .queryParam("ItemIdType", SEARCH_TYPE)
                .queryParam("ItemId", isbn)
                .queryParam("Output", OUTPUT)
                .queryParam("Version", LATEST_VERSION)
                .build();
    }

    private Function<UriBuilder, URI> createSearchBooksUriByPaging(
            final String searchBookKeyword,
            final int page,
            final int size
    ) {
        return uriBuilder -> uriBuilder
                .path(aladinItemSearchUri)
                .queryParam("ttbkey", aladinApiKey)
                .queryParam("Query", searchBookKeyword)
                .queryParam("Start", page)
                .queryParam("MaxResults", size)
                .queryParam("CategoryId", DOMESTIC_COMPUTER_CATEGORY_ID)
                .queryParam("Output", OUTPUT)
                .queryParam("Version", LATEST_VERSION)
                .build();
    }

    private AladinItemResponses getAladinItemResponses(
            final Function<UriBuilder, URI> uri,
            final String context
    ) {
        final URI requestUri = uri.apply(UriComponentsBuilder.newInstance());

        try {
            final AladinItemResponses response = restClient.get()
                    .uri(requestUri)
                    .retrieve()
                    .body(AladinItemResponses.class);

            if (response == null || response.item() == null) {
                throw new AladinApiException(
                        String.format("알라딘 API 응답이 비정상입니다: %s, uri= %s, item= null", context, requestUri)
                );
            }
            return response;

        } catch (final RestClientException e) {
            throw new AladinApiException(
                    String.format("알라딘 API 통신 중 오류: %s, uri= %s, errorMessage= %s", context, requestUri, e.getMessage())
            );
        }
    }
}
