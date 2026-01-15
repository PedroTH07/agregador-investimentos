package io.projects.agregadorinvestimentos.repository;

import io.projects.agregadorinvestimentos.entity.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BillingAddressRepository
        extends JpaRepository<BillingAddress, UUID> {
}
