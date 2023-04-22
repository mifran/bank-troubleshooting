package com.genecab.bank;

import org.springframework.data.annotation.Id;

public record Account(@Id Long id, String name, String type) {

}
