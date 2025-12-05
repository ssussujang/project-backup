package com.test.ocr.DTO;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class SaveDTO {
    private int s_id;
    private String userId;
    private String s_shop;
    private String s_adrr;
    private LocalDate s_date;

    private String items_name;
    private String items_price;
    private String items_count;

    // 파싱용 리스트
    private List<String> itemNames;
    private List<Integer> itemPrices;
    private List<Integer> itemCounts;
}






