package com.poc.rom.entity;

import com.poc.rom.enums.MenuItemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="menu_item")
public class MenuItem {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    private float price;

    @Enumerated(EnumType.STRING)
    private MenuItemType menuItemType;

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", menuItemType=" + menuItemType +
                '}';
    }
}
