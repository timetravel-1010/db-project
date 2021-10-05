CREATE TABLE Ciudad(
	nombre varchar(100) PRIMARY KEY
);

CREATE TABLE Teatro(
	idTeatro integer PRIMARY KEY, 
	ventas integer NOT NULL DEFAULT 0
);

CREATE TABLE TeatrosCiudad(
	nombreCiudad varchar(100) REFERENCES Ciudad(nombre),
	idTeatro integer REFERENCES Teatro(idTeatro)
);

CREATE TABLE Ventas(
	idVenta serial PRIMARY KEY, 
	valorVenta integer, -- total de la venta
	valorBoleta integer,
	fechaVenta timestamp DEFAULT current_timestamp,
	cantidadBoleta integer,
	idTeatro integer REFERENCES Teatro(idTeatro)
); 

CREATE TYPE tipo_sala AS ENUM ('3D', 'normal'); 

CREATE TABLE Sala(
	idSala char(6) PRIMARY KEY,
	idTeatro integer REFERENCES Teatro(idTeatro),
	tipoSala tipo_sala,
	capacidad_sillas integer
);

CREATE TABLE Funciones(
	idFuncion char(2) PRIMARY KEY,
	cantidadBoletas integer,
	fecha date,
	pelicula varchar(100),
	idSala char(6) REFERENCES Sala(idSala)
);

CREATE TABLE Usuario(
	idUsuario serial PRIMARY KEY,
	email varchar(50) NOT NULL UNIQUE,
	nombreUsuario varchar(100) NOT NULL UNIQUE,
	Clave varchar(100) NOT NULL
);


CREATE TYPE tipo_silleteria AS ENUM ('VIP', 'general');

CREATE TABLE Boleta(
	idBoleta serial PRIMARY KEY, -- bo-01,...,bo-99
	precioBoleta integer,
	silla char(2), -- A1, ..., A15, B1,...,B7,...
	idUsuario integer REFERENCES Usuario(idUsuario),
	tipoSilleteria varchar(7),
	idFuncion char(9) REFERENCES Funciones(idFuncion),
	idVenta int REFERENCES Ventas(idVenta)  
);
SELECT * FROM funciones;
INSERT INTO Ciudad (nombre) VALUES ('Cali'), ('Barranquilla'), ('Pasto');

INSERT INTO Teatro (idTeatro) VALUES (1), (2), (3);

INSERT INTO TeatrosCiudad (nombreCiudad, idTeatro)
VALUES ('Cali', 1), ('Barranquilla', 2), ('Pasto', 3);

INSERT INTO Sala (idSala, idTeatro, tipoSala, capacidad_sillas)
VALUES  ('sala01', 1, '3D', 100),
		('sala02', 1, 'normal', 60),
		('sala03', 2, '3D', 100),
		('sala04', 2, 'normal', 60),
		('sala05', 3, '3D', 100),
		('sala06', 3, 'normal', 60);

SET DATESTYLE TO 'European';

INSERT INTO Funciones (idFuncion, cantidadBoletas, fecha, pelicula, idSala)
VALUES 	('01', 100, '08-03-2015', 'La Purga', 'sala01'),
		('02', 60, '01-10-2021', 'IA: Inteligencia Artificial', 'sala02'),
		('03', 100, '01-10-2021', 'Las Vidas Posibles de Mr. Nobody', 'sala03'),
		('04', 60, '02-05-2011', 'Lolita', 'sala04'),
		('05', 100, current_date, '2001: Una Odisea del Espacio', 'sala05'),
		('06', 60, '08-03-2018', 'Alien', 'sala06'),
		('07', 100, '17-04-2017', 'Actividad Paranormal LV', 'sala01'),
		('08', 60, '08-03-2018', 'Alien', 'sala02'),
		('09', 60, current_date, 'The Boys', 'sala02'),
		('10', 100, current_date, 'A Clockwork Orange', 'sala01');

INSERT INTO Usuario (email, nombreUsuario, Clave)
VALUES  ('user01@dot.exe', 'user01', '123'),
		('user02@dot.exe', 'user02', '123'),
		('user03@dot.exe', 'user03', '123'),
		('user04@dot.exe', 'user04', '123');
