# Importador de documentos a GEDE

Este proyecto permite importar documentos escaneados en PDF a GEDE usando su API Rest.

*Nota*: Este proceso se apoya en una base de datos SQLite para llevar un control de los expedientes procesados y evitar duplicados.

El proceso de importación se realiza de la siguiente forma:

- Se leen los documentos PDF de un directorio con un formato específico.
- Se procesa el nombre para extrer el número de expediente y se comprueba si no existe en la base de datos SQLite.
- Si no existe, se crea el expediente en GEDE y se guarda en la base de datos SQLite.
- ~~Se firma el documento PDF con el sello electrónico de la entidad.~~ **(Pendiente revisar si se mantiene esta funcionalidad, actualmente deshabilitada)**
- Se suben el binario del documentoa GEDE.
- Se crea el documento en GEDE.
- Se guarda el registro del procesado en la base de datos SQLite.


## Requisitos

### Entorno de desarrollo
- Java 17 o superior
- Maven 3.6.3 o superior

## Configuracion

Es necesario actualizar la configuración de los siguientes ficheros:

### gede.properties

Datos de acceso y rutas al gestor documental. Configuración del directorio de lectura y fase.

```
# Datos de acceso al API
gede.usuario=PEOPLENET
gede.password=PEOPLENET
gede.organismo=SAETAS

# Rutas base del API Rest
gede.api.base=https://pre-plataforma.sevilla.org/gede-rest/api/v1

# Servicios
gede.api.ticket=${gede.api.base}/ticket
gede.api.ticket.validar=${gede.api.base}/ticket
gede.api.binarios=${gede.api.base}/binarios
gede.api.documentos.crear=${gede.api.base}/documentos
gede.api.documentos.agreagar.firmantes=${gede.api.base}/documentos/1/firmantes
gede.api.documentos.cerrar=${gede.api.base}/documentos/{idDocumento}/cerrar

#gede.api.ticket=https://pre-plataforma.sevilla.org/gede-rest/api/v1/ticket
#gede.api.ticket.validar=https://pre-plataforma.sevilla.org/gede-rest/api/v1/ticket
#gede.api.binarios=https://pre-plataforma.sevilla.org/gede-rest/api/v1/binarios
#gede.api.documentos.crear=https://pre-plataforma.sevilla.org/gede-rest/api/v1/documentos
#gede.api.documentos.agreagar.firmantes=https://pre-plataforma.sevilla.org/gede-rest/api/v1/documentos/1/firmantes
#gede.api.documentos.cerrar=https://pre-plataforma.sevilla.org/gede-rest/api/v1/documentos/{idDocumento}/cerrar

# Configuración de expedientes
# Directorio raíz del procesado de expedientes
dir.expedientes=expedientes\\
# Fase 
# 0 = Única carpeta con un fichero por cada expediente
# 1 = Carpeta por expediente con todos los documentos de ese expediente
fase=0
```
### afirma.properties

```
#Datos afirma
afirma.ws.endpoint=http://preafirma.aviacion.fomento.es/afirmaws/services/DSSAfirmaSign?wsdl
afirma.ws.username= comparecePruebas
afirma.ws.password= 8TEygqSSiM
afirma.ws.keyname= sello_electronico
afirma.ws.claimedidentity  = aesa.suncomparece
afirma.ws.hashalgorithmpdf = http://www.w3.org/2000/09/xmldsig#sha1
afirma.ws.signatureformpdf = urn:afirma:dss:1.0:profile:XSS:PAdES:1.1.2:forms:LTV
afirma.ws.signaturetypepdf = urn:afirma:dss:1.0:profile:XSS:forms:PAdES
```

## Ejecución


La aplicación se compila en formato jar ejecutable y requiere ser ejecutada con ___Java 17 o superior___

Para ejecutar la aplicación,  desde línea de comandos se invocará la siguiente comando:

```bash
java -jar gede-importer-1.0.0.jar --spring.config.location=./config/
```
En la misma carpeta que el ejecutable, se ubicará la carpeta config con dos ficheros: gede.properties y afirma.properties
donde están las propiedades que se deben configurar por entorno tal como se indica anteriomente.

Una vez ejecutada la aplicación se generará un fichero `gede-importer.log` con los detalles de la ejecución, así como un fichero `gede-importer.db`
con los registros en una base de datos SQLite de los ficheros procesados. 

Estos dos ficheros pueden usarse para revisar el proceso y ver posibles errores durante la ejecución del mismo.


