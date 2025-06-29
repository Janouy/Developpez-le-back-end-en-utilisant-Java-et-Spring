package com.openclassrooms.chatopapi.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.openclassrooms.chatopapi.model.Rental;


public interface RentalRepository extends CrudRepository<Rental, Integer> {
	Optional<Rental> findById(Long id);
	@Override
	Iterable<Rental> findAll();
}