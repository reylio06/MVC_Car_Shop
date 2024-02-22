Create a MVC App for a car shop with the following functionalities:

 

1.            Add a new car

2.            Update a car

3.            Delete a car

4.            Get car details by id -> CarDetailDTO

5.            Get cars from a given manufacturer -> CarManufacturerDTO

6.            Get cars with price lower than a given value -> CarDTO

7.            Get all cars -> CarDTO

 

Model class:

Car

id: int

manufacturer: string

model: String

price: long

productionDate: Timestamp

fuelType: String – recommended to create an ENUM with the allowed values

horsepower: int

 

 

Other technical requirements:

-              ExceptionHandling

-              Classes split in at least 3 packages (model, view, controller)

-              Cars must be written in a  file

 

 

Model:

class Vehicle as abstract class with attributes:  id, productionDate, price

class Car with the remaining attributes listed above and 3 methods that convert the entity to a DTO (toCarDetailDTO...)

Note: we will extend the shop to sell different types of vehicles in the future

 

View:

At least one class that takes care of displaying the menu, and I/O interaction with the user

View has access only to Controller classes.

 

Controller:

Following DTO(Data Transfer Object) classes:

               CarDetailDTO - has all attibutes of a car

               CarManufacturerDTO - with id, model, price

               CarDTO - with id, manufacturer, model, price

              

A CarController class that ensures the connection between CarDAO and View classes.

 

A CarDAO class that reads and writes cars to a csv file.

DAO - Data Access Object

Exception handling

 

 

Example:

 

1, VW, Golf VII, 15000, 20.12.2018, diesel, 101

2, BMW, X3, 20000, 14.03.2017, gasoline , 130

3, VW, Passat, 21000, 20.12.2020, diesel, 125

 

Option 5: BMW

 

2, X3, 20000

 

Option 6: 20000

1, VW, Golf VII, 15000
