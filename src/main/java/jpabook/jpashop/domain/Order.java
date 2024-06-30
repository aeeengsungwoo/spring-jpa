package jpabook.jpashop.domain;

import jakarta.persistence.*;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Lazy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.*;
import static jpabook.jpashop.domain.OrderStatus.CANCEL;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    //== 생성 메서드 ==//

    public static Order createOrder(Member member, Delivery delivery, OrderItem ... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//

    //주문 취소
    public void cancel(){
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송된 상품은 취소가 불가능합니다.");

        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem: orderItems){
            orderItem.cancel();
        }
    }

    //==조회 로직==//

    //전체 주문 가격


    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems){
            totalPrice += orderItem.totalPrice();
        }
        return totalPrice;
    }
    // 위 코드를 아래 코드와 같이 스트림 문법을 사용해서 간단히 나타낼 수 있다.
    // for 부분에서 option + enter 누르면 리스트가 뜨는데, 거기서 stream sum()을 누르면 스트림 문법을 사용한 방법으로 바뀐다.
    // 이후 total 부분에서 option + command + N 을 누르면 더 간단하게 바꿀 수 있다.
//    public int getTotalPrice(){
//        return orderItems.stream()
//                .mapToInt(OrderItem::totalPrice)
//                .sum();
//    }

}
