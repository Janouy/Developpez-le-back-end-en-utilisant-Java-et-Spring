package com.openclassrooms.chatopapi.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RentalsListResponse", description = "Wrapper containing the list of rentals", example = "{\n"
		+ "  \"rentals\": []\n" + "}")
public class RentalsListResponse {

	@ArraySchema(schema = @Schema(implementation = RentalResponse.class), arraySchema = @Schema(description = "List of rentals"))
	private List<RentalResponse> rentals;

	public RentalsListResponse() {
	}

	public RentalsListResponse(List<RentalResponse> rentals) {
		this.rentals = rentals;
	}

	public List<RentalResponse> getRentals() {
		return rentals;
	}

	public void setRentals(List<RentalResponse> rentals) {
		this.rentals = rentals;
	}
}
