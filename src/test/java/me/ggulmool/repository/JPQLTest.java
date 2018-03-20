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
import javax.persistence.TypedQuery;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class)
public class JPQLTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EntityManager em;

    @Test
    public void basic() throws Exception {
        TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
        List<Member> resultList = query.getResultList();
        for (Member member : resultList) {
            logger.info("{}", member);
        }
    }
}
