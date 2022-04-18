package teamexpress.velo9.common.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResult<T> {

	private T data;
	private T subData;
}
