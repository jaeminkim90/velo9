package teamexpress.velo9.post.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamexpress.velo9.member.domain.LookRepository;
import teamexpress.velo9.member.domain.Love;
import teamexpress.velo9.member.domain.LoveRepository;
import teamexpress.velo9.member.domain.Member;
import teamexpress.velo9.member.domain.MemberRepository;
import teamexpress.velo9.post.domain.Post;
import teamexpress.velo9.post.domain.PostRepository;
import teamexpress.velo9.post.domain.PostStatus;
import teamexpress.velo9.post.domain.PostTag;
import teamexpress.velo9.post.domain.PostTagQueryRepository;
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
import teamexpress.velo9.post.dto.ReadDTO;
import teamexpress.velo9.post.dto.SearchCondition;
import teamexpress.velo9.post.dto.SeriesDTO;
import teamexpress.velo9.post.dto.SeriesPostSummaryDTO;
import teamexpress.velo9.post.dto.TempSavedPostDTO;
import teamexpress.velo9.post.dto.TemporaryPostWriteDTO;

@Service
@Slf4j
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
	private final PostTagQueryRepository postTagQueryRepository;

	@Transactional
	public Long write(PostSaveDTO postSaveDTO) {
		Post findPost = postRepository.findById(postSaveDTO.getPostId()).orElse(new Post()); // 포스트 존재하며 불러오고, 아닐 경우 새로 생성
		Member findMember = getMember(postSaveDTO.getMemberId());// member 찾아온다

		// 없는 member일 경우 예외 발생
		if (findMember == null) {
			throw new IllegalStateException("잘못된 요청입니다.");
		}

		// post의 id와 member의 id가 같은지 여부를 확인. 같지 않을 경우 예외 발생
		if (!Objects.equals(findPost.getMember().getId(), findMember.getId())) {
			throw new IllegalStateException("잘못된 요청입니다.");
		}

		// 파라미터로 받아온 DTO의 정보를 이용해 새로운 post 엔티티를 생성한다
		findPost.newOrEdit(
			postSaveDTO.getTitle(),
			postSaveDTO.getIntroduce(),
			postSaveDTO.getContent(),
			postSaveDTO.getAccess(),
			findMember);

		return findPost.getId(); // 다 만들어지면 id를 반환한다
	}

	@Transactional
	public void writeTemporary(TemporaryPostWriteDTO temporaryPostWriteDTO) {
		if (temporaryPostWriteDTO.getPostId() != null) {
			writeAlternativeTemporary(temporaryPostWriteDTO);
			return;
		}

		writeNewTemporary(temporaryPostWriteDTO);
	}

	@Transactional
	public void delete(Long id) {
		lookRepository.deleteByPostId(id);
		loveRepository.deleteByPostId(id);
		postRepository.deleteById(id);
	}

	public PostSaveDTO getPostById(Long id) {
		Post post = postRepository.findWritePost(id).orElse(new Post());
		List<PostTag> postTags = postTagQueryRepository.findByPost(post);
		return new PostSaveDTO(post, postTags);
	}

	public Slice<SeriesDTO> findSeries(String nickname, Pageable pageable) {
		Slice<Series> seriesList = seriesRepository.findPostBySeriesName(nickname, pageable);
		return seriesList.map(SeriesDTO::new);
	}

	public Slice<PostReadDTO> findPost(String nickname, String tagName, Pageable pageable) {
		Slice<Post> posts = postRepository.findPost(nickname, tagName, pageable);
		return posts.map(PostReadDTO::new);
	}

	@Transactional
	public void loveOrNot(LoveDTO loveDTO) {
		Member member = memberRepository.findById(loveDTO.getMemberId()).orElseThrow();
		Post post = postRepository.findById(loveDTO.getPostId()).orElseThrow();

		toggleLove(member, post);
		postRepository.updateLoveCount(post, loveRepository.countByPost(post));
	}

	@Transactional
	public void look(Long postId, Long memberId) {
		makeLook(memberId, postId);
		postRepository.updateViewCount(postId);
	}

	public Page<PostMainDTO> searchMain(SearchCondition searchCondition, Pageable pageable) {
		return postRepository.search(searchCondition, pageable).map(PostMainDTO::new);
	}

	public List<TempSavedPostDTO> getTempSavedPost(Long id) {
		List<Post> findPosts = postRepository.getTempSavedPost(id, PostStatus.TEMPORARY);

		return findPosts.stream()
			.map(TempSavedPostDTO::new)
			.collect(Collectors.toList());
	}

	private Member getMember(Long memberId) {
		if (memberId == null) {
			throw new NullPointerException("no member is NOT NULL!!!");
		}

		return memberRepository.findById(memberId)
			.orElseThrow(() -> new NullPointerException("no member"));
	}

	private void writeAlternativeTemporary(TemporaryPostWriteDTO temporaryPostWriteDTO) {
		Post post = postRepository.findById(temporaryPostWriteDTO.getPostId()).orElseThrow();

		if (post.getStatus().equals(PostStatus.TEMPORARY)) {
			writeNewTemporary(temporaryPostWriteDTO);
			return;
		}

		if (post.getTemporaryPost() != null) {
			temporaryPostWriteDTO.setAlternativeId(post.getTemporaryPost().getId());
		}

		TemporaryPost temporaryPost = temporaryPostWriteDTO.toTemporaryPost();
		temporaryPostRepository.save(temporaryPost);
		postRepository.updateTempPost(post.getId(), temporaryPost);
	}

	private void writeNewTemporary(TemporaryPostWriteDTO temporaryPostWriteDTO) {
		Long memberId = temporaryPostWriteDTO.getMemberId();

		checkCount(memberId);
		Member member = getMember(memberId);
		postRepository.save(temporaryPostWriteDTO.toPost(member, postRepository.getCreatedDate(temporaryPostWriteDTO.getPostId())));
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

	private void makeLook(Long memberId, Long postId) {
		if (memberId != null && lookRepository.findByPostAndMember(postId, memberId).isEmpty()) {
			//세션문제가 해결되면 이 주석과 함께 수정할것
			lookRepository.saveLook(memberId, postId);
		}
	}

	private void checkCount(Long memberId) {
		if (postRepository.countByMemberAndStatus(memberRepository.findById(memberId).orElseThrow(), PostStatus.TEMPORARY) >= MAX_TEMPORARY_COUNT) {
			throw new IllegalStateException("임시저장은 " + MAX_TEMPORARY_COUNT + "개까지만 가능");
		}
	}

	public Slice<LovePostDTO> getLovePosts(Long memberId, PageRequest page) {
		Slice<Post> lovePosts = postRepository.findByJoinLove(memberId, page);
		return lovePosts.map(LovePostDTO::new);
	}

	public Slice<LookPostDTO> getLookPosts(Long memberId, PageRequest page) {
		Slice<Post> lookPosts = postRepository.findByJoinLook(memberId, page);
		return lookPosts.map(LookPostDTO::new);
	}

	public ReadDTO findReadPost(Long postId, String nickname) {
		Post findPost = postRepository.findById(postId).orElseThrow();
		List<Post> pagePost = postRepository.findPrevNextPost(findPost);
		Post readPost = postRepository.findReadPost(postId, nickname);
		List<PostTag> postTags = postTagQueryRepository.findByPost(findPost);
		return new ReadDTO(readPost, pagePost, postTags);
	}

	public Slice<SeriesPostSummaryDTO> findSeriesPost(String nickname, String seriesName, PageRequest page) {
		Slice<Post> seriesPosts = postRepository.findByJoinSeries(nickname, seriesName, page);
		return seriesPosts.map(SeriesPostSummaryDTO::new);
	}
}