/*
INSERT INTO boleta (idBoleta, idfuncion, idVenta) 
VALUES 	('06', 1), ('06', 1), ('06', 1),
		('06', 2), ('06', 2), ('01', 2), 
		('01', 3), ('01', 3), ('01', 3);
*/
	
	SELECT * FROM usuario;
	
INSERT INTO boleta (idBoleta, idfuncion)--, idVenta) 
VALUES 	('bo-01', '06'),--, 1), ('bo-02', '06', 1), ('bo-03', '06', 1),
		('bo-04', '06'),--, 2), ('bo-05', '06', 2), ('bo-06', '01', 2), 
		('bo-07', '01');--, 3), ('bo-08', '01', 3), ('bo-09', '01', 3);
	
/* Consultas */	

-- Funciones presentadas entre el año 2000 y el 2020 (incluidos).
SELECT * FROM funciones
WHERE fecha BETWEEN '01/01/2000' AND  '31/12/2020';

-- Salas en las que se ha presentado la pelicula 'Alien' con fecha 08/03/2018.
SELECT sala.idsala, teatro.idteatro, pelicula, fecha 
FROM Teatro, funciones f, sala
WHERE 	sala.idsala = f.idsala AND 
		f.pelicula = 'Alien' AND 
		fecha = '08/03/2018' AND 
		sala.idteatro = teatro.idteatro;
select * from buscarPelicula(?, TO_DATE(?, 'DD MM YYYY'));
select * from buscarPelicula('Alien', TO_DATE('08/03/2018', 'DD MM YYYY'));

CREATE OR REPLACE FUNCTION buscarPelicula(nombreP varchar(100), fechaP date) 
returns table(idsala sala.idsala%type, idteatro int, pelicula funciones.pelicula%type, fecha funciones.fecha%type)
as $$			
DECLARE 
	fila record;
BEGIN
	
	FOR fila IN (SELECT sala.idsala, teatro.idteatro, f.pelicula, f.fecha 
				FROM Teatro, funciones f, sala
				WHERE 	sala.idsala = f.idsala AND 
				f.pelicula = nombreP AND 
				f.fecha = fechaP AND 
				sala.idteatro = teatro.idteatro)
	LOOP 
	idsala := fila.idsala;
	idteatro := fila.idteatro;
	pelicula := fila.pelicula;
	fecha := fila.fecha; -- SE HARA UN MACHING PARA RELACIONAR LAS VARIABLES clave la cual se creo y la variable clave de la tabla fila, que se creo en la consulta
	RETURN NEXT; -- PARA QUE LA VARIABLE FILA RECORRA LA CONSULTA
	END LOOP;
	
END;
$$
LANGUAGE plpgsql;

SELECT * FROM funciones;

-- 	Cuantas sillas hay disponibles en una sala y una funcion particular.
SELECT (s.capacidad_sillas - count(idboleta)) AS sillas_disponibles
FROM boleta b, funciones f, sala s 
WHERE b.idfuncion = f.idfuncion AND 
f.idsala = s.idsala AND 
s.idsala  = 'sala01' AND 
f.idfuncion = '10'
GROUP BY (s.capacidad_sillas); 
SELECT * FROM boleta;
-- Cantidad de boletas que ha vendido cada sala.
CREATE VIEW cantidad_boletas_vendidas AS 
SELECT s.idsala, count(b.idboleta) AS cantidad_boletas FROM sala s, boleta b, funciones f 
WHERE f.idsala = s.idsala AND 
		b.idfuncion = f.idfuncion
	GROUP BY(s.idsala);
	

