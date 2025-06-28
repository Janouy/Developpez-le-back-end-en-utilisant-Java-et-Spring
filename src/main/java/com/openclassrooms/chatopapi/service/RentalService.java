package com.openclassrooms.chatopapi.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.chatopapi.dto.CreatedRentalRequest;
import com.openclassrooms.chatopapi.dto.UpdateRentalRequest;
import com.openclassrooms.chatopapi.model.Rental;
import com.openclassrooms.chatopapi.repository.RentalRepository;

@Service
public class RentalService {

	private final RentalRepository rentalRepository;

	public RentalService(RentalRepository rentalRepository) {
		this.rentalRepository = rentalRepository;
	}

	public Rental buildRentalFromRequest(CreatedRentalRequest request, Long userId) {
		System.out.println(userId);
		Rental rental = new Rental();
		rental.setName(request.getName());
		rental.setSurface(request.getSurface());
		rental.setPrice(request.getPrice());
		rental.setDescription(request.getDescription());
		rental.setOwnerId(userId);
		return rental;
	}

	public String savePicture(MultipartFile picture) throws IOException {
		String filename = UUID.randomUUID() + "_" + picture.getOriginalFilename();
		Path filePath = Paths.get("uploads", filename);
		Files.createDirectories(filePath.getParent());
		Files.copy(picture.getInputStream(), filePath);
		return "http://localhost:8080/api/uploads/" + filename;
	}

	public Rental saveRental(Rental rental) {
		return rentalRepository.save(rental);
	}

	public Optional<Rental> findById(Integer id) {
		return rentalRepository.findById(id);
	}

	public boolean isOwner(Long userId, Rental rental) {
		return userId.equals(rental.getOwnerId());
	}

	public Rental updateRentalFromRequest(Rental rental, UpdateRentalRequest request) {
		rental.setName(request.getName());
		rental.setSurface(request.getSurface());
		rental.setPrice(request.getPrice());
		rental.setDescription(request.getDescription());
		return rentalRepository.save(rental);
	}

}
