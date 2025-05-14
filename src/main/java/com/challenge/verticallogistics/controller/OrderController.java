package com.challenge.verticallogistics.controller;

import com.challenge.verticallogistics.dto.request.DateRangeRequest;
import com.challenge.verticallogistics.dto.response.UserOrdersResponse;
import com.challenge.verticallogistics.service.OrderProcessingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "API de Pedidos", description = "API para operações de processamento de pedidos")
public class OrderController {

    private final OrderProcessingService orderProcessingService;

    @PostMapping("/upload")
    @Operation(
            summary = "Carregar e processar arquivo de pedidos",
            description = "Carrega um arquivo contendo dados de pedidos em formato de largura fixa e retorna os pedidos processados e normalizados"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Pedidos processados com sucesso",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserOrdersResponse.class))
    )
    public ResponseEntity<List<UserOrdersResponse>> uploadFile(@RequestParam("file") MultipartFile file) {
        List<UserOrdersResponse> response = orderProcessingService.processOrderFile(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Obter todos os pedidos",
            description = "Recupera todos os pedidos no sistema"
    )
    public ResponseEntity<List<UserOrdersResponse>> getAllOrders() {
        List<UserOrdersResponse> response = orderProcessingService.getAllOrders();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/filter")
    @Operation(
            summary = "Filtrar pedidos",
            description = "Filtrar pedidos por ID do pedido ou intervalo de datas"
    )
    public ResponseEntity<List<UserOrdersResponse>> filterOrders(@Valid DateRangeRequest request) {
        List<UserOrdersResponse> response;

        if (request.getOrderId() != null && !request.getOrderId().isEmpty()) {
            response = orderProcessingService.getOrdersByOrderId(Long.parseLong(request.getOrderId()));
        } else if (request.getStartDate() != null || request.getEndDate() != null) {
            response = orderProcessingService.getOrdersByDateRange(
                    request.getStartDate(),
                    request.getEndDate());
        } else {
            response = orderProcessingService.getAllOrders();
        }

        return ResponseEntity.ok(response);
    }
}