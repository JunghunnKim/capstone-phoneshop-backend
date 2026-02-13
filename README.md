# 📱 Phone Shop Backend (Spring Boot)

휴대폰 쇼핑몰 졸업작품의 **백엔드 서버**입니다.
프론트엔드 팀원들이 **로컬에서 쉽게 실행하고 API 연동 테스트**를 할 수 있도록 작성되었습니다.

---
노션에 카카오페이 시크릿키 환경변수 설정방법 있음!!
---
## 📂 프로젝트 구조 (중요한 것만)

```
src/main/java/com/shop/phoneshop
 ├── controller   # API 컨트롤러
 ├── service      # 비즈니스 로직
 ├── repository   # JPA Repository
 ├── model        # Entity
 └── dto          # Request / Response DTO
```

---

## 🚀 실행 방법 (프론트엔드 팀원용)

### 1️⃣ MySQL 설치 및 DB 생성

MySQL이 설치되어 있어야 합니다.

```sql
CREATE DATABASE phoneshop;
```

---

### 2️⃣ application.yml 확인

`src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/phoneshop
    username: root
    password: ${DB_PASSWORD}

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

password: (여기에 본인 mysql root 비밀번호 작성하기)

---

### 5️⃣ 서버 실행 확인

브라우저 또는 Postman에서:

```
http://localhost:8080
```

에러 없이 실행되면 성공 ✅

---

## 📮 API 테스트 (Postman 기준)

### ✅ 회원가입

```
POST /users/signup
```

```json
{
  "email": "test@test.com",
  "password": "1234",
  "name": "홍길동"
}
```

---

### ✅ 로그인

```
POST /users/login
```

```json
{
  "email": "test@test.com",
  "password": "1234"
}
```

---

### ✅ 비밀번호 찾기 (임시 비밀번호 발급)

```
POST /users/password/reset
```

```json
{
  "email": "test@test.com",
  "name": "홍길동"
}
```

---

### ✅ 회원정보 수정 (이름, 비밀번호)

```
PUT /users/update
```

```json
{
  "email": "test@test.com",
  "name": "김철수",
  "password": "newpassword"
}
```

---

### ✅ 회원탈퇴

```
DELETE /users/withdraw
```

```json
{
  "email": "test@test.com",
  "name": "김철수",
  "password": "newpassword"
}
```

---

## ⚠️ 주의사항

* MySQL이 실행 중이어야 합니다
* DB 이름은 반드시 `phoneshop`
* 포트 충돌 시 `8080` 사용 중인지 확인

---

## 🙋 문의

백엔드 담당자에게 문의 주세요 🙌
