package org.tim.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Locale;

@Document(indexName = "translation-version")
@Data
@NoArgsConstructor
public class TranslationVersion {

    @Id
    @Setter(AccessLevel.NONE)
    private String id;

    @NotNull
    private String content;

    @NotNull
    @NonNull
    private Locale locale;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updateDate;

    private Boolean isValid;

    private String createdBy;

    private Boolean isArchived;

    @NotNull
    private String translationId;
}
