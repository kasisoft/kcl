CREATE SCHEMA IF NOT EXISTS hr;

USE hr;

DROP TABLE IF EXISTS countries;
CREATE TABLE countries (
   CountryISOCode varchar(2) default NULL,
   CountryName varchar(40) default NULL,
   RegionID int(11) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO countries VALUES 
 ('AR','Argentina',2),
 ('AU','Australia',3),
 ('BE','Belgium',1),
 ('BR','Brazil',2),
 ('CA','Canada',2),
 ('CH','Switzerland',1),
 ('CN','China',3),
 ('DE','Germany',1),
 ('DK','Denmark',1),
 ('EG','Egypt',4),
 ('FR','France',1),
 ('HK','HongKong',3),
 ('IL','Israel',4),
 ('IN','India',3),
 ('IT','Italy',1),
 ('JP','Japan',3),
 ('KW','Kuwait',4),
 ('MX','Mexico',2),
 ('NG','Nigeria',4),
 ('NL','Netherlands',1),
 ('SG','Singapore',3),
 ('UK','United Kingdom',1),
 ('US','United States of America',2),
 ('ZM','Zambia',4),
 ('ZW','Zimbabwe',4);

DROP TABLE IF EXISTS departments;
CREATE TABLE departments (
   DepartmentID int(11) default NULL,
   DepartmentName varchar(30) default NULL,
   ManagerID int(11) default NULL,
   LocationID int(11) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO departments VALUES 
 (10,'Administration',200,1700),
 (20,'Marketing',201,1800),
 (30,'Purchasing',114,1700),
 (40,'Human Resources',203,2400),
 (50,'Shipping',121,1500),
 (60,'IT',103,1400),
 (70,'Public Relations',204,2700),
 (80,'Sales',145,2500),
 (90,'Executive',100,1700),
 (100,'Finance',108,1700),
 (110,'Accounting',205,1700),
 (120,'Treasury',NULL,1700),
 (130,'Corporate Tax',NULL,1700),
 (140,'Control And Credit',NULL,1700),
 (150,'Shareholder Services',NULL,1700),
 (160,'Benefits',NULL,1700),
 (170,'Manufacturing',NULL,1700),
 (180,'Construction',NULL,1700),
 (190,'Contracting',NULL,1700),
 (200,'Operations',NULL,1700),
 (210,'IT Support',NULL,1700),
 (220,'NOC',NULL,1700),
 (230,'IT Helpdesk',NULL,1700),
 (240,'Government Sales',NULL,1700),
 (250,'Retail Sales',NULL,1700),
 (260,'Recruiting',NULL,1700),
 (270,'Payroll',NULL,1700);
 
DROP TABLE IF EXISTS employees;
CREATE TABLE employees (
   EmployeeID int(11) default NULL,
   FirstName varchar(20) default NULL,
   LastName varchar(25) default NULL,
   Email varchar(25) default NULL,
   PhoneNumber varchar(20) default NULL,
   HireDate datetime default NULL,
   JobCode varchar(10) default NULL,
   Salary double default NULL,
   CommissionPercentage double default NULL,
   ManagerID int(11) default NULL,
   DepartmentID int(11) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
INSERT INTO employees VALUES 
 (198,'Donald','OConnell','DOCONNEL','650.507.9833','1999-06-21 00:00:00','SH_CLERK',2600,NULL,124,50),
 (199,'Douglas','Grant','DGRANT','650.507.9844','2000-01-13 00:00:00','SH_CLERK',2600,NULL,124,50),
 (200,'Jennifer','Whalen','JWHALEN','515.123.4444','1987-09-17 00:00:00','AD_ASST',4400,NULL,101,10),
 (201,'Michael','Hartstein','MHARTSTE','515.123.5555','1996-02-17 00:00:00','MK_MAN',13000,NULL,100,20),
 (202,'Pat','Fay','PFAY','603.123.6666','1997-08-17 00:00:00','MK_REP',6000,NULL,201,20),
 (203,'Susan','Mavris','SMAVRIS','515.123.7777','1994-06-07 00:00:00','HR_REP',6500,NULL,101,40),
 (204,'Hermann','Baer','HBAER','515.123.8888','1994-06-07 00:00:00','PR_REP',10000,NULL,101,70),
 (205,'Shelley','Higgins','SHIGGINS','515.123.8080','1994-06-07 00:00:00','AC_MGR',12000,NULL,101,110),
 (206,'William','Gietz','WGIETZ','515.123.8181','1994-06-07 00:00:00','AC_ACCOUNT',8300,NULL,205,110),
 (100,'Steven','King','SKING','515.123.4567','1987-06-17 00:00:00','AD_PRES',24000,NULL,NULL,90),
 (101,'Neena','Kochhar','NKOCHHAR','515.123.4568','1989-09-21 00:00:00','AD_VP',17000,NULL,100,90),
 (102,'Lex','De Haan','LDEHAAN','515.123.4569','1993-01-13 00:00:00','AD_VP',17000,NULL,100,90),
 (103,'Alexander','Hunold','AHUNOLD','590.423.4567','1990-01-03 00:00:00','IT_PROG',9000,NULL,102,60),
 (104,'Bruce','Ernst','BERNST','590.423.4568','1991-05-21 00:00:00','IT_PROG',6000,NULL,103,60),
 (105,'David','Austin','DAUSTIN','590.423.4569','1997-06-25 00:00:00','IT_PROG',4800,NULL,103,60),
 (106,'Valli','Pataballa','VPATABAL','590.423.4560','1998-02-05 00:00:00','IT_PROG',4800,NULL,103,60),
 (107,'Diana','Lorentz','DLORENTZ','590.423.5567','1999-02-07 00:00:00','IT_PROG',4200,NULL,103,60),
 (108,'Nancy','Greenberg','NGREENBE','515.124.4569','1994-08-17 00:00:00','FI_MGR',12000,NULL,101,100),
 (109,'Daniel','Faviet','DFAVIET','515.124.4169','1994-08-16 00:00:00','FI_ACCOUNT',9000,NULL,108,100),
 (110,'John','Chen','JCHEN','515.124.4269','1997-09-28 00:00:00','FI_ACCOUNT',8200,NULL,108,100),
 (111,'Ismael','Sciarra','ISCIARRA','515.124.4369','1997-09-30 00:00:00','FI_ACCOUNT',7700,NULL,108,100),
 (112,'Jose Manuel','Urman','JMURMAN','515.124.4469','1998-03-07 00:00:00','FI_ACCOUNT',7800,NULL,108,100),
 (113,'Luis','Popp','LPOPP','515.124.4567','1999-12-07 00:00:00','FI_ACCOUNT',6900,NULL,108,100),
 (114,'Den','Raphaely','DRAPHEAL','515.127.4561','1994-12-07 00:00:00','PU_MAN',11000,NULL,100,30),
 (115,'Alexander','Khoo','AKHOO','515.127.4562','1995-05-18 00:00:00','PU_CLERK',3100,NULL,114,30),
 (116,'Shelli','Baida','SBAIDA','515.127.4563','1997-12-24 00:00:00','PU_CLERK',2900,NULL,114,30),
 (117,'Sigal','Tobias','STOBIAS','515.127.4564','1997-07-24 00:00:00','PU_CLERK',2800,NULL,114,30),
 (118,'Guy','Himuro','GHIMURO','515.127.4565','1998-11-15 00:00:00','PU_CLERK',2600,NULL,114,30),
 (119,'Karen','Colmenares','KCOLMENA','515.127.4566','1999-08-10 00:00:00','PU_CLERK',2500,NULL,114,30),
 (120,'Matthew','Weiss','MWEISS','650.123.1234','1996-07-18 00:00:00','ST_MAN',8000,NULL,100,50),
 (121,'Adam','Fripp','AFRIPP','650.123.2234','1997-04-10 00:00:00','ST_MAN',8200,NULL,100,50),
 (122,'Payam','Kaufling','PKAUFLIN','650.123.3234','1995-05-01 00:00:00','ST_MAN',7900,NULL,100,50),
 (123,'Shanta','Vollman','SVOLLMAN','650.123.4234','1997-10-10 00:00:00','ST_MAN',6500,NULL,100,50),
 (124,'Kevin','Mourgos','KMOURGOS','650.123.5234','1999-11-16 00:00:00','ST_MAN',5800,NULL,100,50),
 (125,'Julia','Nayer','JNAYER','650.124.1214','1997-07-16 00:00:00','ST_CLERK',3200,NULL,120,50),
 (126,'Irene','Mikkilineni','IMIKKILI','650.124.1224','1998-09-28 00:00:00','ST_CLERK',2700,NULL,120,50),
 (127,'James','Landry','JLANDRY','650.124.1334','1999-01-14 00:00:00','ST_CLERK',2400,NULL,120,50),
 (128,'Steven','Markle','SMARKLE','650.124.1434','2000-03-08 00:00:00','ST_CLERK',2200,NULL,120,50),
 (129,'Laura','Bissot','LBISSOT','650.124.5234','1997-08-20 00:00:00','ST_CLERK',3300,NULL,121,50),
 (130,'Mozhe','Atkinson','MATKINSO','650.124.6234','1997-10-30 00:00:00','ST_CLERK',2800,NULL,121,50),
 (131,'James','Marlow','JAMRLOW','650.124.7234','1997-02-16 00:00:00','ST_CLERK',2500,NULL,121,50),
 (132,'TJ','Olson','TJOLSON','650.124.8234','1999-04-10 00:00:00','ST_CLERK',2100,NULL,121,50),
 (133,'Jason','Mallin','JMALLIN','650.127.1934','1996-06-14 00:00:00','ST_CLERK',3300,NULL,122,50),
 (134,'Michael','Rogers','MROGERS','650.127.1834','1998-08-26 00:00:00','ST_CLERK',2900,NULL,122,50),
 (135,'Ki','Gee','KGEE','650.127.1734','1999-12-12 00:00:00','ST_CLERK',2400,NULL,122,50),
 (136,'Hazel','Philtanker','HPHILTAN','650.127.1634','2000-02-06 00:00:00','ST_CLERK',2200,NULL,122,50),
 (137,'Renske','Ladwig','RLADWIG','650.121.1234','1995-07-14 00:00:00','ST_CLERK',3600,NULL,123,50),
 (138,'Stephen','Stiles','SSTILES','650.121.2034','1997-10-26 00:00:00','ST_CLERK',3200,NULL,123,50),
 (139,'John','Seo','JSEO','650.121.2019','1998-02-12 00:00:00','ST_CLERK',2700,NULL,123,50),
 (140,'Joshua','Patel','JPATEL','650.121.1834','1998-04-06 00:00:00','ST_CLERK',2500,NULL,123,50);

DROP TABLE IF EXISTS job_history;
CREATE TABLE job_history (
   EmployeeID int(11) default NULL,
   StartDate datetime default NULL,
   EndDate datetime default NULL,
   JobCode varchar(10) default NULL,
   DepartmentID int(11) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
INSERT INTO job_history VALUES 
 (102,'1993-01-13 00:00:00','1998-07-24 00:00:00','IT_PROG',60),
 (101,'1989-09-21 00:00:00','1993-10-27 00:00:00','AC_ACCOUNT',110),
 (101,'1993-10-28 00:00:00','1997-03-15 00:00:00','AC_MGR',110),
 (201,'1996-02-17 00:00:00','1999-12-19 00:00:00','MK_REP',20),
 (114,'1998-03-24 00:00:00','1999-12-31 00:00:00','ST_CLERK',50),
 (122,'1999-01-01 00:00:00','1999-12-31 00:00:00','ST_CLERK',50),
 (200,'1987-09-17 00:00:00','1993-06-17 00:00:00','AD_ASST',90),
 (176,'1998-03-24 00:00:00','1998-12-31 00:00:00','SA_REP',80),
 (176,'1999-01-01 00:00:00','1999-12-31 00:00:00','SA_MAN',80),
 (200,'1994-07-01 00:00:00','1998-12-31 00:00:00','AC_ACCOUNT',90);

DROP TABLE IF EXISTS jobs;
CREATE TABLE jobs (
   JobCode varchar(10) default NULL,
   JobTitle varchar(35) default NULL,
   MinSalary int(11) default NULL,
   MaxSalary int(11) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
INSERT INTO jobs VALUES 
 ('AD_PRES','President',20000,40000),
 ('AD_VP','Administration Vice President',15000,30000),
 ('AD_ASST','Administration Assistant',3000,6000),
 ('FI_MGR','Finance Manager',8200,16000),
 ('FI_ACCOUNT','Accountant',4200,9000),
 ('AC_MGR','Accounting Manager',8200,16000),
 ('AC_ACCOUNT','Public Accountant',4200,9000),
 ('SA_MAN','Sales Manager',10000,20000),
 ('SA_REP','Sales Representative',6000,12000),
 ('PU_MAN','Purchasing Manager',8000,15000),
 ('PU_CLERK','Purchasing Clerk',2500,5500),
 ('ST_MAN','Stock Manager',5500,8500),
 ('ST_CLERK','Stock Clerk',2000,5000),
 ('SH_CLERK','Shipping Clerk',2500,5500),
 ('IT_PROG','Programmer',4000,10000),
 ('MK_MAN','Marketing Manager',9000,15000),
 ('MK_REP','Marketing Representative',4000,9000),
 ('HR_REP','Human Resources Representative',4000,9000),
 ('PR_REP','Public Relations Representative',4500,10500);
 
DROP TABLE IF EXISTS locations;
CREATE TABLE locations (
   LocationID int(11) default NULL,
   StreetAddress varchar(40) default NULL,
   PostalCode varchar(12) default NULL,
   City varchar(30) default NULL,
   StateProvince varchar(25) default NULL,
   CountryISOCode varchar(2) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
INSERT INTO locations VALUES 
 (1000,'1297 Via Cola di Rie','00989','Roma','','IT'),
 (1100,'93091 Calle della Testa','10934','Venice','','IT'),
 (1200,'2017 Shinjuku-ku','1689','Tokyo','Tokyo Prefecture','JP'),
 (1300,'9450 Kamiya-cho','6823','Hiroshima','','JP'),
 (1400,'2014 Jabberwocky Rd','26192','Southlake','Texas','US'),
 (1500,'2011 Interiors Blvd','99236','South San Francisco','California','US'),
 (1600,'2007 Zagora St','50090','South Brunswick','New Jersey','US'),
 (1700,'2004 Charade Rd','98199','Seattle','Washington','US'),
 (1800,'147 Spadina Ave','M5V 2L7','Toronto','Ontario','CA'),
 (1900,'6092 Boxwood St','YSW 9T2','Whitehorse','Yukon','CA'),
 (2000,'40-5-12 Laogianggen','190518','Beijing','','CN'),
 (2100,'1298 Vileparle (E)','490231','Bombay','Maharashtra','IN'),
 (2200,'12-98 Victoria Street','2901','Sydney','New South Wales','AU'),
 (2300,'198 Clementi North','540198','Singapore','','SG'),
 (2400,'8204 Arthur St','','London','','UK'),
 (2500,'Magdalen Centre, The Oxford Science Park','OX9 9ZB','Oxford','Oxford','UK'),
 (2600,'9702 Chester Road','09629850293','Stretford','Manchester','UK'),
 (2700,'Schwanthalerstr. 7031','80925','Munich','Bavaria','DE'),
 (2800,'Rua Frei Caneca 1360 ','01307-002','Sao Paulo','Sao Paulo','BR'),
 (2900,'20 Rue des Corps-Saints','1730','Geneva','Geneve','CH'),
 (3000,'Murtenstrasse 921','3095','Bern','BE','CH'),
 (3100,'Pieter Breughelstraat 837','3029SK','Utrecht','Utrecht','NL'),
 (3200,'Mariano Escobedo 9991','11932','Mexico City','Distrito Federal,','MX');
 
DROP TABLE IF EXISTS regions;
CREATE TABLE regions (
   RegionID int(11) default NULL,
   RegionName varchar(25) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
INSERT INTO regions VALUES 
 (1,'Europe'),
 (2,'Americas'),
 (3,'Asia'),
 (4,'Middle East and Africa');
