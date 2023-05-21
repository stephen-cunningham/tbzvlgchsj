package com.gen.weather.entitites;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public abstract class IdentifiableEntity {
  @Id
  @GeneratedValue
  @JsonProperty(access = Access.READ_ONLY)
  @Column(columnDefinition = "uuid", nullable = false)
  private UUID id;

  public UUID getId() {
    return id;
  }
}
