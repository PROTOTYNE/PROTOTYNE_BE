package com.prototyne.Enterprise.service.ProductService;

import com.prototyne.Enterprise.converter.ProductConverter;
import com.prototyne.Enterprise.web.dto.ProductDTO;
import com.prototyne.Users.service.LoginService.JwtManager;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.domain.Enterprise;
import com.prototyne.domain.Product;
import com.prototyne.repository.EnterpriseRepository;
import com.prototyne.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final JwtManager jwtManager;
    private final ProductRepository productRepository;
    private final EnterpriseRepository enterpriseRepository;

    // 시제품 목록 조회
    @Override
    public List<ProductDTO.ProductResponse> getProducts(String accessToken) {
        Long enterpriseId = jwtManager.validateJwt(accessToken);

        List<Product> products = productRepository.findByEnterpriseId(enterpriseId);
        return  products.stream()
                .map(ProductConverter::toProductResponse) // 컨버터 메소드 호출
                .collect(Collectors.toList());
    }

    // 세제품 등록
    @Override
    public Product createProduct(String accessToken, ProductDTO.CreateProductRequest productRequest) {
        Long enterpriseId = jwtManager.validateJwt(accessToken);

        // 기업 조회
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.ENTERPRISE_ERROR_ID));

        Product newProduct = ProductConverter.toProduct(productRequest, enterprise);
        return productRepository.save(newProduct);
    }

    // 시제품 삭제
    @Override
    public void deleteProduct(String accessToken, Long productId) {
        Long enterpriseId = jwtManager.validateJwt(accessToken);

        // 시제품 객체 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.PRODUCT_ERROR_ID));

        // 해당 시제품을 기업이 가졌는지 확인
        if (!product.getEnterprise().getId().equals(enterpriseId)) {
            throw new TempHandler(ErrorStatus.ENTERPRISE_ERROR_PRODUCT);
        }

        // 해당 시제품에 연결된 이벤트가 있는지 확인
        if (!product.getEventList().isEmpty()) {
            throw new TempHandler(ErrorStatus.PRODUCT_ERROR_EVENTLIST);
        }

        // 제품 삭제
        productRepository.delete(product);
    }


}
