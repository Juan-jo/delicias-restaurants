package org.delicias.products_recommend.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.delicias.restaurant.domain.model.RestaurantTemplate;

@Entity
@Table(name = "restaurant_products_recommend")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRecommend {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "restaurant_products_recommend_seq")
    @SequenceGenerator(
            name = "restaurant_products_recommend_seq",
            allocationSize = 1
    )
    private Integer id;

    @ManyToOne
    @JoinColumn(name="restaurant_tmpl_id", referencedColumnName = "id")
    private RestaurantTemplate restaurantTmpl;

    @Column(name = "product_tmpl_id")
    private Integer productTmplId;

    @Column(name = "sequence")
    private Short sequence;
}
