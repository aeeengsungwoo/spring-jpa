package jpabook.jpashop.domain;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Lazy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    /*
     CascadeType.ALL 설정을 안해주면
     persist(orderItemA)
     persist(orderItemB)
     persist(orderItemC)
     persist(order)
     이렇게 아이템까지 다 save해줘야 한다. 하지만 CascadeType.ALL 설정으로
     persist(order) 이 한줄로 이전과 같은 결과를 만들 수 있다. Cascade가 persist를 전파하기 때문에 가능한 일이다.
    */


    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문의 상태 [ORDER, CANCEL]


    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
