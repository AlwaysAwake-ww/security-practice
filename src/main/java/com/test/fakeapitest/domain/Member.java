package com.test.fakeapitest.domain;


import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity // database table 과 매핑하는 객체
@Table(name="member") // db의 테이블 이름
@NoArgsConstructor
@Getter
@Setter
public class Member {

    @Id // member table 의 pk
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // userId 자동 생성
    private Long memberId;

    @Column(length = 255)
    private String email;

    @Column(length = 50)
    private String name;

    @Column(length = 500)
    private String password;

    @CreationTimestamp  // 현재 시간이 저장 될 때 자동 생성
    private LocalDateTime regdate;

    @ManyToMany // 다대다 관계 설정
    //
    @JoinTable(name = "member_role", joinColumns = @JoinColumn(name="member_id"), inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();



    @Override
    public String toString(){
        return "User{"+"memberId="+memberId+", email="+email+", name="+name+", password="+password+", regdate="+regdate+"}";
    }

    // member 에 새로운 role 삽입 메서드
    public void addrole(Role role){
        roles.add(role);
    }







}
