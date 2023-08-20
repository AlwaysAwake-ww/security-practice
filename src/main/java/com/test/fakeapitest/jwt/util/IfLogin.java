package com.test.fakeapitest.jwt.util;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




// 어노테이션으로 쓰는 클래스

// AOP (Aspect Oriented Programming) : 관점지향 프로그래밍
// 을 위해 사용되는 어노테이션

// @Target : Java 컴파일러가 이 어노테이션이 어디 적용될 지 결정하기 위해 사용
// ElementType.PARAMETER : 매개변수 선언 시 사용
// @Retention : 어노테이션이 실제로 적용되고 유지되는 범위
// RetentionPolicy.RUNTIME : 컴파일 이후에도 JVM에 의해 계속 참조가 가능 >> 주로 리플렉션이나 로깅에 사용
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface IfLogin {


}
