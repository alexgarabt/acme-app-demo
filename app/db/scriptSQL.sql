-- Reset database
--Derby does not support DROP TABLE IF EXISTS 

DROP TABLE PROCEDIMIENTOS;
DROP TABLE INCOMPATIBILIDADESDEPROCEDIMIENTOS;
DROP TABLE PROCEDIMIENTOSQUESUELENAPLICARSEJUNTOS;
DROP TABLE DESCRIPCIONESDEPROCEDIMIENTOS;
DROP TABLE CONSEJOS;
DROP TABLE LLAMADASNOCRITICAS;
DROP TABLE ACTIVACIONESDEOPERATIVOS;
DROP TABLE DECISIONESDETRASLADOAHOSPITAL;
DROP TABLE HOSPITALES;
DROP TABLE OPERATIVOS;
DROP TABLE LLAMADASCRITICAS;
DROP TABLE LLAMADASDEASEGURADOS;
DROP TABLE LLAMADASINFUNDADAS;
DROP TABLE LLAMADAS;
DROP TABLE POLIZASCONTRATADAS;
DROP TABLE POLIZAS;
DROP TABLE CONDUCTORESENTURNO;
DROP TABLE MEDICOSENTURNO;
DROP TABLE TURNOSDEOPERATIVO;
DROP TABLE OPERADORESENTURNO;
DROP TABLE TURNOSDEOPERADOR;
DROP TABLE CONSULTORIOS;
DROP TABLE VEHICULOS;
DROP TABLE MODELOS;
DROP TABLE MARCAS;

DROP TABLE ESTADOSDEVEHICULO;
DROP TABLE ESTADOSDEOPERATIVO;
DROP TABLE TIPOSDETURNODEOPERATIVO;
DROP TABLE TIPOSDETURNODEOPERADOR;

DROP TABLE DISPONIBILIDADES;
DROP TABLE VINCULACIONESCONLAEMPRESA;
DROP TABLE ROLESENEMPRESA;
DROP TABLE TIPOSDEDISPONIBILIDAD;
DROP TABLE TIPOSDEVINCULACION;
DROP TABLE TIPOSDEROL;
DROP TABLE EMPLEADOS;
DROP TABLE ASEGURADOS;
DROP TABLE PERSONAS;

DROP TABLE DIRECCIONES;

-- Datatype
create table DIRECCIONES
(
	Id SMALLINT not null,
	NombreDeLaVia VARCHAR(20) not null,
	Numero SMALLINT,
	Otros VARCHAR(20),
	CodigoPostal INT not null,
	Localidad VARCHAR(20) not null,
	Provincia VARCHAR(20) not null,
		PRIMARY KEY(Id)
);

-- Entity
create table PERSONAS
(
	Nif VARCHAR(9) not null primary key,
	Nombre VARCHAR(50) not null,
	Apellidos VARCHAR(50) not null,
	FechaDeNacimiento DATE not null,
	Telefono VARCHAR(12) not null,
	DireccionPostal SMALLINT not null,
		FOREIGN KEY(DireccionPostal) REFERENCES DIRECCIONES(Id)
);

--Entity
create table ASEGURADOS
(
	Nif VARCHAR(9) not null primary key,
	Sexo CHAR not null,
	numeroDeCuenta VARCHAR(24) not null,
	    FOREIGN KEY(Nif) REFERENCES PERSONAS(Nif)
);

-- Entity
create table EMPLEADOS
(
	Nif VARCHAR(9) not null primary key,
	FechaInicioEnEmpresa DATE not null,
	Password VARCHAR(10) not null,
            FOREIGN KEY(Nif) REFERENCES PERSONAS(Nif)
);

-- Enum
create table TIPOSDEROL
(
	IdTipo SMALLINT not null,
	NombreTipo VARCHAR(20) not null unique,
		PRIMARY KEY(IdTipo)
);

INSERT INTO TIPOSDEROL
VALUES  (1,'Gerente'),
        (2,'Operador'),
        (3,'Médico'),
        (4,'Conductor');

-- Enum
create table TIPOSDEVINCULACION
(
	IdTipo SMALLINT not null,
	NombreTipo VARCHAR(20) not null unique,
		PRIMARY KEY(IdTipo)

);

