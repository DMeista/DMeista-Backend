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
### DataBase UML
![DB UML](./db.png)