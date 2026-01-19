package io.projects.agregadorinvestimentos.client.dto;

import java.util.List;

public record BrapiResponseDto(List<StockDto> results) {

    public double getRegularPrice() {
        return results.getFirst().regularMarketPrice();
    }
}
