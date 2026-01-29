package org.delicias.menu.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.delicias.menu_products.domain.model.MenuProduct;
import org.delicias.restaurant.domain.model.RestaurantTemplate;

import java.util.Set;


@Entity
@Table(name = "restaurant_menu")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantMenu {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "restaurant_menu_seq")
    @SequenceGenerator(
            name = "restaurant_menu_seq",
            allocationSize = 1
    )
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name="restaurant_tmpl_id", referencedColumnName = "id")
    private RestaurantTemplate restaurantTmpl;

    private boolean available;

    private Short sequence;

    @OneToMany(mappedBy = "menu")
    @OrderBy("sequence")
    private Set<MenuProduct> products;

    public RestaurantMenu(Integer menuId) {
        this.id = menuId;
    }
}
