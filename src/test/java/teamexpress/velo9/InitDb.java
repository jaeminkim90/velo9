package teamexpress.velo9;

import java.util.ArrayList;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
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

@Profile("test")
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

		public void dbInit1() {

			memberRepository.save(createMember("jinwook", "jinwook", "1234", "test1@nate.com", Role.ROLE_USER));

			memberRepository.save(Member.builder()
				.nickname("admin")
				.blogTitle("blogTitle")
				.introduce("introduce")
				.socialEmail("1@naver.com")
				.socialGithub("2@nate.com")
				.username("id")
				.password(passwordEncoder.encode("1234"))
				.email("test2@nate.com")
				.role(Role.ROLE_USER)
				.memberThumbnail(memberThumbnailRepository.save(new MemberThumbnail()))
				.build()
			);

			memberRepository.save(createMember("test", "test", "1234", "test3@nate.com", Role.ROLE_USER));
			memberRepository.save(createSocialMember("test4@nate.com", Role.ROLE_USER));

			Member member = memberRepository.findByNickname("admin").get();

			Series series1 = createSeries("series1", member);
			Series series2 = createSeries("series2", member);
			Series series3 = createSeries("series3", member);
			Series series4 = createSeries("series4", member);
			Series series5 = createSeries("series5", member);
			Series series6 = createSeries("series6", member);
			Series series7 = createSeries("series7", member);
			Series series8 = createSeries("series8", member);
			Series series9 = createSeries("series9", member);
			Series series10 = createSeries("series10", member);
			seriesRepository.save(series1);
			seriesRepository.save(series2);
			seriesRepository.save(series3);
			seriesRepository.save(series4);
			seriesRepository.save(series5);
			seriesRepository.save(series6);
			seriesRepository.save(series7);
			seriesRepository.save(series8);
			seriesRepository.save(series9);
			seriesRepository.save(series10);

			Tag tag1 = new Tag("tag1");
			Tag tag2 = new Tag("tag2");
			tagRepository.save(tag1);
			tagRepository.save(tag2);

			Post savedPost = postRepository.save(Post.builder()
				.member(member)
				.title("title")
				.introduce("introduce")
				.content("content")
				.access(PostAccess.PUBLIC)
				.series(series1)
				.status(PostStatus.GENERAL)
				.postThumbnail(postThumbnailRepository.save(new PostThumbnail().builder()
						.uuid("uuid")
					.build()))
				.temporaryPost(temporaryPostRepository.save(new TemporaryPost()))
				.build()
			);

			postTagRepository.save(PostTag.builder().post(savedPost).tag(tag1).build());

			for (int i = 1; i < 4; i++) {
				savedPost = postRepository.save(createPost("" + i, "" + i, "" + i, member, series1));
				loveRepository.save(createLove(member, savedPost));
				lookRepository.save(createLook(member, savedPost));

				postTagRepository.save(PostTag.builder().post(savedPost).tag(tag1).build());
				postTagRepository.save(PostTag.builder().post(savedPost).tag(tag2).build());
			}

			for (int i = 5; i < 7; i++) {
				savedPost = postRepository.save(createTemp("" + i, "" + i, "" + i, member, series1));
				loveRepository.save(createLove(member, savedPost));
				lookRepository.save(createLook(member, savedPost));
			}
		}

		private Post createPost(String title, String introduce, String content, Member member, Series series) {
			return Post.builder().access(PostAccess.PUBLIC).title(title).introduce(introduce).content(content).member(member).series(series).status(PostStatus.GENERAL).build();
		}

		private Post createTemp(String title, String introduce, String content, Member member, Series series) {
			return Post.builder().access(PostAccess.PUBLIC).title(title).introduce(introduce).content(content).member(member).series(series).status(PostStatus.TEMPORARY).build();
		}

		private Series createSeries(String name, Member member) {
			return Series.builder().name(name).member(member).posts(new ArrayList<>()).build();
		}

		private Member createMember(String nickname, String username, String password, String email, Role roleUser) {
			return Member.builder().nickname(nickname).blogTitle(nickname).username(username).password(passwordEncoder.encode(password)).email(email).posts(new ArrayList<>()).role(roleUser).build();
		}

		private Member createSocialMember(String email, Role roleUser) {
			return Member.builder().email(email).posts(new ArrayList<>()).role(roleUser).build();
		}

		private Love createLove(Member member, Post post) {
			return Love.builder().member(member).post(post).build();
		}

		private Look createLook(Member member, Post post) {
			return Look.builder().member(member).post(post).build();
		}
	}
}
