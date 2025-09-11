package todoktodok.backend.member.application;

import java.util.Arrays;

public enum ImageType {

    PNG("image/png"),
    JPG("image/jpg"),
    JPEG("image/jpeg"),
    ;

    private final String type;

    ImageType(final String type) {
        this.type = type;
    }

    public static boolean contains(final String contentType) {
        return Arrays.stream(ImageType.values())
                .anyMatch(imageType -> contentType.equals(imageType.type));
    }
}
