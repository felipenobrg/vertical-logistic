package com.challenge.verticallogistics.parser;

import com.challenge.verticallogistics.exception.OrderProcessingException;
import com.challenge.verticallogistics.model.OrderLine;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class FixedWidthOrderFileParser implements OrderFileParser {

    private static final int USER_ID_START = 0;
    private static final int USER_ID_LENGTH = 10;
    private static final int USER_NAME_START = 10;
    private static final int USER_NAME_LENGTH = 45;
    private static final int ORDER_ID_START = 55;
    private static final int ORDER_ID_LENGTH = 10;
    private static final int PRODUCT_ID_START = 65;
    private static final int PRODUCT_ID_LENGTH = 10;
    private static final int PRODUCT_VALUE_START = 75;
    private static final int PRODUCT_VALUE_LENGTH = 12;
    private static final int PURCHASE_DATE_START = 87;
    private static final int PURCHASE_DATE_LENGTH = 8;
    private static final int LINE_LENGTH = PURCHASE_DATE_START + PURCHASE_DATE_LENGTH;

    @Override
    public List<OrderLine> parseOrderFile(InputStream fileInputStream) {
        List<OrderLine> orderLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() < LINE_LENGTH) {
                    throw new OrderProcessingException("Invalid line length: " + line.length() + ". Expected: " + LINE_LENGTH);
                }

                OrderLine orderLine = parseOrderLine(line);
                orderLines.add(orderLine);
            }
        } catch (IOException e) {
            throw new OrderProcessingException("Error reading file", e);
        }

        return orderLines;
    }

    private OrderLine parseOrderLine(String line) {
        return OrderLine.builder()
                .userId(extractField(line, USER_ID_START, USER_ID_LENGTH).trim())
                .userName(extractField(line, USER_NAME_START, USER_NAME_LENGTH).trim())
                .orderId(extractField(line, ORDER_ID_START, ORDER_ID_LENGTH).trim())
                .productId(extractField(line, PRODUCT_ID_START, PRODUCT_ID_LENGTH).trim())
                .productValue(extractField(line, PRODUCT_VALUE_START, PRODUCT_VALUE_LENGTH).trim())
                .purchaseDate(extractField(line, PURCHASE_DATE_START, PURCHASE_DATE_LENGTH).trim())
                .build();
    }

    private String extractField(String line, int start, int length) {
        int end = Math.min(start + length, line.length());
        return line.substring(start, end);
    }
}