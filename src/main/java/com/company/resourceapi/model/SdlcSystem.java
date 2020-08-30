package com.company.resourceapi.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SdlcSystem {

    private Long id;

    private String baseUrl;

    private String description;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