INSERT INTO TIPOSDEVINCULACION
VALUES  (1,'ConNormalidad'),
        (2,'FueraDeLaEmpresa'),
        (3,'EnERTE');

-- Enum
create table TIPOSDEDISPONIBILIDAD
(
	IdTipo SMALLINT not null,
	NombreTipo VARCHAR(20) not null unique,
		PRIMARY KEY(IdTipo)
);

INSERT INTO TIPOSDEDISPONIBILIDAD
VALUES  (1,'DeVacaciones'),
        (2,'DeBaja'),
	(3, 'Disponible');


-- Association
create table ROLESENEMPRESA
(
	FechaInicioEnPuesto DATE not null,
	Empleado VARCHAR(9) not null,
	FechaPermisoConduccion DATE,
	NumeroColegiadoMedico VARCHAR(9),
	Rol SMALLINT not null,
            FOREIGN KEY(Empleado) REFERENCES EMPLEADOS(Nif),
            FOREIGN KEY(Rol) REFERENCES TIPOSDEROL(IdTipo)
);

-- Association
create table VINCULACIONESCONLAEMPRESA
(
	FechaInicio DATE not null,
	FechaFin Date,
	Empleado VARCHAR(9) not null,
	Vinculo SMALLINT not null,
		FOREIGN KEY(Empleado) REFERENCES EMPLEADOS(Nif),
		FOREIGN KEY(Vinculo) REFERENCES TIPOSDEVINCULACION(IdTipo) 
);

-- Association
create table DISPONIBILIDADES
(
	FechaInicio DATE not null,
	FechaFin DATE,
	Empleado VARCHAR(9) not null,
	Disponibilidad SMALLINT not null,
		FOREIGN KEY(Empleado) REFERENCES EMPLEADOS(Nif),
		FOREIGN KEY(Disponibilidad) REFERENCES TIPOSDEDISPONIBILIDAD(IdTipo)
);

--Enum 
create table TIPOSDETURNODEOPERATIVO (
	IdTipo SMALLINT not null,
	NombreTipo VARCHAR(20) not null unique,
		PRIMARY KEY(IdTipo)

);

INSERT INTO TIPOSDETURNODEOPERATIVO
VALUES (1,'DeDia7'),
       (2,'DeNoche19');

--Enum 
create table TIPOSDETURNODEOPERADOR (
	IdTipo SMALLINT not null,
	NombreTipo VARCHAR(20) not null unique,
		PRIMARY KEY(IdTipo)
);

INSERT INTO TIPOSDETURNODEOPERADOR 
VALUES (1,'DeMañana7'),
       (2,'DeTarde15'),
       (3,'DeNoche23');

--Enum 
create table ESTADOSDEOPERATIVO (
	IdEstado SMALLINT not null,
	NombreEstado VARCHAR(20) not null unique,
		PRIMARY KEY(IdEstado)
);

INSERT INTO ESTADOSDEOPERATIVO 
VALUES (1,'disponible'),
       (2,'activado'),
       (3,'bloqueado');

--Enum 
create table ESTADOSDEVEHICULO (
	IdEstado SMALLINT not null,
	NombreEstado VARCHAR(20) not null unique,
		PRIMARY KEY(IdEstado)
);

INSERT INTO ESTADOSDEVEHICULO
VALUES (1,'enServicio'),
       (2,'enTaller'),
       (3,'deBaja');

create table MARCAS (
	Id SMALLINT not null,
	Nombre VARCHAR(20) not null unique,
		PRIMARY KEY(Id)
);

create table MODELOS (
	Id SMALLINT not null,
	Nombre VARCHAR(20) not null,
	Marca SMALLINT not null,
		PRIMARY KEY(Id),
		FOREIGN KEY(Marca) REFERENCES MARCAS(Id)
);

create table VEHICULOS (
	Matricula VARCHAR(8) not null,
	UbicacionLatitud REAL not null,
	UbicacionLongitud REAL not null,
	FechaAlta DATE not null,
	Estado SMALLINT not null,
	Modelo SMALLINT not null,
		PRIMARY KEY(Matricula),
		FOREIGN KEY(Estado) REFERENCES ESTADOSDEVEHICULO(IdEstado),
		FOREIGN KEY(Modelo) REFERENCES MODELOS(Id)
);

