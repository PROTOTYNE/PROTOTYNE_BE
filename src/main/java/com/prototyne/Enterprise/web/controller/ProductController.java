package com.prototyne.Enterprise.web.controller;

import com.prototyne.Enterprise.service.ProductService.ProductService;
import com.prototyne.Enterprise.web.dto.ProductDTO;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.domain.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("enterpriseProductController")
@RequiredArgsConstructor
@RequestMapping("/enterprise")
@Tag(name = "${swagger.tag.enterprise-product}")
public class ProductController {

    private final JwtManager jwtManager;
    private final ProductService productService;

    // 시제품 목록 조회
    @GetMapping("/products")
    @Operation(summary = "시제품 목록 조회 API - 인증 필수",
            description = "시제품 체험 목록 > 시제품 목록",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<List<ProductDTO.ProductResponse>> getProductsList(HttpServletRequest token) {
        String oauthToken = jwtManager.getToken(token);
        List<ProductDTO.ProductResponse> productList =  productService.getProducts(oauthToken);
        return ApiResponse.onSuccess(productList);
    }

    // 시제품 등록
    @PostMapping("/products")
    @Operation(summary = "시제품 등록 API - 인증 필수",
            description = "시제품 등록(추가)",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<Long> create(HttpServletRequest token,
                                    @Valid @RequestBody ProductDTO.CreateProductRequest productRequest){
        String oauthToken = jwtManager.getToken(token);
        Product newProduct = productService.createProduct(oauthToken, productRequest);
        return ApiResponse.onSuccess(newProduct.getId());
    }
}
