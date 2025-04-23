package org.teacher.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.teacher.model.Currency;
import org.teacher.model.Role;
import org.teacher.model.Tariff;
import org.teacher.model.User;
import org.teacher.repository.CurrencyRepository;
import org.teacher.repository.TariffRepository;
import org.teacher.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }
}

