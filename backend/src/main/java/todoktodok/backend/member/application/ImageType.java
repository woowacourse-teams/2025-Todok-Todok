package todoktodok.backend.member.application;

import java.util.Arrays;

public enum ImageType {

    PNG("image/png", ".png"),
    JPG("image/jpg", ".jpg"),
    JPEG("image/jpeg", ".jpeg"),
    ;

    private final String type;
    private final String extension;

    ImageType(
            final String type,
            final String extension
    ) {
        this.type = type;
        this.extension = extension;
    }

    public static boolean contains(final String contentType) {
        return Arrays.stream(ImageType.values())
                .anyMatch(imageType -> contentType.equals(imageType.type));
    }

    public static String getExtension(final String contentType) {
        return Arrays.stream(ImageType.values())
                .filter(imageType -> contentType.equals(imageType.type))
                .findFirst()
                .get().extension;
    }
}
