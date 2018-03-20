package me.ggulmool.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "MEMBER")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String username;
    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID", foreignKey = @ForeignKey(name = "FK_TEAM"))
    private Team team;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Order> orders;

    protected Member() {
    }

    public Member(Integer age, String username) {
        this.age = age;
        this.username = username;
    }

    public void setTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public Integer getAge() {
        return age;
    }

    public Team getTeam() {
        return team;
    }

    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public String toString() {
        return String.format("Member[%s]", username);
    }
}
