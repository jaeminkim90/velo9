package teamexpress.velo9.post.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamexpress.velo9.member.domain.Look;
import teamexpress.velo9.member.domain.LookRepository;
import teamexpress.velo9.member.domain.Love;
import teamexpress.velo9.member.domain.LoveRepository;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.member.domain.MemberRepository;
import teamexpress.velo9.post.api.PostThumbnailFileUploader;
import teamexpress.velo9.post.domain.Post;
import teamexpress.velo9.post.domain.PostRepository;
import teamexpress.velo9.post.domain.PostStatus;
import teamexpress.velo9.post.domain.PostTag;
import teamexpress.velo9.post.domain.PostTagRepository;
import teamexpress.velo9.post.domain.PostThumbnail;
import teamexpress.velo9.post.domain.PostThumbnailRepository;
import teamexpress.velo9.post.domain.Series;
import teamexpress.velo9.post.domain.SeriesRepository;
import teamexpress.velo9.post.domain.TemporaryPost;
import teamexpress.velo9.post.domain.TemporaryPostRepository;
import teamexpress.velo9.post.dto.LookPostDTO;
import teamexpress.velo9.post.dto.LoveDTO;
import teamexpress.velo9.post.dto.LovePostDTO;
import teamexpress.velo9.post.dto.PostMainDTO;
import teamexpress.velo9.post.dto.PostReadDTO;
import teamexpress.velo9.post.dto.PostSaveDTO;
import teamexpress.velo9.post.dto.PostWriteDTO;
import teamexpress.velo9.post.dto.ReadDTO;
import teamexpress.velo9.post.dto.SearchCondition;
import teamexpress.velo9.post.dto.SeriesDTO;
import teamexpress.velo9.post.dto.SeriesPostSummaryDTO;
import teamexpress.velo9.post.dto.SeriesReadDTO;
import teamexpress.velo9.post.dto.TempSavedPostDTO;
import teamexpress.velo9.post.dto.TemporaryPostWriteDTO;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	private static final int MAX_TEMPORARY_COUNT = 20;

	private final PostRepository postRepository;
	private final SeriesRepository seriesRepository;
	private final MemberRepository memberRepository;
	private final LoveRepository loveRepository;
	private final LookRepository lookRepository;
	private final TemporaryPostRepository temporaryPostRepository;
	private final PostThumbnailRepository postThumbnailRepository;
	private final PostTagRepository postTagRepository;

	@Transactional
	public Post write(PostSaveDTO postSaveDTO, Long memberId) {
		PostThumbnail postThumbnail = getPostThumbnail(postSaveDTO.getThumbnailFileName());
		Series series = getSeries(postSaveDTO.getSeriesId());
		Member member = getMember(memberId);
		if (postThumbnail != null) {
			postThumbnailRepository.save(postThumbnail);
		}

		Post post = null;

		if (postSaveDTO.getPostId() == null) {
			post = postRepository.save(postSaveDTO.toPost(member, series, postThumbnail));
		}

		if (postSaveDTO.getPostId() != null) {
			post = postRepository.findById(postSaveDTO.getPostId()).orElseThrow();
			checkSameMember(post, memberId);
			postSaveDTO.setIntroduce();
			post.edit(postSaveDTO.getTitle(),
				postSaveDTO.getIntroduce(),
				postSaveDTO.getContent(),
				postSaveDTO.getAccess(),
				series,
				postThumbnail);
		}

		return post;
	}

	@Transactional
	public Long writeTemporary(TemporaryPostWriteDTO temporaryPostWriteDTO, Long memberId) {
		if (temporaryPostWriteDTO.getPostId() != null) {
			return writeAlternativeTemporary(temporaryPostWriteDTO, memberId);
		}

		return writeNewTemporary(temporaryPostWriteDTO, memberId);
	}

	@Transactional
	public void remove(Long id) {
		lookRepository.deleteByPostId(id);
		loveRepository.deleteByPostId(id);
		postRepository.deleteById(id);
	}

	public PostWriteDTO findPostById(Long id) {
		Post post = postRepository.findWritePost(id).orElseThrow(
			() -> new IllegalStateException("존재하지 않는 포스트입니다."));
		List<PostTag> postTags = postTagRepository.findByPost(post);
		return new PostWriteDTO(post, postTags);
	}

	public Slice<SeriesDTO> findSeries(String nickname, Pageable pageable) {
		Slice<Series> seriesList = seriesRepository.findPostBySeriesName(nickname, pageable);
		List<Long> seriesIds = seriesList.map(Series::getId).toList();
		List<Post> postList = postRepository.findPostByIds(seriesIds);
		return seriesList.map(series -> new SeriesDTO(series, postList));
	}

	public Slice<PostReadDTO> findMainPost(String nickname, String tagName, Pageable pageable) {
		Slice<Post> posts = postRepository.findPost(nickname, tagName, pageable);
		List<Long> postIds = posts.map(Post::getId).toList();
		List<PostTag> postTagList = postTagRepository.findByPostIds(postIds);
		return posts.map(post -> new PostReadDTO(post, postTagList));
	}

	@Transactional
	public void loveOrNot(LoveDTO loveDTO, Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow();
		Post post = postRepository.findById(loveDTO.getPostId()).orElseThrow();

		toggleLove(member, post);
		postRepository.updateLoveCount(post, loveRepository.countByPost(post));
	}

	@Transactional
	public void look(Post post, Long memberId) {
		if (memberId != null) {
			Member member = memberRepository.findById(memberId).orElseThrow();
			makeLook(member, post);
		}
	}

	public Page<PostMainDTO> searchMain(SearchCondition searchCondition, Pageable pageable) {
		return postRepository.search(searchCondition, pageable).map(PostMainDTO::new);
	}

	public List<TempSavedPostDTO> findTempPosts(Long id) {
		List<Post> findPosts = postRepository.getTempSavedPost(id, PostStatus.TEMPORARY);

		return findPosts.stream()
			.map(TempSavedPostDTO::new)
			.collect(Collectors.toList());
	}

	private PostThumbnail getPostThumbnail(String fileName) {
		PostThumbnail postThumbnail = null;
		if (fileName != null) {
			postThumbnail = PostThumbnailFileUploader.divideFileName(fileName).toPostThumbnail();
		}
		return postThumbnail;
	}

	private Member getMember(Long memberId) {
		if (memberId == null) {
			throw new NullPointerException("member id MUST NOT BE NULL!!!");
		}

		return memberRepository.findById(memberId)
			.orElseThrow(() -> new NullPointerException("no member"));
	}

	private Series getSeries(Long seriesId) {
		return seriesId == null ? null : seriesRepository.findById(seriesId).orElse(null);
	}

	private Long writeAlternativeTemporary(TemporaryPostWriteDTO temporaryPostWriteDTO, Long memberId) {
		Post post = postRepository.findById(temporaryPostWriteDTO.getPostId()).orElseThrow();

		if (post.getStatus().equals(PostStatus.TEMPORARY)) {
			return writeNewTemporary(temporaryPostWriteDTO, memberId);
		}

		if (post.getTemporaryPost() != null) {
			temporaryPostWriteDTO.setAlternativeId(post.getTemporaryPost().getId());
		}

		TemporaryPost temporaryPost = temporaryPostWriteDTO.toTemporaryPost();
		temporaryPostRepository.save(temporaryPost);
		postRepository.updateTempPost(post.getId(), temporaryPost);

		return post.getId();
	}

	private Long writeNewTemporary(TemporaryPostWriteDTO temporaryPostWriteDTO, Long memberId) {
		checkCountTemp(memberId);
		Member member = getMember(memberId);
		return postRepository.save(
			temporaryPostWriteDTO.toPost(
				member, postRepository.getCreatedDate(temporaryPostWriteDTO.getPostId()))).getId();
	}

	private void toggleLove(Member member, Post post) {
		loveRepository.findByPostAndMember(post, member).ifPresentOrElse(
			loveRepository::delete,
			() -> loveRepository.save(
				Love.builder()
					.post(post)
					.member(member)
					.build()
			)
		);
	}

	private void makeLook(Member member, Post post) {
		if (lookRepository.findByPostAndMember(post.getId(), member.getId()).isEmpty()) {
			lookRepository.save(Look.builder().member(member).post(post).build());
		}
	}

	private void checkCountTemp(Long memberId) {
		if (postRepository.countByMemberAndStatus(memberRepository.findById(memberId).orElseThrow(), PostStatus.TEMPORARY) >= MAX_TEMPORARY_COUNT) {
			throw new IllegalStateException("임시저장은 " + MAX_TEMPORARY_COUNT + "개까지만 가능");
		}
	}

	public Slice<LovePostDTO> findLovePosts(Long memberId, PageRequest page) {
		Slice<Post> lovePosts = postRepository.findByJoinLove(memberId, page);
		return lovePosts.map(LovePostDTO::new);
	}

	public Slice<LookPostDTO> findReadPost(Long memberId, PageRequest page) {
		Slice<Post> lookPosts = postRepository.findByJoinLook(memberId, page);
		return lookPosts.map(LookPostDTO::new);
	}

	@Transactional
	public ReadDTO findPostDetails(Long postId, String nickname, Long memberId) {
		Post findPost = postRepository.findPostMemberById(postId).orElseThrow();
		findPost.addViewCount();
		look(findPost, memberId);
		checkOwner(findPost, nickname);
		List<Post> pagePost = postRepository.findPrevNextPost(findPost);
		List<PostTag> postTags = postTagRepository.findByPost(findPost);
		return new ReadDTO(findPost, pagePost, postTags);
	}

	public Slice<SeriesPostSummaryDTO> findPostsInSeries(String nickname, String seriesName, PageRequest page) {
		Slice<Post> seriesPosts = postRepository.findByJoinSeries(nickname, seriesName, page);
		List<Long> postIds = seriesPosts.map(Post::getId).toList();
		List<PostTag> postTagList = postTagRepository.findByPostIds(postIds);
		return seriesPosts.map(post -> new SeriesPostSummaryDTO(post, postTagList));
	}

	public List<SeriesReadDTO> findAllSeries(String nickname) {
		return seriesRepository.findUsedSeries(nickname)
			.stream().map(SeriesReadDTO::new).collect(Collectors.toList());
	}

	private void checkOwner(Post findPost, String nickname) {
		if (!findPost.getMember().getNickname().equals(nickname)) {
			throw new IllegalStateException("비정상적인 접근입니다.");
		}
	}

	private void checkSameMember(Post post, Long memberId) {
		if (post.getMember().getId() != memberId) {
			throw new IllegalStateException("잘못된 접근입니다.");
		}
	}
}
