package edu.unisabana.tyvs.domain.service;

import edu.unisabana.tyvs.domain.model.Gender;
import edu.unisabana.tyvs.domain.model.Person;
import edu.unisabana.tyvs.domain.model.RegisterResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Pruebas del dominio - estado al terminar la ITERACION 2 del README.
 *
 * A partir de aqui usted agrega, con TDD, las pruebas de las clases de
 * equivalencia que faltan (id invalido, edad, duplicados).
 */
public class RegistryTest {

    private Registry registry;

    /**
     * Un Registry NUEVO antes de cada prueba.
     *
     * Importante: cuando implemente DUPLICATED, el Registry guardara estado
     * (los ids ya registrados). Si compartiera la misma instancia entre
     * pruebas, una prueba podria "ensuciar" a la siguiente y los resultados
     * dependerian del orden de ejecucion. Cada prueba debe ser independiente.
     */
    @Before
    public void setUp() {
        registry = new Registry();
    }

    @Test
    public void shouldRegisterValidPerson() {
        // Arrange: preparar los datos
        Person person = new Person("Ana", 1, 30, Gender.FEMALE, true);

        // Act: ejecutar la accion que queremos probar
        RegisterResult result = registry.registerVoter(person);

        // Assert: verificar el resultado esperado
        Assert.assertEquals(RegisterResult.VALID, result);
    }

    @Test
    public void shouldRejectDeadPerson() {
        // Arrange: preparar los datos
        Person dead = new Person("Carlos", 2, 40, Gender.MALE, false);

        // Act: ejecutar la accion que queremos probar
        RegisterResult result = registry.registerVoter(dead);

        // Assert: verificar el resultado esperado
        Assert.assertEquals(RegisterResult.DEAD, result);
    }

}
