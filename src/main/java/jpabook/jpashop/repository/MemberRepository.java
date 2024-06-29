package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
//    @PersistenceContext //JPA
    private final EntityManager em; // 이 선언으로 스프링이 의존성 주입을 해준다.

    public void save(Member member){ //JPA로 저장하는 로직.
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name =:name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
