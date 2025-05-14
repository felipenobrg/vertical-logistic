package com.challenge.verticallogistics;

import com.challenge.verticallogistics.controller.OrderController;
import com.challenge.verticallogistics.dto.request.DateRangeRequest;
import com.challenge.verticallogistics.dto.response.OrderResponse;
import com.challenge.verticallogistics.dto.response.ProductResponse;
import com.challenge.verticallogistics.dto.response.UserOrdersResponse;
import com.challenge.verticallogistics.service.OrderProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest {

    @Mock
    private OrderProcessingService orderProcessingService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private List<UserOrdersResponse> mockUserOrdersResponses;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();

        mockUserOrdersResponses = createMockResponseData();
    }
    private List<UserOrdersResponse> createMockResponseData() {
        ProductResponse prodA1 = ProductResponse.builder()
                .product_id(111L)
                .value("256.24")
                .build();
        ProductResponse prodA2 = ProductResponse.builder()
                .product_id(122L)
                .value("512.24")
                .build();
        OrderResponse orderA = OrderResponse.builder()
                .order_id(123L)
                .total("1024.48")
                .date("2021-12-01")
                .products(Arrays.asList(prodA1, prodA2))
                .build();
        UserOrdersResponse userA = UserOrdersResponse.builder()
                .user_id(1L)
                .name("Zarelli")
                .orders(Collections.singletonList(orderA))
                .build();

        ProductResponse prodB1 = ProductResponse.builder()
                .product_id(111L)
                .value("256.24")
                .build();
        ProductResponse prodB2 = ProductResponse.builder()
                .product_id(122L)
                .value("512.24")
                .build();
        OrderResponse orderB = OrderResponse.builder()
                .order_id(12345L)
                .total("512.24")
                .date("2020-12-01")
                .products(Arrays.asList(prodB1, prodB2))
                .build();
        UserOrdersResponse userB = UserOrdersResponse.builder()
                .user_id(2L)
                .name("Medeiros")
                .orders(Collections.singletonList(orderB))
                .build();

        return Arrays.asList(userA, userB);
    }

    @Test
    @DisplayName("Should process fixed-width file and return normalized order data")
    void testUploadFile_SuccessfullyProcessesFixedWidthFile() throws Exception {
        String fileContent =
                "0000000002 Medeiros                                  0000012345000000011100000256.242020120100000000002 Medeiros                                  0000012345000000012200000256.242020120100000000001 Zarelli                                   0000000123000000011100000512.242021120100000000001 Zarelli                                   0000000123000000012200000512.2420211201";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "orders.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileContent.getBytes()
        );

        when(orderProcessingService.processOrderFile(any(MultipartFile.class)))
                .thenReturn(mockUserOrdersResponses);

        ResponseEntity<List<UserOrdersResponse>> response = orderController.uploadFile(file);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserOrdersResponses, response.getBody());
        verify(orderProcessingService, times(1)).processOrderFile(any(MultipartFile.class));
    }

    @Test
    @DisplayName("Should return all orders when requesting without filters")
    void testGetAllOrders_ReturnsAllOrders() {
        when(orderProcessingService.getAllOrders()).thenReturn(mockUserOrdersResponses);

        ResponseEntity<List<UserOrdersResponse>> response = orderController.getAllOrders();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserOrdersResponses, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(orderProcessingService, times(1)).getAllOrders();
    }

    @Test
    @DisplayName("Should filter orders by order ID when orderId is provided")
    void testFilterOrders_ByOrderId_ReturnsMatchingOrders() {
        DateRangeRequest request = new DateRangeRequest();
        request.setOrderId("123");

        List<UserOrdersResponse> filteredOrders = Collections.singletonList(mockUserOrdersResponses.get(0));
        when(orderProcessingService.getOrdersByOrderId(123L)).thenReturn(filteredOrders);

        ResponseEntity<List<UserOrdersResponse>> response = orderController.filterOrders(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(filteredOrders, response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(123L, response.getBody().get(0).getOrders().get(0).getOrder_id());
        verify(orderProcessingService, times(1)).getOrdersByOrderId(123L);
        verify(orderProcessingService, never()).getOrdersByDateRange(any(), any());
        verify(orderProcessingService, never()).getAllOrders();
    }

    @Test
    @DisplayName("Should filter orders by date range when start and end dates are provided")
    void testFilterOrders_ByDateRange_ReturnsOrdersInRange() {

        DateRangeRequest request = new DateRangeRequest();
        request.setOrderId("123");

        List<UserOrdersResponse> filteredOrders = Collections.singletonList(mockUserOrdersResponses.get(0));
        when(orderProcessingService.getOrdersByOrderId(123L)).thenReturn(filteredOrders);

        ResponseEntity<List<UserOrdersResponse>> response = orderController.filterOrders(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(filteredOrders, response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(123L, response.getBody().get(0).getOrders().get(0).getOrder_id());

        verify(orderProcessingService, times(1)).getOrdersByOrderId(123L);
        verify(orderProcessingService, never()).getOrdersByDateRange(any(), any());
        verify(orderProcessingService, never()).getAllOrders();
    }

    @Test
    @DisplayName("Should filter orders by start date only when only start date is provided")
    void testFilterOrders_WithStartDateOnly_ReturnsOrdersFromStartDate() {
        DateRangeRequest request = new DateRangeRequest();
        LocalDate startDate = LocalDate.of(2021, 1, 1);
        request.setStartDate(startDate);

        List<UserOrdersResponse> filteredOrders = Collections.singletonList(mockUserOrdersResponses.get(0));
        when(orderProcessingService.getOrdersByDateRange(startDate, null))
                .thenReturn(filteredOrders);

        ResponseEntity<List<UserOrdersResponse>> response = orderController.filterOrders(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(filteredOrders, response.getBody());
        assertEquals(1, response.getBody() != null ? response.getBody().size() : 0);
        assertEquals("2021-12-01", response.getBody().get(0).getOrders().get(0).getDate().toString());
        verify(orderProcessingService, never()).getOrdersByOrderId(anyLong());
        verify(orderProcessingService, times(1)).getOrdersByDateRange(startDate, null);
        verify(orderProcessingService, never()).getAllOrders();
    }

    @Test
    @DisplayName("Should filter orders by end date only when only end date is provided")
    void testFilterOrders_WithEndDateOnly_ReturnsOrdersUntilEndDate() {
        DateRangeRequest request = new DateRangeRequest();
        LocalDate endDate = LocalDate.of(2020, 12, 31);
        request.setEndDate(endDate);

        List<UserOrdersResponse> filteredOrders = Collections.singletonList(mockUserOrdersResponses.get(1));
        when(orderProcessingService.getOrdersByDateRange(null, endDate))
                .thenReturn(filteredOrders);

        ResponseEntity<List<UserOrdersResponse>> response = orderController.filterOrders(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(filteredOrders, response.getBody());
        assertEquals(1, response.getBody() != null ? response.getBody().size() : 0);
        assertEquals("2020-12-01", response.getBody().get(0).getOrders().get(0).getDate().toString());
        verify(orderProcessingService, never()).getOrdersByOrderId(anyLong());
        verify(orderProcessingService, times(1)).getOrdersByDateRange(null, endDate);
        verify(orderProcessingService, never()).getAllOrders();
    }

    @Test
    @DisplayName("Should return all orders when no filter parameters are provided")
    void testFilterOrders_NoParameters_ReturnsAllOrders() {
        DateRangeRequest request = new DateRangeRequest();

        when(orderProcessingService.getAllOrders()).thenReturn(mockUserOrdersResponses);

        ResponseEntity<List<UserOrdersResponse>> response = orderController.filterOrders(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUserOrdersResponses, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(orderProcessingService, never()).getOrdersByOrderId(anyLong());
        verify(orderProcessingService, never()).getOrdersByDateRange(any(), any());
        verify(orderProcessingService, times(1)).getAllOrders();
    }

    @Test
    @DisplayName("Should successfully process file upload request via MockMvc")
    void testMvcIntegration_UploadFile() throws Exception {
        String fileContent =
                "0000000002 Medeiros                                  0000012345000000011100000256.242020120100000000002 Medeiros                                  0000012345000000012200000256.242020120100000000001 Zarelli                                   0000000123000000011100000512.242021120100000000001 Zarelli                                   0000000123000000012200000512.2420211201";

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "orders.txt",
                MediaType.TEXT_PLAIN_VALUE,
                fileContent.getBytes()
        );

        when(orderProcessingService.processOrderFile(any(MultipartFile.class)))
                .thenReturn(mockUserOrdersResponses);

        mockMvc.perform(multipart("/api/orders/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].user_id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Zarelli")))
                .andExpect(jsonPath("$[1].user_id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Medeiros")));
    }

    @Test
    @DisplayName("Should successfully get all orders via MockMvc")
    void testMvcIntegration_GetAllOrders() throws Exception {
        when(orderProcessingService.getAllOrders()).thenReturn(mockUserOrdersResponses);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].user_id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Zarelli")))
                .andExpect(jsonPath("$[0].orders[0].order_id", is(123)))
                .andExpect(jsonPath("$[0].orders[0].products", hasSize(2)))
                .andExpect(jsonPath("$[1].user_id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Medeiros")))
                .andExpect(jsonPath("$[1].orders[0].order_id", is(12345)))
                .andExpect(jsonPath("$[1].orders[0].products", hasSize(2)));
    }

    @Test
    @DisplayName("Should successfully filter orders by order ID via MockMvc")
    void testMvcIntegration_FilterOrdersByOrderId() throws Exception {
        List<UserOrdersResponse> filteredOrders = Collections.singletonList(mockUserOrdersResponses.get(0));
        when(orderProcessingService.getOrdersByOrderId(123L)).thenReturn(filteredOrders);

        mockMvc.perform(get("/api/orders/filter")
                        .param("orderId", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user_id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Zarelli")))
                .andExpect(jsonPath("$[0].orders[0].order_id", is(123)));
    }

    @Test
    @DisplayName("Should successfully filter orders by date range via MockMvc")
    void testMvcIntegration_FilterOrdersByDateRange() throws Exception {
        List<UserOrdersResponse> filteredOrders = Collections.singletonList(mockUserOrdersResponses.get(0)); // Zarelli's orders
        LocalDate startDate = LocalDate.of(2021, 1, 1);
        LocalDate endDate = LocalDate.of(2021, 12, 31);

        when(orderProcessingService.getOrdersByDateRange(eq(startDate), eq(endDate)))
                .thenReturn(filteredOrders);

        mockMvc.perform(get("/api/orders/filter")
                        .param("startDate", "2021-01-01")
                        .param("endDate", "2021-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].user_id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Zarelli")))
                .andExpect(jsonPath("$[0].orders[0].date", is("2021-12-01")));
    }

    }