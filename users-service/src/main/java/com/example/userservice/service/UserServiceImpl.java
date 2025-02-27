package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl  implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final Environment env;
    private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);

        UserDto returnUserDto = modelMapper.map(userEntity, UserDto.class);
        return returnUserDto;
    }

    @Override
    public UserDto getUserBySuerId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        // Using as RestTemplate
//        String orderUrl = String.format(env.getProperty("order_service.url"), userId);
//        ResponseEntity<List<ResponseOrder>> orderListResponse =
//                restTemplate.exchange(orderUrl,
//                        HttpMethod.GET,
//                        null,
//                        new ParameterizedTypeReference<List<ResponseOrder>>() { });

        // Using a feign client
        // Reign Exception handling
//        List<ResponseOrder> orderList = null;
//        try{
//            orderList = orderServiceClient.getOrders(userId);
//
//        } catch (FeignException ex) {
//            log.error(ex.getMessage());
//        }
        List<ResponseOrder> orderList = orderServiceClient.getOrders(userId);
        userDto.setOrders(orderList);

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(entity -> new ModelMapper().map(entity, UserDto.class))
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(), true, true, true, true, new ArrayList<>());
    }
}
