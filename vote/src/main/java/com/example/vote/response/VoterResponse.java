package com.example.vote.response;

import lombok.Data;

@Data
public class VoterResponse {

    private Integer id;
    private Integer version;
    private String name;
    private String nationalId;
    private String email;
    private String phone;
}
