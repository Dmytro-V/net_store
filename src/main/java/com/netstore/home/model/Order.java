package com.netstore.home.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @OneToMany
    private List<LineOrder> linesForOrder = new ArrayList<>();

    @Column(name = "destination")
    private String destination;

    @Column(name = "order_status")
    @Enumerated(value=EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "creation_date")
    private Date creationDate;


}
