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
            // 비영속 상태
            Member member1 = new Member(150L , "A");
            Member member2 = new Member(160L , "B");

            // 아직까지 insert 쿼리가 나가지않는다.
            em.persist(member1);
            em.persist(member2);

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