package io.projects.agregadorinvestimentos.service;

import io.projects.agregadorinvestimentos.client.BrapiClient;
import io.projects.agregadorinvestimentos.controller.dto.AccountStockResponseDto;
import io.projects.agregadorinvestimentos.controller.dto.AssociateAccountStockDto;
import io.projects.agregadorinvestimentos.entity.AccountStock;
import io.projects.agregadorinvestimentos.entity.AccountStockId;
import io.projects.agregadorinvestimentos.repository.AccountRepository;
import io.projects.agregadorinvestimentos.repository.AccountStockRepository;
import io.projects.agregadorinvestimentos.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    @Value("${token.brapi.secret}")
    private static String TOKEN;
    private final AccountRepository accountRepository;
    private final StockRepository stockRepository;
    private final AccountStockRepository accountStockRepository;
    private final BrapiClient brapiClient;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository, BrapiClient brapiClient) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
    }

    public void associateStock(String accountId, AssociateAccountStockDto data) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var stock = stockRepository.findById(data.stockId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var accountStockId = new AccountStockId(
                UUID.fromString(accountId),
                data.stockId());
        var accountStock = new AccountStock(
                accountStockId,
                account,
                stock,
                data.quantity());
        accountStockRepository.save(accountStock);
    }

    public List<AccountStockResponseDto> listStocks(String accountId) {
        var account = accountRepository.findById(UUID.fromString(accountId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return account.getAccountStocks()
                .stream()
                .map(as -> new AccountStockResponseDto(
                        as.getStock().getTicker(),
                        as.getQuantity(),
                        getTotal(as.getQuantity(), as.getStock().getTicker())
                        ))
                .toList();
    }

    private double getTotal(Integer quantity, String ticker) {
        var response = brapiClient.getQuote(ticker, TOKEN);
        double price = response.getRegularPrice();
        return price * quantity;
    }
}
