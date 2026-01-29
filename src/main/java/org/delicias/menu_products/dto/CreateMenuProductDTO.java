package org.delicias.menu_products.dto;

import jakarta.validation.constraints.NotNull;
import org.delicias.common.validation.OnCreate;

public record CreateMenuProductDTO(
        @NotNull(message = "The parameter is mandatory", groups = { OnCreate.class})
        Integer productTmplId,

        @NotNull(message = "The parameter is mandatory", groups = { OnCreate.class})
        Short sequence
) { }
