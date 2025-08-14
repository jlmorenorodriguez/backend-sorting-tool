# Product Sorting System

This REST service allows sorting products based on weighted scoring criteria, following hexagonal architecture and Domain-Driven Design (DDD) principles.

## Features

- **Product sorting** based on multiple criteria with configurable weights
- **Implemented criteria**:
   - Sales by units: Prioritizes products with higher number of sales
   - Stock ratio: Prioritizes products with better size availability
- **Hexagonal architecture**: Clear separation between domain, application, and infrastructure
- **MongoDB persistence**: Product storage with transparent integration
- **REST API**: Endpoints to utilize the system's functionality
- **Swagger documentation**: Interactive interface to explore and test the API
- **Complete tests**: Unit, integration, and end-to-end

## Requirements

- Java 17+
- Maven 3.6+
- Docker and Docker Compose (for database and deployment)

## Quick start

### Option 1: Using Docker Compose (recommended)

```bash
# Clone the repository
git clone https://github.com/yourusername/product-sorting.git
cd product-sorting

# Start the complete application with Docker
make docker-all

# Alternatively, if you only want MongoDB and run the app locally
make docker-db
make run
```

### Option 2: Run without MongoDB (testing only)

```bash
# If you only want to test the API and Swagger without MongoDB
make run-no-mongo
```

### Option 3: Manual setup

```bash
# Install MongoDB (or use an existing instance)
# Adjust application.yml with MongoDB configuration

# Build and run
mvn clean package
java -jar target/product-sorting-1.0.0.jar
```

## API Documentation

The API is documented with Swagger/OpenAPI, accessible at:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/api-docs

### Direct usage example with curl

```bash
curl -X POST http://localhost:8080/api/products/sort \
  -H "Content-Type: application/json" \
  -d '{"salesCriterionWeight": 0.7, "stockRatioCriterionWeight": 0.3}'
```

## Project structure

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/productsorting/
│   │   │       ├── domain/          # Domain layer
│   │   │       ├── application/     # Application layer
│   │   │       └── infrastructure/  # Infrastructure layer
│   │   └── resources/
│   │       └── application.yml      # Configuration
│   └── test/                        # Tests
├── docker-compose.yml               # Docker configuration
├── Dockerfile                       # Application image
├── Makefile                         # Useful commands
└── pom.xml                          # Maven configuration
```

## Architecture

The solution implements hexagonal architecture (also known as ports and adapters):

1. **Domain**: The system's core with business rules
   - Entities: Product
   - Interfaces: SortingCriteria
   - Implementations: SalesCriterion, StockRatioCriterion
   - Services: ProductSortingDomainService

2. **Application**: Use cases that orchestrate the domain
   - Services: ProductSortingService
   - DTOs: ProductSortingRequest, ProductSortingResponse
   - Factories: SortingCriteriaFactory

3. **Infrastructure**: Technical adapters
   - Repositories: ProductMongoRepository, ProductRepositoryImpl
   - Controllers: ProductSortingController
   - Configuration: ApplicationConfig, WebConfig, OpenApiConfig

## Useful commands (Makefile)

```bash
# Build the application
make build

# Run application with MongoDB
make run

# Run application without MongoDB (testing only)
make run-no-mongo

# Update dependencies
make update-deps 

# Start MongoDB only
make docker-db

# Start complete application in Docker
make docker-all

# View application logs
make docker-logs

# Stop containers
make docker-stop
```

## Troubleshooting

### MongoDB connection issue

If you see errors like "Connection refused" when starting the application:

1. Make sure MongoDB is running:
   ```bash
   make docker-db
   ```

2. Or run the application without MongoDB:
   ```bash
   make run-no-mongo
   ```

### Dependency issues

If you have problems with Swagger dependencies:

```bash
make fix-swagger
```

## Extensibility

To add a new sorting criterion:

1. Implement the `SortingCriteria` interface
2. Add the new criterion in `SortingCriteriaFactory`
3. Update the REST controller to accept the new parameter
