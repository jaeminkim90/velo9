package teamexpress.velo9.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import teamexpress.velo9.post.domain.PostThumbnail;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostThumbnailDTO {

	private static final String NAME_SEPARATOR = "_";
	private static final String THUMBNAIL_MARK = "s_";
	private static final String BACKSLASH = "\\";

	private String uuid;
	private String name;
	private String path;

	public PostThumbnailDTO(PostThumbnail postThumbnail) {
		if (postThumbnail != null) {
			this.uuid = postThumbnail.getUuid();
			this.name = postThumbnail.getName();
			this.path = postThumbnail.getPath();
		}
	}

	public PostThumbnail toPostThumbnail() {
		return PostThumbnail.builder()
			.uuid(this.uuid)
			.path(this.path)
			.name(this.name)
			.build();
	}

	public String getFileName() {
		return this.uuid + NAME_SEPARATOR + this.name;
	}

	public String getSFileName() {
		return THUMBNAIL_MARK + this.getFileName();
	}

	public String getSFileNameWithPath() {
		return this.path + BACKSLASH + this.getSFileName();
	}
}
