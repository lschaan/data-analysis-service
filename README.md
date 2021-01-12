# Data Analysis Service

Service responsible for reading a **.dat** file in the input directory and outputting a **.done.dat** file with a
user-friendly summary in the output directory.

### Using the application

1. Run with gradle, using `./gradlew run`
2. Enter a **.dat** file in the **~/data/in** directory.
3. Access the **~/data/out** directory to check the results.

### Accepted format

- The **.dat** file that is entered in the directory must follow some rules:

1. There are 3 possible ID's, each representing a specific kind of data:

- **001**, for Salesman
- **002**, for Customer
- **003**, for Sale

If the line contains an invalid ID, the program will not accept it, and the file will be ignored.

2. These are the accepted layouts for each kind of data:

- 001çCNPJçNameçSalary
- 002çCPFçNameçBusinessArea
- 003çSale IDç[Item ID-Item Quantity-Item Price]çSalesman name

The format must be followed strictly, since the processing of the file will shut down completely if any errors are
found.






