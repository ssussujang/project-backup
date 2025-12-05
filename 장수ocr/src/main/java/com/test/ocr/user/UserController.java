package com.test.ocr.user;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.test.ocr.DTO.Role;
import com.test.ocr.DTO.SaveDTO;
import com.test.ocr.DTO.UserDTO;
import com.test.ocr.service.ServiceIMP;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/userLogin")  // 로그인 페이지 화면 열기
    public String loginPage() {
        return "userLogin";   // ← 로그인 HTML 파일 이름
    }

    @Autowired
    private ServiceIMP usi;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/signup")
    public String gosignup(Model model) {
        model.addAttribute("userdto", new UserDTO());
        return "signupPage";
    }
    
  
    

    @PostMapping("/register.reg")
    public String register(
            @Valid @ModelAttribute("userdto") UserDTO u,
            Errors errors, Model model) {

        u.setU_pw(passwordEncoder.encode(u.getU_pw()));
        u.setRole(Role.ROLE_USER);

        if (errors.hasErrors()) {
            return "signupPage";
        }

        usi.signup(u);
        return "home";
    }
    
    /** "5500,4500원" 같은 문자열을 개별 가격으로 나누어 합계 구하기 */
    private int sumPrices(String pricesStr) {
        if (pricesStr == null || pricesStr.isBlank()) return 0;

        int sum = 0;

        // 1) 콤마 기준으로 나누기
        String[] parts = pricesStr.split(",");

        for (String part : parts) {
            // 2) 숫자만 남기기 (원, 공백 등 제거)
            String onlyNumber = part.replaceAll("[^0-9]", "").trim();

            if (onlyNumber.isBlank()) continue;

            try {
                sum += Integer.parseInt(onlyNumber);
            } catch (NumberFormatException e) {
                // 파싱 안 되는 건 무시 (필요하면 로그)
            }
        }

        return sum;
    }
    
    
    @GetMapping("/mypage")
    public String mypage(
            @RequestParam(value = "yearMonth", required = false) String yearMonth,
            @RequestParam(value = "day", required = false) Integer day,
            Model model, Principal principal) {

        String userId = principal.getName();

        // 기본값: 현재 년-월
        if (yearMonth == null) {
            yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }
        
        // ========================
        // 0. 이번 달 / 이전 달 계산
        // ========================
        YearMonth ym = YearMonth.parse(yearMonth);             // 이번 달
        YearMonth prevYm = ym.minusMonths(1);                  // 이전 달

        String prevYearMonth = prevYm.format(DateTimeFormatter.ofPattern("yyyy-MM"));

        // 1. 해당 월의 모든 영수증 불러오기 (day 조건 없이)
        List<SaveDTO> receipts = usi.getSavedReceiptsDate(userId, yearMonth);
        
        // 👉 1-1. 이번 달 총합 계산
        int monthTotal = 0;
        
        for (SaveDTO s : receipts) {
            String priceStr = s.getItems_price();   // 문자열 가격
           

            monthTotal += sumPrices(priceStr);
        }
        
        // ========================
        // 2. 이전 달 영수증 + 합계
        // ========================
        List<SaveDTO> prevReceipts = usi.getSavedReceiptsDate(userId, prevYearMonth);

        int prevMonthTotal = 0;
        for (SaveDTO s : prevReceipts) {
            String priceStr = s.getItems_price();

            prevMonthTotal += sumPrices(priceStr);
        }
        

        // 2. 날짜별로 grouping
        Map<Integer, List<SaveDTO>> grouped = new HashMap<>();

        for (SaveDTO s : receipts) {

            if (s.getS_date() == null) continue;  // 날짜 없는 데이터 skip

            int d = s.getS_date().getDayOfMonth();

            grouped.computeIfAbsent(d, k -> new ArrayList<>()).add(s);
        }

        // 3. 달력 생성
        List<Integer> calendar = usi.buildCalendar(yearMonth);

        model.addAttribute("yearMonth", yearMonth);
        model.addAttribute("grouped", grouped);
        model.addAttribute("calendar", calendar);
        
        model.addAttribute("monthTotal", monthTotal);   // 👉 합계도 추가
        model.addAttribute("prevMonthTotal", prevMonthTotal); // 지난 달 합계
        model.addAttribute("diffTotal", monthTotal - prevMonthTotal); // 차이 (이번 - 지난)
        
        // ✅ 선택한 날짜 있으면 그 날의 리스트 뽑아서 따로 넘김
        if (day != null) {
            model.addAttribute("selectedDay", day);
            List<SaveDTO> selectedReceipts = grouped.getOrDefault(day, Collections.emptyList());
            model.addAttribute("selectedReceipts", selectedReceipts);
        } // ✅ 선택한 날짜 있으면 그 날의 리스트 뽑아서 따로 넘김
        if (day != null) {
            model.addAttribute("selectedDay", day);
            List<SaveDTO> selectedReceipts = grouped.getOrDefault(day, Collections.emptyList());
            model.addAttribute("selectedReceipts", selectedReceipts);
        }

        return "info";
    }
    
  
    @PostMapping("/signout")
    public String signout() {
    	return "signout";
    }
    
    @PostMapping("/modify")
    public String modify() {
  
    	return "modify";
    	
    }
    
    
}

