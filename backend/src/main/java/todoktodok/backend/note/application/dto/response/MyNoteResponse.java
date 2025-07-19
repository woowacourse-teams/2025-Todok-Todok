package todoktodok.backend.note.application.dto.response;

public record MyNoteResponse(
        Long noteId,
//        BookResponse book, //todo BookResponse 추가하기
        String snap,
        String memo
) {
}
