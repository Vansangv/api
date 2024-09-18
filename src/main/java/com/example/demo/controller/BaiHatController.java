package com.example.demo.controller;

import com.example.demo.dto.BaiHatRequest;
import com.example.demo.dto.BaiHatResponse;
import com.example.demo.entity.BaiHat;
import com.example.demo.repository.BaiHatRepository;
import com.example.demo.repository.CaSiRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("baihat")
public class BaiHatController {

    @Autowired
    private BaiHatRepository bhRepo;

    @Autowired
    private CaSiRepository csRepo;

    @GetMapping()
    public ResponseEntity<?> findAll() {
      List<BaiHatResponse> list = new ArrayList<>();
      bhRepo.findAll().forEach(c -> list.add(c.toResponse()));
      return ResponseEntity.ok(list);
    }

    @GetMapping("page")
    public ResponseEntity<?> page(@RequestParam(defaultValue = "0") Integer page) {
        Pageable p = PageRequest.of(page, 10);
        List<BaiHatResponse> list = new ArrayList<>();
        bhRepo.findAll(p).forEach(c -> list.add(c.toResponse()));
        return ResponseEntity.ok(list);
    }

    @GetMapping("detail/{id}")
    public ResponseEntity<?> detail(@PathVariable Integer id) {
        return ResponseEntity.ok().body(bhRepo.findById(id).stream().map(BaiHat::toResponse));
    }

    @PostMapping("add")
    public ResponseEntity<?> add(@Valid @RequestBody BaiHatRequest baiHatRequest, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
//        }
        if (bindingResult.hasErrors()) {
            StringBuilder mess = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> mess.append(error.getDefaultMessage()).append("\n"));
            return ResponseEntity.badRequest().body(mess.toString());
            }
        if (bhRepo.existsByTenBaiHat(baiHatRequest.getTenBaiHat())) {
            return ResponseEntity.badRequest().body("tên bài hát đã tồn tại");
        }
        if (!csRepo.existsById(baiHatRequest.getCaSiID())) {
            return ResponseEntity.badRequest().body("id ca sĩ k tồn tại");
        }
        BaiHat baiHat = baiHatRequest.toEntity();
        baiHat.setCaSi(csRepo.getById(baiHatRequest.getCaSiID()));
        bhRepo.save(baiHat);
        return ResponseEntity.ok("thêm thành công");
    }

    @PutMapping("update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody BaiHatRequest baiHatRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        if (bhRepo.existsByTenBaiHat(baiHatRequest.getTenBaiHat())) {
            return ResponseEntity.badRequest().body("tên bài hát đã tồn tại");
        }
        if (!csRepo.existsById(baiHatRequest.getCaSiID())) {
            return ResponseEntity.badRequest().body("id ca sĩ k tồn tại");
        }
        if (bhRepo.findById(id).isPresent()) {
            BaiHat baiHat = baiHatRequest.toEntity();
            baiHat.setId(id);
            baiHat.setCaSi(csRepo.getById(baiHatRequest.getCaSiID()));
            bhRepo.save(baiHat);
            return ResponseEntity.ok("Update thành công ");
        } else {
            return ResponseEntity.badRequest().body("Không tìm thấy id cần update");
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        if(bhRepo.findById(id).isPresent()){
            bhRepo.deleteById(id);
            return ResponseEntity.ok("Xóa thành công");
        }else {
            return ResponseEntity.badRequest().body("Không tìm thấy id cần xóa");
        }
    }
    @GetMapping("searchHH")
    public ResponseEntity<?> searchHH(@RequestParam(name = "tenCS") String tenCS,
                                    @RequestParam(name = "min") Double min,
                                    @RequestParam(name = "max") Double max) {
        return ResponseEntity.ok().body(bhRepo.findAll().stream().filter(
                bh -> (bh.getCaSi().getTenCaSi().equalsIgnoreCase(tenCS) && (bh.getGia() >= min && bh.getGia() <= max))
        ).map(BaiHat::toResponse));
    }

    @GetMapping("search")
    public ResponseEntity<?> search(@RequestParam(name = "ten", defaultValue = "a") String ten,
                                    @RequestParam(name = "min") Integer min,
                                    @RequestParam(name = "max") Integer max
    ) {
        List<BaiHatResponse> list = bhRepo.findAll().stream()
                .filter(bh -> (bh.getTenBaiHat().toLowerCase().contains(ten.toLowerCase()))
                        && (bh.getThoiLuong() >= min && bh.getThoiLuong() <= max))
                .map(BaiHat::toResponse).collect(Collectors.toList());
        return list.isEmpty() ? ResponseEntity.badRequest().body("Không có bài hát nào phù hợp")
                : ResponseEntity.ok().body(list);
    }

    @GetMapping("max")
    public ResponseEntity<?> max() {
        return ResponseEntity.ok().body(bhRepo.findAll().stream().
                max(Comparator.comparing(BaiHat::getGia).thenComparing(BaiHat::getThoiLuong))
                .map(BaiHat::toResponse));
    }

    @GetMapping("sort")
    public ResponseEntity<?> sort() {
        return ResponseEntity.ok().body(bhRepo.findAll().stream().sorted(Comparator.comparing(BaiHat::getGia).reversed())
                .map(BaiHat::toResponse));
    }

    @GetMapping("group")
    private ResponseEntity<?> group() {
        return ResponseEntity.ok().body(bhRepo.findAll().stream().collect(Collectors.groupingBy(
                bh -> bh.getCaSi().getTenCaSi(),
                Collectors.summingInt(BaiHat::getThoiLuong)
        )));
    }

    @GetMapping("group1")
    public ResponseEntity<?> group1(@RequestParam(name = "tenBH") String tenBH) {
        double sum = bhRepo.findAll().stream()
                .filter(bh -> bh.getTenBaiHat().toLowerCase().contains(tenBH.toLowerCase()))
                .mapToDouble(BaiHat::getGia)
                .sum();

        return ResponseEntity.ok().body("Tổng giá trị của các bài hát chứa '" + tenBH + "' là: " + sum);
    }
    @GetMapping("average-price")
    public ResponseEntity<?> getAveragePriceBySongName(@RequestParam(name = "tenBH") String tenBH) {
        OptionalDouble averagePrice = bhRepo.findAll().stream()
                .filter(bh -> bh.getTenBaiHat().equalsIgnoreCase(tenBH))
                .mapToDouble(BaiHat::getGia)
                .average();

        return averagePrice.isPresent()
                ? ResponseEntity.ok().body("Trung bình giá của bài hát '" + tenBH + "' là: " + averagePrice.getAsDouble())
                : ResponseEntity.badRequest().body("Không tìm thấy bài hát nào có tên: " + tenBH);
    }
//    @GetMapping("mostArtists")
//    public ResponseEntity<?> findSongWithMostArtists() {
//        Optional<BaiHat> baiHatWithMostArtists = bhRepo.findAll().stream()
//                .max(Comparator.comparingInt(bh -> bh.getCaSi()));
//
//        return baiHatWithMostArtists.isPresent()
//                ? ResponseEntity.ok().body(baiHatWithMostArtists.get().toResponse())
//                : ResponseEntity.badRequest().body("Không tìm thấy bài hát nào có ca sĩ");
//    }



}
