package com.test.ocr.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.test.ocr.DTO.SaveDTO;
import com.test.ocr.mapper.ReceiptMapper;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@org.springframework.stereotype.Service
public class ReceiptService {

	@Value("${clova.secret}")
    private String secretKey;

    @Value("${clova.url}")
    private String apiUrl;
    
    public JSONObject callClovaOCR(MultipartFile file) throws Exception {

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "file",
                        file.getOriginalFilename(),
                        RequestBody.create(MediaType.parse("image/jpeg"), file.getBytes())
                )
                .build();

        Request request = new Request.Builder()
                .url(apiUrl)
                .header("X-OCR-SECRET", secretKey)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String result = response.body().string();

        return new JSONObject(result);
    }
    
    
    @Autowired
    private ReceiptMapper mapper;

    public void saveReceipt(SaveDTO dto) {

        // 1) save 테이블에 메인 데이터 저장
        mapper.insertReceipt(dto);

        // 2) insert된 receipt의 PK 가져오기
        int receiptId = mapper.getLastInsertId();

        // 3) 상품들 저장
        for (int i = 0; i < dto.getItemNames().size(); i++) {
            mapper.insertItems(
                    receiptId,
                    dto.getItemNames().get(i),
                    dto.getItemPrices().get(i),
                    dto.getItemCounts().get(i)
            );
        }
    }
    
    
    
    
}

