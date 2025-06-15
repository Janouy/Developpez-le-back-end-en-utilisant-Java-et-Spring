package com.openclassrooms.chatopapi.dto;

import java.util.List;

import com.openclassrooms.chatopapi.model.Rental;

public class RentalResponse {

    public Integer id;
    public String name;
    public Integer surface;
    public Integer price;
    public List<String> picture;
    public String description;
    public Integer owner_id;
    public String created_at;
    public String updated_at;

    public RentalResponse(Rental rental) {
        this.id = rental.getId();
        this.name = rental.getName();
        this.surface = rental.getSurface();
        this.price = rental.getPrice();
        this.picture = List.of(rental.getPicture());
        this.description = rental.getDescription();
        this.owner_id = rental.getOwnerId();
        this.created_at = rental.getCreatedAt().toLocalDate().toString();
        this.updated_at = rental.getUpdatedAt().toLocalDate().toString();
    }
}
