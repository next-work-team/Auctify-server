package org.example.auctify.config.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * QueryDSL 설정 클래스
 * QueryDSL을 사용하여 데이터베이스에 타입 세이프한 쿼리를 작성할 수 있도록 도와줍니다.
 */
@Configuration
public class QueryDslConfig {

    /**
     * EntityManager는 엔티티를 관리하고 JPA 작업을 처리합니다.
     * PersistenceContext를 사용해 스프링 컨테이너가 관리하는 EntityManager를 주입받습니다.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JPAQueryFactory를 스프링 빈으로 등록하여,
     * 애플리케이션 전반에서 QueryDSL의 타입 세이프 쿼리를 간편하게 생성할 수 있도록 지원합니다.
     *
     * @return JPAQueryFactory 인스턴스
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}