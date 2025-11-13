package todoktodok.backend.book.application.service.command;

import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.book.application.dto.request.BookRequest;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.book.domain.repository.BookRepository;
import todoktodok.backend.book.infrastructure.aladin.AladinItemResponse;
import todoktodok.backend.book.infrastructure.aladin.AladinItemResponses;
import todoktodok.backend.book.infrastructure.aladin.AladinRestClient;
import todoktodok.backend.member.domain.repository.MemberRepository;

@Service
@Transactional
@AllArgsConstructor
public class BookCommandService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final AladinRestClient aladinRestClient;

    public Long createOrUpdateBook(
            final Long memberId,
            final BookRequest bookRequest
    ) {
        validateExistsMember(memberId);

        final String isbn = bookRequest.bookIsbn();

        return bookRepository.findByIsbn(isbn)
                .map(existingBook -> {
                    updateBookChanged(bookRequest, existingBook, isbn);
                    return existingBook.getId();
                })
                .orElseGet(() -> {
                    final Book newBook = createNewBook(bookRequest, isbn);
                    final Book savedBook = bookRepository.save(newBook);
                    return savedBook.getId();
                });
    }

    private void validateExistsMember(final Long memberId) {
        if (!memberRepository.existsByIdAndDeletedAtIsNull(memberId)) {
            throw new NoSuchElementException(
                    String.format("해당 회원을 찾을 수 없습니다: %s", memberId)
            );
        }
    }

    private void updateBookChanged(
            final BookRequest bookRequest,
            final Book existingBook,
            final String isbn
    ) {
        final AladinItemResponse aladinBookInfo = getBookInfoFromAladin(isbn);
        existingBook.update(
                bookRequest.bookTitle(),
                aladinBookInfo.description(),
                bookRequest.bookAuthor(),
                aladinBookInfo.publisher(),
                isbn,
                bookRequest.bookImage()
        );
    }

    private Book createNewBook(
            final BookRequest bookRequest,
            final String isbn
    ) {
        final AladinItemResponse aladinBookInfo = getBookInfoFromAladin(isbn);

        return Book.builder()
                .title(bookRequest.bookTitle())
                .summary(aladinBookInfo.description())
                .author(bookRequest.bookAuthor())
                .publisher(aladinBookInfo.publisher())
                .isbn(isbn)
                .image(bookRequest.bookImage())
                .build();
    }

    private AladinItemResponse getBookInfoFromAladin(final String isbn) {
        final AladinItemResponses responses = aladinRestClient.searchBookByIsbn(isbn);
        return responses.item().getFirst();
    }
}
