package com.test.ocr.service;


import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.test.ocr.DTO.ItemDTO;
import com.test.ocr.DTO.ReceiptDTO;
import com.test.ocr.DTO.SaveDTO;
import com.test.ocr.DTO.UserDTO;
import com.test.ocr.mapper.ReceiptMapper;
import com.test.ocr.mapper.UserMapper;

import lombok.AllArgsConstructor;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ServiceIMP implements Service {
	
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ReceiptMapper receiptMapper;

    @Override
    public void signup(UserDTO u) {
        System.out.println("DB 등록 시도: " + u);
        userMapper.signup(u);
        System.out.println("DB 등록 완료");
    }

    @Override
    public UserDTO findUser(String u_id) {
        return userMapper.findUser(u_id);
    }

    public List<ReceiptDTO> getReceiptsByMonth(String yearMonth, String userId) {
        return receiptMapper.getReceiptsByMonth(yearMonth, userId);
    }

    public List<ItemDTO> findItemsByDate(int day, List<ReceiptDTO> receipts) {
        List<ItemDTO> items = new ArrayList<>();

        for (ReceiptDTO receipt : receipts) {
            if (receipt.getR_date().getDayOfMonth() == day) {
                if (receipt.getItems() != null) {
                    items.addAll(receipt.getItems());
                }
            }
        }
        return items;
    }

    public void save(SaveDTO dto) {

        String names = String.join(",", dto.getItemNames());
        String prices = dto.getItemPrices().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String counts = dto.getItemCounts().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        // ★★ 반드시 이렇게 호출해야 한다!
        userMapper.insertSave(
    	    dto.getUserId(),
    	    dto.getS_shop(),
    	    dto.getS_adrr(),
    	    dto.getS_date().toString(),  // ← LocalDate 이면 toString() 필요
    	    names,
    	    prices,
    	    counts
    	);
    }
    
    public List<SaveDTO> getSavedReceipts(String userId) {

        List<SaveDTO> list = userMapper.getSavedReceipts(userId);

        for (SaveDTO dto : list) {

            dto.setItemNames(Arrays.asList(dto.getItems_name().split(",")));

            dto.setItemPrices(
                Arrays.stream(dto.getItems_price().split(","))
                    .map(Integer::valueOf)
                    .collect(Collectors.toList())
            );

            dto.setItemCounts(
                Arrays.stream(dto.getItems_count().split(","))
                    .map(Integer::valueOf)
                    .collect(Collectors.toList())
            );
        }

        return list;
    }
    
    

    public List<SaveDTO> getSavedReceiptsDate(String userId, String yearMonth) {
        return userMapper.getSavedReceiptsDate(userId, yearMonth);
    }
    
    public List<Integer> buildCalendar(String yearMonth) {
        YearMonth ym = YearMonth.parse(yearMonth);
        int lastDay = ym.lengthOfMonth();

        List<Integer> days = new ArrayList<>();
        for (int i = 1; i <= lastDay; i++) days.add(i);

        return days;
    }
    
}
