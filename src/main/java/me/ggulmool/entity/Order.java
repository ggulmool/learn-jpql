package me.ggulmool.entity;

import javax.persistence.*;

@Entity
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(name = "FK_MEMBER"))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", foreignKey = @ForeignKey(name = "FK_PRODUCT"))
    private Product product;

    @Column(name = "ORDERAMOUNT")
    private Integer orderAmount;

    @Embedded
    private Address address;

    public Order() {
    }

    public Order(Integer orderAmount, Address address) {
        this.orderAmount = orderAmount;
        this.address = address;
    }

    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return String.format("Order[%d %s %s]", id, member, product);
    }
}
