package com.example.position.response;

import lombok.Data;

@Data
public class ElectionDefinitionResponse {
    private Integer id;
    private Integer version;
    private String name;
    private String startDate;
    private String endDate;
    private String description;
}