create table CONSULTORIOS (
	Id SMALLINT not null,
	Direccion SMALLINT not null,
		PRIMARY KEY(Id),
		FOREIGN KEY(Direccion) REFERENCES DIRECCIONES(Id)
);

create table TURNOSDEOPERADOR (
	Id INT not null,
	FechaTurno DATE not null,
	FechaCreacion DATE not null,
	TipoDeTurno SMALLINT not null,
		PRIMARY KEY(Id),
		FOREIGN KEY(TipoDeTurno) REFERENCES TIPOSDETURNODEOPERADOR(IdTipo)

);

create table OPERADORESENTURNO (
	Turno INT not null,
	Operador VARCHAR(9) not null,
		PRIMARY KEY(Turno,Operador),
		FOREIGN KEY(Turno) REFERENCES TURNOSDEOPERADOR(Id),
		FOREIGN KEY(Operador) REFERENCES EMPLEADOS(Nif)
);

create table TURNOSDEOPERATIVO (
	Id INT not null,
	FechaTurno DATE not null,
	FechaCreacion DATE not null,
	TipoDeTurno SMALLINT not null,
		PRIMARY KEY(Id),
		FOREIGN KEY(TipoDeTurno) REFERENCES TIPOSDETURNODEOPERATIVO(IdTipo)
);

create table MEDICOSENTURNO (
	Turno INT not null,
	Medico VARCHAR(9) not null,
		PRIMARY KEY(Turno,Medico),
		FOREIGN KEY(Turno) REFERENCES TURNOSDEOPERATIVO(Id),
		FOREIGN KEY(Medico) REFERENCES EMPLEADOS(Nif)
);

create table CONDUCTORESENTURNO (
	Turno INT not null,
	Conductor VARCHAR(9) not null,
		PRIMARY KEY(Turno,Conductor),
		FOREIGN KEY(Turno) REFERENCES TURNOSDEOPERATIVO(Id),
		FOREIGN KEY(Conductor) REFERENCES EMPLEADOS(Nif)
);

create table POLIZAS (
	NumeroPoliza VARCHAR(15) not null,
	FechaInicio DATE not null,
	FechaVencimiento DATE not null,
		PRIMARY KEY(NumeroPoliza)
);

create table POLIZASCONTRATADAS (
	Asegurado VARCHAR(9) not null,
	Poliza VARCHAR(15) not null,
		PRIMARY KEY(Asegurado,Poliza),
		FOREIGN KEY(Asegurado) REFERENCES ASEGURADOS(Nif),
		FOREIGN KEY(Poliza) REFERENCES POLIZAS(NumeroPoliza)
);


create table LLAMADAS (
	Id INT not null,
	NumeroTelefonoOrigen VARCHAR(12) not null,
	FechaInicio DATE not null,
	HoraInicio TIME not null,
	FechaFin DATE not null,
	HoraFin TIME not null,
	NombreComunicante VARCHAR(100) not null,
	AtendidaPor VARCHAR(9) not null,
		PRIMARY KEY(Id),
		FOREIGN KEY(AtendidaPor) REFERENCES EMPLEADOS(Nif)
);


create table LLAMADASINFUNDADAS (
	Id INT not null,
		PRIMARY KEY(Id),
		FOREIGN KEY(Id) REFERENCES LLAMADAS(Id)
);

create table LLAMADASDEASEGURADOS (
	Id INT not null,
	DescripcionDeLaEmergencia VARCHAR(255) not null,
	Paciente VARCHAR(9) not null,
		PRIMARY KEY(Id),
		FOREIGN KEY(Id) REFERENCES LLAMADAS(Id),
		FOREIGN KEY(Paciente) REFERENCES ASEGURADOS(Nif)
);

create table LLAMADASCRITICAS (
	Id INT not null,
		PRIMARY KEY(Id),
		FOREIGN KEY(Id) REFERENCES LLAMADAS(Id)
);

