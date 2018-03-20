package me.ggulmool.repository;

import me.ggulmool.MainApplication;
import me.ggulmool.entity.Address;
import me.ggulmool.entity.Member;
import me.ggulmool.entity.Product;
import me.ggulmool.entity.Team;
import me.ggulmool.entity.dto.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
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

    @Test
    public void useQuery() throws Exception {
        Query query = em.createQuery("select m.username, m.age from Member m");
        List resultList = query.getResultList();

        for (Object o : resultList) {
            Object[] result = (Object[]) o;
            logger.info("{}", result[0]);
            logger.info("{}", result[1]);
        }
    }

    @Test
    public void usePrameterBind() throws Exception {
        TypedQuery<Member> query = em.createQuery("select m from Member m where m.username = :username",
                Member.class).setParameter("username", "김남열");
        List<Member> resultList = query.getResultList();
        for (Member member : resultList) {
            logger.info("{}", member);
        }
    }

    @Test
    public void useLocationBind() throws Exception {
        List<Member> members = em.createQuery("select m from Member m where m.username = ?1",
                Member.class).setParameter(1, "한예슬")
                .getResultList();
        for (Member member : members) {
            logger.info("{}", member);
        }
    }

    @Test
    public void projection() throws Exception {
        String query = "select o.address from Order o";
        List<Address> addresses = em.createQuery(query, Address.class)
                .getResultList();

        for (Address address : addresses) {
            logger.info("{}", address);
        }
    }

    @Test
    public void projectionScala() throws Exception {
        String query = "select m.username from Member m";
        List<String> usernames = em.createQuery(query, String.class).getResultList();
        for (String username : usernames) {
            logger.info("{}", username);
        }
    }

    @Test
    public void projectionScala2() throws Exception {
        String query = "select avg(o.orderAmount) from Order o";
        Double orderAmountAvg = em.createQuery(query, Double.class).getSingleResult();
        logger.info("{}", orderAmountAvg);
    }

    @Test
    public void projectionScalaMultiValue() throws Exception {
        String query = "select m.username, m.age from Member m";
        List<Object[]> resultList = em.createQuery(query).getResultList();

        for (Object[] row : resultList) {
            String username = (String) row[0];
            int age = (int) row[1];
            logger.info("{}, {}", username, age);
        }
    }

    @Test
    public void projectionScalaMultiEntity() throws Exception {
        // inner join
        List<Object[]> resultList = em.createQuery("select o.member, o.product, o.orderAmount from Order o").getResultList();
        for (Object[] row : resultList) {
            Member member = (Member) row[0];
            Product product = (Product) row[1];
            int orderAmount = (int) row[2];
            logger.info("{}, {}, {}", member, product, orderAmount);
        }
    }

    @Test
    public void projectionByScalaNewDto() throws Exception {
        List<UserDTO> resultList = em.createQuery("select new me.ggulmool.entity.dto.UserDTO(m.username, m.age) from Member m")
                .getResultList();

        for (UserDTO userDTO : resultList) {
            logger.info("{}", userDTO);
        }
    }

    @Test
    public void paging() throws Exception {
        TypedQuery<Member> query = em.createQuery("select m from Member m order by m.username desc", Member.class);
        query.setFirstResult(0);
        query.setMaxResults(3);
        List<Member> resultList = query.getResultList();

        for (Member member : resultList) {
            logger.info("{}", member);
        }
    }

    @Test
    public void innerJoin1() throws Exception {
        TypedQuery<Member> query = em.createQuery(
                "select m from Member m inner join m.team t " +
                "where t.name = :teamName", Member.class);

        List<Member> members = query.setParameter("teamName", "NMS개발팀").getResultList();
        members.forEach(m -> logger.info("{}", m));
    }

    @Test
    public void innerJoin2() throws Exception {
        Query query = em.createQuery(
                "select m, t from Member m inner join m.team t " +
                        "where t.name = :teamName"
        );

        List<Object[]> result = query.setParameter("teamName", "NMS개발팀").getResultList();
        result.forEach(r -> {
            Member member = (Member) r[0];
            Team team = (Team) r[1];
            logger.info("{}, {}", member, team);
        });
    }

    @Test
    public void leftJoin1() throws Exception {
        //List<Object[]> resultList = em.createQuery("select t, m from Team t, in(t.members) m").getResultList();

        List<Object[]> resultList = em.createQuery("select t, m from Team t left join t.members m").getResultList();
        resultList.forEach(r -> {
            Team team = (Team) r[0];
            Member member = (Member) r[1];
            logger.info("{}, {}", team, member);
        });
    }

    /*
    a left join b where on 차이
    where은 join된 결과를 filter하고
    on은 join하기 전에 filter한다.
    보통 where는 a조건 on은 b조건
    select * from a left join b a.key = b.key on b.condition = ?
    where a.filter = ?
     */
    @Test
    public void leftJoin2() throws Exception {
        Query query = em.createQuery("select m, t from Member m left join m.team t where t.name = :teamName");
        List<Object[]> resultList = query.setParameter("teamName", "인사팀").getResultList();

        resultList.forEach(r -> {
            Member member = (Member) r[0];
            Team team = (Team) r[1];
            logger.info("{}, {}", member, team);
        });

//      Member[소지섭], Team[인사팀]
    }

    @Test
    public void leftJoinOn() throws Exception {
        Query query = em.createQuery("select m, t from Member m left join m.team t on t.name = :teamName");
        List<Object[]> resultList = query.setParameter("teamName", "인사팀").getResultList();

        resultList.forEach(r -> {
            Member member = (Member) r[0];
            Team team = (Team) r[1];
            logger.info("{}, {}", member, team);
        });

//        Member[김남열], null
//        Member[홍길동], null
//        Member[손예진], null
//        Member[소지섭], Team[인사팀]
//        Member[한예슬], null
    }

    @Test
    public void fetchJoin1() throws Exception {
        List<Member> members = em.createQuery("select m from Member m join fetch m.team", Member.class)
                .getResultList();

        members.forEach(member -> logger.info("{}, {}", member.getUsername(), member.getTeam().getName()));
    }

    @Test
    public void fetchJoin2() throws Exception {
        List<Team> teams = em.createQuery("select distinct t from Team t join fetch t.members where t.name =:teamName", Team.class)
        //List<Team> teams = em.createQuery("select  t from Team t join fetch t.members where t.name =:teamName", Team.class)
                .setParameter("teamName", "NMS개발팀")
                .getResultList();

        logger.info("{}", teams.size());
        teams.forEach(team -> {
            logger.info("{}", team);
            team.getMembers().forEach(member -> {
                logger.info("{}", member);
            });
        });
    }

    @Test
    public void pathExpression() throws Exception {
        String query = "select o.member.team " +
                        "from Order o " +
                        "where o.product.name = '상품1' and o.address.city='seoul'";

        em.createQuery(query, Team.class).getResultList()
                .forEach(team -> logger.info("{}", team));

//        select
//        team2_.team_id as team_id1_3_,
//                team2_.name as name2_3_
//        from
//        orders order0_ cross
//                join
//        member member1_
//        inner join
//        team team2_
//        on member1_.team_id=team2_.team_id cross
//                join
//        product product3_
//        where
//        order0_.member_id=member1_.member_id
//        and order0_.product_id=product3_.product_id
//        and product3_.name='상품1'
//        and order0_.city='seoul'
    }

    @Test
    public void whereSub() throws Exception {
        String query = "select m from Member m where m.team = ANY (select t from m.team t)";
        String query1 = "select m from Member m where m.age > (select avg(m2.age) from Member m2)";
        String query2 = "select m from Member m where exists (select t from m.team t where t.name = 'NMS개발팀')";
        em.createQuery(query2, Member.class)
                .getResultList().stream()
                .forEach(member -> logger.info("{}", member));
    }

    @Test
    public void whereBetween() throws Exception {
        String query = "select m from Member m where m.age between 20 and 30";
        em.createQuery(query, Member.class).getResultList()
                .stream().forEach(member -> logger.info("{}", member));
    }

    @Test
    public void whereIn() throws Exception {
        String query = "select m from Member m where m.username in('김남열','손예진')";
        em.createQuery(query, Member.class).getResultList()
                .stream().forEach(member -> logger.info("{}", member));
    }

    @Test
    public void whereLike() throws Exception {
        String query = "select m from Member m where m.username like '김남_'";
        em.createQuery(query, Member.class).getResultList()
                .stream().forEach(member -> logger.info("{}", member));
    }

    @Test
    public void whereNull() throws Exception {
        String query = "select m from Member m where m.team is null";
        em.createQuery(query, Member.class).getResultList()
                .stream().forEach(member -> logger.info("{}", member));
    }

    @Test
    public void whereCollection() throws Exception {
        String query = "select m from Member m where m.orders is not empty";
        em.createQuery(query, Member.class).getResultList()
                .stream().forEach(member -> logger.info("{}", member));
    }

    @Test
    public void printDate() throws Exception {
        String query = "select CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP from Member m";
        List<Object[]> resultList = em.createQuery(query).getResultList();
        for (Object[] row : resultList) {
            Date date = (Date) row[0];
            Time time = (Time) row[1];
            Timestamp timestamp = (Timestamp) row[2];
            logger.info("date = {}, time = {}, timestamp = {}", date, time, timestamp);
        }
    }

    @Test
    public void case1() throws Exception {
        String query = "select case when m.age <=20 then '학생요금' " +
                        "when m.age >= 60 then '경로요금' " +
                        "else '일반요금' end from Member m";
        em.createQuery(query, String.class).getResultList()
                .stream().forEach(result -> logger.info("{}", result));
    }

    @Test
    public void case2() throws Exception {
        String query = "select case t.name when 'NMS개발팀' THEN '인센티브110%' " +
                        "when '인사팀' then '인센티브120%' " +
                        "else '인센50%' end from Team t";
        em.createQuery(query, String.class).getResultList()
                .stream().forEach(result -> logger.info("{}", result));
    }

    @Test
    public void coalesce() throws Exception {
        String query = "select coalesce(m.username, '이름없는 회원') from Member m";
        em.createQuery(query, String.class).getResultList()
                .stream().forEach(result -> logger.info("{}", result));
    }

    @Test
    public void directEntity() throws Exception {
        String query = "select m from Member m where m = :member";
        Member whereMember = new Member();
        whereMember.setId(20004L);
        em.createQuery(query, Member.class).setParameter("member", whereMember)
                .getResultList()
                .stream().forEach(member -> logger.info("{}", member));
    }

    @Test
    public void namedQuery() throws Exception {
        em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", "손예진")
                .getResultList()
                .stream().forEach(member -> logger.info("{}", member));
    }

    @Test
    public void namedQuery2() throws Exception {
        Long result = em.createNamedQuery("Member.count", Long.class)
                .getSingleResult();
        logger.info("{}", result);
    }
}
