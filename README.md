# Importador de documentos a GEDE

Este proyecto permite importar documentos escaneados en PDF a GEDE usando su API Rest

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

```bash
 java -jar gede-0.0.1-SNAPSHOT.jar
```