create table OPERATIVOS (
	Id INT not null,
	FechaCreacion DATE not null,
	Estado SMALLINT not null,
	Turno INT not null,
	Base SMALLINT not null,
	Vehiculo VARCHAR(8) not null,
	Conductor VARCHAR(9) not null,
	Medico VARCHAR(9) not null,
		PRIMARY KEY(Id),
		FOREIGN KEY(Estado) REFERENCES ESTADOSDEOPERATIVO(IdEstado),
		FOREIGN KEY(Turno) REFERENCES TURNOSDEOPERATIVO(Id),
		FOREIGN KEY(Base) REFERENCES CONSULTORIOS(Id),
		FOREIGN KEY(Vehiculo) REFERENCES VEHICULOS(Matricula),
		FOREIGN KEY(Conductor) REFERENCES EMPLEADOS(Nif),
		FOREIGN KEY(Medico) REFERENCES EMPLEADOS(Nif)
);

create table HOSPITALES (
	Id SMALLINT not null,
	Nombre VARCHAR(50) not null,
	Direccion SMALLINT not null,
		PRIMARY KEY(Id),
		FOREIGN KEY(Direccion) REFERENCES DIRECCIONES(Id)
);

create table DECISIONESDETRASLADOAHOSPITAL (
	Id INT not null,
	FechaInicioTraslado DATE not null,
	HoraInicioTraslado TIME not null,
	DestinoDeTraslado SMALLINT not null,
		PRIMARY KEY(Id),
		FOREIGN KEY(DestinoDeTraslado) REFERENCES HOSPITALES(Id)
);

create table ACTIVACIONESDEOPERATIVOS (
	Id INT not null,
	DireccionDondeAcudir SMALLINT not null,
	FechaActivación DATE not null,
	HoraActivación TIME not null,
	OperativoActivado INT not null,
	FechaSeHaceCargoMedico DATE,
	HoraSeHaceCargoMedico TIME,
	FechaCierre DATE,
	HoraCierre TIME,
	DecisionTrasladoAHospital INT,
		PRIMARY KEY(Id),
		FOREIGN KEY(DireccionDondeAcudir) REFERENCES DIRECCIONES(Id),
		FOREIGN KEY(DecisionTrasladoAHospital) REFERENCES DECISIONESDETRASLADOAHOSPITAL(Id),
		FOREIGN KEY(OperativoActivado) REFERENCES OPERATIVOS(Id)
);

create table LLAMADASNOCRITICAS (
	Id INT not null,
	EsLeve BOOLEAN not null,
	RequiereOperativo INT,
		PRIMARY KEY(Id),
		FOREIGN KEY(RequiereOperativo) REFERENCES ACTIVACIONESDEOPERATIVOS(Id)
);

create table CONSEJOS (
	Descripcion VARCHAR(255) not null,
	Resultado VARCHAR(255) not null,
	Soluciona BOOLEAN not null,
	Llamada INT not null,
		FOREIGN KEY(Llamada) REFERENCES LLAMADASNOCRITICAS(Id)
);

create table DESCRIPCIONESDEPROCEDIMIENTOS (
	Id SMALLINT not null,
	Nombre VARCHAR(50) not null,
	Descripcion VARCHAR(255) not null,
	Variantes VARCHAR(255) not null,
		PRIMARY KEY(Id)
);

create table PROCEDIMIENTOSQUESUELENAPLICARSEJUNTOS (
	Id1 SMALLINT not null,
	Id2 SMALLINT not null,
		PRIMARY KEY(Id1,Id2),
		FOREIGN KEY(Id1) REFERENCES DESCRIPCIONESDEPROCEDIMIENTOS(Id),
		FOREIGN KEY(Id2) REFERENCES DESCRIPCIONESDEPROCEDIMIENTOS(Id)
);

create table INCOMPATIBILIDADESDEPROCEDIMIENTOS (
	Id1 SMALLINT not null,
	Id2 SMALLINT not null,
		PRIMARY KEY(Id1,Id2),
		FOREIGN KEY(Id1) REFERENCES DESCRIPCIONESDEPROCEDIMIENTOS(Id),
		FOREIGN KEY(Id2) REFERENCES DESCRIPCIONESDEPROCEDIMIENTOS(Id)
);

create table PROCEDIMIENTOS (
	ResultadoObservado VARCHAR(255) not null,
	FechaInicio DATE not null,
	HoraInicio TIME not null,
	Descripcion SMALLINT not null,
	SeAplicaEn INT not null,
		FOREIGN KEY(Descripcion) REFERENCES DESCRIPCIONESDEPROCEDIMIENTOS(Id),
		FOREIGN KEY(SeAplicaEn) REFERENCES ACTIVACIONESDEOPERATIVOS(Id)
);

