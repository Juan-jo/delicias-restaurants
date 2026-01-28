package org.delicias.products_recommend.dto;

import jakarta.validation.constraints.NotNull;
import org.delicias.common.validation.OnCreate;

public record CreateProductRecommendDTO(
        @NotNull(message = "The parameter is mandatory", groups = { OnCreate.class})
        Integer productTmplId,

        @NotNull(message = "The parameter is mandatory", groups = { OnCreate.class})
        Short sequence
) { }
