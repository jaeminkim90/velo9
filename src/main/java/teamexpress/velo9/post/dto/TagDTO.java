package teamexpress.velo9.post.dto;

import lombok.Data;
import teamexpress.velo9.post.domain.Tag;

@Data
public class TagDTO {

	private Long tagId;
	private String tagName;

	public TagDTO(Tag tag) {
		this.tagId = tag.getId();
		this.tagName = tag.getName();
	}
}
