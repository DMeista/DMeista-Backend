# DMeista 
![GitHub top language](https://img.shields.io/github/languages/top/kangsinhee/tut_kotlin_springboot?color=red&style=flat-square)

>  2020-10-22 ~ 2021-04-15 (현재 유지보수 중)

![LOGO](https://www.notion.so/image/https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fb37262be-22b2-4669-bcb7-064fd0c46a13%2Flogo.png?table=block&id=13d65f0c-0498-4628-bd8b-b60a479b1d9e&width=250&userId=dfbe6b8d-7e07-4ba1-a7ee-268c7dc2fadb&cache=v2)

`DMeista`는 게시물 포스팅과 이미지 자동태그, 게시물 검색,온라인 친구 관리 등의 SNS 서비스입니다

### API Docs

[DMeista API Docs(Swagger)](http://3.36.218.14:8080/swagger-ui.html#/)

### Tech Stack
*자세한 내용은 [build.gradle](./build.gradle.kts)을 확인해 주세요*

* **Common**
    * `Kotlin` 1.4.21
    * `Gradle` 6.8.2

* **Framework**
    * `Spring boot`, `Spring Security`, `Spring Data Jpa` 2.4.1

* **Library**
    * `Kotlin Coroutine` 1.4.2
    * `Junit` 5.7.0
    * `Swagger` 2.9.2
    * `Retrofit` 2.7.0
    * `jjwt` 0.9.1
    
* **Database**
    * `MySQL`
    * `Redis`
    
* **APIs**
    * `Kakao Vision API`
    * `Slack API`
    
* **Deployment**
    * `Github Actions`
    * `AWS EC2`, `RDS`
    * `Docker Swarm`

### Project Tree

```
📦 DMeista
├─ .github
│  └─ workflows
│     └─ black.yml
├─ gradle
│  └─ wrapper
│     ├─ gradle-wrapper.jar
│     └─ gradle-wrapper.propertice
├─ src
│  ├─ main
│  │  ├─ kotlin
│  │  │  └─ sinhee
│  │  │     └─ kang
│  │  │        └─ tutorial
│  │  │           ├─ domain
│  │  │           │  ├─ auth
│  │  │           │  │  ├─ controller
│  │  │           │  │  │  ├─ AuthController.kt
│  │  │           │  │  │  └─ UserController.kt
│  │  │           │  │  ├─ domain
│  │  │           │  │  │  ├─ emailLimiter
│  │  │           │  │  │  │  ├─ repository
│  │  │           │  │  │  │  │  └─ EmailLimiterRepository.kt
│  │  │           │  │  │  │  └─ EmailLimiter.kt
│  │  │           │  │  │  ├─ refreshToken
│  │  │           │  │  │  │  ├─ repository
│  │  │           │  │  │  │  │  └─ RefreshTokenRepository.kt
│  │  │           │  │  │  │  └─ RefreshToken.kt
│  │  │           │  │  │  └─ verification
│  │  │           │  │  │     ├─ enums
│  │  │           │  │  │     │  └─ EmailVerificationStatus.kt
│  │  │           │  │  │     ├─ repository
│  │  │           │  │  │     │  └─ EmailVerificationRepository.kt
│  │  │           │  │  │     └─ EmailVerification.kt
│  │  │           │  │  ├─ dto
│  │  │           │  │  │  ├─ request
│  │  │           │  │  │  │  ├─ ChangePasswordRequest.kt
│  │  │           │  │  │  │  ├─ ChangePasswordRequest.kt
│  │  │           │  │  │  │  ├─ SignInRequest.kt
│  │  │           │  │  │  │  ├─ SignUpRequest.kt
│  │  │           │  │  │  │  └─ VerifyCodeRequest.kt
│  │  │           │  │  │  └─ response
│  │  │           │  │  │     └─ TokenResponse.kt
│  │  │           │  │  └─ service
│  │  │           │  │     ├─ auth
│  │  │           │  │     │  ├─ AuthService.kt
│  │  │           │  │     │  └─ AuthServiceImpl.kt
│  │  │           │  │     ├─ email
│  │  │           │  │     │  ├─ EmailService.kt
│  │  │           │  │     │  └─ EmailServiceImpl.kt
│  │  │           │  │     └─ user
│  │  │           │  │        ├─ UserService.kt
│  │  │           │  │        └─ UserServiceImpl.kt
│  │  │           │  ├─ file
│  │  │           │  │  ├─ controller
│  │  │           │  │  │  └─ ImageController.kt
│  │  │           │  │  ├─ domain
│  │  │           │  │  │  ├─ repository
│  │  │           │  │  │  │  └─ ImageFileRepository.kt
│  │  │           │  │  │  └─ ImageFile.kt
│  │  │           │  │  └─ service
│  │  │           │  │     ├─ ImageService.kt
│  │  │           │  │     └─ ImageServiceImpl.kt
│  │  │           │  ├─ post
│  │  │           │  │  ├─ controller
│  │  │           │  │  │  ├─ CommentController.kt
│  │  │           │  │  │  └─ PostController.kt
│  │  │           │  │  ├─ domain
│  │  │           │  │  │  ├─ comment
│  │  │           │  │  │  │  ├─ repository
│  │  │           │  │  │  │  │  └─ CommentRepository.kt
│  │  │           │  │  │  │  └─ Comment.kt
│  │  │           │  │  │  ├─ emoji
│  │  │           │  │  │  │  ├─ enums
│  │  │           │  │  │  │  │  └─ EmojiStatus.kt
│  │  │           │  │  │  │  ├─ repository
│  │  │           │  │  │  │  │  └─ EmojiRepository.kt
│  │  │           │  │  │  │  └─ Emoji.kt
│  │  │           │  │  │  ├─ post
│  │  │           │  │  │  │  ├─ repository
│  │  │           │  │  │  │  │  └─ PostRepository.kt
│  │  │           │  │  │  │  └─ Post.kt
│  │  │           │  │  │  ├─ subcomment
│  │  │           │  │  │  │  ├─ repository
│  │  │           │  │  │  │  │  └─ SubCommentRepository.kt
│  │  │           │  │  │  │  └─ SubComment.kt
│  │  │           │  │  │  └─ view
│  │  │           │  │  │     ├─ repository
│  │  │           │  │  │     │  └─ ViewRepository.kt
│  │  │           │  │  │     └─ View.kt
│  │  │           │  │  ├─ dto
│  │  │           │  │  │  ├─ request
│  │  │           │  │  │  │  └─ CommentRequest.kt
│  │  │           │  │  │  └─ response
│  │  │           │  │  │     ├─ EmojiResponse.kt
│  │  │           │  │  │     ├─ PostCommentsResponse.kt
│  │  │           │  │  │     ├─ PostContentResponse.kt
│  │  │           │  │  │     ├─ PostEmojiListResponse.kt
│  │  │           │  │  │     ├─ PostListResponse.kt
│  │  │           │  │  │     ├─ PostResponse.kt
│  │  │           │  │  │     └─ PostSubCommentsResponse.kt
│  │  │           │  │  └─ service
│  │  │           │  │     ├─ comment
│  │  │           │  │     │  ├─ CommentService.kt
│  │  │           │  │     │  └─ CommentServiceImpl.kt
│  │  │           │  │     ├─ emoji
│  │  │           │  │     │  ├─ EmojiService.kt
│  │  │           │  │     │  └─ EmoJiServiceImpl.kt
│  │  │           │  │     └─ post
│  │  │           │  │        ├─ PostService.kt
│  │  │           │  │        └─ PostServiceImpl.kt
│  │  │           │  └─ user
│  │  │           │     ├─ controller
│  │  │           │     │  ├─ FriendController.kt
│  │  │           │     │  └─ UserInfoController.kt
│  │  │           │     ├─ domain
│  │  │           │     │  ├─ friend
│  │  │           │     │  │  ├─ enums
│  │  │           │     │  │  │  └─ FriendStatus.kt
│  │  │           │     │  │  ├─ repository
│  │  │           │     │  │  │  └─ FriendRepository.kt
│  │  │           │     │  │  └─ Friend.kt
│  │  │           │     │  └─ user
│  │  │           │     │     ├─ enums
│  │  │           │     │     │  └─ AccountRole.kt
│  │  │           │     │     ├─ repository
│  │  │           │     │     │  └─ UserRepository.kt
│  │  │           │     │     └─ User.kt
│  │  │           │     ├─ dto
│  │  │           │     │  └─ response
│  │  │           │     │     ├─ UserInfoResponse.kt
│  │  │           │     │     ├─ UserListResponse.kt
│  │  │           │     │     └─ UserResponse.kt
│  │  │           │     └─ service
│  │  │           │        ├─ friend
│  │  │           │        │  ├─ FriendService.kt
│  │  │           │        │  └─ FriendServiceImpl.kt
│  │  │           │        └─ user
│  │  │           │           ├─ UserInfoService.kt
│  │  │           │           └─ UserInfoServiceImpl.kt
│  │  │           ├─ global
│  │  │           │  ├─ businessException
│  │  │           │  │  ├─ dto
│  │  │           │  │  │  └─ ErrorResponse.kt
│  │  │           │  │  ├─ exception
│  │  │           │  │  │  ├─ auth
│  │  │           │  │  │  │  └─ 
│  │  │           │  │  │  ├─ common
│  │  │           │  │  │  │  └─ 
│  │  │           │  │  │  └─ post
│  │  │           │  │  │     └─ 
│  │  │           │  │  ├─ BusinessException.kt
│  │  │           │  │  ├─ ErrorCode.kt
│  │  │           │  │  ├─ ErrorHandler.kt
│  │  │           │  │  └─ GlobalExceptionHandler.kt
│  │  │           │  ├─ config
│  │  │           │  │  ├─ AsyncConfig.kt
│  │  │           │  │  ├─ CachingConfig.kt
│  │  │           │  │  └─ SwaggerConfig.kt
│  │  │           │  └─ security
│  │  │           │     ├─ authentication
│  │  │           │     │  ├─ AuthDetails.kt
│  │  │           │     │  ├─ AuthDetailsService.kt
│  │  │           │     │  └─ AuthenticationFacade.kt
│  │  │           │     ├─ errorHandle
│  │  │           │     │  ├─ ExceptionHandlerConfig.kt
│  │  │           │     │  └─ ExceptionHandlerFilter.kt
│  │  │           │     ├─ jwt
│  │  │           │     │  ├─ enums
│  │  │           │     │  │  └─ TokenType.kt
│  │  │           │     │  ├─ JwtConfigurer.kt
│  │  │           │     │  ├─ JwtTokenFilter.kt
│  │  │           │     │  └─ JwtTokenProvider.kt
│  │  │           │     ├─ requestLog
│  │  │           │     │  ├─ RequestLogConfigurer.kt
│  │  │           │     │  ├─ RequestLogFilter.kt
│  │  │           │     │  └─ WrappedRequest.kt
│  │  │           │     └─ SecurityConfig.kt
│  │  │           ├─ infra
│  │  │           │  ├─ api
│  │  │           │  │  ├─ kakao
│  │  │           │  │  │  ├─ dto
│  │  │           │  │  │  │  ├─ ResultResponse.kt
│  │  │           │  │  │  │  └─ VisionResponse.kt
│  │  │           │  │  │  ├─ service
│  │  │           │  │  │  │  ├─ VisionLabelService.kt
│  │  │           │  │  │  │  └─ VisionLabelServiceImpl.kt
│  │  │           │  │  │  └─ KakaoApi.kt
│  │  │           │  │  └─ slack
│  │  │           │  │     ├─ dto
│  │  │           │  │     │  ├─ Attachment.kt
│  │  │           │  │     │  ├─ Field.kt
│  │  │           │  │     │  └─ SlackMessageRequest.kt
│  │  │           │  │     ├─ service
│  │  │           │  │     │  ├─ SlackMessageService.kt
│  │  │           │  │     │  └─ SlackMessageServiceImpl.kt
│  │  │           │  │     └─ SlackApi.kt
│  │  │           │  └─ redis
│  │  │           │     ├─ EmbeddedRedisConfig.kt
│  │  │           │     └─ RedisRepositoryConfig.kt
│  │  │           └─ TutorialApplication.kt
│  │  └─ resources
│  │     └─ application.properties
│  └─ test
│     ├─ kotlin
│     │  └─ sinhee
│     │     └─ kang
│     │        └─ tutorial
│     │           ├─ auth
│     │           │  ├─ AuthTestApis.kt
│     │           │  └─ UserTestApis.kt
│     │           ├─ post
│     │           │  ├─ CommentTestApis.kt
│     │           │  ├─ EmojiTestApis.kt
│     │           │  └─ PostTestApis.kt
│     │           ├─ user
│     │           │  └─ FriendTestApis.kt
│     │           ├─ DiVariables.kt
│     │           └─ TestApis.kt
│     └─ resources
│        └─ application-test.properties
├─ .gitignor
├─ build.gradle.kts
├─ db.png
├─ Dockerfile
├─ gradlew
├─ gradlew.bat
├─ README.md
└─ settings.gradle.kts
```
©generated by [Project Tree Generator](https://woochanleee.github.io/project-tree-generator)