INSERT INTO DIRECCIONES VALUES 
(1,'Libertad', 4, 'Piso 11 B', 47140, 'Laguna de Duero', 'Valladolid'),
(2, 'Calle Principal', 123, 'Piso 2', 28001, 'Madrid', 'Madrid'),
(3, 'Avenida del Parque', 45, '', 41001, 'Sevilla', 'Sevilla'),
(4, 'Calle Gran Vía', 321, 'Local 5', 08001, 'Barcelona', 'Barcelona'),
(5, 'Calle Mayor', 67, '', 48001, 'Bilbao', 'Vizcaya');

INSERT INTO PERSONAS VALUES
('12345678A', 'Juan', 'García López', '1990-03-15', '123456789', 2),
('98765432B', 'María', 'Martínez Pérez', '1985-07-20', '987654321', 3),
('45678912C', 'Pedro', 'Fernández González', '1978-11-10', '456789123', 4),
('78912345D', 'Ana', 'Rodríguez Sánchez', '1995-05-25', '789123456', 5),
('32165498E', 'Carlos', 'López Martínez', '1982-09-05', '321654987', 1),
('11111111A', 'Carlos', 'Sánchez Pérez', '1992-04-10', '111111111', 2),
('22222222B', 'Laura', 'González Martínez', '1988-08-20', '222222222', 3),
('33333333C', 'Pedro', 'Gómez Ruiz', '1990-12-05', '333333333', 4),
('11111112A', 'Asier', 'García Anta', '2002-07-10', '111111191', 2),
('44444444D', 'Manuel', 'Rodríguez García', '2001-07-10', '111111192', 2),
('55555555D', 'Victor', 'Rodríguez García', '2008-07-10', '717272611', 4),
('66666666D', 'Augusto', 'Rodríguez García', '2001-07-10', '672262616', 1),
('77777777D', 'Izan', 'Rodríguez García', '2003-07-10', '818181818', 3);

INSERT INTO ASEGURADOS (
    Nif,
    Sexo,
    numeroDeCuenta
) VALUES 
('32165498E', 'h', 'ES7620770024003102575766'),
('11111112A', 'h', 'ES7620770024032322575766');

-- Añadir nuevas pólizas a la tabla POLIZAS
INSERT INTO POLIZAS (NumeroPoliza, FechaInicio, FechaVencimiento) VALUES
('POL123456', '2023-01-01', '2024-11-01'),
('POL654321', '2023-01-01', '2024-11-01'),
('POL789012', '2023-01-01', '2024-11-01'),
('POL210987', '2023-01-01', '2024-11-01');

-- Asignar dos pólizas a cada asegurado en la tabla POLIZASCONTRATADAS
INSERT INTO POLIZASCONTRATADAS (Asegurado, Poliza) VALUES
('32165498E', 'POL123456'),
('32165498E', 'POL654321'),
('11111112A', 'POL789012'),
('11111112A', 'POL210987');

INSERT INTO EMPLEADOS VALUES
('12345678A', '2020-01-15', 'pass123'), -- Juan García López
('98765432B', '2019-05-20', 'emp456'), -- María Martínez Pérez
('45678912C', '2018-11-10', 'secure789'), -- Pedro Fernández González
('78912345D', '2021-03-25', 'key321'),  -- Ana Rodríguez Sánchez
('11111111A', '2020-01-15', 'pass111'), -- Carlos Sánchez Pérez
('22222222B', '2019-05-20', 'pass222'),	-- Laura González Martínez
('33333333C', '2018-11-10', 'pass333'), -- Pedro Gómez Ruiz
('44444444D', '2014-11-10', 'pass444'), -- Manuel Rodríguez García
('55555555D', '2014-11-10', 'pass555'), -- Víctor Rodríguez García
('66666666D', '2014-11-10', 'pass666'), -- Augusto Rodríguez García
('77777777D', '2014-11-10', 'pass777'); -- izan Rodríguez García

