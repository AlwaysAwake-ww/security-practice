

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
