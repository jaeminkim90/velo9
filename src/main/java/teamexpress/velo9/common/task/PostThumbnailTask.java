package teamexpress.velo9.common.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import teamexpress.velo9.post.domain.PostThumbnail;
import teamexpress.velo9.post.domain.PostThumbnailRepository;

@Component
@RequiredArgsConstructor
public class PostThumbnailTask {

	private static final String ROOT_PATH = "c:\\resources";
	private static final String NAME_SEPARATOR = "_";
	private static final String THUMBNAIL_MARK = "s_";
	private static final int ONE_DAY_AGO = -1;

	private final PostThumbnailRepository postThumbnailRepository;

	private String getFolderYesterday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, ONE_DAY_AGO);

		String str = sdf.format(cal.getTime());

		return str.replace("-", File.separator);
	}

	@Scheduled(cron = "0 47 13 * * *")
	public void deleteUnusedFiles() {
		List<PostThumbnail> fileList = postThumbnailRepository.getOldFiles();

		List<Path> fileListPaths = fileList.stream().map(
				vo -> Paths.get(ROOT_PATH, vo.getPath(), vo.getUuid() + NAME_SEPARATOR + vo.getName()))
			.collect(Collectors.toList());

		fileList.stream().map(
				vo -> Paths.get(ROOT_PATH,
					vo.getPath(),
					THUMBNAIL_MARK + vo.getUuid() + NAME_SEPARATOR + vo.getName()))
			.forEach(fileListPaths::add);

		File targetDir = Paths.get(ROOT_PATH, getFolderYesterday()).toFile();

		File[] removeFiles =
			targetDir.listFiles(file -> !fileListPaths.contains(file.toPath()));

		Arrays.stream(removeFiles).forEach(File::delete);
	}
}
