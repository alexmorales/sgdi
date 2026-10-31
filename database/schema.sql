-- SGDI - Esquema relacional aprobado V2.0
-- Compatible con MySQL 8.0.16+ (restricciones CHECK aplicadas).
-- Ejecucion no destructiva: IF NOT EXISTS no modifica tablas preexistentes.
-- No incluye datos de ejemplo ni credenciales.

CREATE DATABASE IF NOT EXISTS `sgdi`
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE `sgdi`;

-- Entidad DER: usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id_usuario` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `correo` VARCHAR(50) NOT NULL,
  `password_hash` VARCHAR(225) NOT NULL,
  `activo` BOOLEAN NOT NULL,
  `fecha_creacion` TIMESTAMP NOT NULL,
  `ultimo_ingreso` DATETIME NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `uq_usuarios_correo` (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Entidad DER: roles
CREATE TABLE IF NOT EXISTS `roles` (
  `id_rol` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL,
  `descripcion` VARCHAR(200) NOT NULL,
  `activo` BOOLEAN NOT NULL,
  `fecha_creacion` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Entidad DER: tipos_documento (entidad adicional aprobada)
CREATE TABLE IF NOT EXISTS `tipos_documento` (
  `id_tipo_documento` INT NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(50) NOT NULL,
  `descripcion` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`id_tipo_documento`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Entidad DER: usuarios_roles (PK compuesta, sin AUTO_INCREMENT)
CREATE TABLE IF NOT EXISTS `usuarios_roles` (
  `id_usuario` INT NOT NULL,
  `id_rol` INT NOT NULL,
  `fecha_asignacion` DATETIME NOT NULL,
  PRIMARY KEY (`id_usuario`, `id_rol`),
  CONSTRAINT `fk_usuarios_roles_usuario` FOREIGN KEY (`id_usuario`)
    REFERENCES `usuarios` (`id_usuario`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_usuarios_roles_rol` FOREIGN KEY (`id_rol`)
    REFERENCES `roles` (`id_rol`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Entidad DER: categorias
CREATE TABLE IF NOT EXISTS `categorias` (
  `id_categoria` INT NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,
  `nombre` VARCHAR(50) NOT NULL,
  `descripcion` VARCHAR(200) NOT NULL,
  `estado` BOOLEAN NOT NULL,
  `fecha_creacion` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id_categoria`),
  CONSTRAINT `fk_categorias_usuario` FOREIGN KEY (`id_usuario`)
    REFERENCES `usuarios` (`id_usuario`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Entidad DER: proveedor_ia
CREATE TABLE IF NOT EXISTS `proveedor_ia` (
  `id_proveedor` INT NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,
  `nombre` VARCHAR(50) NOT NULL,
  `descripcion` VARCHAR(200) NOT NULL,
  `estado` BOOLEAN NOT NULL,
  `fecha_creacion` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id_proveedor`),
  CONSTRAINT `fk_proveedor_ia_usuario` FOREIGN KEY (`id_usuario`)
    REFERENCES `usuarios` (`id_usuario`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Entidad DER: notificaciones
CREATE TABLE IF NOT EXISTS `notificaciones` (
  `id_notificacion` INT NOT NULL AUTO_INCREMENT,
  `id_usuario` INT NOT NULL,
  `asunto` VARCHAR(50) NOT NULL,
  `mensaje` VARCHAR(200) NOT NULL,
  `leida` BOOLEAN NOT NULL,
  `fecha_envio` DATETIME NOT NULL,
  PRIMARY KEY (`id_notificacion`),
  CONSTRAINT `fk_notificaciones_usuario` FOREIGN KEY (`id_usuario`)
    REFERENCES `usuarios` (`id_usuario`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Entidad DER: documentos (id_tipo_documento reemplaza tipo_documento)
CREATE TABLE IF NOT EXISTS `documentos` (
  `id_documento` INT NOT NULL AUTO_INCREMENT,
  `id_categoria` INT NOT NULL,
  `id_usuario` INT NOT NULL,
  `id_tipo_documento` INT NOT NULL,
  `nombre` VARCHAR(50) NOT NULL,
  `descripcion` VARCHAR(200) NOT NULL,
  `path` VARCHAR(200) NOT NULL,
  `tamanio` BIGINT NOT NULL COMMENT 'Tamano del archivo en bytes',
  `fecha_carga` DATETIME NOT NULL,
  PRIMARY KEY (`id_documento`),
  CONSTRAINT `fk_documentos_categoria` FOREIGN KEY (`id_categoria`)
    REFERENCES `categorias` (`id_categoria`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_documentos_usuario` FOREIGN KEY (`id_usuario`)
    REFERENCES `usuarios` (`id_usuario`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_documentos_tipo` FOREIGN KEY (`id_tipo_documento`)
    REFERENCES `tipos_documento` (`id_tipo_documento`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Entidad DER: clasificacion_ia (admite multiples registros por documento)
CREATE TABLE IF NOT EXISTS `clasificacion_ia` (
  `id_clasificacion` INT NOT NULL AUTO_INCREMENT,
  `id_documento` INT NOT NULL,
  `id_proveedor` INT NOT NULL,
  `categoria_sugerida` VARCHAR(200) NOT NULL,
  `confianza` DECIMAL(5,2) NOT NULL,
  `observaciones` VARCHAR(200) NOT NULL,
  `fecha_clasificacion` DATETIME NOT NULL,
  PRIMARY KEY (`id_clasificacion`),
  CONSTRAINT `chk_clasificacion_ia_confianza` CHECK (`confianza` >= 0 AND `confianza` <= 1),
  CONSTRAINT `fk_clasificacion_ia_documento` FOREIGN KEY (`id_documento`)
    REFERENCES `documentos` (`id_documento`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_clasificacion_ia_proveedor` FOREIGN KEY (`id_proveedor`)
    REFERENCES `proveedor_ia` (`id_proveedor`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- VERIFICACIONES (solo lectura)
SHOW TABLES FROM `sgdi`;
DESCRIBE `usuarios`;
DESCRIBE `roles`;
DESCRIBE `usuarios_roles`;
DESCRIBE `categorias`;
DESCRIBE `tipos_documento`;
DESCRIBE `documentos`;
DESCRIBE `notificaciones`;
DESCRIBE `proveedor_ia`;
DESCRIBE `clasificacion_ia`;

-- Inspeccion de las claves foraneas, sus referencias y reglas referenciales.
SELECT k.TABLE_NAME AS tabla_hija,
       k.COLUMN_NAME AS columna_fk,
       k.REFERENCED_TABLE_NAME AS tabla_padre,
       k.REFERENCED_COLUMN_NAME AS columna_referenciada,
       r.UPDATE_RULE AS on_update,
       r.DELETE_RULE AS on_delete
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS k
JOIN INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS AS r
  ON r.CONSTRAINT_SCHEMA = k.CONSTRAINT_SCHEMA
 AND r.TABLE_NAME = k.TABLE_NAME
 AND r.CONSTRAINT_NAME = k.CONSTRAINT_NAME
WHERE k.TABLE_SCHEMA = 'sgdi'
  AND k.REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY k.TABLE_NAME, k.CONSTRAINT_NAME, k.ORDINAL_POSITION;

-- Inspeccion de restricciones CHECK.
SELECT tc.TABLE_NAME, tc.CONSTRAINT_NAME, cc.CHECK_CLAUSE
FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS tc
JOIN INFORMATION_SCHEMA.CHECK_CONSTRAINTS AS cc
  ON cc.CONSTRAINT_SCHEMA = tc.CONSTRAINT_SCHEMA
 AND cc.CONSTRAINT_NAME = tc.CONSTRAINT_NAME
WHERE tc.TABLE_SCHEMA = 'sgdi' AND tc.CONSTRAINT_TYPE = 'CHECK';

-- Inspeccion del DDL efectivo, especialmente util si la tabla ya existia.
SHOW CREATE TABLE `clasificacion_ia`;
