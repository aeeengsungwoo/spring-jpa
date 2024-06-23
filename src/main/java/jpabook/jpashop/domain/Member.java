package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
    /*
     여기서 orders 의 초기화를 하는데, 이 방법 말고 다른 방법으로 초기화를 하게되면 Null pointer 문제가 발생할 수 있다.
     따라서 위와 같은 방법으로 초기화를 하고(이 방법은 널 포인터 문제 안생김) 다른 클래스에서 orders를 변경하지 않도록 해야한다.
     이렇게 객체를 샐성한다는 것은, 이 객체를 DB에 저장한다는 의미인데(영속화 시키겠다는), 다른데서 바꿔버리면 디비 값이랑 달라지는 문제가 생긴다.
    */


}
