package teamexpress.velo9.post.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchCondition {

	private boolean tagSelect;
	private String content;
}
