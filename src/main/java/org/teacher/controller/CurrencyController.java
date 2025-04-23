package org.teacher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teacher.model.Currency;
import org.teacher.repository.CurrencyRepository;

@RestController
@RequestMapping("/currencies")
public class CurrencyController extends BaseController<Currency, Long> {

    public CurrencyController(CurrencyRepository repository) {
        super(repository, "currencies");
    }

}
