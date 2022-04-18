package teamexpress.velo9;

import java.util.ArrayList;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import teamexpress.velo9.member.domain.Look;
import teamexpress.velo9.member.domain.LookRepository;
import teamexpress.velo9.member.domain.Love;
import teamexpress.velo9.member.domain.LoveRepository;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.member.domain.MemberRepository;
import teamexpress.velo9.member.domain.MemberThumbnail;
import teamexpress.velo9.member.domain.MemberThumbnailRepository;
import teamexpress.velo9.member.domain.Role;
import teamexpress.velo9.post.domain.Post;
import teamexpress.velo9.post.domain.PostAccess;
import teamexpress.velo9.post.domain.PostRepository;
import teamexpress.velo9.post.domain.PostStatus;
import teamexpress.velo9.post.domain.PostTag;
import teamexpress.velo9.post.domain.PostTagRepository;
import teamexpress.velo9.post.domain.PostThumbnail;
import teamexpress.velo9.post.domain.PostThumbnailRepository;
import teamexpress.velo9.post.domain.Series;
import teamexpress.velo9.post.domain.SeriesRepository;
import teamexpress.velo9.post.domain.Tag;
import teamexpress.velo9.post.domain.TagRepository;
import teamexpress.velo9.post.domain.TemporaryPost;
import teamexpress.velo9.post.domain.TemporaryPostRepository;

@Component
@RequiredArgsConstructor
public class InitDb {

	private final InitService initService;

	@PostConstruct
	public void init() {
		initService.dbInit1();
	}

	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService {

		private final PostRepository postRepository;
		private final MemberRepository memberRepository;
		private final PasswordEncoder passwordEncoder;
		private final SeriesRepository seriesRepository;
		private final PostThumbnailRepository postThumbnailRepository;
		private final TemporaryPostRepository temporaryPostRepository;
		private final TagRepository tagRepository;
		private final LoveRepository loveRepository;
		private final LookRepository lookRepository;
		private final MemberThumbnailRepository memberThumbnailRepository;
		private final PostTagRepository postTagRepository;

		private Member createMember2(
			String username,
			String password,
			String nickname,
			String introduce,
			String email,
			String blogTitle,
			String socialEmail,
			String socialGithub) {
			return Member.builder().
				username(username)
				.password(passwordEncoder.encode(password))
				.nickname(nickname)
				.introduce(introduce)
				.email(email + "@test.com")
				.blogTitle(blogTitle)
				.socialEmail(socialEmail)
				.socialGithub(socialGithub)
				.memberThumbnail(memberThumbnailRepository.save(new MemberThumbnail()))
				.role(Role.ROLE_USER)

				.build();
		}

		public void dbInit1() {

			for (int i = 0; i < 30; i++) {
				Member member = createMember2("username" + i, "1234", "nickname" + i, "introduce" + i, "email" + i, "blogTitle", "socialEmail", "socialGithub");
				memberRepository.save(member);
				Series series1 = createSeries("series" + i, member);
				Series series2 = createSeries("testSeries" + i + 1, member);
				seriesRepository.save(series1);
				seriesRepository.save(series2);
				Tag tag1 = new Tag("tag" + i);
				Tag tag2 = new Tag("testTag" + i + 1);
				tagRepository.save(tag1);
				tagRepository.save(tag2);
				for (int j = 0; j < 30; j++) {

					Post savedPost = postRepository.save(Post.builder()
						.member(member)
						.title("title" + i)
						.introduce("introduce")
						.content("content")
						.access(PostAccess.PUBLIC)
						.series(series1)
						.status(PostStatus.GENERAL)
						.postThumbnail(postThumbnailRepository.save(new PostThumbnail()))
						.temporaryPost(temporaryPostRepository.save(new TemporaryPost()))
						.build());
					postTagRepository.save(PostTag.builder().post(savedPost).tag(tag1).build());
					postTagRepository.save(PostTag.builder().post(savedPost).tag(tag2).build());
					loveRepository.save(createLove(member, savedPost));
					lookRepository.save(createLook(member, savedPost));
				}
			}
		}

		private Series createSeries(String name, Member member) {
			return Series.builder().name(name).member(member).posts(new ArrayList<>()).build();
		}

		private Love createLove(Member member, Post post) {
			return Love.builder().member(member).post(post).build();
		}

		private Look createLook(Member member, Post post) {
			return Look.builder().member(member).post(post).build();
		}
	}
}
