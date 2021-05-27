Title: Appointment System
Purpose: Provide the user with an interface where it is possible to manage, add, update and delete records from customers and appointments that the customers might schedule. 
Author: David Vaca
Contact Information:
	Email: dvaca1@wgu.edu

Application Version: 1.0
Date of Revision: 12/23/2020

IDE version: IntelliJ Community 2020.3
JDK version: 11.0.9
JavaFX version: 11.0.2

Directions:
1.- Input login credentials on initial "Login" window. Error messages will be shown if the credentials are wrong or the connection with the database failed. 
2.- The "Main Screen" window will show three tabs located on the top left corner: "Appointments", "Customers" and "Reports".
3.- The appointments tab contains 3 tabs located on the bottom left corner: "All Appointments", "Appointments Appointments by month" and "Appointments by week".
	3.1.- The "All Appointments" tab contains the "Appointments" table with all the appointment records from the data base. 
		3.1.1.- Appointment records can be filter by any of the fields using the "Search" bar on the top right corner. 
		3.1.2.- The "Appointments" table can be refresh using the "Refresh" button beside the "Search" bar.
		3.1.3.- New appointment records can be added using the "Add" button located under the "Appointments" table.
			3.1.3.1.- The "Add Appointment" window will open after the "Add" button is pressed. 
			3.1.3.2.- The user will be required to fill out all the fields in order to save. 
			3.1.3.3.- The fields "Title", "Description", "Location" and "Type" are regular required fields, with the only condition of not be left empty. 
			3.1.3.4.- The "Contact", "Time", "Customer ID" and "User ID" are dynamic combo boxes, and will be populated with information from the data base. An option must be selected in order to save. 
			3.1.3.5.- The "Date" field is a date picker, and will only accept a valid option from the date picker.
			3.1.3.6.- The "Save" button will close the window and input the appointment record into the data base. 
			3.1.3.7.- The "Cancel" button will generate a message asking for confirmation in order to close the "Add Appointment" window. 
		3.1.4.- Appointment records can be updated using the "Update" button located under the "Appointments" table. 
			3.1.4.1.- The "Update Appointment" window will open after the "Update" button is pressed. 
			3.1.4.2.- The "Update Appointment" window will auto-populate all the fields, letting the user change any of them. 
			3.1.4.3.- The "Date" and "Time" fields will not auto populate if the date or time of the appointment record are before the current date of the system.  
			3.1.4.4.- The fields "Title", "Description", "Location" and "Type" are regular required fields, with the only condition of not be left empty. 
			3.1.4.5.- The "Contact", "Time", "Customer ID" and "User ID" are dynamic combo boxes, and will be populated with information from the data base. An option must be selected in order to save. 
			3.1.4.6.- The "Date" field is a date picker, and will only accept a valid option from the date picker.
			3.1.4.7.- The "Save" button will close the window and update the appointment record into the data base. 
			3.1.4.8.- The "Cancel" button will generate a message asking for confirmation in order to close the "Update Appointment" window. 
		3.1.5.- Appointment records can be deleted using the "Delete" button located under the "Appointments" table. 
			3.1.5.1.- When the "Delete" button is pressed the user will be asked for confirmation. 
			3.1.5.1.- Depending on the user response, the appointment record will be deleted or not. 
		3.1.6.- In order for the "Update" and "Delete" buttons to work, an appointment record from the "Appointments" table must be selected. 

	3.2.- The "Appointments by month" tab contains a view of the appointment records count per day Appointments by month.
		3.2.1.- A different month can be selected by using the controls on the bottom right corner. 
		3.2.2.- If a "Day" is double-clicked the "Appointments by week" tab will be selected and populated with information the week on which the selected "Day" belongs to. 
	3.3.- The "Appointments by week" tab contains a view of the appointment records per day by the schedules time. 
		3.3.1.- A different week can be selected by using the controls on the bottom right corner. 
		3.3.2.- If an appointment record is double-clicked the "All Appointments" tab will be selected and the clicked appointment will be selected in the "Appointments" table. 
4.- The customers table contains only the "Customers" table. 
	4.1.- Customer records can be filter by any of the fields using the "Search" bar on the top right corner. 
	4.2.- The "Customers" table can be refresh using the "Refresh" button beside the "Search" bar.
	4.3.- New customer records can be added using the "Add" button located under the "Customers" table.
		4.3.1.- The "Add Customer" window will open after the "Add" button is pressed. 
		4.3.2.- The user will be required to fill out all the fields in order to save. 
		4.3.3.- The fields "Name" and "Address" are regular required fields, with the only condition of not to be left empty. 
		4.3.4.- The "Postal Code" field has a limit and requirement of 5 characters.
		4.3.5.- The "Phone Number" filed can be 10 or 12 digits long but no more than 12 or less than 10.
		4.3.6.- The "Country" and "State" are dynamic combo boxes, and will be populated with information from the data base. An option must be selected in order to save. 
		4.3.7.- The "Save" button will close the window and input the customer record into the data base. 
		4.3.8.- The "Cancel" button will generate a message asking for confirmation in order to close the "Add Customer" window. 
	4.4.- Customer records can be updated using the "Update" button located under the "Customers" table. 
		4.4.1.- The "Update Customer" window will open after the "Update" button is pressed. 
		4.4.2.- The "Update Customer" window will auto-populate all the fields, letting the user change any of them. 
		4.4.3.- The fields "Name" and "Address" are regular required fields, with the only condition of not to be left empty. 
		4.4.4.- The "Postal Code" field has a limit and requirement of 5 characters.
		4.4.5.- The "Phone Number" filed can be 10 or 12 digits long but no more than 12 or less than 10.
		4.4.6.- The "Country" and "State" are dynamic combo boxes, and will be populated with information from the data base. An option must be selected in order to save. 
		4.4.7.- The "Save" button will close the window and update the customer record into the data base. 
		4.4.8.- The "Cancel" button will generate a message asking for confirmation in order to close the "Update Customer" window. 
	4.5.- Customer records can be deleted using the "Delete" button located under the "Customers" table. 
		4.5.1.- When the "Delete" button is pressed the user will be asked for confirmation. 
		4.5.1.- Depending on the user response, the customer record will be deleted or not. 
	4.6.- In order for the "Update" and "Delete" buttons to work, a customer record from the "Customers" table must be selected. 
5.- The "Reports" tab contains 3 tabs located on the bottom left corner: "Appointments by type", "Contact Schedules" and "Appointments by location".
	5.1.- The "Appointments by type" tab contains a view of the of the appointment records count per type Appointments by month.
		5.1.1.- A different year can be selected by using the controls on the bottom right corner. 
		5.1.2.- If a "type" is double-clicked the "Appointment Appointments by month" tab will be selected and populated with information of the double-clicked month. 
	5.2.- The "Contacts Schedule" tab contains a view of the appointments every contact has for each day of the selected week.
		5.2.1.- A different week can be selected by using the controls on the bottom right corner. 
		5.2.2.- If an appointment record is double-clicked the "All Appointments" tab will be selected and the clicked appointment will be selected in the "Appointments" table. 
		5.2.3.- In order to view the appointment records, is necessary to select a "Contact" name from the choice box  on the top. 
	5.3.- The "Appointments by location" tab contains a view of the of the appointment records count per location Appointments by month.
		5.3.1.- A different year can be selected by using the controls on the bottom right corner. 
		5.3.2.- If a "type" is double-clicked the "Appointment Appointments by month" tab will be selected and populated with information of the double-clicked month.


6.- The extra report I used is located at the "Appointnments by location" tab under the "Reports" tab. 
	6.1.- The report shows the total appointments per location by month. 