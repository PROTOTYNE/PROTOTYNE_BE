package com.prototyne.Users.service.DeliveryService;

import com.prototyne.Users.converter.DeliveryConverter;
import com.prototyne.Users.web.dto.DeliveryDto;
import com.prototyne.apiPayload.code.status.ErrorStatus;
import com.prototyne.apiPayload.exception.handler.TempHandler;
import com.prototyne.config.JwtManager;
import com.prototyne.domain.User;
import com.prototyne.domain.mapping.DeliveryAddress;
import com.prototyne.repository.DeliveryAddressRepository;
import com.prototyne.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.prototyne.Users.converter.DeliveryConverter.toDeliveryInfo;
import static com.prototyne.Users.converter.DeliveryConverter.toNewDeliveryInfo;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {
    private final UserRepository userRepository;
    private final JwtManager jwtManager;
    private final DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    public DeliveryServiceImpl(JwtManager jwtManager, UserRepository userRepository, DeliveryAddressRepository deliveryAddressRepository) {
        this.userRepository = userRepository;
        this.jwtManager = jwtManager;
        this.deliveryAddressRepository = deliveryAddressRepository;
    }

    @Override
    @Transactional
    public DeliveryDto.deliveryInfoResponse addNewDeliveryAddress(String accessToken, DeliveryDto req){
        Long id = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(id).orElseThrow(()-> new TempHandler(ErrorStatus.LOGIN_ERROR_ID));

        List<DeliveryAddress> userAddressList = deliveryAddressRepository.findByUser(user);
        if(userAddressList.size() >= 10)
            // 유저 당 배송지는 최대 10개 등록
            throw new TempHandler(ErrorStatus.DELIVERY_LIST_SIZE_EXCEEDED);

        if(req.isDefault()){
            // 현재 새롭게 등록되는 배송지가 기본 배송지로 등록된다면, 기존 기본 배송지로 등록되었던 배송지는 false로 바꾸어줌
            resetDefaultDelivery(user);
        }

        DeliveryAddress newAddr = toNewDeliveryInfo(user, req); // 새롭게 배송 정보 엔티티 생성
        if(userAddressList.isEmpty())
            newAddr.setDefault(true);

        deliveryAddressRepository.save(newAddr);
        return toDeliveryInfo(newAddr);
    }

    @Override
    public List<DeliveryDto.deliveryInfoResponse> getMyDeliveryList(String accessToken) {
        Long id = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(id).orElseThrow(() -> new TempHandler(ErrorStatus.LOGIN_ERROR_ID));

        List<DeliveryAddress> deliveryAddressList = deliveryAddressRepository.findByUser(user);

        return deliveryAddressList.stream()
                .map(DeliveryConverter::toDeliveryInfo)
                .toList();
    }

    @Override
    public DeliveryDto.deliveryInfoResponse getOneDeliveryInfo(String accessToken, Long deliveryId){
        Long id = jwtManager.validateJwt(accessToken);
        User user = userRepository.findById(id).orElseThrow(()-> new TempHandler(ErrorStatus.LOGIN_ERROR_ID));
        DeliveryAddress delivery = deliveryAddressRepository.findByUserAndId(user, deliveryId)
                .orElseThrow(()-> new TempHandler(ErrorStatus.DELIVERY_NOT_FOUND));

        return toDeliveryInfo(delivery);
    }

    private void resetDefaultDelivery(User user) {
        List<DeliveryAddress> userAddressList = deliveryAddressRepository.findByUser(user);
        userAddressList.stream()
                .filter(DeliveryAddress::isDefault)
                .forEach(address -> {
                    address.setDefault(false);
                    deliveryAddressRepository.save(address);
                });
    }

}