INSERT INTO ROLESENEMPRESA VALUES
('2023-01-15', '12345678A','2021-01-12','111111111', 1), -- Juan García López, Gerente
('2022-03-10', '12345678A','2021-01-12','111111111', 2), -- Juan García López, Operador
('2022-01-05', '12345678A','2021-01-12','111111111', 3), -- Juan García López, Médico
('2020-01-15', '12345678A','2019-05-14', NULL, 4), -- Juan García López, Conductor
('2019-05-20', '98765432B',NULL, NULL, 2), -- María Martínez Pérez, Operador
('2018-11-10', '45678912C','2017-10-08', NULL, 4), -- Pedro Fernández González, Conductor
('2021-03-25', '78912345D','2020-02-23', NULL, 4), -- Ana Rodríguez Sánchez, Conductor
('2022-01-15', '11111111A',NULL, NULL, 2), -- Carlos Sánchez Pérez, Operador
('2023-03-20', '22222222B','2022-01-18', '222222222', 3), -- Laura González Martínez, Médico
('2021-06-10', '33333333C','2020-05-08',NULL, 4), -- Pedro Gómez Ruiz, Conductor
('2014-11-10', '44444444D',NULL,NULL, 2), -- Manuel Rodríguez García, Operador
('2014-11-10', '55555555D',NULL,NULL, 2), -- Victor Rodríguez García, Operador
('2014-11-10', '66666666D',NULL,NULL, 2), -- Augusto Rodríguez García, Operador
('2014-11-10', '77777777D',NULL,NULL, 2); -- Izan Rodríguez García, Operador

INSERT INTO VINCULACIONESCONLAEMPRESA VALUES
('2022-01-15', NULL, '12345678A', 1), -- Juan García López, Con Normalidad
('2020-06-08', '2022-01-15', '12345678A', 2), -- Juan García López, Fuera de la Empresa
('2020-01-15', '2020-06-08', '12345678A', 1), -- Juan García López, Con Normalidad
('2019-05-20', NULL, '98765432B', 2), -- María Martínez Pérez, Fuera de la Empresa
('2018-11-10', NULL, '45678912C', 3), -- Pedro Fernández González, En ERTE
('2021-03-25', NULL, '78912345D', 1), -- Ana Rodríguez Sánchez, Con Normalidad
('2020-01-15',NULL, '11111111A', 1), -- Carlos Sánchez Pérez, Disponible
('2019-05-20',NULL, '22222222B', 1), -- Laura González Martínez, Disponible
('2018-11-10',NULL, '33333333C', 1), -- Pedro Gómez Ruiz, Disponible
('2014-11-10',NULL, '44444444D', 1), -- Manuel Rodríguez García, Disponible
('2014-11-10',NULL, '55555555D', 1), -- Victor Rodríguez García, Disponible
('2014-11-10',NULL, '66666666D', 1), -- Augusto Rodríguez García, Disponible
('2014-11-10',NULL, '77777777D', 1); -- Izan Rodríguez García, Disponible

INSERT INTO DISPONIBILIDADES VALUES
('2023-01-15', NULL, '12345678A', 3), -- Juan García López, Disponible
('2020-01-15', '2023-01-15', '12345678A', 1), -- Juan García López, De Vacaciones
('2019-05-20', NULL, '98765432B', 3), -- María Martínez Pérez, Disponible
('2018-11-10', NULL, '45678912C', 2), -- Pedro Fernández González, De Baja
('2021-03-25', NULL, '78912345D', 1), -- Ana Rodríguez Sánchez, De Vacaciones
('2020-01-15',NULL, '11111111A', 3), -- Carlos Sánchez Pérez, Disponible
('2019-05-20',NULL, '22222222B', 3), -- Laura González Martínez, Disponible
('2018-11-10',NULL, '33333333C', 3), -- Pedro Gómez Ruiz, Disponible
('2014-11-10',NULL, '44444444D', 3), -- Manuel Rodríguez García, Disponible
('2014-11-10', NULL, '55555555D', 1), -- Victor Rodríguez García, De Vacaciones
('2014-11-10', NULL, '66666666D', 2), -- Augusto Rodríguez García, De Baja
('2014-11-10', NULL, '77777777D', 3); -- Izan Rodríguez García

