package com.test.ocr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.test.ocr.DTO.ReceiptDTO;
import com.test.ocr.DTO.SaveDTO;

@Mapper
public interface ReceiptMapper {

	 List<ReceiptDTO> getReceiptsByMonth(@Param("yearMonth") String yearMonth,
             @Param("userId") String userId);
	 
	 void insertReceipt(SaveDTO dto);

	    int getLastInsertId();

	    void insertItems(
	        int receiptId,
	        String itemName,
	        int itemPrice,
	        int itemCount
	    );
}
