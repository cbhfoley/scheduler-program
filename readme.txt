C195 - Scheduling Application 
Author: Chad Foley
Application version: 1.0
2/20/2024

PURPOSE: 
To interact with existing database to allow displaying and manipulation of data within
the database. Allows for creating, reading, updating and deleting (CRUD) of both customers and appointments. 

Also has some useful built in reporting features to display data based on user selection.

VERSION INFORMATION:
IDE VERSION: IntelliJ IDEA 2023.2.2 (Community Edition)
JDK Version: 17.0.1
JavaFX Version: 11.0.2
MySQL Connector Driver: 8.0.25 

DIRECTIONS:
After launching the program, enter valid username and password to login. The username and password must be
valid entries within the SQL database. Navigate through the menu's with the various buttons. Alerts will 
indicate if you are doing anything the program doesn't like, such as not inputting all the information in a form that
is requesting user input. When you are done you can either exit out of the program. Or navigate back to the main menu,
click logout, and then click exit. Both will successfully log you out of that session.

ADDITIONAL REPORT:
The additional report I chose displays a schedule of all of a specified users (using a combo box) appointments. 
The display is also slightly altered from other appointment displays to display the User_Name, instead of the User_ID
for readability.