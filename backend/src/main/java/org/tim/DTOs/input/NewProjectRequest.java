package org.tim.DTOs.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Setter
@Getter
public class NewProjectRequest {

    public NewProjectRequest() {
        this.targetLocales = new ArrayList<>();
        this.replaceableLocaleToItsSubstitute = new HashMap<>();
    }

    @NotBlank(message = "Name cant be blank")
    private String name;

    @NotBlank(message = "Source locale con't be blank")
    private String sourceLocale;

    @NotNull(message = "Target locales can't be null")
    final private List<String> targetLocales;

    @NotNull(message = "Replaceable locale to it's substitute field can't be null")
    final private Map<String, String> replaceableLocaleToItsSubstitute;
}
