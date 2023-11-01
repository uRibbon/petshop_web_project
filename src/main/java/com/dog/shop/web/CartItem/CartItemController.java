package com.dog.shop.web.CartItem;

import com.dog.shop.api.service.KakaoApiService;
import com.dog.shop.domain.User;
import com.dog.shop.domain.cart.Cart;
import com.dog.shop.domain.cart.CartItem;
import com.dog.shop.domain.product.Product;
import com.dog.shop.dto.*;
import com.dog.shop.help.JwtHelper;
import com.dog.shop.product.dto.ProductResDTO;
import com.dog.shop.product.service.CartService;
import com.dog.shop.product.service.ProductService;
import com.dog.shop.repository.CartItemRepository;
import com.dog.shop.repository.CartRepository;
import com.dog.shop.repository.UserRepository;
import com.dog.shop.service.AuthService;
import com.dog.shop.service.CartItemService;
import com.dog.shop.utils.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cartItem") // C -> c로 변경
@RequiredArgsConstructor
public class CartItemController {

    // 장바구니 목록 보여주기
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final CartService cartService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final JwtHelper jwtHelper;
    private final CartRepository cartRepository;
    private final KakaoApiService kakaoApiService;
    private final CartItemRepository cartItemRepository;
    @GetMapping("/getList")
    public ModelAndView getList() {
        List<CartItemResDto> cartItems = cartItemService.getCartItems();
        return new ModelAndView("cartItem", "cartItems", cartItems);
    }

    // 나만의 장바구니
    @GetMapping("/getCartItem")
    public ModelAndView getCartItem(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 사용자가 로그인하지 않은 경우, 로그인 페이지로 리다이렉트 로그인 토큰정보로 비로그인 장바구니 선택시 에러처리
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken)  {
            response.sendRedirect("/login");
            return null;
        }

        String token = jwtHelper.extractTokenFromCookies(request);
        Optional<User> userOpt = jwtHelper.extractUserFromToken(token);
        Long userId = userOpt.get().getId();
        List<CartItemResDto> cartItems = cartItemService.getUserCartItems(userId);

        String address = userOpt.get().getAddress();

        int fee = kakaoApiService.calculateDistance(address);

        ModelAndView mav = new ModelAndView("cartItem");
        mav.addObject("cartItems", cartItems);
        mav.addObject("fee", fee);

        return mav;
    }
    /*@GetMapping("/getCartItem")
    public ModelAndView getCartItem(HttpServletRequest request) {
        String token = jwtHelper.extractTokenFromCookies(request);
        Optional<User> userOpt = jwtHelper.extractUserFromToken(token);
        Long userId = userOpt.get().getId();
        List<CartItemResDto> cartItems = cartItemService.getUserCartItems(userId);

        String address = userOpt.get().getAddress();

        int fee = kakaoApiService.calculateDistance(address);

        // TODO 장바구니가 null일 경우도 고려해야함
        return new ModelAndView("cartItem", "cartItems", cartItems);
    }*/

    @PostMapping("/addcartItem")
    public String addCartItem(@ModelAttribute("multiFormDto") MultiFormDto multiFormDto){
        CartItemReqDto cartItemReqDto = multiFormDto.getCartItemReqDto();
        ProductResDTO productResDTO = multiFormDto.getProductResDTO();
        CartResDto cartResDto = multiFormDto.getCartResDto();

        // 여기에서 서비스 클래스를 호출하여 데이터를 저장
        cartItemService.saveCartItem(cartItemReqDto,productResDTO,cartResDto);

        return "redirect:/cartItem/getCartItem"; // C -> c로 변경
    }

    private String getJwtTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT-TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 상품의 정보를 가져오면서 장바구니 등록설정창 들어가기
    /*@GetMapping("/signup/{id}")
    public String showSignUpForm(@PathVariable Long id, Model model, CartItemReqDto cartItemReqDto, HttpServletRequest request) {
        // 로그인 정보를 바탕으로 토큰에 등록되어있는 id를받아오기
        String token = jwtHelper.extractTokenFromCookies(request);
        Optional<User> userOpt = jwtHelper.extractUserFromToken(token);
        Long userId = userOpt.get().getId();
        // 처음 계정 생성시 Cart(장바구니가 없을시에 User Entity 를먼저 가져오는로직)
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

        return "add-cartItem";
    }*/ // 장바구니 등록


    @GetMapping("/signup/{id}")
    public String productToCart(@PathVariable Long id, Model model, HttpServletRequest request) {

        // 로그인 정보를 바탕으로 토큰에 등록되어있는 id를받아오기
        String token = jwtHelper.extractTokenFromCookies(request);
        Optional<User> userOpt = jwtHelper.extractUserFromToken(token);
        Long userId = userOpt.get().getId();
        // 처음 계정 생성시 Cart(장바구니가 없을시에 User Entity 를먼저 가져오는로직)
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
        Product product = productService.fetchProductById(id);
        int stock = 1; // 수량

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(stock);
        cartItem.setCart(cart);
        cartItem.setUnitPrice(product.getPrice());
        cartItem.setSubTotal(product.getPrice() * stock);

        cartItemRepository.save(cartItem);

        String address = userOpt.get().getAddress();

        int fee = kakaoApiService.calculateDistance(address);


        model.addAttribute("cart", cart);
        model.addAttribute("fee", fee);


        return "test-test3";
    }









// 장바구니 수정폼가기
    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model){
        CartItemResDto cartItemResDto = cartItemService.getCartItemById(id);
    model.addAttribute("cartItem",cartItemResDto);
    return "update-cartItem";
}

// 장바구니 수정 하기
    @PostMapping("/update/{id}")
    public String updateCartItem(@PathVariable("id") long id, @Valid CartItemReqDto cartItem,
                             BindingResult result, Model model) { //id를써야해서 폼을씀
        if (result.hasErrors()) {
            System.out.println(">>> hasErros cartItem "  + cartItem);
            model.addAttribute("cartItem",cartItem);
            return "update-cartItem";
        }
//        cartItemService.updateCustomerForm(cartItem);
        cartItemService.updateCartItem(id,cartItem);
        return "redirect:/cartItem/getList"; // C -> c
    }

    // 장바구니 삭제
    @GetMapping("/delete/{id}")
    public String deleteCartItem(@PathVariable("id") long id) {
        cartItemService.deleteCartItem(id);
        return "redirect:/cartItem/getList"; // C -> c
    }
}
/*if (token != null) {
            String email = jwtUtil.getEmailFromToken(token);
            if (email != null) {
                // TODO 에러처리 필요
                User user = userRepository.findByEmail(email).orElseThrow();
                // 이후 로직 처리...
                Long userId = user.getId();

            }
        }*/