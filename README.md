# Finanzas CRUD (Jakarta EE 10 + PrimeFaces) - GlassFish 7

## Requisitos
- JDK 17+
- Maven 3.8+
- GlassFish 7.0.25
- SQL Server

## Configuración de DataSource en GlassFish
1. Instala el driver JDBC de SQL Server en GlassFish (si tu instalación no lo trae):
   - Copia el JAR `mssql-jdbc-*.jar` en: `glassfish7/glassfish/domains/domain1/lib/` y reinicia el servidor.

2. Crea el **JDBC Connection Pool**:
   - Driver Classname: `com.microsoft.sqlserver.jdbc.SQLServerDriver`
   - URL ejemplo:
     `jdbc:sqlserver://localhost:1433;databaseName=FinanzasDB;encrypt=true;trustServerCertificate=true`
   - Usuario / contraseña según tu SQL Server.

3. Crea el **JDBC Resource**:
   - JNDI Name: `jdbc/FinanzasDS`
   - Pool Name: el pool creado en el paso anterior.

> El proyecto usa `persistence.xml` con `jta-data-source` apuntando a `jdbc/FinanzasDS`.

## Ejecutar
- Compilar: `mvn clean package`
- Desplegar el WAR en GlassFish 7:
  - `target/finanzas-crud.war`

## Páginas
- Catálogos: TipoPago, TipoIngreso, TipoEgreso, RenglonEgreso
- Gestión: Egreso, Ingreso, Usuario, Transaccion, Corte
