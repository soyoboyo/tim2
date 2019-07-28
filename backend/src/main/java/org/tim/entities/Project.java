package org.tim.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.tim.exceptions.ValidationException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

import static org.tim.constants.UserMessages.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Project {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NonNull
    @Column(unique = true)
    private String name;

    @NotNull
    @NonNull
    private Locale sourceLocale;

    @Setter(AccessLevel.NONE)
    @ManyToMany(cascade = {
            CascadeType.MERGE, CascadeType.PERSIST},
            fetch = FetchType.EAGER)
    @JoinTable(name = "project_locale_wrapper_replacement",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "locale_wrapper_id"))
    final private Map<LocaleWrapper, LocaleWrapper> replaceableLocaleToItsSubstitute = new HashMap<>();

    @NotNull
    @Setter(AccessLevel.NONE)
    @ManyToMany(cascade = {
            CascadeType.MERGE, CascadeType.DETACH},
            fetch = FetchType.EAGER)
    @JoinTable(name = "project_locale_wrapper",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "locale_wrapper_id"))
    final private Set<LocaleWrapper> targetLocales = new HashSet<>();

    public void addTargetLocale(@NotNull List<LocaleWrapper> locales) {
        targetLocales.addAll(locales);
    }

    public void removeTargetLocale(@NotNull LocaleWrapper locale) {
        targetLocales.remove(locale);
        removeReplaceableLocale(locale);
        removeSubstituteLocale(locale);
    }

    public Optional<LocaleWrapper> getSubstituteLocale(LocaleWrapper replaceableLocale) {
        return Optional.ofNullable(replaceableLocaleToItsSubstitute.get(replaceableLocale));
    }

    public void removeReplaceableLocale(LocaleWrapper replaceableLocale) {
        replaceableLocaleToItsSubstitute.remove(replaceableLocale);
    }

    public void removeSubstituteLocale(LocaleWrapper substituteLocale) {
        replaceableLocaleToItsSubstitute.entrySet().removeIf(e -> e.getValue().equals(substituteLocale));
    }

    public void updateSubstituteLocale(LocaleWrapper replaceableLocale, LocaleWrapper substituteLocale) {
        if (!targetLocales.contains(replaceableLocale)) {
            throw new ValidationException(formatMessage(LCL_NOT_IN_TARGET, replaceableLocale.getLocale().toString()));
        }
        if (!targetLocales.contains(substituteLocale)) {
            throw new ValidationException(formatMessage(LCL_NOT_IN_TARGET, replaceableLocale.getLocale().toString()));
        }
        if (checkIfGraphHasCycles(replaceableLocale, substituteLocale)) {
            throw new ValidationException(LCL_CYCLES);
        }
        replaceableLocaleToItsSubstitute.put(replaceableLocale, substituteLocale);
    }

    private Boolean checkIfGraphHasCycles(LocaleWrapper child, LocaleWrapper parent) {
        if (replaceableLocaleToItsSubstitute.get(parent) == null) {
            return false;
        } else if (replaceableLocaleToItsSubstitute.get(parent).equals(child)) {
            return true;
        }
        return checkIfGraphHasCycles(child, replaceableLocaleToItsSubstitute.get(parent));
    }
}
