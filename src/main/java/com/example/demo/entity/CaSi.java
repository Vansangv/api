package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.tags.shaded.org.apache.xalan.processor.XSLTAttributeDef;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "ca_si")
public class CaSi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ten_ca_si")
    private String tenCaSi;

    @Column(name = "que_quan")
    private String queQuan;

}
