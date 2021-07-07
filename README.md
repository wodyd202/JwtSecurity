# JwtSecurity

## Tech
- access token
- refresh token
- default token store is inmemory token store

###
## Installation
###

For production test...

```sh
https://github.com/wodyd202/JwtSecurity.git
mvn test
```

###
## How to use
###

1. add this dependency into pom.xml

```sh
<project>
	<dependencies>
		...
		<dependency>
			<groupId>com.ljy.jwt</groupId>
			<artifactId>SpringBootJWT</artifactId>
			<version>0.0.1</version>
		</dependency>
	</dependencies>

	<distributionManagement>
		<repository>
			<id>release</id>
			<url>https://github.com/wodyd202/JwtSecurity/tree/master/release/com/kakao/SpringBootJWT</url>
		</repository>
	</distributionManagement>
</project>
```

2. create security configure class(extends com.ljy.jwt.config.JwtSecurityConfigurerAdapter class) after add @EnableWebSecurity Annotation

3. add this propertie into application.properties

```sh
spring.jwt.header=
spring.jwt.secretKey=
spring.jwt.accessTokenExpireAt=
spring.jwt.refreshTokenExpireAt=

spring.jwt.refreshToken.empty.errorMsg=
spring.jwt.refreshToken.notExist.errorMsg=
spring.jwt.refreshToken.invalid.errorMsg=

spring.jwt.accessToken.empty.errorMsg=
spring.jwt.accessToken.invalid.errorMsg=
```

## Endpoint
![Build Status](https://img.shields.io/static/v1?label=&message=accessToken%20Boot&color=green)
application/x-www-form-urlencoded
###
[POST]/oauth/token

- param
String identifier(require)
String password(require)

![Build Status](https://img.shields.io/static/v1?label=&message=refreshToken%20Boot&color=green)
application/x-www-form-urlencoded
### 
[POST]/oauth/refresh-token

- param
String identifier(require)
String refreshToken(require)
