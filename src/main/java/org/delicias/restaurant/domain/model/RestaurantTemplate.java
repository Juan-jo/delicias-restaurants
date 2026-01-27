package org.delicias.restaurant.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.delicias.restaurant.dto.RestaurantTemplateDTO;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Table(name = "restaurant_template")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantTemplate {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "restaurant_template_id_seq")
    @SequenceGenerator(
            name = "restaurant_template_id_seq",
            allocationSize = 1
    )
    private Integer id;

    private String name;

    private String description;

    private String phone;

    @Column(name = "position", columnDefinition = "GEOGRAPHY(Point, 4326)")
    private Point position;

    @Column(name = "address")
    private String address;

    @Column(name = "image_cover_url")
    private String imageCover;

    @Column(name = "image_logo_url")
    private String imageLogo;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    /*@OneToMany(mappedBy = "restaurantTmpl")
    @OrderBy("id")
    private Set<RestaurantScheduled> schedules;*/

    /*@OneToMany(mappedBy = "restaurantTmpl")
    @OrderBy("sequence")
    private Set<RestaurantTmplMenu> menus;*/


    public void update(RestaurantTemplateDTO templateDTO) {
        this.name = templateDTO.name();
        this.description = templateDTO.description();
        this.phone = templateDTO.phone();
    }

    /*


    public void updatePosition(double longitude, double latitude) {
        GeometryFactory geometryFactory = new GeometryFactory();
        this.position = geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }*/


    public RestaurantTemplate(Integer id) {
        this.id = id;
    }
}
