package pl.marczuk.bookstore.dto;

import lombok.*;

@Data
public class topDto {
    private Integer position;
    private String name;
    private Integer value;

    public topDto(Integer position, String name, Integer value) {
        this.position = position;
        this.name = name;
        this.value = value;
    }

}
