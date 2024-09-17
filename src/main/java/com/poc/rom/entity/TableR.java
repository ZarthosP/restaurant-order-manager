package com.poc.rom.entity;

import com.poc.rom.enums.TableStatus;
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
@Table(name="tableR")
public class TableR {

    @Id
    @GeneratedValue
    private Long id;

    private int number;

    private int numberOfClients;

    @Enumerated(EnumType.STRING)
    private TableStatus tableStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;
}
