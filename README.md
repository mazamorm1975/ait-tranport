# Transporte

## Ejecutar con Docker Compose

Desde la carpeta del proyecto, ejecuta:

```powershell
docker compose up --build
```

La API quedará disponible en `http://localhost:8080` y MySQL en el puerto
`3307` del equipo. Si ese puerto está ocupado, define `MYSQL_HOST_PORT` con
otro puerto disponible; por ejemplo, en PowerShell:

```powershell
$env:MYSQL_HOST_PORT = "3308"
docker compose up --build
```

La interfaz de Swagger estará en `http://localhost:8080/docs` y la
especificación OpenAPI en `http://localhost:8080/v3/api-docs`.

Compose inicia primero la base de datos y espera a que esté
lista antes de arrancar la aplicación. Los datos de MySQL se conservan en el
volumen `mysql_data`.

Para cambiar la contraseña de desarrollo, define `MYSQL_ROOT_PASSWORD` antes
de iniciar Compose. En PowerShell:

```powershell
$env:MYSQL_ROOT_PASSWORD = "tu-contraseña"
docker compose up --build
```

La contraseña predeterminada (`root`) es solo para desarrollo local; cámbiala
antes de usar este Compose en un entorno compartido o de producción.

Para detener los contenedores sin borrar los datos:

```powershell
docker compose down
```

Para eliminar también el volumen y los datos de MySQL:

```powershell
docker compose down --volumes
```

## Consultar órdenes

- `GET /v1/orders/{id}` consulta una orden por su UUID y responde `404` si no existe.
- `GET /v1/orders` lista todas las órdenes; los filtros son opcionales y se
  combinan cuando se envían juntos:

```text
/v1/orders?status=IN_TRANSIT&date=2026-09-29&origin=Madrid&destination=Sevilla
```

`date` filtra por la fecha de creación (`createdAt`), y los filtros de origen y
destino buscan coincidencias parciales sin distinguir mayúsculas de minúsculas.

## Asignar conductor y adjuntar archivos

- `POST /v1/orders/{orderId}/assignment` asigna un conductor. Envía
  `{"driverId":"<UUID del conductor>"}`; solo permite órdenes `CREATED` y
  conductores activos. Cada orden puede tener una asignación.
- `GET /v1/orders/{orderId}/assignment` consulta la asignación y los archivos adjuntos.
- `POST /v1/orders/{orderId}/assignment/document` recibe `multipart/form-data`
  con el campo `file` y acepta un PDF (máximo 10 MB).
- `POST /v1/orders/{orderId}/assignment/image` recibe `multipart/form-data`
  con el campo `file` y acepta una imagen PNG o JPG (máximo 10 MB).
- `GET /v1/orders/{orderId}/assignment/files/{type}` descarga el archivo;
  `{type}` es `DOCUMENT` o `IMAGE`.

Los archivos se guardan en MySQL y se pueden cargar de nuevo para reemplazar el
archivo del mismo tipo. En Swagger, crea primero una asignación y luego usa los
endpoints de carga seleccionando el archivo en el campo `file`.
