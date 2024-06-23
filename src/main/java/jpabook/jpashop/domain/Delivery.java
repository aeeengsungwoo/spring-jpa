package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    // Enum Type은 기본적으로 ORDINAL인데, 이건 1 2 3 같은 숫자로 디비에 값이 들어간다. 중간에 status를 추가하면 값이 밀려서 망해버림
    // 꼭 String으로 설정하자.
    private DeliveryStatus status; //ENUM [READY(준비), COMP(배송)]
}
