package com.test.ocr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.test.ocr.DTO.SaveDTO;
import com.test.ocr.DTO.UserDTO;

@Mapper
public interface UserMapper {

	void signup(UserDTO u);

    UserDTO findUser(String u_id);


    void insertSave(
        @Param("userId") String userId,
        @Param("s_shop") String shop,
        @Param("s_adrr") String addr,
        @Param("s_date") String date,
        @Param("items_name") String names,
        @Param("items_price") String prices,
        @Param("items_count") String counts
    );
    
    List<SaveDTO> getSavedReceipts(@Param("userId") String userId);
    
    
    List<SaveDTO> getSavedReceiptsDate(
        @Param("userId") String userId,
        @Param("yearMonth") String yearMonth
    );
	
}
