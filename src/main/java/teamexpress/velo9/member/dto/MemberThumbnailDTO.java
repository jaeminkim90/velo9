package teamexpress.velo9.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import teamexpress.velo9.member.domain.MemberThumbnail;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberThumbnailDTO {

	private static final String NAME_SEPARATOR = "_";
	private static final String THUMBNAIL_MARK = "s_";
	private static final String BACKSLASH = "\\";

	private String uuid;
	private String name;
	private String path;

	public MemberThumbnailDTO(MemberThumbnail memberThumbnail) {
		uuid = memberThumbnail.getUuid();
		name = memberThumbnail.getName();
		path = memberThumbnail.getPath();
	}

	public MemberThumbnail toMemberThumbnail() {

		return MemberThumbnail.builder()
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
