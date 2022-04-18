# :pushpin: velo9
### `velo9`는 단순하고 직관적인 사용이 가능한 `웹 기반 블로그 서비스`입니다.  
>[데모 사이트 바로가기](www.naver.com)

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

![](https://velog.velcdn.com/images/woply/post/49b5e805-d230-4716-ae34-f7cb25185397/image.png)

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

> ### 4.1.6. MarkDown 미리보기 :pushpin: [코드 확인](www.naver.com)
  - 글 작성 시, MarkDown 문법이 적용된 포스트 결과물 미리보기를 지원합니다.<br>

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

</br>

## 5. 핵심 트러블 슈팅
### 5.1. title
- content

<br>

## 6. 그 외 트러블 슈팅
<details>
<summary>title</summary>
<div markdown="1">

- content

</div>
</details>

<details>
<summary>title</summary>
<div markdown="1">

- content

</div>
</details>

<details>
<summary>title</summary>
<div markdown="1">

- content

</div>
</details>

<details>
<summary>title</summary>
<div markdown="1">

- content

</div>
</details>

<details>
<summary>title</summary>
<div markdown="1">

- content

</div>
</details>

<br>