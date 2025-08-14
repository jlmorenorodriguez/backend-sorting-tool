# Sistema de Ordenación de Productos

Este servicio REST permite ordenar productos según criterios de puntuación ponderados, siguiendo una arquitectura hexagonal y principios de Domain-Driven Design (DDD).

## Características

- **Ordenación de productos** basada en múltiples criterios con pesos configurables
- **Criterios implementados**:
    - Ventas por unidades: Prioriza productos con mayor número de ventas
    - Ratio de stock: Prioriza productos con mejor disponibilidad de tallas
- **Arquitectura hexagonal**: Separación clara entre dominio, aplicación e infraestructura
- **Persistencia en MongoDB**: Almacenamiento de productos con integración transparente
- **API REST**: Endpoints para utilizar la funcionalidad del sistema
- **Documentación con Swagger**: Interfaz interactiva para explorar y probar la API
- **Tests completos**: Unitarios, integración y end-to-end

## Requisitos

- Java 17+
- Maven 3.6+
- Docker y Docker Compose (para la base de datos y despliegue)

## Inicio rápido

### Opción 1: Usando Docker Compose (recomendado)

```bash
# Clonar el repositorio
git clone https://github.com/tuusuario/product-sorting.git
cd product-sorting

# Iniciar la aplicación completa con Docker
make docker-all

# Alternativamente, si solo quieres MongoDB y ejecutar la app localmente
make docker-db
make run
```

### Opción 2: Ejecutar sin MongoDB (solo para pruebas)

```bash
# Si solo quieres probar la API y Swagger sin MongoDB
make run-no-mongo
```

### Opción 3: Configuración manual

```bash
# Instalar MongoDB (o usar una instancia existente)
# Ajustar application.yml con la configuración de MongoDB

# Compilar y ejecutar
mvn clean package
java -jar target/product-sorting-1.0.0.jar
```

## Documentación de la API

La API está documentada con Swagger/OpenAPI, accesible en:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/api-docs

### Ejemplo de uso directo con curl

```bash
curl -X POST http://localhost:8080/api/products/sort \
  -H "Content-Type: application/json" \
  -d '{"salesCriterionWeight": 0.7, "stockRatioCriterionWeight": 0.3}'
```

## Estructura del proyecto

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/productsorting/
│   │   │       ├── domain/          # Capa de dominio
│   │   │       ├── application/     # Capa de aplicación
│   │   │       └── infrastructure/  # Capa de infraestructura
│   │   └── resources/
│   │       └── application.yml      # Configuración
│   └── test/                        # Tests
├── docker-compose.yml               # Configuración de Docker
├── Dockerfile                       # Imagen de la aplicación
├── Makefile                         # Comandos útiles
└── pom.xml                          # Configuración de Maven
```

## Arquitectura

La solución implementa una arquitectura hexagonal (también conocida como ports and adapters):

1. **Dominio**: El núcleo del sistema con las reglas de negocio
    - Entidades: Product
    - Interfaces: SortingCriteria
    - Implementaciones: SalesCriterion, StockRatioCriterion
    - Servicios: ProductSortingDomainService

2. **Aplicación**: Casos de uso que orquestan el dominio
    - Servicios: ProductSortingService
    - DTOs: ProductSortingRequest, ProductSortingResponse
    - Factories: SortingCriteriaFactory

3. **Infraestructura**: Adaptadores técnicos
    - Repositorios: ProductMongoRepository, ProductRepositoryImpl
    - Controladores: ProductSortingController
    - Configuración: ApplicationConfig, WebConfig, OpenApiConfig

## Comandos útiles (Makefile)

```bash
# Compilar la aplicación
make build

# Ejecutar aplicación con MongoDB
make run

# Ejecutar aplicación sin MongoDB (solo para pruebas)
make run-no-mongo

# Actualizar dependencias
make update-deps 

# Iniciar solo MongoDB
make docker-db

# Iniciar toda la aplicación en Docker
make docker-all

# Ver logs de la aplicación
make docker-logs

# Detener contenedores
make docker-stop
```

## Solución de problemas

### Problema de conexión a MongoDB

Si ves errores como "Connection refused" al iniciar la aplicación:

1. Asegúrate de que MongoDB esté en ejecución:
   ```bash
   make docker-db
   ```

2. O ejecuta la aplicación sin MongoDB:
   ```bash
   make run-no-mongo
   ```

### Problemas con dependencias

Si tienes problemas con las dependencias de Swagger:

```bash
make fix-swagger
```

## Extensibilidad

Para añadir un nuevo criterio de ordenación:

1. Implementar la interfaz `SortingCriteria`
2. Añadir el nuevo criterio en `SortingCriteriaFactory`
3. Actualizar el controlador REST para aceptar el nuevo parámetro

## Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo LICENSE para más detalles.