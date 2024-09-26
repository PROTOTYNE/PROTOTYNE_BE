package com.prototyne.Enterprise.service.ProductService;

import com.prototyne.Enterprise.converter.ProductConverter;
import com.prototyne.Enterprise.web.dto.ProductDTO;
import com.prototyne.apiPayload.config.JwtManager;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.aws.s3.AmazonS3Manager;
import com.prototyne.domain.Enterprise;
import com.prototyne.domain.Product;
import com.prototyne.domain.ProductImage;
import com.prototyne.repository.EnterpriseRepository;
import com.prototyne.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final JwtManager jwtManager;
    private final ProductRepository productRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final AmazonS3Manager s3Manager;

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
    public Long createProduct(String accessToken,
                                 ProductDTO.CreateProductRequest productRequest,
                                 List<MultipartFile> images) {
        Long enterpriseId = jwtManager.validateJwt(accessToken);

        // 기업 조회
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.ENTERPRISE_ERROR_ID));

        // null일 경우 빈 리스트로 대체
        if (images == null) images = new ArrayList<>();

        // 이미지 3장 이상일 경우 에러
        if (images.size() > 3)
            throw new TempHandler(ErrorStatus.IMAGE_LIMIT_EXCEEDED);  // 새로운 에러 코드 정의 필요

        // S3에 이미지 업로드
        List<String> imageUrls = s3Manager.uploadFiles("product", images);
        Product newProduct = ProductConverter.toProduct(productRequest, enterprise, imageUrls);

        // 시제품 저장 및 id 반환
        Product product = productRepository.save(newProduct);
        return product.getId();
    }

    // 시제품 삭제
    @Override
    public void deleteProduct(String accessToken, Long productId) {
        Long enterpriseId = jwtManager.validateJwt(accessToken);

        // 시제품 객체 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new TempHandler(ErrorStatus.PRODUCT_ERROR_ID));

        // 해당 시제품을 기업이 가졌는지 확인
        if (!product.getEnterprise().getId().equals(enterpriseId))
            throw new TempHandler(ErrorStatus.ENTERPRISE_ERROR_PRODUCT);

        // 해당 시제품에 연결된 이벤트가 있는지 확인
        if (!product.getEventList().isEmpty())
            throw new TempHandler(ErrorStatus.PRODUCT_ERROR_EVENTLIST);

        // 시제품의 이미지가 있을 경우, S3에서 이미지 제거
        if (product.getProductImageList() != null && !product.getProductImageList().isEmpty()) {
            List<String> imagefiles = product.getProductImageList().stream()
                    .map(ProductImage::getImageUrl)
                    .collect(Collectors.toList());

            // S3에서 여러 이미지 삭제
            s3Manager.deleteFiles("product", imagefiles);
        }

        // 제품 삭제
        productRepository.delete(product);
    }
}
