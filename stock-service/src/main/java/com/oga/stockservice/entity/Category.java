package com.oga.stockservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    /** The name. */
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    private long employeeId;

    /** The description **/
    @Column(name = "description")
    private String description;

    /** The products. */
    @OneToMany(mappedBy = "category"
            , orphanRemoval = true, cascade = CascadeType.PERSIST)
    private List<Product> products;







}

