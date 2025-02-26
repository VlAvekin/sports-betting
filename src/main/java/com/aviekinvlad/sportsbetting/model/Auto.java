package com.aviekinvlad.sportsbetting.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author Vlad Aviekin
 * @date 26.02.2025
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auto {

    @NotNull(message = "{validate.auto.brand}")
    private String brand;

    @NotNull(message = "{validate.auto.amount}")
    @Min(value = 0, message = "{validate.auto.amount.min}")
    @JsonFormat(shape=JsonFormat.Shape.STRING)
    @JsonSerialize(using = AmountSerializer.class)
    private BigDecimal amount;
}
