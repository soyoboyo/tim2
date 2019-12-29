package org.tim.DTOs.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageWithWarningsDTO {

    @NotNull
    private String id;

    @NotBlank
    @NonNull
    private String key;

    @NotBlank
    @NonNull
    private String content;

    private String description;

    @NonNull
    private String projectId;

    WarningDTO warnings;

}
