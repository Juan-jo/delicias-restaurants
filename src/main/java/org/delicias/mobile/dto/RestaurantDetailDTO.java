package org.delicias.mobile.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record RestaurantDetailDTO(
        Integer id,
        String name,
        String imageCoverUrl,
        RestaurantInfo info,
        List<RecommendedItem> recommended,
        List<Menu> menu,
        boolean alreadyExistsShoppingCart,
        Integer shoppingCartLinesSize,
        UUID shoppingCartId
) {

    @Builder
    public record RestaurantInfo(
            String imageLogoUrl,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm a")
            LocalTime hourStart,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm a")
            LocalTime hourEnd,

            boolean available,

            String address
    ) { }

    @Builder
    public record RecommendedItem(
            Integer id,
            String picture,
            String name,
            BigDecimal priceList
    ) { }

    @Builder
    public record Menu(
            String name,
            List<ProductMenu> products
    ) {}

    @Builder
    public record ProductMenu(
            Integer id,
            String name,
            String picture,
            BigDecimal priceList,
            String description
    ) {}

}
