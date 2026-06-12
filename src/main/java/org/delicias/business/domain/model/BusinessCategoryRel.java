package org.delicias.business.domain.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.persistence.*;
import lombok.*;
import org.delicias.common.dto.PagedResult;
import org.delicias.restaurant.domain.model.RestaurantTemplate;

import java.util.List;

@Entity
@Table(name = "business_category_rel")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessCategoryRel extends PanacheEntityBase {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "business_category_rel_id_seq")
    @SequenceGenerator(
            name = "business_category_rel_id_seq",
            allocationSize = 1
    )
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="restaurant_id", referencedColumnName = "id")
    private RestaurantTemplate restaurantTmpl;

    @Column(name = "zone_business_category_id")
    private Integer zoneBusinessCategoryId;

    private Short sequence;

    private Boolean active;

    public static PagedResult<BusinessCategoryRel> getByZoneBusinessCategId(
            Integer zoneBusinessCategoryId,
            String name,
            int page,
            int size
    ) {

        String sql = "zoneBusinessCategoryId = :zoneBusinessCategoryId";
        var params = Parameters.with("zoneBusinessCategoryId", zoneBusinessCategoryId);

        if(name != null && !name.isBlank()) {
            sql += " and LOWER(restaurantTmpl.name) LIKE :name ";
            params.and("name", "%" + name.toLowerCase() + "%");
        }

        PanacheQuery<BusinessCategoryRel> query = find(
                sql,
                Sort.by("sequence", Sort.Direction.Ascending),
                params
        );


        long total = query.count();

        List<BusinessCategoryRel> data = query
                .page(Page.of(page, size))
                .list();

        return new PagedResult<>(data, total, page, size);

    }
}
