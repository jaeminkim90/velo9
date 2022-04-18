package teamexpress.velo9.post.domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostThumbnailType {
	PNG("image/png"), JPEG("image/jpeg");

	private static final List<PostThumbnailType> types;

	static {
		types = Arrays.stream(PostThumbnailType.values()).collect(Collectors.toList());
	}

	private final String contentType;

	public static boolean check(String contentType) {
		return types.stream().noneMatch(type -> type.contentType.equals(contentType));
	}
}
