package org.tim.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Locale;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LocaleWrapper {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NonNull
    private Locale locale;

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LocaleWrapper)) return false;
        final LocaleWrapper other = (LocaleWrapper) o;
        final Object this$locale = this.getLocale();
        final Object other$locale = other.getLocale();
        return this$locale == null ? other$locale == null : this$locale.equals(other$locale);
    }

    @Override
    public int hashCode() {
        return locale.hashCode();
    }
}
