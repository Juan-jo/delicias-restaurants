package org.delicias.menu_products.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.delicias.menu.domain.model.RestaurantMenu;

@Entity
@Table(name = "restaurant_menu_products")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuProduct {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "restaurant_menu_product_seq")
    @SequenceGenerator(
            name = "restaurant_menu_product_seq",
            allocationSize = 1
    )
    private Integer id;

    @ManyToOne
    @JoinColumn(name="restaurant_menu_id", referencedColumnName = "id")
    private RestaurantMenu menu;

    @Column(name = "product_tmpl_id")
    private Integer productTmplId;

    @Column(name = "sequence")
    private Short sequence;
}
