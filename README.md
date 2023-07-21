

https://fakestoreapi.com
위 URL을 보고 같은 기능을 하는 Web Api 연습



**Product**

모든 상품
- Get/products

id가 1인 하나의 상품
- Get/products/[상품 id]

5건의 상품
- Get/products?limit=5

상품 정렬 (내림차순)
- Get/products?sort=desc

모든 카테고리
- Get/products/categories

특정 카테고리
- Get/products/category/[카테고리명]


상품 추가
- Post/products

상품 수정
- Put/products/[상품 id]
- Patch/products/[상품 id]

상품 삭제
- Delete/products/[상품 id]


**Cart**

모든 장바구니 정보
- Get/carts

장바구니 내 특정 상품 정보
- Get/carts/[장바구니 id]

장바구니 정보 n개 출력
- Get/carts?limit=[n]

장바구니 정렬 [내림차순]
- Get/carts?sort=desc

특정 날짜 사이의 장바구니 정보
- Get/carts?startdate=[시작 날짜]&enddate=[종료 날짜]


특정 유저의 장바구니 정보
- Get/carts/user/[사용자 id]

장바구니 상품 추가
- Post/carts/[상품 id]

장바구니 상품 수정
- Put/carts/[상품 id]
- Patch/carts/[상품 id]

장바구니 상품 삭제
- Delete/carts/[상품 id]


**User**

모든 사용자
- Get/users

특정 사용자
- Get/users/[사용자 id]

사용자 n개
- Get/users?limit=[n]

사용자 정렬 [내림차순]
- Get/users?sort=desc

사용자 추가
- Post/users

사용자 수정
- Put/users/[사용자 id]
- Pathch/users/[사용자 id]

사용자 삭제
- Delete/users/[사용자 id]


**Login**

사용자 로그인
- Post/auth/login


==============================================================


**Spring Security**


1. 사용자는 HTTP Request로 id(username)과 password를 가진 상태로 접근
2. Authentication Filter는 사용자로부터 받은 id(username)과 password를 이용하여 UsernamePasswordAuthenticationToken을 생성
3. 생성된 Token을 AuthenticationManager로 전달, AuthenticationManager는 전달된 Token에 저장된 Username을 4, 5, 6번 과정을 거쳐 DB에 존재하는지 확인
4. DB에 User 존재 여부를 확인 가능한 AuthenticationManager는 username에 해당하는 user가 있다면 UsernamePasswordAuthenticationToken에서 password를 DB에 저장된 형태와 같은 해쉬함수를 활용하여 암호화 시킨 이후 DB의 user객체의 password와 비교
5. 모든 비교 성공 시 AuthenticationManager는 Authentication이라는 객체 생성, SecurityContextHolder 내부의 SecurityContext에 저장하여 세션값 유지
