package todoktodok.backend.shelf.application.service.command;

import java.util.NoSuchElementException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todoktodok.backend.book.application.dto.response.BookResponse;
import todoktodok.backend.book.domain.Book;
import todoktodok.backend.book.domain.repository.BookRepository;
import todoktodok.backend.member.domain.Member;
import todoktodok.backend.member.domain.repository.MemberRepository;
import todoktodok.backend.shelf.application.dto.request.ShelfRequest;
import todoktodok.backend.shelf.domain.Shelf;
import todoktodok.backend.shelf.domain.repository.ShelfRepository;

@Service
@Transactional
@AllArgsConstructor
public class ShelfCommandService {

    private final ShelfRepository shelfRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;

    public BookResponse addBook(
            final Long memberId,
            final ShelfRequest shelfRequest
    ) {
        final Member member = getMember(memberId);
        final Book book = getBook(shelfRequest.bookId());

        final Shelf shelf = Shelf.builder()
                .member(member)
                .book(book)
                .build();
        final Shelf savedShelf = shelfRepository.save(shelf);
        return new BookResponse(savedShelf.getBook());
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 회원을 찾을 수 없습니다"));
    }

    private Book getBook(final Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("해당하는 도서를 찾을 수 없습니다"));
    }
}
