package com.example.demo.entity;

import com.example.demo.dto.BaiHatResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "bai_hat")
public class BaiHat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ten_bai_hat")
    private String tenBaiHat;

    @Column(name = "ten_tac_gia")
    private String tenTacGia;

    @Column(name = "thoi_luong")
    private Integer thoiLuong;

    @Column(name = "ngay_san_xuat")
    private String ngaySanXuat;

    @Column(name = "gia")
    private Double gia;

    @OneToOne
    @JoinColumn(name = "ca_si_id")
    private CaSi caSi;

    public BaiHatResponse toResponse() {
        return new BaiHatResponse(id, tenBaiHat, tenTacGia, thoiLuong, ngaySanXuat, gia, caSi.getTenCaSi(), caSi.getQueQuan());
    }
}
