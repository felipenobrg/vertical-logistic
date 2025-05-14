package com.challenge.verticallogistics.service.impl;

import com.challenge.verticallogistics.dto.response.OrderResponse;
import com.challenge.verticallogistics.dto.response.ProductResponse;
import com.challenge.verticallogistics.dto.response.UserOrdersResponse;
import com.challenge.verticallogistics.exception.OrderProcessingException;
import com.challenge.verticallogistics.model.Order;
import com.challenge.verticallogistics.model.OrderLine;
import com.challenge.verticallogistics.model.Product;
import com.challenge.verticallogistics.model.User;
import com.challenge.verticallogistics.parser.OrderFileParser;
import com.challenge.verticallogistics.repository.OrderRepository;
import com.challenge.verticallogistics.service.OrderProcessingService;
import com.challenge.verticallogistics.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderProcessingServiceImpl implements OrderProcessingService {

    private final OrderFileParser orderFileParser;
    private final OrderRepository orderRepository;

    @Override
    public List<UserOrdersResponse> processOrderFile(MultipartFile file) {
        try {
            List<OrderLine> orderLines = orderFileParser.parseOrderFile(file.getInputStream());
            List<User> users = normalizeOrderLines(orderLines);
            orderRepository.clear();
            orderRepository.saveAll(users);
            return mapToUserOrdersResponseList(users);
        } catch (IOException e) {
            throw new OrderProcessingException("Failed to process file", e);
        }
    }

    @Override
    public List<UserOrdersResponse> getAllOrders() {
        return mapToUserOrdersResponseList(orderRepository.findAll());
    }

    @Override
    public List<UserOrdersResponse> getOrdersByOrderId(Long orderId) {
        return mapToUserOrdersResponseList(orderRepository.findByOrderId(orderId));
    }

    @Override
    public List<UserOrdersResponse> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        return mapToUserOrdersResponseList(orderRepository.findByDateRange(startDate, endDate));
    }

    private List<User> normalizeOrderLines(List<OrderLine> orderLines) {
        // Group by userId
        Map<String, Map<String, List<OrderLine>>> userOrdersMap = orderLines.stream()
                .collect(Collectors.groupingBy(OrderLine::getUserId,
                        Collectors.groupingBy(OrderLine::getOrderId)));

        List<User> users = new ArrayList<>();

        // Process each user
        userOrdersMap.forEach((userId, ordersMap) -> {
            String userName = ordersMap.values().stream()
                    .flatMap(Collection::stream)
                    .findFirst()
                    .map(OrderLine::getUserName)
                    .orElse("");

            List<Order> orders = new ArrayList<>();

            // Process each order for the user
            ordersMap.forEach((orderId, orderLinesList) -> {
                List<Product> products = new ArrayList<>();
                BigDecimal orderTotal = BigDecimal.ZERO;
                LocalDate orderDate = null;

                // Process each product in the order
                for (OrderLine line : orderLinesList) {
                    BigDecimal productValue = new BigDecimal(line.getProductValue().trim());
                    orderTotal = orderTotal.add(productValue);

                    // Parse the date only once per order
                    if (orderDate == null) {
                        orderDate = DateUtil.parseFromLegacyFormat(line.getPurchaseDate());
                    }

                    Product product = Product.builder()
                            .productId(Long.parseLong(line.getProductId()))
                            .value(productValue)
                            .build();

                    products.add(product);
                }

                Order order = Order.builder()
                        .orderId(Long.parseLong(orderId))
                        .date(orderDate)
                        .total(orderTotal)
                        .products(products)
                        .build();

                orders.add(order);
            });

            User user = User.builder()
                    .userId(Long.parseLong(userId))
                    .name(userName.trim())
                    .orders(orders)
                    .build();

            users.add(user);
        });

        return users;
    }

    private List<UserOrdersResponse> mapToUserOrdersResponseList(List<User> users) {
        return users.stream()
                .map(this::mapToUserOrdersResponse)
                .collect(Collectors.toList());
    }

    private UserOrdersResponse mapToUserOrdersResponse(User user) {
        return UserOrdersResponse.builder()
                .user_id(user.getUserId())
                .name(user.getName())
                .orders(user.getOrders().stream()
                        .map(this::mapToOrderResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .order_id(order.getOrderId())
                .total(order.getTotal().toString())
                .date(DateUtil.formatToApiFormat(order.getDate()))
                .products(order.getProducts().stream()
                        .map(this::mapToProductResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .product_id(product.getProductId())
                .value(product.getValue().toString())
                .build();
    }
}