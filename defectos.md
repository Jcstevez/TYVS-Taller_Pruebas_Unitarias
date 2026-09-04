# Registro de Defectos — EJEMPLO RESUELTO

> ℹ️ **Este archivo es un ejemplo del profesor**, no su entrega. Muestra el nivel de detalle y los dos formatos aceptados.
> Para su taller, parta de [`defectos_template.md`](defectos_template.md) y documente los defectos que **usted** encuentre al ejecutar sus propias pruebas.

Este documento recopila los defectos encontrados durante la ejecución de pruebas unitarias del proyecto **Registraduría**.
Cada defecto debe documentarse claramente para facilitar su análisis y corrección.

Los defectos de abajo se detectaron sobre el estado del código **al terminar la iteración 1** del README (cuando `registerVoter` aún devolvía `VALID` para cualquier entrada).

---

## Formato 1: Lista detallada (narrativa)

### Defecto 01

- **Caso de prueba**: Persona con edad -1 (edad inválida).
- **Entrada**: `Person(name="Juan", id=101, age=-1, gender=MALE, alive=true)`
- **Resultado esperado**: `INVALID_AGE`
- **Resultado obtenido**: `VALID`
- **Causa probable**: Falta de validación de edad negativa en `Registry.registerVoter`.
- **Estado**: Abierto

---

### Defecto 02

- **Caso de prueba**: Persona muerta.
- **Entrada**: `Person(name="Ana", id=102, age=45, gender=FEMALE, alive=false)`
- **Resultado esperado**: `DEAD`
- **Resultado obtenido**: `VALID`
- **Causa probable**: No se evalúa la condición `alive=false`.
- **Estado**: **Resuelto** — corregido en la iteración 2 (`if (!p.isAlive()) return RegisterResult.DEAD;`) y verificado con la prueba `shouldRejectDeadPerson`.

---

### Defecto 03

- **Caso de prueba**: Registro duplicado con el mismo `id`.
- **Entradas**:
  - Persona 1: `Person(name="Carlos", id=200, age=30, gender=MALE, alive=true)`
  - Persona 2: `Person(name="Carla", id=200, age=25, gender=FEMALE, alive=true)`
- **Resultado esperado**:
  - Persona 1 → `VALID`
  - Persona 2 → `DUPLICATED`
- **Resultado obtenido**:
  - Persona 1 → `VALID`
  - Persona 2 → `VALID`
- **Causa probable**: No hay verificación de `id` previamente registrado.
- **Estado**: Abierto

---

## Formato 2: Tabla de defectos (bug tracking)

| ID | Caso de Prueba | Entrada | Resultado Esperado | Resultado Obtenido | Causa Probable | Estado |
|-----|---------------------|---------|--------------------|--------------------|----------------|--------|
| 01 | Edad inválida | `Person(id=101, age=-1, alive=true)` | `INVALID_AGE` | `VALID` | No se valida edad negativa | Abierto |
| 02 | Persona muerta | `Person(id=102, age=45, alive=false)` | `DEAD` | `VALID` | No se evalúa condición `alive=false` | Resuelto (iteración 2) |
| 03 | Registro duplicado | `Person(id=200, age=30, alive=true)` + `Person(id=200, age=25, alive=true)` | 1º → `VALID` 2º → `DUPLICATED` | 1º → `VALID` 2º → `VALID` | No hay verificación de `id` duplicado | Abierto |

---

## Convenciones de Estado

| Estado | Significado |
|---------|-------------|
| **Abierto** | El defecto fue detectado pero no corregido. |
| **En progreso** | El defecto se encuentra en análisis o corrección. |
| **Resuelto** | El defecto fue corregido y validado mediante pruebas. |

---

## Observaciones

- Se pueden usar **ambos formatos** o elegir uno como estándar de equipo.
- El objetivo es **gestionar la calidad del software** y **demostrar un proceso sistemático de testing**.
- Mantener este archivo actualizado durante todo el ciclo de desarrollo.

---

---

### Análisis de mutantes sobrevivientes (PIT)

- **Mutantes sobrevivientes**: `Person.getName()` (línea 19, mutante que reemplaza el retorno por `""`) y `Person.getGender()` (línea 31, mutante que reemplaza el retorno por `null`). Ambos marcados `NO_COVERAGE`.
- **Comportamiento no verificado**: ningún test ejercita una decisión de `Registry` que dependa de `name` o `gender`.
- **Por qué no se puede "matar" con una prueba adicional**: según la tabla de reglas de negocio del README (R1-R7), `registerVoter` solo evalúa `id`, `age` y `alive`. `name` y `gender` no participan en ninguna regla. Escribir un test que solo llame a `getName()` sin usar el resultado en una decisión de `Registry` no eliminaría el mutante de forma significativa (sería el mismo error que el "pruebaInutil" que menciona el README sobre coverage theater).
- **Conclusión**: estos dos mutantes sobrevivientes no son un defecto de nuestras pruebas, sino evidencia correcta de que `name` y `gender` son atributos informativos de `Person` sin impacto en las reglas de negocio actuales. El mutation score (92% global, 100% en `domain.service`) confirma que toda la lógica de negocio SÍ está verificada.