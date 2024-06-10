### Spring Security 개요

Spring Security는 스프링 프레임워크의 강력하고 확장 가능한 보안 프레임워크


#### 주요 기능

1. 인증 (Authentication):
    - 사용자가 누구인지 확인하는 과정
    - 사용자 이름과 비밀번호를 통해 이루어짐
    - 소셜 로그인, OAuth2 등 다양한 인증 방식 지원

2. 인가 (Authorization):
    - 인증된 사용자가 애플리케이션의 어떤 자원에 접근할 수 있는지 결정
    - 역할 기반 접근 제어 (Role-based Access Control, RBAC)
    - 권한 기반 접근 제어 (Authority-based Access Control)

3. 보호 (Protection):
    - CSRF(Cross-Site Request Forgery) 보호
    - 세션 관리 및 보호
    - URL 접근 제어

4. 확장성 (Extensibility):
    - 커스텀 인증 제공자, 필터, 접근 결정 매니저 등 구현 가능
    - 다양한 보안 요구 사항에 맞게 확장 및 커스터마이징 가능

### Spring Security의 인증 (Authentication)

인증은 사용자가 누구인지 확인하는 과정입니다. 일반적으로 사용자 이름과 비밀번호를 통해 이루어지며, 올바른 자격 증명을 제공하면 사용자가 시스템에 접근할 수 있습니다.

#### 인증 과정

1. 사용자 입력: 사용자가 로그인 폼에 사용자 이름과 비밀번호를 입력합니다.
2. 인증 요청: 클라이언트는 서버로 인증 요청을 보냅니다.
3. 자격 증명 검증: 서버는 사용자 저장소(예: 데이터베이스)에서 사용자 정보를 조회하여 자격 증명을 검증합니다.
4. 인증 성공: 자격 증명이 유효하면 서버는 사용자를 인증하고, 보통 세션이나 토큰을 생성하여 사용자에게 반환합니다.
5. 인증 실패: 자격 증명이 유효하지 않으면 서버는 인증 실패 응답을 반환합니다.

### Spring Security의 인가 (Authorization)

인가(권한 부여)는 인증된 사용자가 애플리케이션의 특정 자원에 접근할 수 있는 권한이 있는지를 결정하는 과정입니다.

#### 인가 과정

1. 권한 확인: 서버는 사용자가 요청한 자원에 접근할 수 있는 권한이 있는지 확인합니다.
2. 접근 허용: 사용자가 해당 자원에 접근할 권한이 있으면 요청을 처리합니다.
3. 접근 거부: 사용자가 권한이 없으면 접근을 거부하고, 적절한 오류 메시지를 반환합니다.


### SecurityConfig과 기본 사용법

Spring Security를 사용하기 위해서는 `SecurityConfig` 클래스를 통해 보안 설정을 구성해야 합니다. 최신 스프링 부트 3.x 버전에서는 `SecurityFilterChain`을 사용하여 보안 설정을 정의합니다.

#### 기본 구성 단계

1. SecurityConfig 클래스 생성:
   - 스프링 설정 클래스에 보안 설정을 정의
   - `@Configuration` 어노테이션을 사용하여 스프링 설정 클래스로 지정
   - `SecurityFilterChain` 빈을 정의하여 보안 필터 체인을 설정

2. 비밀번호 암호화 설정:
   - `PasswordEncoder` 빈을 정의하여 비밀번호를 암호화

3. 인증 관리자 설정:
   - `AuthenticationManager` 빈을 정의하여 인증을 처리

4. HTTP 보안 설정:
   - 특정 URL 패턴에 대한 접근 제어를 설정
   - CSRF 보호, 세션 관리 등 다양한 보안 설정을 구성

