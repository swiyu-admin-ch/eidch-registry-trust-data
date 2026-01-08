package ch.admin.bj.swiyu.registry.trust.data.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "non_compliance_list")
@NoArgsConstructor
public class NonComplianceList {

    @Id
    private UUID id;

    @NotNull
    @Version
    private Long version;

    @NotNull
    private Instant publishedAt;

    @Column(columnDefinition = "text")
    @NotNull
    private String payload; // stores all actors as a JSON array

    public NonComplianceList(UUID id, Instant publishedAt, String payload) {
        this.id = id;
        this.publishedAt = publishedAt;
        this.payload = payload;
    }
}
