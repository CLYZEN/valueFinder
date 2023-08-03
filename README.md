<img src="./images/logo.png" alt="Logo of the project" align="right">

# 프로젝트 명
> 프로젝트 한줄소개

소개...



## 팀원
<table>
  <tbody>
    <tr>
      <td align="center"><a href="https://github.com/CLYZEN"><img src="https://avatars.githubusercontent.com/u/106406602?v=4" width="100px;" alt=""/><br /><sub><b>팀장 : 박승철</b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/cheonyeonsu"><img src="https://avatars.githubusercontent.com/u/130732132?v=4" width="100px;" alt=""/><br /><sub><b>팀원 : 천연수</b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/endendrh222"><img src="https://avatars.githubusercontent.com/u/130732107?v=4" width="100px;" alt=""/><br /><sub><b>팀원 : 고재혁</b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/hanzoeun"><img src="https://avatars.githubusercontent.com/u/130732055?v=4" width="100px;" alt=""/><br /><sub><b>팀원 : 장정호</b></sub></a><br /></td>
      <td align="center"><a href="https://github.com/jyong0000"><img src="https://avatars.githubusercontent.com/u/130732144?v=4" width="100px;" alt=""/><br /><sub><b>팀원 : 허재용</b></sub></a><br /></td>
    </tr>
  </tbody>
</table>
<hr>
### [간트 차트](https://docs.google.com/spreadsheets/d/14Vpw906-2PBuaMtk_2xPcMcmKJ7Zt-6X10sSfAEXYeA/edit#gid=1115838130)
### [ERD](https://www.erdcloud.com/d/AJECkdDa5fRg4ioaw)
<hr>

## 📚 목차

* [작업 규칙](#작업-규칙)

<hr>
## 작업 규칙

### 커밋, 브랜치 네이밍 룰

#### 커밋
:heavy_plus_sign:add : 새로운 기능에 대한 커밋
:wrench:fix : 잘못된 부분 수정
:bomb:build : 빌드 관련 파일 수정에 대한 커밋(application.properties, pom.xml ...)
:pencil:chore : 그 외 자잘한 수정에 대한 커밋(기타 변경)
:bookmark:docs : 문서 수정에 대한 커밋
:boom:rm : 기능 삭제
:ghost:refactor : 코드 리팩토링에 대한 커밋
:cyclone:ing : 개발 중 커밋 (커밋 기준 ~~ 완료 / ~~ 미완료)
:tada:complete : 기능 구현 완료에 대한 커밋

ex)
:heavy_plus_sign:add : 로그인 기능 add
:cyclone:ing : 로그인 기능 완료 / redirect url 미완료
<hr>
#### 브랜치

><strong>절대 main 브랜치에 push 하지 않습니다!! develop 브랜치에 push 합니다!!</strong>

##### 브랜치 네이밍 규칙
feature/#{리퀘스트 번호}-{기능분류}-{기능명}
```
ex)
feature/#1-member-login
```
##### 풀 리퀘스트 네이밍 규칙
[#번호] 내용
```
ex)
[#1] 로그인 기능 구현
```

##### 로컬 저장소(내 컴퓨터) 에서 브랜치 생성하는 법
```git
프로젝트 루트 폴더에서
git branch {브랜치명} // 브랜치 생성
git checkout {브랜치명} // 브랜치 이동
```
##### 개발한 브랜치를 push 하고 싶어요
```git
git branch // 현재 작업중인 브랜치 확인

  develope
  main
* feature/#1-member-login   // *이 붙어있는 곳이 현재 작업중인 브랜치


git add .                                 // 트래킹 중이지 않은 파일 추가
git commit -m ":cyclone:ing : 로그인 기능 완료 / redirect url 미완료"       // 커밋 메시지 작성
git push origin feature/#1-member-login   // 작업이 끝나지 않았다면
git push origin feature/develop           // 작업이 끝났다면
```
##### push 한 후 쓰지 않는 로컬 브랜치를 지우고 싶어요
```git
git branch -d {브랜치명}
```
##### 깃허브에 있는 브랜치를 내려받고 싶어요
```git
git branch // 현재 작업중인 브랜치 확인

  develope
  main
* feature/#1-member-login   // *이 붙어있는 곳이 현재 작업중인 브랜치

git pull origin {내려받을 브랜치명}
```
<hr>



### 코드 네이밍 룰

####모든 자바 메소드명, 변수명은 카멜 케이스를 따릅니다. 
####또한 누구나 알기 쉬운 단어를 사용합니다.
메소드명은 동사로 네이밍합니다.

:+1:
```java
private String personName; 

public void getUserId() {

}
```

:-1:
```java
private String PersonName;
private String personname; 

public void userid() {

}
```

#### 클래스 명은 파스칼 케이스를 따릅니다.

:+1:
```text
SampleCode.java
SampleCodeDto.java
```

:-1:
```text
samplecode.java
sampleCodeDto.java
```

#### HTML 파일 명, 패키지명 은 모두 소문자를 사용합니다.

:+1:
```text
samplecode.html
```

:-1:
```text
sample_code.html
sampleCode.html
```
#### 패키지명은 모두 소문자를 사용합니다.
#### ENUM이나 상수는 대문자로 네이밍합니다.




