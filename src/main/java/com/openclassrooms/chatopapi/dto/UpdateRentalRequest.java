package com.openclassrooms.chatopapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateRentalRequest {

	@NotBlank(message = "Name is mandatory")
	@Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
	public String name;

	@NotNull(message = "Surface is mandatory")
	@Min(value = 1, message = "Surface must be greater than 0")
	public Integer surface;

	@NotNull(message = "Price is mandatory")
	@Min(value = 1, message = "Price must be greater than 0")
	public Integer price;

	@NotBlank(message = "Description is mandatory")
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
