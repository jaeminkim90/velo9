# :pushpin: velo9
### `velo9`는 단순하고 직관적인 사용이 가능한 `웹 기반 블로그 서비스`입니다.


</br>

## 1. 제작 기간 & 참여 인원
- 2022년 2월 21일 ~ 4월 16일
- 팀 프로젝트(5명)
  - 백엔드: 김진욱 / 손찬우 / 김재민
  - 프런트엔드: 조민수 / 박찬하

</br>

## 2. 사용 기술
#### `Back-end`
- Java 11
- Spring Boot 2.6.4
- Gradle
- Spring Data JPA
- QueryDSL
- MySQL 8.0
- Spring Security
#### `Front-end`
- React
- SCSS
- ES6

</br>

## 3. ERD 설계

<details>
<summary><b>ERD 전체 보기</b></summary>

<div markdown="1">

![](https://velog.velcdn.com/images/woply/post/b30e1438-7808-4d18-b746-39f5b3e18ad8/image.jpeg)
</div>
</details>

<br>

## 4. 핵심 기능
**`velo9`는 단순하고 직관적인 사용이 가능한 `웹 기반 블로그 서비스`입니다.** <br><br>
velo9는 블로그 활동에 필요한 다양한 편의 기능을 제공합니다.<br>
누구나 쉽고 간편하게 `포스트 작성`이 가능하며, 태그와 시리즈 정보를 활용해 `포스트를 빠르게 탐색`할 수 있습니다.

<details>
<summary><b>핵심 기능 설명 펼치기</b></summary>

<div markdown="1">

## 4.1. 포스트 작성 관련 기능

<br>

> ### 4.1.1. 글 작성과 글 수정을 한 곳에서 처리 :pushpin: [코드 확인](https://github.com/team-express/velo9/blob/fb2cdc52f5a47e4bb1afaa4b15ce39540d57f85c/src/main/java/teamexpress/velo9/post/service/PostService.java#L61)
- 신규 글 작성과 기존 글 수정을 단일 `Controller - Service - Repository`에서 처리할 수 있도록 코드를 설계하였습니다. <br>

<details>
<summary><b> 동작 구조도 펼치기</b></summary>

<div markdown="1">

![](https://velog.velcdn.com/images/woply/post/22a12441-8864-44c5-bd5d-b7e130443520/image.jpg)

</div>
</details>




<br>

> ### 4.1.2. 포스트용 섬네일 등록 :pushpin: [코드 확인](https://github.com/team-express/velo9/blob/fb2cdc52f5a47e4bb1afaa4b15ce39540d57f85c/src/main/java/teamexpress/velo9/post/api/PostThumbnailFileUploader.java#L37)
- 포스트에 대한 정보를 한 눈에 확인 할 수 있도록 섬네일 업로드를 지원합니다.<br>

<details>
<summary><b> 동작 구조도 펼치기</b></summary>

<div markdown="1">

![](https://velog.velcdn.com/images/woply/post/f88d7a93-0aba-4715-b8b4-81effc6592bd/image.jpg)

</div>
</details>

<br>


> ### 4.1.3. 태그, 시리즈 등록 :pushpin: [태그 관련 코드 확인](https://github.com/team-express/velo9/blob/fb2cdc52f5a47e4bb1afaa4b15ce39540d57f85c/src/main/java/teamexpress/velo9/post/service/TagService.java#L26) / [시리즈 관련 코드 확인](https://github.com/team-express/velo9/blob/fb2cdc52f5a47e4bb1afaa4b15ce39540d57f85c/src/main/java/teamexpress/velo9/post/service/SeriesService.java#L32)
- 포스트 내용을 쉽게 파악하고, 조회할 수 있도록 태그와 시리즈를 추가할 수 있습니다<br>

<details>
<summary><b> 동작 구조도 펼치기</b></summary>

<div markdown="1">

![](https://velog.velcdn.com/images/woply/post/2d55f142-f0ef-4645-92b7-b007c3472fd5/image.jpg)

</div>
</details>

<br>

> ### 4.1.4. 포스트 소개글 자동 등록 :pushpin: [코드 확인](https://github.com/team-express/velo9/blob/fb2cdc52f5a47e4bb1afaa4b15ce39540d57f85c/src/main/java/teamexpress/velo9/post/dto/PostSaveDTO.java#L50)
- 포스트 소개글 미입력시, 본문 내용의 150자를 소개글로 자동 등록합니다.<br>

<details>
<summary><b> 동작 구조도 펼치기</b></summary>

<div markdown="1">

![](https://velog.velcdn.com/images/woply/post/c0adb39e-71d2-41e6-a4c7-bab184a419c6/image.jpg)

</div>
</details>

<br>

> ### 4.1.5. 임시 저장 :pushpin: [코드 확인](https://github.com/team-express/velo9/blob/fb2cdc52f5a47e4bb1afaa4b15ce39540d57f85c/src/main/java/teamexpress/velo9/post/service/PostService.java#L169)
- 작성 중인 포스트는 x분 마다 자동 저장됩니다.<br>

<details>
<summary><b> 동작 구조도 펼치기</b></summary>

<div markdown="1">

![](https://velog.velcdn.com/images/woply/post/110c7d6f-95b5-44e4-a0e8-0980a6eb8901/image.jpg)

</div>
</details>


<br>


## 4.2. 포스트 조회 관련 기능


> ### 4.2.1. (메인 화면)멀티 검색 지원 :pushpin: [코드 확인](https://github.com/team-express/velo9/blob/fb2cdc52f5a47e4bb1afaa4b15ce39540d57f85c/src/main/java/teamexpress/velo9/post/domain/PostRepositoryCustomImpl.java#L50)
- 메인 화면에서 키워드 검색 시, 포스트 내용과 태그 내용을 선택하여 검색할 수 있습니다.<br>

<details>
<summary><b> 동작 구조도 펼치기</b></summary>

<div markdown="1">

![](https://velog.velcdn.com/images/woply/post/319e767f-a40d-4bcb-a90a-494f79a7577b/image.jpg)

</div>
</details>

<br>


> ### 4.2.2. (메인 화면)정렬 조건 지원 :pushpin: [코드 확인](https://github.com/team-express/velo9/blob/fb2cdc52f5a47e4bb1afaa4b15ce39540d57f85c/src/main/java/teamexpress/velo9/post/controller/MainController.java#L36)
- 메인 화면에서 포스트 조회 시, 원하는 정렬 조건을 설정하여 포스트 목록을 조회할 수 있습니다.<br>

<details>
<summary><b> 동작 구조도 펼치기</b></summary>

<div markdown="1">

![](https://velog.velcdn.com/images/woply/post/5130ab71-9dc6-4641-a77c-2eda2c90a2ef/image.jpg)

</div>
</details>

<br>


> ### 4.2.3. (사용자 글 목록 화면) 태그, 시리즈 정보 기반 포스트 탐색 :pushpin: [태그 활용 코드 확인](https://github.com/team-express/velo9/blob/fb2cdc52f5a47e4bb1afaa4b15ce39540d57f85c/src/main/java/teamexpress/velo9/post/domain/PostRepositoryCustomImpl.java#L34) / [시리즈 활용 코드 확인](https://github.com/team-express/velo9/blob/fb2cdc52f5a47e4bb1afaa4b15ce39540d57f85c/src/main/java/teamexpress/velo9/post/domain/PostRepositoryCustomImpl.java#L102)
- 포스트에 포함된 태그 정보와 시리즈 정보를 이용하여 관심있는 주제의 포스트를 탐색할 수 있습니다.<br>

<details>
<summary><b> 동작 구조도 펼치기</b></summary>

<div markdown="1">

![](https://velog.velcdn.com/images/woply/post/1bd41fef-c0ea-4f06-a44e-f6f78a58188f/image.jpg)

</div>
</details>

  <br>


> ### 4.2.4. (포스트 상세 화면) 이전 글, 다음 글 보기 지원  :pushpin: [코드 확인](https://github.com/team-express/velo9/blob/fb2cdc52f5a47e4bb1afaa4b15ce39540d57f85c/src/main/java/teamexpress/velo9/post/domain/PostRepositoryCustomImpl.java#L116)
- (동일한 시리즈 정보를 가지고 있거나, 등록된 순서를 기반으로) 현재 보고 있는 포스트의 이전 글과 다음 글을 보여 줍니다. <br>

<details>
<summary><b> 동작 구조도 펼치기</b></summary>

<div markdown="1">

![](https://velog.velcdn.com/images/woply/post/3a585f56-f5e5-4223-a2f5-f3471ff8f1fa/image.jpg)

</div>
</details>

<br>


> ### 4.2.5. (사용자 아카이브) 좋아요, 최근 읽은 글 목록 지원 :pushpin: [좋아요 관련 코드 확인](https://github.com/team-express/velo9/blob/fb2cdc52f5a47e4bb1afaa4b15ce39540d57f85c/src/main/java/teamexpress/velo9/post/domain/PostRepositoryCustomImpl.java#L67) / [읽은 글 관련 코드 확인](https://github.com/team-express/velo9/blob/fb2cdc52f5a47e4bb1afaa4b15ce39540d57f85c/src/main/java/teamexpress/velo9/post/domain/PostRepositoryCustomImpl.java#L85)
- 사용자가 '읽은 적'이 있는 모든 포스트와 '좋아요'를 누른 모든 포스트를 별도로 보여줍니다. <br>

<details>
<summary><b> 동작 구조도 펼치기</b></summary>

<div markdown="1">

![](https://velog.velcdn.com/images/woply/post/e981cfa1-a0ee-4f83-8fc1-e95331aa91b9/image.jpg)

</div>
</details>

<br>

</div>
</details>

<br>

## 5. 핵심 트러블 슈팅
### 5.1. 컨벤션 관련 트러블 슈팅
- 문제점
  - 팀원들 간에 메서드 관련 컨벤션을 정하지 않아 메서드명이 불분명한 뜻을 가지고 있거나 같은 뜻을 가지고 있지만 표기는 다른 단어를 사용하는 경우가 생겼습니다.
- 해결
  - `find` vs `get`, `remove` vs `delete`, `add` vs `insert` 와 같이 비슷한 단어를 사용한 경우를 찾아서 저희 팀 만의 컨벤션을 정하여 통일하였습니다.
  - 메서드의 경우 `해야할 작업` + `사용목적` + `찾아올 값` 으로 작성하였고 DTO도 비슷하게 `도메인` + `사용목적` + `DTO` 로 작성하였습니다.

<br>

### 5.2. 스프링 시큐리티 트러블 슈팅

<details>
<summary>문제점과 해결 방법 펼쳐보기</summary>
<div markdown="1">

- 문제점
  - 스프링 시큐리티 관련 지식이 부족하여 설계 및 코드 작성에 실수를 범하였습니다.
  - 세션에 회원의 정보를 저장하고 프론트에서 직접 꺼내서 사용하는 방식을 생각했는데 우리는 리액트와 통신해야 하기 때문에 세션에 저장하고 꺼내는 방식은 사용할 수가 없었습니다.
  - 소셜로그인 리액트와의 연결 방식

- 1차 구현은 Ajax 통신을 통한 인증으로 구현하였다. 하지만 정확한 작동 방법을 알지 못하여 커스텀 방식을 사용하였습니다.
- 2차 구현 → FormLogin 방식으로 구현했지만 실행되는 곳에서 Json 데이터를 받아오지 못하는 문제가 생겼습니다.
- 3차 구현 → Ajax 방식을 토대로 새로운 로그인 구현 방식을 만들어서 Json데이터를 받아오고 로그인 판단 여부를 성공적으로 처리하게 되었습니다.
- 추가로 로그인하지 않은 사용자가 접근하지 못하는 인가 정책도 추가했습니다.

- 기존 방식

![](https://velog.velcdn.com/images/woply/post/b2092df0-240f-4147-817c-cd045afa3342/image.png)


- 위의 프로세스대로 구현을 했지만 Ajax 확인 여부를 위한 헤더 설정 부분의 XMLHttpRequest 설정이 먹히지가 않아서 프로세스가 제대로 실행되지가 않았습니다.

## 해결 1. 로그인 방식 → Ajax가 아닌 커스텀 로그인으로 변경 방식 :

formLogin 방식은 Json 형태의 값을 받아오기 힘들다. 그렇기 때문에 Ajax방식을 토대로 Json 데이터를 받아오면서 로그인을 구현했습니다.

`CustomLoginProcessingFilter.attemptAuthentication`

1. Post 방식으로 `/login` 에 접근 하였는지 확인했습니다.
2. objectMapper를 사용하여 Json 형식으로 넘어온 `MemberLoginDTO`를 받아옵니다.
3. `AjaxAuthenticationToken` 에 MemberLoginDTO 의 username 값과 password값을 담아서 `AuthenticationManager`로 보냅니다.

`CustomAuthenticationProvider.authenticate`

1. authentication 에서 내가 입력한 username과 password를 빼올 수 있습니다.
2. userDetailsService에서 username으로 MemberContext 라는 Member가 담긴 객체를 받아온다. MemberContext 는 User를 상속받아 Member의 username, password, Role 을 같이 담고 있는 객체입니다.
3. 내가 입력한 password와 조회한 Member의 password를 비교하여 불일치 시 `BadCredentialsException` 예외를 던집니다.
4. 그리고 `AjaxAuthenticationToken`에 MemberContext 에 담긴 member를 다시 저장시킵니다.

`CustomUserDetailsService.loadUserByUsername`

1. 사용자가 입력한 username을 DB에 조회하여 실제 존재하는 멤버인지 확인하는 절차입니다.
2. 찾아온 멤버 객체와 멤버 객체가 가지고 있는 Role을 MemberContext에 저장시킵니다.

`CustomAuthenticationSuccessHandler.onAuthenticationSuccess`

1. 사용자가 올바른 username 과 password를 입력하면 이 메서드로 오게 된다. 이 메서드로 온다는 것은 로그인 처리에 성공했다는 의미를 뜻합니다.
2. `authentication`에서 멤버를 꺼내온 후 세션에 멤버 아이디를 저장하고 Http 상태 코드 OK를 응답합니다.

`CustomAuthenticationFailureHandler.onAuthenticationFailure`

1. 사용자가 로그인에 실패(아이디 또는 비밀번호가 틀렸을 경우)하게 되면 이 메서드로 오게 됩니다.
2. Exception이 `BadCredentialsException` 일 경우 비밀번호가 틀렸다는 내용을 응답하게 됩니다.
3. if문에 있는 Exception에 걸리지 않았을 경우는 아이디 또는 비밀번호가 틀렸다는 내용을 응답하게 됩니다.

## 해결 2. 세션 저장 방식


기존에 세션에 회원 정보를 저장하는 방식을 사용했습니다. 하지만 이 방식으로는 리액트를 사용하는 프론트쪽에서 세션에서 아무 정보도 꺼내 오지 못하는 방식입니다.

SSR(서버 사이드 렌더링)방식으로만 구현을 해봐서 생긴 허점이었습니다.

JWT 토큰을 사용하는 방식으로 구현을 하려 했지만 세션에 저장한 정보는 우리가 사용하는 것으로 하고 프론트에서 사용자 정보가 필요할 때마다 백엔드에 호출하면 JSON 데이터로 보내주는 방식을 선택했습니다.

## 해결 3. 소셜로그인

### 리액트와의 연결을 생각하지 않은 OAuth2 설계

리액트와의 연결을 너무 간단하게 생각했습니다.


리액트에서 구글이나 깃헙으로 로그인하는 URL만 연결해 주면 백엔드 쪽에서 처리해주는 데이터가 프론트쪽으로 넘어갈 것이라고 생각했습니다. 하지만 이렇게 되면 로그인된 후 백엔드 서버 쪽의 URL로 넘어가게 됩니다.

리액트에서 백엔드 쪽의 소셜로그인을 호출하는 경우 Axios 나 Fetch 가 먹히지 않는다고 합니다. 그렇기에 a태그로 호출을 해줘야 하는데 a태그로는 백엔드 쪽의 데이터를 그대로 받아오지 못하기 때문에 다른 처리가 필요했습니다.

`OAuth2SuccessHandler` 가 해결책이었습니다. 로그인이 성공적으로 이루어질 경우 우리는 이미 회원가입이 된 사용자인지 아닌지의 판단이 필요했습니다. `OAuth2SuccessHandler` 에서 `MemberService` 를 호출해서 회원가입이 된 사용자인지 판단하도록 했습니다.


이미 회원가입이 된 사용자의 경우 `http://localhost:3000/success` 로 리다이렉트 되어 백엔드쪽으로 사용자 정보를 호출합니다.

회원가입이 되어있지 않은 사용자의 경우 `http://localhost:3000/firstLogin` 으로 리다이렉트 되어 회원가입이 실행되도록 하여 리액트와의 연결문제를 해결하였습니다.

## cors 설정

스프링 부트에서 cors 설정 시, `configuration.setAllowCredentials()` 와 `configuration.addAllowedOrigin()`을 동시에 사용할 수 없도록 변경 되었습니다.

→ 이 문제를 해결하려면 `**configuration.addAllowedOriginPattern()**`을 사용하면 됩니다.

같은 origin의 경우 request header 에 cookie가 추가되는데 orgin이 달라지는 경우 자동으로 추가되지 않아 생기는 문제점 해결 →  프론트에서 `withCredentials: true` 백엔드에서는 `configuration.setAllowCredentials(true);` 설정을 해주어야 합니다.

</div>
</details>

<br>

### 5.3. Spring Rest Docs + MockMVC 테스트 트러블 슈팅

<details>
<summary>문제점과 해결 방법 펼쳐보기</summary>
<div markdown="1">

- 이슈 1: PathVariable이 먹지 않았던 이슈

```java
	@Test
	void writeGet() throws Exception {

		mockMvc.perform(get("/write")
				.param("id", "1"))
			.andExpect(status().isOk())
			//생략
            ;
	}
```


get방식에서 JSON(.content( )), 쿼리스트링(.param( ))을 잘 사용하고 있었습니다.
pathVariable이 포함된 컨트롤러도 테스트 해야하여 구글링을 하여보니, [get("/write/{path}", "value")] 이런식으로 쓰는 걸 확인하여
그대로 적용하니 익셉션이 발생하였습니다.

```
java.lang.IllegalArgumentException: urlTemplate not found.
If you are using MockMvc did you use RestDocumentationRequestBuilders
to build the request?
```

해당 메세지를 그대로 긁어 구글링을 하여보니 경로파라미터를 사용할때는 MockMvcRequestBuilders 아닌 RestDocumentationRequestBuilders의 get()을
사용하여야 한다고 합니다.

```java
	@Test
	void series() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.get("/{nickname}/series", "admin"))
			.andExpect(status().isOk())
			//생략
            ;
	}
```

<br>


- 이슈 2: 문서에 굳이 넣고 싶지 않은 이들이 있을 때


```java
	@Test
	void series() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.get("/{nickname}/series", "admin"))
			.andExpect(status().isOk())
			.andDo(document("GetSeries", pathParameters(
					parameterWithName("nickname").description("")
				),
				responseFields(
					fieldWithPath("data.content").description("").optional(),
					fieldWithPath("data.size").description("").optional(),
					fieldWithPath("data.number").description("").optional(),
					fieldWithPath("data.first").description("").optional(),
					fieldWithPath("data.last").description("").optional(),
					fieldWithPath("data.numberOfElements").description("").optional(),
					fieldWithPath("data.empty").description("").optional(),
					fieldWithPath("subData").description("").optional()
				)
			));
	}
```


기본적으로 요청과 응답의 필드(또는 파라미터 등)와 문서화할 필드가 일치해야 합니다. 그렇지 않으면 아래와 같은 익셉션이 발생합니다.

```
org.springframework.restdocs.snippet.SnippetException:
The following parts of the payload were not documented:
```

하지만 어떤 이유로 추가하고 싶지 않은 경우가 있습니다.


원래는 이 문제 때문이 아니라 restController라 json으로 resp, req를 받아야 하는데, Object안에 Object가 있는 경우에 어떻게 표현해야하는지를 찾다가 .을 찍고 들어가는 방법도 찾았고,

relaxed~를 이용하는 방법을 얻어 걸린 듯 찾은 것입니다.


```java
	@Test
	void series() throws Exception {
		this.mockMvc.perform(RestDocumentationRequestBuilders.get("/{nickname}/series", "admin"))
			.andExpect(status().isOk())
			.andDo(document("GetSeries", pathParameters(
					parameterWithName("nickname").description("")
				),
				relaxedResponseFields(
					fieldWithPath("data.content").description("").optional(),
					fieldWithPath("data.size").description("").optional(),
					fieldWithPath("data.number").description("").optional(),
					fieldWithPath("data.first").description("").optional(),
					fieldWithPath("data.last").description("").optional(),
					fieldWithPath("data.numberOfElements").description("").optional(),
					fieldWithPath("data.empty").description("").optional(),
					fieldWithPath("subData").description("").optional()
				)
			));
	}
```


위와 같이 relaxed~가 붙은 메서드를 사용하면 원하는 것만 사용할 수 있습니다.

물론 relaxed가 붙지 않은 메서드는 요소가 완전히 같아야 하기 때문에,
검증적인 측면에서 장점이 있을 것이라고 생각합니다.


<br>


- 이슈 3: field 아닌 parameter일 때 발생하는 문제



requestFields(), responseFields() - fieldWithPath 에서
필드란 json {"field":"value"} 에서의 필드를 의미합니다.

쿼리스트링, pathVariable을 문서화 및 커스텀 해야할 때는 다음과 같이 하여야 합니다.


```java
	pathParameters(
        parameterWithName("nickname").description("")
        )
        requestParameters(
        parameterWithName("id").description(").optional()
        )
```

```snippet
|===
|경로파라미터|설명

{{#parameters}}

|{{#tableCellContent}}`+{{name}}+`{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}

{{/parameters}}
|===
```


{{#fields}} 아닌 {{#parameters}}로,
}`+{{path}}+` 아닌 `+{{name}}+`입니다.

이는 바로 위 메서드 이름에서 유추할 수 있는 부분입니다.

이 밖에도 restDocs를 공부하면서
Andy Wilkinson라는 분의 도움을 많이 받았습니다.

해당 관련해서 어딜가나 링크되어있는 저장소의 주인입니다.


</div>
</details>

<br>
