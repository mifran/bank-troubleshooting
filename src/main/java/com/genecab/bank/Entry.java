package com.genecab.bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Entry(Long id, String description, LocalDateTime dateTime, BigDecimal amount, BigDecimal balance) {

}
