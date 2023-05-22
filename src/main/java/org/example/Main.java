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


            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member  = new Member();
            member.setUsername("MemberA");
            // 연관관계 주인인 member에서 값 세팅
            member.changeTeam(team);
            em.persist(member);



            em.flush();
            em.clear();

            Team findTeam = em.find(Team.class, team.getId());
            List<Member> members = findTeam.getMembers();

            System.out.println("===============");
            for (Member m : members) {
                System.out.println("m =" +m.getUsername());
            }
            System.out.println("===============");

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