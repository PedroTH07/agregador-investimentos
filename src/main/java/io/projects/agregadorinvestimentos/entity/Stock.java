package io.projects.agregadorinvestimentos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_stocks")
public class Stock {

    @Id
    @Column(name = "ticker")
    private String ticker;

    @Column(name = "description")
    private String description;

    public Stock() {
    }

    public Stock(String ticker, String description) {
        this.ticker = ticker;
        this.description = description;
    }

    public String getTickets() {
        return ticker;
    }

    public void setTickets(String tickets) {
        this.ticker = tickets;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
