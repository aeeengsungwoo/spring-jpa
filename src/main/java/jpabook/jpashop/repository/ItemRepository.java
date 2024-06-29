package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item){
        if (item.getId()==null){ // 새로운 상품을 등록하는 경우엔 당연히 id값이 없다.
            em.persist(item); // 그렇기 때문에 새로운 아이템 값을 저장해주는 것.
        }else {
            em.merge(item); // 근데 만약 id값이 존재하는 경우에는 이미 저장되어있던 상품의 정보를 업데이트 하는 것
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
