package com.example.demo.dto;

import com.example.demo.entity.BaiHat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaiHatRequest {

    private Integer id;

    @NotBlank(message = "Tên bài hát không được để trống")
    private String tenBaiHat;

    @NotBlank(message = "Tên tác giả không được để trống")
    private String tenTacGia;

    @NotNull(message = "thời lượng không được để trống")
    @Min(value = 1,message = "thời lượng phải là số lớn hơn 0")
    private Integer thoiLuong;

    @NotBlank(message = "Ngày sản xuất không được để trống")
    private String ngaySanXuat;

    @NotNull(message = "giá không được để trống")
    @Min(value = 1,message = "đơn giá phải là số lớn hơn 0")
    private Double gia;

    @NotNull(message = "ca sĩ không được để trống")
    private Integer caSiID;

    public BaiHat toEntity(){
        return new BaiHat(id,tenBaiHat,tenTacGia,thoiLuong,ngaySanXuat,gia,null);
    }

}
