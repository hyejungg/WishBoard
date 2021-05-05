[Git] Git 규칙 정의
===============
## 목차
1. [개발 프로세스](#개발-프로세스)
2. [Commit message](#1-commit-message)
3. [Branch 규칙](#2-branch-규칙)
4. [Merge 규칙](#3-merge-규칙)
5. [Issue 규칙](#4-issue-규칙)
6. [참고](#참고)
---

## 개발 프로세스
> 1. issue 생성
> 2. issue 기반 branch 생성
> 3. issue와 관련된 feature 개발 완료
> 4. PR이 주요 branch로 merge
> 5. merge 후 feature branch 제거
> 6. issue close

## 1. Commit message
- **제목에서 카테고리만 영어로 작성, 설명은 한글로 작성**
- **제목 행 첫 글자는 대문자로 작성**
- 제목과 본문을 한 줄 띄워 분리하기
- 제목은 [category] - [simple message]
  |category|설명|
  |------|---|
  |Fix|잘못된 부분 수정|
  |Add|기능 추가|
  |Mod|코드 수정|
  |Rm|기능 삭제|
- 제목 끝에 `.` 금지
- 본문은 `어떻게`보다 `무엇을`, `왜`에 맞춰 작성하기
    - 왜 커밋했는지
    - 버그 수정의 경우 무슨 문제가 있었는지 설명

## 2. Branch 규칙
- 브랜치 이름은 **[category]_[iss번호] (ex. new_123)**
  |category|설명|
  |------|---|
  |new|새 기능 추가|
  |test|테스트(새 라이브러리, 배포환경, 실험)|
  |bug|버그 수정|
  |hotfix|수정|
  |clean|클린코드로 변경|
  
## 3. Merge 규칙
- 브랜치 merge 전 **아래 과정을 반드시 진행**한다.
    1. 개인이 작업한 브랜치를 원격 저장소에 push한 후 **Pull requests** 를 작성한다.
    2. 다른 팀원이 **Pull requests** 를 확인하고, 코드에 대한 리뷰를 작성한다.
    3. 팀원이 수정을 요청하면 로컬에서 수정 후 다시 push하고, (수정 완료를 댓글로 공지한다.?)
    4. 팀원이 merge에 동의하면, 해당되는 원격 브랜치에 merge한다.
  
## 4. Issue 규칙
- 한 기능 당 하나의 issue 생성



---

### 참고
- [[VCS/Git]프로젝트를 위한 협업 준비 규칙](https://nomad-programmer.tistory.com/35)
- [[Git] 좋은 커밋 메세지 작성하기위한 규칙들](https://beomseok95.tistory.com/328)
- [Git 사용 규칙 - Git commit 메세지](https://tttsss77.tistory.com/58)