select idSala, cantidad_boletas from salaVentas(TO_DATE('11/?/2021', 'DD MM YYYY'));-- consulta de java.
-- pasar el argumento del mes -> EXTRACT( MONTH from  TO_DATE('11/mes/2021', 'DD MM YYYY'), mes -> argumento que llega de la interfaz
-- Sala que mas boletas ha vendido en el mes.
SELECT s.idsala, cbv.cantidad_boletas as cantidad_boletas 
FROM sala s, boleta b, funciones f, cantidad_boletas_vendidas cbv
WHERE 	f.idsala = s.idsala AND 
		b.idfuncion = f.idfuncion AND 
		EXTRACT(MONTH FROM f.fecha) = '10' AND
		(SELECT max(cantidad_boletas) FROM cantidad_boletas_vendidas) = cbv.cantidad_boletas AND 
		s.idsala = cbv.idsala
	GROUP BY(s.idsala, cantidad_boletas);

select * from cantidad_boletas_vendidas cbv ;

--Funcion de salaVentas

CREATE OR REPLACE FUNCTION salaVentas(fechaV date) 
returns table(idsala sala.idsala%type, cantidadBoletas int )
as $$			
DECLARE 
	fila record;
BEGIN
	
	FOR fila IN (SELECT s.idsala, cbv.cantidad_boletas as cantidad_boletas 
				FROM sala s, boleta b, funciones f, cantidad_boletas_vendidas cbv
				WHERE 	f.idsala = s.idsala AND 
						b.idfuncion = f.idfuncion AND 
						EXTRACT(MONTH FROM fechaV) = EXTRACT(MONTH FROM f.fecha) AND
						(SELECT max(cantidad_boletas) FROM cantidad_boletas_vendidas) = cbv.cantidad_boletas AND 
						s.idsala = cbv.idsala
					GROUP BY(s.idsala, cantidad_boletas))
	LOOP 
	idsala := fila.idsala;
	cantidadBoletas := fila.cantidad_boletas;
	 -- SE HARA UN MACHING PARA RELACIONAR LAS VARIABLES clave la cual se creo y la variable clave de la tabla fila, que se creo en la consulta
	RETURN NEXT; -- PARA QUE LA VARIABLE FILA RECORRA LA CONSULTA
	END LOOP;
	
END;
$$
LANGUAGE plpgsql;
select * from salaVentas(TO_DATE('02/10/2021', 'DD MM YYYY'));
select * from salaVentas('02/10/2021');
select * from funciones f;

-- Funcion usada para establecer el precio de una boleta dependiendo de su tipoSilleteria.
CREATE OR REPLACE FUNCTION setPrecioBoleta()
RETURNS TRIGGER 
LANGUAGE plpgsql
AS $$
BEGIN 
	-- cuando se inserta un nuevo registro en Boleta.
	IF NEW.tipoSilleteria = 'VIP' THEN 
	
		UPDATE Boleta 
		SET precioBoleta = 7000
		WHERE idBoleta = NEW.idBoleta;
	ELSE 
		UPDATE Boleta 
		SET precioBoleta = 3500
		WHERE idBoleta = NEW.idBoleta;
	END IF;
	
	RETURN NEW;
END;
$$;	

-- Trigger usado para establecer el precio de una boleta.
CREATE TRIGGER precioBoleta 
	AFTER INSERT ON Boleta
	FOR EACH ROW EXECUTE PROCEDURE setPrecioBoleta(); 

-- Un listado de los teatros con el valor de la venta por tipo de silleteria en 1 mes.
CREATE VIEW ventasTeatro AS 
SELECT t.idteatro, b.tiposilleteria, sum(b.precioboleta) AS valorVenta --v.valorventa AS valor_venta
FROM teatro t, ventas v, boleta b 
WHERE v.idteatro = t.idteatro AND 
		EXTRACT(MONTH FROM v.fechaventa) = EXTRACT(MONTH FROM current_date) AND 
		b.idVenta = v.idVenta
	GROUP BY (t.idteatro, b.tiposilleteria);
SELECT * FROM ventasTeatro;

-- Vista para obtener el valor total de cada venta.
CREATE VIEW valor_ventas AS 
SELECT sum(b.precioboleta) AS valor_venta, v.idventa FROM boleta b, ventas v
WHERE b.idVenta = v.idventa
GROUP BY (v.idventa);

-- Actualizacion del valor total de cada venta.
UPDATE ventas AS v
SET valorventa = vv.valor_venta
FROM valor_ventas vv
WHERE vv.idVenta = v.idventa;


