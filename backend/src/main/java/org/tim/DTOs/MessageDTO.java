package org.tim.DTOs;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;
import org.tim.entities.Translation;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;


@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private Long id;

    @NotBlank
    @NonNull
    private String key;

    @NotBlank
    @NonNull
    private String content;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@UpdateTimestamp
	private LocalDateTime updateDate;

    private String description;

    @NonNull
    private Long projectId;

    private List<Translation> translations;
}
