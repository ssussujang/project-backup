package com.test.ocr.receipt;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.test.ocr.DTO.DayDTO;
import com.test.ocr.DTO.ItemDTO;
import com.test.ocr.DTO.ReceiptDTO;
import com.test.ocr.DTO.SaveDTO;
import com.test.ocr.service.OcrService;
import com.test.ocr.service.ServiceIMP;

@Controller
@RequestMapping("/receipt")
public class ReceiptController {
	
	@Autowired
	private OcrService ocrService;
	
	@Autowired
	private ServiceIMP service;
	
	
	
	
	@PostMapping("/uploadReceipt")
	public String uploadReceipt(@RequestParam("receipt") MultipartFile file, Model model) {

	    System.out.println("파일 이름 : " + file.getOriginalFilename());

	    JSONObject json = ocrService.callClovaOCR(file);
	    ReceiptDTO receipt = ocrService.parseReceipt(json);

	    // ✔ OCR 결과 자체가 단일 영수증이므로 총합은 r_total 하나!
	    int totalPrice = receipt.getR_total();

	    model.addAttribute("receipt", receipt);
	    model.addAttribute("totalPrice", totalPrice); // ← 추가됨
	 

	    return "home";
	}
	
	@GetMapping("/writeReceipt")
	public String writeReceiptForm(Model model, Principal principal) {

	    // 1) 빈 receipt 객체 생성
	    ReceiptDTO receipt = new ReceiptDTO();
	    receipt.setR_shop("");   // 가게
	    receipt.setR_adrr("");   // 주소
	    receipt.setR_date(LocalDate.now());   // 날짜 (문자열이면 "" / LocalDate면 LocalDate.now() 등등)

	    // 2) 상품 정보도 "빈 줄" 하나 만들어서 넣기
	    List<ItemDTO> items = new ArrayList<>();

	    ItemDTO item = new ItemDTO();
	    item.setI_name("");     // 상품명
	    item.setI_price(0);  // 가격 (Integer면 null로 두면 input에는 빈칸으로 나옴)
	    item.setI_count(0);  // 수량
	    items.add(item);

	    receipt.setItems(items);

	    // 3) 모델에 넣기 (view에서 쓰는 이름이 지금 'receipt' 니까 그대로 맞춤)
	    model.addAttribute("receipt", receipt);

	    // 필요하면 yearMonth 같은 것도 같이 넣어줘도 됨
	    // model.addAttribute("yearMonth", ...);

	    // 4) 기존에 이 폼이 있는 html (home or info) 리턴
	    return "home";   // 현재 저 테이블이 있는 템플릿 이름으로 바꿔줘
	}

	
	
	
	@PostMapping("/mypage/data")
	public String loadMonthData(@RequestParam("yearMonth") String yearMonth, Model model, 
	                            Principal principal) {

	    String userId = principal.getName();   // 로그인한 유저 ID
	    List<ReceiptDTO> receipts = service.getReceiptsByMonth(yearMonth, userId);

	    model.addAttribute("receipts", receipts);
	    model.addAttribute("yearMonth", yearMonth);

	    return "info";
	}
	
	
	public List<List<DayDTO>> buildCalendar(String yearMonth, List<ReceiptDTO> receipts) {

	    YearMonth ym = YearMonth.parse(yearMonth);
	    int lastDay = ym.lengthOfMonth();
	    LocalDate firstDay = ym.atDay(1);

	    int firstWeek = firstDay.getDayOfWeek().getValue();  // 월=1 ~ 일=7

	    List<List<DayDTO>> calendar = new ArrayList<>();
	    List<DayDTO> week = new ArrayList<>(Collections.nCopies(7, null));

	    int dayIndex = firstWeek % 7;
	    for (int day = 1; day <= lastDay; day++) {

	    	DayDTO daydto = new DayDTO(); 
	        daydto.setDay(day);
	        daydto.setItems(service.findItemsByDate(dayIndex, receipts));

	        week.set(dayIndex, daydto);

	        dayIndex++;
	        if (dayIndex == 7) {
	            calendar.add(week);
	            week = new ArrayList<>(Collections.nCopies(7, null));
	            dayIndex = 0;
	        }
	    }

	    calendar.add(week);
	    return calendar;
	}

	@PostMapping("/save")
	public String saveReceipt(@ModelAttribute SaveDTO dto, Principal principal) {

		dto.setUserId(principal.getName());   // 현재 로그인한 사용자 ID

	    service.save(dto);   // 저장 호출
	    
	    return "home";  // 완료 후 홈으로 이동
	}
	
	
}
