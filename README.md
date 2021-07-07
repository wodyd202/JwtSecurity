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
```
