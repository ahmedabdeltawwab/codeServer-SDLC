package com.company.resourceapi.model;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Project {

    private Long id;

    private String externalId;

    private String name;

    private SdlcSystem sdlcSystem;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;
}
