package org.teacher.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.teacher.model.Tariff;
import org.teacher.service.TariffService;

import java.util.List;

@RestController
@RequestMapping("/api/tariffs")
@RequiredArgsConstructor
public class TariffController {
    private final TariffService tariffService;

    @GetMapping("/my")
    public ResponseEntity<List<Tariff>> getMyTariffs(@AuthenticationPrincipal UserDetails userDetails) {
        List<Tariff> tariffs = tariffService.getTariffsForTeacher(userDetails.getUsername());
        return ResponseEntity.ok(tariffs);
    }
}
