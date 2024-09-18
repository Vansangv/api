package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaiHatResponse {

    private Integer id;

    private String tenBaiHat;

    private String tenTacGia;

    private Integer thoiLuong;

    private String ngaySanXuat;

    private Double gia;

    private String tenCaSi;

    private String queQuan;

}
