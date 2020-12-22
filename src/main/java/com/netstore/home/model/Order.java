package com.netstore.home.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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


    @OneToOne
    private User user;

    @OneToMany
    private List<LineOrder> linesForOrder;

    @Column(name = "destination")
    private String destination;

    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "creation_date")
    private Date creationDate;


}
