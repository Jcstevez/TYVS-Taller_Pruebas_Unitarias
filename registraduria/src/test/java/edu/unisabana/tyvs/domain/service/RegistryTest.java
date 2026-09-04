package edu.unisabana.tyvs.domain.service;

import edu.unisabana.tyvs.domain.model.Gender;
import edu.unisabana.tyvs.domain.model.Person;
import edu.unisabana.tyvs.domain.model.RegisterResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Pruebas por EJEMPLO del dominio: cada prueba fija una entrada concreta y su
 * resultado esperado. Estado al terminar la ITERACION 2 del README.
 *
 * Complemento: RegistryPropertiesTest expresa las mismas reglas como
 * PROPIEDADES sobre rangos completos de entradas, en vez de ejemplos sueltos.
 */
class RegistryTest {

    private Registry registry;

    /**
     * Un Registry NUEVO antes de cada prueba.
     *
     * Importante: cuando implemente DUPLICATED, el Registry guardara estado
     * (los ids ya registrados). Si compartiera la misma instancia entre
     * pruebas, una prueba podria "ensuciar" a la siguiente y los resultados
     * dependerian del orden de ejecucion. Cada prueba debe ser independiente.
     */
    @BeforeEach
    void setUp() {
        registry = new Registry();
    }

    @Test
    @DisplayName("Una persona viva y mayor de edad queda registrada")
    void shouldRegisterValidPerson() {
        // Arrange: preparar los datos
        Person person = new Person("Ana", 1, 30, Gender.FEMALE, true);

        // Act: ejecutar la accion que queremos probar
        RegisterResult result = registry.registerVoter(person);

        // Assert: verificar el resultado esperado
        assertEquals(RegisterResult.VALID, result);
    }

    @Test
    @DisplayName("Una persona no viva se rechaza con DEAD")
    void shouldRejectDeadPerson() {
        // Arrange: preparar los datos
        Person dead = new Person("Carlos", 2, 40, Gender.MALE, false);

        // Act: ejecutar la accion que queremos probar
        RegisterResult result = registry.registerVoter(dead);

        // Assert: verificar el resultado esperado
        assertEquals(RegisterResult.DEAD, result);
    }

    @Test
    @DisplayName("Una persona nula se rechaza con INVALID")
    void shouldReturnInvalidWhenPersonIsNull() {
        // Act
        RegisterResult result = registry.registerVoter(null);

        // Assert
        assertEquals(RegisterResult.INVALID, result);
    }

    @Test
    @DisplayName("Un id igual a cero se rechaza con INVALID (borde)")
    void shouldRejectWhenIdIsZero() {
        // Arrange
        Person person = new Person("Luis", 0, 25, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.INVALID, result);
    }

    @Test
    @DisplayName("Un id negativo se rechaza con INVALID")
    void shouldRejectWhenIdIsNegative() {
        // Arrange
        Person person = new Person("Luis", -5, 25, Gender.MALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.INVALID, result);
    }

        @Test
    @DisplayName("Una edad negativa se rechaza con INVALID_AGE (borde inferior)")
    void shouldRejectWhenAgeIsNegative() {
        // Arrange
        Person person = new Person("Marta", 10, -1, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.INVALID_AGE, result);
    }

    @Test
    @DisplayName("Una edad mayor a 120 se rechaza con INVALID_AGE (borde superior)")
    void shouldRejectWhenAgeIsOver120() {
        // Arrange
        Person person = new Person("Marta", 11, 121, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.INVALID_AGE, result);
    }

    @Test
    @DisplayName("Una edad de exactamente 120 anios SI es valida (borde superior incluido)")
    void shouldAcceptMaxAge120() {
        // Arrange
        Person person = new Person("Marta", 12, 120, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.VALID, result);
    }

        @Test
    @DisplayName("Una persona de 17 anios se rechaza con UNDERAGE (borde superior de la clase menor)")
    void shouldRejectUnderageAt17() {
        // Arrange
        Person person = new Person("Sofia", 13, 17, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.UNDERAGE, result);
    }

    @Test
    @DisplayName("Una persona de 18 anios SI se registra (borde inferior de la clase adulta)")
    void shouldAcceptAdultAt18() {
        // Arrange
        Person person = new Person("Sofia", 14, 18, Gender.FEMALE, true);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.VALID, result);
    }

    @Test
    @DisplayName("Registrar dos veces el mismo id devuelve DUPLICATED la segunda vez")
    void shouldRejectDuplicatedId() {
        // Arrange
        Person primera = new Person("Andres", 20, 25, Gender.MALE, true);
        Person segunda = new Person("Andres Impostor", 20, 40, Gender.MALE, true);
        registry.registerVoter(primera); // primer registro: queda VALID

        // Act
        RegisterResult result = registry.registerVoter(segunda);

        // Assert
        assertEquals(RegisterResult.DUPLICATED, result);
    }

    @Test
    @DisplayName("Una persona muerta Y menor de edad da DEAD, no UNDERAGE (R3 se evalua antes que R5)")
    void deadRuleTakesPrecedenceOverUnderage() {
        // Arrange: persona muerta de 15 anios
        Person person = new Person("Nino", 30, 15, Gender.MALE, false);

        // Act
        RegisterResult result = registry.registerVoter(person);

        // Assert
        assertEquals(RegisterResult.DEAD, result);
    }
}
