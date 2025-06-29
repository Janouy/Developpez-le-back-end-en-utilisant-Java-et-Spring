package com.openclassrooms.chatopapi.dto;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreatedRentalRequest {

	@NotBlank(message = "Name is mandatory")
	@Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
	@Schema(description = "Rental name", requiredMode = RequiredMode.REQUIRED)
	public String name;

	@NotNull(message = "Surface is mandatory")
	@Min(value = 1, message = "Surface must be greater than 0")
	@Schema(description = "Rental surface", requiredMode = RequiredMode.REQUIRED)
	public Integer surface;

	@NotNull(message = "Price is mandatory")
	@Min(value = 1, message = "Price must be greater than 0")
	@Schema(description = "Rental price", requiredMode = RequiredMode.REQUIRED)
	public Integer price;

	@NotNull(message = "Picture is mandatory")
	@Schema(description = "Rental picture file", type = "string", format = "binary", implementation = MultipartFile.class, requiredMode = RequiredMode.REQUIRED)
	public MultipartFile picture;

	@NotBlank(message = "Description is mandatory")
	@Schema(description = "Rental price", requiredMode = RequiredMode.REQUIRED)
	public String description;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSurface() {
		return surface;
	}

	public void setSurface(Integer surface) {
		this.surface = surface;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public MultipartFile getPicture() {
		return picture;
	}

	public void setPicture(MultipartFile picture) {
		this.picture = picture;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
