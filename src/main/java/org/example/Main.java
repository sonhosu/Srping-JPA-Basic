package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //엔티티 매니저 팩토리는 aplication 로딩시 1개만 생성하고 공유해야한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        //EntityManager 는 쓰레드간 공유 X
        EntityManager em = emf.createEntityManager();

        //JPA 의 모든 데이터변경은 트랜젝션 안에서 발생해야한다.
        EntityTransaction transaction = em.getTransaction();
        //트랜젝션 시작
        transaction.begin();

        try {
            // 객체를 테이블에 맞추어 모델링
            // 객체지향적인 방법은 아니다.
           /* Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

           Member member  = new Member();
           member.setName("MemberA");
           member.setTeamId(team.getId());
           em.persist(member);

            Member findMember = em.find(Member.class, member.getId());
            Long findTeamId = findMember.getTeamId();
            Team findTeam = em.find(Team.class,findTeamId);*/


            // 객체 지향 모델링 사용시
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member  = new Member();
            member.setUsername("MemberA");
            member.setTeam(team);
            em.persist(member);


            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());

            List<Member> members = findMember.getTeam().getMembers();

            for (Member m : members){
                System.out.println("member:" + m.getUsername());
            }


            // 커밋시점에서 insert 쿼리가 나간다
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}