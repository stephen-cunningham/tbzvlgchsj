package com.gen.weather.entitites;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ManagedEntityIT {
  @Autowired private EntityManager entityManager;

  @Test
  public void generatedId_IsNotNull() {
    TestEntity testEntity = new TestEntity();
    entityManager.persist(testEntity);

    assertNotNull(testEntity.getId());
  }

  @Entity
  private static class TestEntity extends ManagedEntity {}
}