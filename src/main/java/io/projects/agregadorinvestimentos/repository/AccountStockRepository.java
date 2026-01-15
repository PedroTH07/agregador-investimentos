package io.projects.agregadorinvestimentos.repository;

import io.projects.agregadorinvestimentos.entity.AccountStock;
import io.projects.agregadorinvestimentos.entity.AccountStockId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountStockRepository
        extends JpaRepository<AccountStock, AccountStockId> {
}
