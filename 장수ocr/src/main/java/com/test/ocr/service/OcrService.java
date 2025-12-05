package com.test.ocr.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.test.ocr.DTO.ItemDTO;
import com.test.ocr.DTO.ReceiptDTO;

@Service
public class OcrService {
	
	public JSONObject callClovaOCR(MultipartFile file) {
		
		try {

            String url = "ocr주소";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.set("X-OCR-SECRET", "시크릿 키");

            JSONObject body = new JSONObject();
            body.put("version", "V2");
            body.put("requestId", UUID.randomUUID().toString());
            body.put("timestamp", System.currentTimeMillis());

            JSONObject image = new JSONObject();
            image.put("format", "jpg");
            image.put("name", "receipt");
            image.put("data", Base64.getEncoder().encodeToString(file.getBytes()));

            JSONArray images = new JSONArray();
            images.put(image);

            body.put("images", images);
         // ⭐ request는 여기서 생성해야 함!
            HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response =
                    restTemplate.postForEntity(url, request, String.class);

            // ⭐ JSON 확인용 출력
            System.out.println("📌 OCR 응답 JSON = " + response.getBody());
            System.out.println("============================");

            return new JSONObject(response.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		
		
	}
	
	public ReceiptDTO parseReceipt(JSONObject json) {
		
		if (json == null)
			return new ReceiptDTO();
		
		ReceiptDTO dto = new ReceiptDTO();
		
		try {
			JSONObject result = json
					.getJSONArray("images")
					.getJSONObject(0)
					.getJSONObject("receipt")
					.getJSONObject("result");
			
			
			 // ---------- 가게 이름 ----------
	        try {
	            dto.setR_shop(
	                result.getJSONObject("storeInfo")
	                      .getJSONObject("name")
	                      .getString("text")
	            );
	        } catch (Exception e) {
	            dto.setR_shop(null);
	        }

	        // ---------- 주소 ----------
	        try {
	        	JSONArray addrArr = result
	                    .getJSONObject("storeInfo")
	                    .optJSONArray("addresses");
	        	
	        	
	        	if (addrArr != null && addrArr.length() > 0) {
	                JSONObject addrObj = addrArr.getJSONObject(0);

	                // text 또는 formatted.value 둘 중 하나 선택
	                String addr = addrObj.optString("text", "");

	                if (addr.isEmpty())
	                    addr = addrObj.optJSONObject("formatted").optString("value", "");

	                dto.setR_adrr(addr);
	            } else {
	                dto.setR_adrr(null);
	            }
	        } catch (Exception e) {
	            dto.setR_adrr(null);
	        }

	        // ---------- 결제 날짜 ----------
	        try {
	            String dateStr =
	                    result.getJSONObject("paymentInfo")
	                          .getJSONObject("date")
	                          .getString("text");   // OCR에서 받은 날짜 (문자열)

	            // 문자열 → LocalDate 변환
	            LocalDate date = LocalDate.parse(dateStr);

	            dto.setR_date(date);
	        } catch (Exception e) {
	            dto.setR_date(null);
	        }
	     // ---------- 총 금액 ----------
	        try {
	            // 1️⃣ 가장 먼저 totalPrice.price.text 시도
	            String totalStr = result.getJSONObject("totalPrice")
	                    .getJSONObject("price")
	                    .getString("text")
	                    .replaceAll(",", "")
	                    .replaceAll("[^0-9]", "");
	            	

	            int total = Integer.parseInt(totalStr);

	            // 총액이 너무 작으면 (예: 300원) → 잘못된 인식이라고 판단
	            if (total < 1000) {
	                // 2️⃣ subTotal.discountPrice 등에서 다시 찾아보기
	                try {
	                    JSONArray subTotalArr = result.getJSONArray("subTotal");
	                    for (int i = 0; i < subTotalArr.length(); i++) {
	                        JSONObject subObj = subTotalArr.getJSONObject(i);

	                        if (subObj.has("price")) {
	                            String realTotal = subObj.getJSONObject("price")
	                                    .getString("text")
	                                    .replaceAll("[^0-9]", "");

	                            total = Integer.parseInt(realTotal);
	                            break;
	                        }
	                    }
	                } catch (Exception ignore) {}
	            }

	            dto.setR_total(total);

	        } catch (Exception e) {
	            dto.setR_total(0);
	        }

	        // ---------- 품목 리스트 ----------
	        List<ItemDTO> itemList = new ArrayList<>();

	        try {
	        	JSONArray subResults = result.optJSONArray("subResults");
	            if (subResults != null && subResults.length() > 0) {
	            	
	            	JSONObject firstBlock = subResults.getJSONObject(0);
	            	
	            	JSONArray items = firstBlock.optJSONArray("items");
	            	
	            	
	            	
	            	if (items != null) {
	            		
	                    for (int i = 0; i < items.length(); i++) {

	                        JSONObject itemObj = items.getJSONObject(i);
	                        ItemDTO item = new ItemDTO();
	            			
	            			// 상품명
	            			item.setI_name(itemObj.optJSONObject("name").optString("text", ""));
	            			
	            			// 가격
	            			
	            			 String priceStr = itemObj.optJSONObject("price")
                                     .optJSONObject("price")
                                     .optString("text", "0")
                                     .replaceAll("[^0-9]", "");
	            			 item.setI_price(Integer.parseInt(priceStr));
	            			
	            			// 수량
	            			
	            			String countStr = itemObj.optJSONObject("count")
                                    .optString("text", "1")
                                    .replaceAll("[^0-9]", "");
	            			item.setI_count(Integer.parseInt(countStr));
	            			
	            			// 카테고리 자동 분류
	            			item.setI_category("미분류");

	                        itemList.add(item);
	            		} 
	            	}
	            }

	        } catch (Exception e) {
	            // subResults가 없을 수 있음
	        	e.printStackTrace();
	        }

	        dto.setItems(itemList);
	       

	    } catch (Exception e) {
	        System.out.println("OCR 파싱 오류: " + e.getMessage());
	    }

	    return dto;
	
			
		
		
	}
	
}
