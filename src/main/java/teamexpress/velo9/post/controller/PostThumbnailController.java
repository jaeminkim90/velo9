package teamexpress.velo9.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import teamexpress.velo9.common.dto.ThumbnailResponseDTO;
import teamexpress.velo9.post.api.PostThumbnailFileUploader;

@RestController
@RequiredArgsConstructor
public class PostThumbnailController {

	private final PostThumbnailFileUploader postThumbnailFileUploader;

	@GetMapping("/displayPostThumbnail")
	public ResponseEntity<byte[]> display(String fileName) {
		return new ResponseEntity<>(
			postThumbnailFileUploader.getImage(fileName),
			postThumbnailFileUploader.getHeader(fileName),
			HttpStatus.OK);
	}

	@PostMapping("/uploadPostThumbnail")
	public ThumbnailResponseDTO upload(MultipartFile uploadFile) {
		return new ThumbnailResponseDTO(postThumbnailFileUploader.upload(uploadFile).getSFileNameWithPath());
	}

	@PostMapping("/deletePostThumbnail")
	public void delete(String fileName) {
		postThumbnailFileUploader.deleteFile(fileName);
	}
}
