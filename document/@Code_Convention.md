[개발]Code Convetion 정의
==========

# 1. 변수명 정의
- 전부 `소문자`로 정의
- 띄어쓰기가 있을 경우 `_` 이용 !!!
- 단, **final 변수는 모두 대문자**로 지정하되 `_`를 사용하여 단어들을 구분
    - ex. RESULT_OK
- **boolean 변수는 is 접두사** 사용
    - ex. isValid
- DB 값 접근 변수는 DB 속성명과 동일하게 지정
- JSONObject로 객체 받아올 경우 관련 DB 테이블 명과 동일하게 지정)
  ![JSONObject 컨벤션 정의](https://user-images.githubusercontent.com/68772751/116817388-111b0580-aba1-11eb-8605-e6bd1e1c5efa.png)

  
# 2. 함수명 정의
- `동사 + 명사`의 순서로 작성해서 **무슨 기능을 하는 함수인지 명확히 알아볼 수 있도록** 짔는다.
- 대소문자 혼용할 수 있지만 **반드시 소문자**로 시작
- **단어의 구분은 단어의 첫글자를 대문자**로 해서 구분
   |함수이름|설명|
   |-------|---|
   |Check|값 검증|
   |Get|값 얻어낼 때|
   |Set|값 설정|
   |Is|Boolean 타입의 함수|
- 속성에 직접 접근하는 메소드
    - getA~, setA~
    - A : DB 속성명과 통일
 
# 3. 클래스명 정의
- 대문자로 시작
- 기능을 알 수 있도록 정의


# 4. 주석 작성 규칙
    ```java
    1. 여러줄 주석
    /*
    @command
    */
    2. 한 줄 주석
    // @command

    // @command 리스트는 아래 표를 참고
    ```
   |@command 이름|설명|
   |-------|---|
   |@subject|주제|
   |@goal|목적|
   |@author|작성자|
   |@brieft|설명|
   |@param|변수, 함수, 인자 설명|
   |@return|반환 값 설명|
   |@see|참고사항 설명|
   |@TODO|반드시 해결해야 하는 중요 문제|
   |@todo|중요하지 않은 문제|
   |@FIXME|오작동 코드|
   |@HACK|아름답지 않은 해결책|
   |@deprecated|삭제 예정|
   
   

***
### 참고
- [github_reyeon1209님의 오픈소스 프로젝트 규칙-branch, merge, 주석 작성](https://github.com/reyeon1209/Open_Source_Project/labels/rules)
- [개발자 명명규칙가이드](https://blog.naver.com/smartv11/222264622430)
- [github_mash-up-mapC백엔드 repository의 개발 규칙 정하기 issue](https://github.com/Mash-Up-MapC/MapC-backend/issues/17)
