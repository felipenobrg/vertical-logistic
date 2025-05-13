package com.challenge.verticallogistics.parser;

import com.challenge.verticallogistics.model.OrderLine;

import java.io.InputStream;
import java.util.List;

public interface OrderFileParser {
    List<OrderLine> parseOrderFile(InputStream fileInputStream);
}