package com.prototyne.Enterprise.web.controller;

import com.prototyne.Enterprise.service.ProductService.ProductService;
import com.prototyne.Enterprise.web.dto.ProductDTO;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.apiPayload.ApiResponse;
import com.prototyne.domain.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "시제품 등록 API - 인증 필수",
            description = "시제품 등록(추가)" + """
                    
                    1. 시제품 명 (productName) - 공백 시 에러
                    2. 설명 (contents - 공백 시 에러
                    3. 티켓 갯수 (reqTickets)
                    4. 추가 안내사항 (notes) - 공백 시 에러
                    5. 카테고리 (category)
                    6. 질문 목록 (1~5)
                    7. imageFiles : 제품 사진 (최대 3장)
                    
                    요청 성공 시, 시제품 아이디(product_id) 반환""",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<Long> createProduct(HttpServletRequest token,
                                    @Valid @RequestPart("productRequest") ProductDTO.CreateProductRequest productRequest,
                                           @RequestPart(value = "imageFiles", required = false) List<MultipartFile> images){
        String oauthToken = jwtManager.getToken(token);
        Long productId = productService.createProduct(oauthToken, productRequest, images);
        return ApiResponse.onSuccess(productId);
    }

    // 시제품 삭제
    @DeleteMapping("/products/{productId}")
    @Operation(summary = "시제품 등록 API - 인증 필수",
            description = """
                    시제품 삭제 \n
                    productId 입력 \n
                    시제품(product)에 연결된 체험(event)이 없어야 삭제 가능""",
            security = {@SecurityRequirement(name = "session-token")})
    public ApiResponse<String> deleteProduct(HttpServletRequest token, @PathVariable Long productId) {
        String oauthToken = jwtManager.getToken(token);
        productService.deleteProduct(oauthToken, productId);
        return ApiResponse.onSuccess("삭제 성공");
    }
}
