package teamexpress.velo9.member.controller;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import teamexpress.velo9.common.controller.BaseController;
import teamexpress.velo9.common.dto.ThumbnailResponseDTO;
import teamexpress.velo9.member.api.MemberThumbnailFileUploader;
import teamexpress.velo9.member.dto.MemberThumbnailDTO;
import teamexpress.velo9.member.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberThumbnailController extends BaseController {

	private final MemberThumbnailFileUploader memberThumbnailFileUploader;
	private final MemberService memberService;

	@PostMapping("/uploadMemberThumbnail")
	public ThumbnailResponseDTO upload(MultipartFile uploadFile, HttpSession session) {
		MemberThumbnailDTO memberThumbnailDTO = memberThumbnailFileUploader.upload(uploadFile);
		memberService.uploadThumbnail(memberThumbnailDTO, getMemberId(session));
		return new ThumbnailResponseDTO(memberThumbnailDTO.getSFileNameWithPath());
	}

	@GetMapping("/displayMemberThumbnail")
	public ResponseEntity<byte[]> display(String fileName) {
		return new ResponseEntity<>(
			memberThumbnailFileUploader.getImage(fileName),
			memberThumbnailFileUploader.getHeader(fileName),
			HttpStatus.OK);
	}

	@PostMapping("/deleteMemberThumbnail")
	public void delete(String fileName, HttpSession session) {
		memberThumbnailFileUploader.deleteFile(fileName);
		memberService.deleteThumbnail(getMemberId(session));
	}
}
