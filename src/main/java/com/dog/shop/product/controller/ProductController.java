package com.dog.shop.product.controller;

import com.dog.shop.api.service.KakaoApiService;
import com.dog.shop.domain.User;
import com.dog.shop.domain.cart.Cart;
import com.dog.shop.domain.popularSearchedKeyword.resDTO.PopularSearchedKeywordResDTO;
import com.dog.shop.dto.CartItemReqDto;
import com.dog.shop.dto.CartResDto;
import com.dog.shop.dto.MultiFormDto;
import com.dog.shop.help.JwtHelper;
import com.dog.shop.product.dto.ProductReqDTO;
import com.dog.shop.product.dto.ProductReqForm;
import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.product.service.CartService;
import com.dog.shop.product.service.ProductService;
import com.dog.shop.repository.CartRepository;
import com.dog.shop.repository.UserRepository;
import com.dog.shop.service.CartItemService;
import com.dog.shop.service.popularKeyword.PopularKeywordService;
import com.dog.shop.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final PopularKeywordService popularKeywordService;


    private final CartService cartService;
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final CartRepository cartRepository;


    @GetMapping("/list")
    public ModelAndView listProducts(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 6);
        Page<ProductResDTO> products = productService.findAllProducts(pageable);
        List<PopularSearchedKeywordResDTO> popularSearchedKeywordResDTOList = popularKeywordService.getResult();

        int totalPages = products.getTotalPages();
        int currentPage = pageable.getPageNumber() + 1;
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());

        model.addAttribute("currentPage", pageable.getPageNumber() + 1); // 페이지 번호는 0부터 시작하므로 1을 더해줍니다.
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("products", products);
        model.addAttribute("pageNumbers", pageNumbers);

        model.addAttribute("keywords", popularSearchedKeywordResDTOList);
        return new ModelAndView("product-list", "products", products);
    }
    @GetMapping("/signup")
    public String showSignUpForm(ProductReqDTO product) {
        return "create-product";
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<String> createProduct (
            @RequestParam("product") String productJson,
            @RequestParam(name = "mainImage", required = false) MultipartFile file) throws JsonProcessingException {

        // ObjectMapper를 사용하여 JSON 문자열을 DTO 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        ProductReqDTO productReqDTO = objectMapper.readValue(productJson, ProductReqDTO.class);
        System.out.println(productReqDTO);

        if (file != null && !file.isEmpty()) {
            String imageUrl = productService.saveImage(file);
            productReqDTO.setMainImageUrl(imageUrl);
        }

        productService.saveProduct(productReqDTO);
        return ResponseEntity.ok("이미지 저장 성공");
    }

    @GetMapping("/{id}")
    public String productDetail(@PathVariable("id") Long id, Model model, CartItemReqDto cartItemReqDto, HttpServletRequest request) {
        String token = jwtHelper.extractTokenFromCookies(request);
        Optional<User> userOpt = jwtHelper.extractUserFromToken(token);
        Long userId = userOpt.get().getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        // 처음 계정 생성시 Cart(장바구니가 없을때) 장바구니를 눌렀을때 장바구니를 새로 생성하고 장바구니로 들어가기
        // 이부분은 Cart 테이블과 user_id 를 초기화하면 user_id와 id가 동일하게되면 정상작동
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // product_id 가져오는부분
        ProductResDTO productResDTO = productService.getProductById(id);

        CartResDto cartResDto = cartService.getCartById(userId);
        MultiFormDto multiFormDto = new MultiFormDto();
        //CartItemReqDto 객체 넣어주기 값은디폴트값들어있음
        multiFormDto.setCartItemReqDto(cartItemReqDto);
        multiFormDto.setCartResDto(cartResDto);
        // product_id만 담겨있는 ProductResDTO값 가져오기 부트가 자동으로 외래키를 매핑함
        multiFormDto.setProductResDTO(productResDTO);

        model.addAttribute("multiFormDto", multiFormDto);

        model.addAttribute("product", productResDTO);
        return "product-detail-list";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model){
        ProductResDTO productResDTO = productService.getProductById(id);
        model.addAttribute("product",productResDTO);
        return "update-product";
    }



    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") long id) {
        productService.deleteProduct(id);
        return "redirect:/products/list";
    }
}