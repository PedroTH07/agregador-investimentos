package io.projects.agregadorinvestimentos.controller;

import io.projects.agregadorinvestimentos.controller.dto.AccountStockResponseDto;
import io.projects.agregadorinvestimentos.controller.dto.AssociateAccountStockDto;
import io.projects.agregadorinvestimentos.controller.dto.CreateAccountDto;
import io.projects.agregadorinvestimentos.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/stocks")
    public ResponseEntity<Void> associateStock(@PathVariable("accountId") String accountId,
                                              @RequestBody AssociateAccountStockDto data) {
        accountService.associateStock(accountId, data);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/stocks")
    public ResponseEntity<List<AccountStockResponseDto>> listStocks(@PathVariable("accountId") String accountId) {
        var stocks = accountService.listStocks(accountId);
        return ResponseEntity.ok(stocks);
    }
}
