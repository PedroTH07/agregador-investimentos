package io.projects.agregadorinvestimentos.service;

import io.projects.agregadorinvestimentos.controller.dto.CreateStockDto;
import io.projects.agregadorinvestimentos.entity.Stock;
import io.projects.agregadorinvestimentos.repository.StockRepository;
import org.springframework.stereotype.Service;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDto data) {
        var stock = new Stock(
                data.ticker(),
                data.description()
        );
        stockRepository.save(stock);
    }
}
