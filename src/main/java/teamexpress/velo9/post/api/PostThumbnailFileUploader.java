package teamexpress.velo9.post.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import teamexpress.velo9.post.domain.PostThumbnailType;
import teamexpress.velo9.post.dto.PostThumbnailDTO;

@Service
public class PostThumbnailFileUploader {

	private static final String ROOT_PATH = "c:\\postThumbnail";
	private static final String BACKSLASH = "\\";

	public static PostThumbnailDTO divideFileName(String fileName) {
		String[] strings = fileName.split("_");

		PostThumbnailDTO postThumbnailDTO = new PostThumbnailDTO();
		postThumbnailDTO.setPath(strings[0].substring(0,strings[0].length()-2));
		postThumbnailDTO.setUuid(strings[1]);
		postThumbnailDTO.setName(strings[2]);

		return postThumbnailDTO;
	}

	public PostThumbnailDTO upload(MultipartFile uploadFile) {
		checkUploadFile(uploadFile);

		PostThumbnailDTO postThumbnailDTO = getThumbnailInfo(getUploadFileName(uploadFile));

		createFile(uploadFile, postThumbnailDTO);

		return postThumbnailDTO;
	}

	public byte[] getImage(String fileName) {
		byte[] result = null;

		try {
			result = FileCopyUtils.copyToByteArray(getFile(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public HttpHeaders getHeader(String fileName) {
		HttpHeaders header = new HttpHeaders();

		try {
			header.add("Content-Type", Files.probeContentType(getFile(fileName).toPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return header;
	}

	public void deleteFile(String fileName) {
		checkDeleteFileName(fileName);

		File file;

		try {
			file = new File(ROOT_PATH + BACKSLASH + URLDecoder.decode(fileName, "UTF-8"));
			file.delete();

			String largeFileName = file.getAbsolutePath().replace("s_", "");
			file = new File(largeFileName);
			file.delete();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void checkDeleteFileName(String fileName) {
		if (fileName == null || fileName.equals("")) {
			throw new IllegalStateException("파일 이름이 없습니다.");
		}
	}

	private File getFile(String fileName) {
		return new File(ROOT_PATH + BACKSLASH + fileName);
	}

	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Date date = new Date();

		String str = sdf.format(date);

		return str.replace("-", File.separator);
	}

	private File getUploadPath(String uploadFolderPath) {
		File uploadPath = new File(ROOT_PATH, uploadFolderPath);

		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}

		return uploadPath;
	}

	private PostThumbnailDTO getThumbnailInfo(String uploadFileName) {
		PostThumbnailDTO postThumbnailDTO = new PostThumbnailDTO();
		postThumbnailDTO.setName(uploadFileName);
		postThumbnailDTO.setPath(getFolder());
		postThumbnailDTO.setUuid(UUID.randomUUID().toString());

		return postThumbnailDTO;
	}


	private void checkUploadFile(MultipartFile uploadFile) {
		if (uploadFile == null || uploadFile.isEmpty() || PostThumbnailType.check(uploadFile.getContentType())) {
			throw new IllegalStateException("지원되지 않는 형식의 파일이거나 빈 파일입니다.");
		}
	}

	private String getUploadFileName(MultipartFile uploadFile) {
		String uploadFileName = uploadFile.getOriginalFilename();
		uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\") + 1);
		return uploadFileName;
	}

	private void createFile(MultipartFile uploadFile, PostThumbnailDTO postThumbnailDTO) {
		File uploadPath = getUploadPath(postThumbnailDTO.getPath());
		File saveFile = new File(uploadPath, postThumbnailDTO.getFileName());

		try {
			uploadFile.transferTo(saveFile);

			FileOutputStream thumbnail =
				new FileOutputStream(new File(uploadPath, postThumbnailDTO.getSFileName()));

			Thumbnailator.createThumbnail(uploadFile.getInputStream(), thumbnail, 80, 80);

			thumbnail.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
