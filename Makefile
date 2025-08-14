# Makefile para facilitar las operaciones comunes

.PHONY: build run run-docker test clean docker-db docker-all docker-stop docker-clean update-deps force-update-deps reset-mvn-repo fix-swagger

# Variables
JAR_NAME = target/product-sorting-1.0.0.jar

# Compilar la aplicación
build:
	@echo "Compilando la aplicación..."
	mvn clean package -DskipTests

# Ejecutar la aplicación localmente
run: build
	@echo "Ejecutando la aplicación..."
	java -jar $(JAR_NAME)

# Ejecutar tests
test:
	@echo "Ejecutando tests..."
	mvn test

# Limpiar proyecto
clean:
	@echo "Limpiando el proyecto..."
	mvn clean
	@echo "Limpiando Docker..."
	docker-compose down

# Actualizar dependencias
update-deps:
	@echo "Actualizando dependencias..."
	mvn dependency:resolve -U

# Forzar actualización de dependencias (limpia el repositorio local)
force-update-deps:
	@echo "Forzando actualización de todas las dependencias..."
	mvn dependency:purge-local-repository -DreResolve=false
	mvn clean dependency:resolve -U
	@echo "Forzando actualización específica de springdoc..."
	mvn dependency:purge-local-repository -DmanualInclude=org.springdoc:springdoc-openapi-starter-webmvc-ui
	mvn dependency:get -Dartifact=org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0 -DremoteRepositories=https://repo1.maven.org/maven2/
	@echo "Limpieza final y compilación..."
	mvn clean install -U
	@echo "Dependencias actualizadas forzosamente."

# Iniciar solo MongoDB en Docker
docker-db:
	@echo "Iniciando MongoDB en Docker..."
	docker-compose up -d mongodb

# Iniciar toda la aplicación en Docker
docker-all: build
	@echo "Iniciando la aplicación completa en Docker..."
	docker-compose up -d --build

# Detener contenedores
docker-stop:
	@echo "Deteniendo contenedores..."
	docker-compose stop

# Eliminar contenedores y volúmenes
docker-clean:
	@echo "Eliminando contenedores y volúmenes..."
	docker-compose down -v

# Mostrar logs de la aplicación
docker-logs:
	@echo "Mostrando logs de la aplicación..."
	docker-compose logs -f product-sorting-app

# Ayuda
help:
	@echo "Comandos disponibles:"
	@echo "  make build             - Compila la aplicación"
	@echo "  make run               - Compila y ejecuta la aplicación localmente"
	@echo "  make test              - Ejecuta los tests"
	@echo "  make clean             - Limpia el proyecto"
	@echo "  make update-deps       - Actualiza dependencias"
	@echo "  make force-update-deps - Fuerza actualización de dependencias"
	@echo "  make reset-mvn-repo    - Resetea completamente el repositorio Maven"
	@echo "  make fix-swagger       - Solución rápida para problemas de Swagger"
	@echo "  make docker-db         - Inicia solo MongoDB en Docker"
	@echo "  make docker-all        - Inicia toda la aplicación en Docker"
	@echo "  make docker-stop       - Detiene los contenedores"
	@echo "  make docker-clean      - Elimina contenedores y volúmenes"
	@echo "  make docker-logs       - Muestra logs de la aplicación"