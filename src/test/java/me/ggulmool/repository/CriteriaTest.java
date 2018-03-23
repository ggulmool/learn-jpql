package me.ggulmool.repository;

import me.ggulmool.MainApplication;
import me.ggulmool.entity.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class CriteriaTest {
    
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EntityManager em;

    @Test
    public void basic() throws Exception {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // CriteriaQuery생성, 반환타입 지정
        CriteriaQuery<Member> cq = cb.createQuery(Member.class);

        // from 절
        Root<Member> m = cq.from(Member.class);

        // 검색 조건 정의
        Predicate usernameEqual = cb.equal(m.get("username"), "김남열");

        // 정렬 조건 정의
        Order ageDesc = cb.desc(m.get("age"));

        // 쿼리 생성
        cq.select(m)
                .where(usernameEqual)
                .orderBy(ageDesc);

        em.createQuery(cq).getResultList().stream()
                .forEach(member -> logger.info("{}", member));
    }
}
