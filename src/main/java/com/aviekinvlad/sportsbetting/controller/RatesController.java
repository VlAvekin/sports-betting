package com.aviekinvlad.sportsbetting.controller;

import com.aviekinvlad.sportsbetting.advice.ErrorDTOException;
import com.aviekinvlad.sportsbetting.model.Auto;
import com.aviekinvlad.sportsbetting.service.MessageService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vlad Aviekin
 * @date 26.02.2025
 */

@RestController
@RequestMapping("${api.prefix}/${api.v1}/rates")
public class RatesController {

    private static final List<Auto> autos = new ArrayList<>();

    @Autowired
    private MessageService messageService;

    @PostConstruct
    public void init() {
        autos.add(new Auto("Audi", BigDecimal.ZERO));
        autos.add(new Auto("BMW", BigDecimal.ZERO));
        autos.add(new Auto("Ferrari", BigDecimal.ZERO));
        autos.add(new Auto("Honda", BigDecimal.ZERO));
    }

    @SneakyThrows
    @GetMapping
    public ResponseEntity<Object> getRates(@RequestParam(defaultValue = "") String brand) {
        if (!brand.isBlank()) {
            Auto auto = getAuto(brand);
            return new ResponseEntity<>(auto, HttpStatus.OK);
        }
        return new ResponseEntity<>(autos, HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<Object> acceptsBets(@Valid @RequestBody Auto auto) {
        Auto autoDb = getAuto(auto.getBrand());

        BigDecimal amount = autoDb.getAmount().add(auto.getAmount());
        autoDb.setAmount(amount);

        return new ResponseEntity<>(autoDb, HttpStatus.CREATED);
    }

    private Auto getAuto(String auto) throws ErrorDTOException {
        Auto autoDb = autos.stream()
                .filter(a -> a.getBrand().equalsIgnoreCase(auto))
                .findFirst()
                .orElse(null);

        if (autoDb == null) {
            throw new ErrorDTOException(messageService.massage("error.auto.brand.empty", auto));
        }
        return autoDb;
    }

}
