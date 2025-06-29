package com.openclassrooms.chatopapi.dto;

import com.openclassrooms.chatopapi.model.Rental;

import io.swagger.v3.oas.annotations.media.Schema;

public class RentalResponse {

	@Schema(description = "Unique identifier of the rental", example = "1")
	public Long id;

	@Schema(description = "Name of the rental", example = "test house 1")
	public String name;

	@Schema(description = "Surface area in square meters", example = "432")
	public Integer surface;

	@Schema(description = "Rental price", example = "300")
	public Integer price;

	@Schema(description = "URL of the rental’s picture", example = "https://blog.technavio.org/wp-content/uploads/2018/12/Online-House-Rental-Sites.jpg")
	public String picture;

	@Schema(description = "Detailed description of the rental", example = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam a lectus eleifend…")
	public String description;

	@Schema(description = "Identifier of the owner", example = "1")
	public Long owner_id;

	@Schema(description = "Creation date (format YYYY/MM/DD)", example = "2012/12/02")
	public String created_at;

	@Schema(description = "Last update date (format YYYY/MM/DD)", example = "2014/12/02")
	public String updated_at;

	public RentalResponse(Rental rental) {
		this.id = rental.getId();
		this.name = rental.getName();
		this.surface = rental.getSurface();
		this.price = rental.getPrice();
		this.picture = rental.getPicture();
		this.description = rental.getDescription();
		this.owner_id = rental.getOwnerId();
		this.created_at = rental.getCreatedAt().toLocalDate().toString();
		this.updated_at = rental.getUpdatedAt().toLocalDate().toString();
	}
}
