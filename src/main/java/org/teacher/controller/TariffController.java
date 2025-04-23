package org.teacher.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teacher.model.Tariff;
import org.teacher.repository.TariffRepository;

@RestController
@RequestMapping("/tariffs")
public class TariffController extends BaseController<Tariff, Long> {

    public TariffController(TariffRepository repository) {
        super(repository, "tariffs");
    }
}
