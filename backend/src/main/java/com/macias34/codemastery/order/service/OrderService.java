package com.macias34.codemastery.order.service;

import com.macias34.codemastery.course.entity.CourseEntity;
import com.macias34.codemastery.course.repository.CourseRepository;
import com.macias34.codemastery.exception.ResourceNotFoundException;
import com.macias34.codemastery.order.dto.CreateOrderRequestDto;
import com.macias34.codemastery.order.dto.OrderDto;
import com.macias34.codemastery.order.entity.OrderEntity;
import com.macias34.codemastery.order.entity.OrderStatus;
import com.macias34.codemastery.order.mapper.OrderMapper;
import com.macias34.codemastery.order.repository.OrderRepository;
import com.macias34.codemastery.user.entity.UserEntity;
import com.macias34.codemastery.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    public void createOrder(CreateOrderRequestDto dto){
        CourseEntity courseEntity = courseRepository.findById(dto.getCourseId()).orElseThrow(ResourceNotFoundException::new);
        courseEntity.setParticipantsCount(courseEntity.getParticipantsCount() + 1);
        UserEntity userEntity = userRepository.findById(dto.getUserId()).orElseThrow(ResourceNotFoundException::new);

        Set<CourseEntity> usersCourses = userEntity.getCourses();
        usersCourses.add(courseEntity);

        userEntity.setCourses(usersCourses);

        OrderEntity order = OrderEntity.builder()
                .courses(List.of(courseEntity))
                .status(OrderStatus.COMPLETED)
                .price(courseEntity.getPrice())
                .user(userEntity).build();

        courseRepository.save(courseEntity);
        userRepository.save(userEntity);
        orderRepository.save(order);
    }

    public OrderDto getOrderById(Integer orderId){
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(ResourceNotFoundException::new);
        return this.orderMapper.mapEntityToDto(order);
    }

    public List<OrderDto> getOrders(){
        List<OrderEntity> orders = orderRepository.findAll();
        return orders.stream().map(orderMapper::mapEntityToDto).toList();
    }
}