#### SecurityConfig 예제 (개념 설명용)

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 스프링 설정 클래스
@EnableWebSecurity // 웹 보안 활성화
@EnableMethodSecurity // 메서드 보안 활성화
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 생성
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager(); // 인증 관리자 빈 생성
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/").permitAll() // 공개 엔드포인트는 접근 허용
                .anyRequest().authenticated() // 다른 모든 요청은 인증 필요
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션 관리 설정: 무상태
            );

        return http.build();
    }
}
```

#### 주요 설정 요소

- 애노테이션
1. `@Configuration`: 스프링 설정 클래스로 지정
2. `@EnableWebSecurity`: 웹 보안을 활성화
3. `@EnableMethodSecurity`: 메서드 수준 보안 활성화

- 클래스
1. PasswordEncoder: 비밀번호를 안전하게 저장하기 위해 암호화하는 클래스입니다.
   - `BCryptPasswordEncoder`를 사용하여 비밀번호를 해싱합니다.

2. AuthenticationManager: 사용자 인증을 처리하는 클래스입니다.
   - `AuthenticationConfiguration`을 사용하여 인증 관리자를 설정합니다.

3. SecurityFilterChain: HTTP 보안 설정을 정의하는 클래스입니다.
   - CSRF 보호 비활성화: `csrf(csrf -> csrf.disable())`
      - CSRF(Cross-Site Request Forgery) 공격을 방지하는 보호 기능을 비활성화합니다. 이는 주로 RESTful API에서 사용됩니다.
   - 접근 제어 설정: `authorizeHttpRequests(auth -> auth...`
      - 특정 URL 패턴에 대한 접근을 제어합니다.
      - `requestMatchers("/public/").permitAll()`: `/public/` 경로는 누구나 접근할 수 있도록 허용합니다.
      - `anyRequest().authenticated()`: 나머지 모든 요청은 인증이 필요합니다.
   - 세션 관리 설정: `sessionManagement(session -> session...`
      - 세션 관리를 무상태로 설정합니다. 이는 주로 JWT와 같은 토큰 기반 인증에 사용됩니다.

### JWT (JSON Web Token)

#### JWT란?

JWT는 JSON 객체를 사용하여 정보를 안전하게 전송하기 위한 컴팩트하고 독립적인 방식입니다. 주로 사용자 인증 및 정보 교환에 사용됩니다.

- JSON Web Token (JWT): 두 개체 간에 JSON 객체를 사용하여 정보를 안전하게 전송하는 방식
- 주 사용 사례: 사용자 인증 및 권한 부여

#### JWT의 구조

JWT는 점(`.`)으로 구분된 세 부분으로 구성됩니다: 헤더, 페이로드, 서명

1. 헤더 (Header):
   - 토큰의 유형 (JWT) 및 사용된 서명 알고리즘 (예: HMAC SHA256) 포함
   - 예제:
     ```json
     {
       "alg": "HS256",
       "typ": "JWT"
     }
     ```

2. 페이로드 (Payload):
   - 토큰에 담을 실제 데이터 (클레임) 포함
   - 클레임은 여러 가지가 있으며, 일반적으로 사용되는 표준 클레임도 있음 (예: iss, exp, sub)
   - 예제:
     ```json
     {
       "sub": "1234567890",
       "name": "John Doe",
       "iat": 1516239022
     }
     ```

3. 서명 (Signature):
   - 헤더와 페이로드를 인코딩한 후, 비밀 키를 사용하여 생성
   - JWT의 무결성과 출처를 검증하는 데 사용됨
   - 예제: HMAC SHA256 알고리즘을 사용한 서명
     ```
     HMACSHA256(
       base64UrlEncode(header) + "." +
       base64UrlEncode(payload),
       secret)
     ```

#### JWT의 장점

- 컴팩트함: JSON 형식이므로 데이터 전송량이 적고 네트워크 성능에 유리
- 자가 포함: JWT는 자체적으로 정보를 담고 있어 서버에서 세션을 유지할 필요 없음
- 확장성: 다양한 클레임을 추가할 수 있어 유연하게 사용 가능

#### JWT 사용 과정

1. 클라이언트 인증 요청:
   - 사용자가 사용자 이름과 비밀번호를 사용하여 서버에 로그인 요청
   - 서버는 사용자의 자격 증명을 검증

2. 서버에서 JWT 생성:
   - 서버는 사용자가 유효하면 JWT를 생성
   - JWT는 사용자 정보와 만료 시간을 포함하여 생성됨

3. 클라이언트에 JWT 전달:
   - 서버는 생성된 JWT를 클라이언트에 전달
   - 클라이언트는 JWT를 로컬 스토리지 또는 쿠키에 저장

4. 보호된 자원 요청:
   - 클라이언트가 보호된 자원에 접근할 때마다 JWT를 요청 헤더에 포함하여 서버에 전송
   - 서버는 JWT의 서명을 검증하고 유효성을 확인

5. 서버에서 요청 처리:
   - JWT가 유효하면 서버는 사용자를 인증하고 요청을 처리
   - JWT가 유효하지 않거나 만료되었으면 서버는 접근을 거부

#### JWT 사용 예시

1. 클라이언트가 로그인 요청:
   - 사용자 이름과 비밀번호를 서버에 제출
   - 예제 요청:
     ```http
     POST /login HTTP/1.1
     Host: example.com
     Content-Type: application/json

     {
       "username": "testuser",
       "password": "password123"
     }
     ```

2. 서버가 JWT 생성 및 반환:
   - 자격 증명 검증 후 JWT 생성
   - 클라이언트에 JWT 전달
   - 예제 응답:
     ```http
     HTTP/1.1 200 OK
     Content-Type: application/json

     {
       "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     }
     ```

3. 클라이언트가 보호된 자원 요청 시 JWT 포함:
   - JWT를 요청 헤더에 포함하여 보호된 자원 요청
   - 예제 요청:
     ```http
     GET /protected HTTP/1.1
     Host: example.com
     Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
     ```

4. 서버가 JWT 검증 및 요청 처리:
   - JWT의 서명 및 유효성 검증
   - 요청이 유효하면 보호된 자원 반환
   - 예제 응답:
     ```http
     HTTP/1.1 200 OK
     Content-Type: application/json

     {
       "data": "protected data"
     }
     ```

#### 요약

- JWT (JSON Web Token): JSON 객체를 사용하여 정보를 안전하게 전송하는 방식
- 구성: 헤더, 페이로드, 서명
- 장점: 컴팩트함, 자가 포함, 확장성
- 사용 과정: 클라이언트 인증 요청, 서버에서 JWT 생성, 클라이언트에 JWT 전달, 보호된 자원 요청 시 JWT 포함, 서버에서 JWT 검증 및 요청 처리


## Spring Boot 3와 Swagger를 사용한 REST API 문서화

### 1. Swagger 소개
- Swagger: RESTful API를 설계, 빌드, 문서화, 소비하는 오픈 소스 소프트웨어 프레임워크
- 주요 기능: API 문서 자동 생성, 사용자 인터페이스 제공, API 테스트 및 이해 용이
- [Swagger 공식 문서](https://swagger.io/docs/)

### 2. SpringDoc OpenAPI 소개
- SpringDoc OpenAPI: Spring Boot 애플리케이션을 위한 Swagger(OpenAPI) 문서화 라이브러리
- 특징: Spring 애플리케이션의 REST API 문서를 자동으로 생성하여 Swagger UI를 통해 시각화
- [Springdoc OpenAPI 공식 문서](https://springdoc.org/)


### 3. 의존성 추가
- `build.gradle` 파일에 Swagger(OpenAPI) 관련 의존성 추가

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0' // SpringDoc OpenAPI
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

### 4. Spring Security에서 보안 설정 추가
- JWT 토큰을 사용한 인증 설정 추가
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    ... // 다른 설정

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                ... // 다른 설정

                // 허용되는 url 패턴 추가
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                        .anyRequest().authenticated()
                )

                ... // 다른 설정

        return http.build();
    }
```

### 5. swagger-ui 확인
- 애플리케이션 실행: Spring Boot 애플리케이션을 실행
- Swagger UI 접속: 브라우저에서 `http://localhost:8080/swagger-ui/index.html` 접속
- 기능 확인: Swagger UI를 통해 API 엔드포인트 확인 및 테스트

### 6. Postman에서 문서 확인
- Postman 설치 및 실행: Postman을 다운로드하고 실행
- Swagger 문서 가져오기:
   - 상단 메뉴에서 "Import" 클릭
   - URL, 파일 업로드 또는 Raw 텍스트로 OpenAPI 스펙 가져오기
- 문서 확인:
   - 가져온 API를 선택하고 "View Documentation" 클릭