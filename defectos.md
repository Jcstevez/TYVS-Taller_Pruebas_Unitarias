# Registro de Defectos — Registraduria

Defectos detectados por el equipo durante el desarrollo TDD de `Registry.registerVoter`.
Se documentan sobre el estado del código **antes** de implementar cada regla, comparado
con el comportamiento correcto exigido por la regla de negocio correspondiente.

---

## Formato 1: Lista detallada (narrativa)

### Defecto 01

- **Caso de prueba**: Persona con edad negativa.
- **Entrada**: `Person("Marta", 10, -1, Gender.FEMALE, true)`
- **Resultado esperado**: `INVALID_AGE`
- **Resultado obtenido**: `VALID` (antes de implementar la regla R4)
- **Causa probable**: `Registry` no validaba el rango biológico de la edad (`0 <= edad <= 120`).
- **Estado**: **Resuelto** — corregido al añadir la guarda de edad y verificado con `shouldRejectWhenAgeIsNegative`.

---

### Defecto 02

- **Caso de prueba**: Registro duplicado con el mismo `id`.
- **Entradas**:
  - Persona 1: `Person("Andres", 20, 25, Gender.MALE, true)`
  - Persona 2: `Person("Andres Impostor", 20, 40, Gender.MALE, true)`
- **Resultado esperado**: 1ª → `VALID`, 2ª → `DUPLICATED`
- **Resultado obtenido**: 1ª → `VALID`, 2ª → `VALID` (antes de implementar la regla R6)
- **Causa probable**: `Registry` no guardaba estado de los `id` ya registrados.
- **Estado**: **Resuelto** — corregido añadiendo un `Set<Integer>` de instancia y verificado con `shouldRejectDuplicatedId`.

---

## Formato 2: Tabla de defectos (bug tracking)

| ID | Caso de Prueba | Entrada | Resultado Esperado | Resultado Obtenido | Causa Probable | Estado |
|----|-----------------|---------|---------------------|----------------------|------------------|--------|
| 01 | Edad negativa | `Person(id=10, age=-1, alive=true)` | `INVALID_AGE` | `VALID` | Sin validación de rango biológico | Resuelto |
| 02 | Registro duplicado | `Person(id=20,...)` x2 | 1º `VALID`, 2º `DUPLICATED` | 1º `VALID`, 2º `VALID` | Sin memoria de ids registrados | Resuelto |

---

## Convenciones de Estado

| Estado | Significado |
|---------|-------------|
| **Abierto** | El defecto fue detectado pero no corregido. |
| **En progreso** | El defecto se encuentra en análisis o corrección. |
| **Resuelto** | El defecto fue corregido y validado mediante pruebas. |

---

### Análisis de mutantes sobrevivientes (PIT)

- **Mutantes sobrevivientes**: `Person.getName()` (línea 19, mutante que reemplaza el retorno por `""`) y `Person.getGender()` (línea 31, mutante que reemplaza el retorno por `null`). Ambos marcados `NO_COVERAGE`.
- **Comportamiento no verificado**: ningún test ejercita una decisión de `Registry` que dependa de `name` o `gender`.
- **Por qué no se puede "matar" con una prueba adicional**: según la tabla de reglas de negocio del README (R1-R7), `registerVoter` solo evalúa `id`, `age` y `alive`. `name` y `gender` no participan en ninguna regla.
- **Conclusión**: estos dos mutantes sobrevivientes no son un defecto de nuestras pruebas, sino evidencia correcta de que `name` y `gender` son atributos informativos de `Person` sin impacto en las reglas de negocio actuales. El mutation score (92% global, 100% en `domain.service`) confirma que toda la lógica de negocio SÍ está verificada.