INSERT INTO TURNOSDEOPERADOR VALUES
(1,'2020-03-12','2020-03-10',1),
-- TODO CAMBIAR FECHA ACTUAL y turno actual(1,2,3)
(2,'2024-07-01','2024-05-29',1),
(3,'2020-03-12','2020-03-10',2);


INSERT INTO OPERADORESENTURNO VALUES
(1,'11111111A'),
(1,'98765432B'),
(2,'44444444D'),
(3,'77777777D');

INSERT INTO TURNOSDEOPERATIVO VALUES
(1,'2020-03-12','2020-03-01',1),
(2,'2021-03-12','2021-03-01',1);

INSERT INTO CONDUCTORESENTURNO VALUES
(1,'33333333C'),
(2,'33333333C');

INSERT INTO MEDICOSENTURNO VALUES
(1,'22222222B'),
(2,'22222222B');

INSERT INTO MARCAS (Id, Nombre)
VALUES (1, 'Ferrari');

INSERT INTO MODELOS (Id, Nombre, Marca)
VALUES (1, '250 GT CALIFORNIA', 1);

INSERT INTO VEHICULOS (Matricula, UbicacionLatitud, UbicacionLongitud, FechaAlta, Estado, Modelo)
VALUES ('1234ABC', 40.4168, -3.7038, '2019-03-01', 1, 1);

INSERT INTO CONSULTORIOS (Id, Direccion)
VALUES (1, 2);

INSERT INTO OPERATIVOS (id,FechaCreacion, Estado, Turno, Base, Vehiculo, Conductor, Medico)
VALUES 
(1,'2019-03-01', 1, 1, 1, '1234ABC', '33333333C', '22222222B'),
(2,'2020-03-01', 1, 2, 1, '1234ABC', '33333333C', '22222222B');

INSERT INTO HOSPITALES (
    Id,
    Nombre,
    Direccion
) VALUES 
(1, 'Hospital General', 2);

INSERT INTO DECISIONESDETRASLADOAHOSPITAL (
    Id,
    FechaInicioTraslado,
    HoraInicioTraslado,
    DestinoDeTraslado
) VALUES 
(1, '2023-05-01', '08:35:00', 1);

INSERT INTO ACTIVACIONESDEOPERATIVOS (
    Id,
    DireccionDondeAcudir,
    FechaActivación,
    HoraActivación,
    OperativoActivado,
    FechaSeHaceCargoMedico,
    HoraSeHaceCargoMedico,
    FechaCierre,
    HoraCierre,
    DecisionTrasladoAHospital
) VALUES 
(1, 1, '2023-05-01', '08:00:00', 1, '2023-05-01', '08:30:00', '2023-05-01', '09:00:00', 1),
(2, 2, '2023-05-02', '10:00:00', 1, '2023-05-02', '10:30:00', '2023-05-02', '11:00:00', NULL);

INSERT INTO LLAMADAS (
    Id,
    NumeroTelefonoOrigen,
    FechaInicio,
    HoraInicio,
    FechaFin,
    HoraFin,
    NombreComunicante,
    AtendidaPor
) VALUES 
(1, '321654987', '2023-05-10', '10:00:00', '2023-05-10', '10:30:00', 'Carlos López Martínez', '11111111A'),
(2, '323232111', '2023-05-10', '12:00:00', '2023-05-10', '12:30:00', 'Asier García Anta', '11111111A');

INSERT INTO LLAMADASDEASEGURADOS (
    Id,
    DescripcionDeLaEmergencia,
    Paciente
) VALUES 
(1, 'Dolor leve en el pecho', '32165498E'),
(2, 'Rotura severa de rodilla', '11111112A');

INSERT INTO LLAMADASNOCRITICAS (
    Id,
    EsLeve,
    RequiereOperativo
) VALUES 
(1, TRUE, 1),
(2, TRUE, 2);

INSERT INTO CONSEJOS (
    Descripcion,
    Resultado,
    Soluciona,
    Llamada
) VALUES 
('Recomendar descanso y observación de síntomas', 'El paciente se curó', TRUE, 1),
('Sugerir visitar al médico de cabecera si los síntomas persisten', 'El paciente considerará la sugerencia', FALSE, 1),
('Sugerir visitar al médico pues puede llevar secuelas severas', 'El paciente irá al médico', FALSE, 2);
