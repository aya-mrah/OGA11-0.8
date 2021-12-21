package com.oga.stockservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class Product.
 *
 * @author devrobot
 * @version 1.0
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    /** The title. */
    @Column(name = "title", nullable = false)
    private String title;

    /** The description. */
    @Column(name = "description")
    private String description;

    /** The weight. */
    @Column(name = "weight")
    private double weight;

    /** The price. */
    @Column(name = "price", nullable = false)
    private double price;

    /** The quantity */
    private double quantity;

    private long employeeId;


    private long mvmStockUp; //+ lorsque le produit est stocké dans l'entrepot
    private long mvmStockDown;  // -lorsque le produit est sortie de l'entrepot

    /** The category. */
    @ManyToOne()
    @JoinColumn(name="category_id")
    @JsonIgnore
    Category category;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "commande_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
    @OneToOne(mappedBy = "product")
        @JsonIgnore
            private LigneDeCommande ligneDeCommande;
        }
