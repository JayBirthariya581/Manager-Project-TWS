
# Manager App

This is a prototype of Manager side Android App  (part of Project ManageMechanicProto created under GearToCare Startup)
used for creating and managing doorstep two wheeler services.



## Features

- Login Authentication using Firebase
- Realtime Database and storage Firebase
- Maps SDK for android
- Places SDK
- PDF generator
- Date and Time Picker
- Managing and updating Customer/Mechanic Profile



##  Working

In this app a manager can
* Create Customers and Mechanics 
* Add a service for that customer
* Assign that service to a particular mechanic
* Once the servcice is done, manager can verify images uploaded by mechanic
* Approve and Generate PDF of service images
## Maps and Places SDK
This app consist of MAPs and Places SDK provided by google.

When a manager clicks on Address field of customer, PlacePickerActivity is called, which consist a Google MAP. 

Here Manager can select exact location(i.e. Latitude and Longitude) of customer by using the marker/picker.

Manager can also search for a particular location by clicking on the change button, which calls the SelectLocationActivity.

This Activity uses Google's Places SDK. Places are filtered according to string entered by manager in the search edit text.
Once the manager click on a place, the latitude and longitude of that place are returned to PlacePickerActivity
and the marker moves to that place.

In the end,when the manager click the select location the address is returned to customers address field.

Now mechanic can reach to that exact location by using this latitude and longitude.

#### Screenshots

![App Screenshot](https://drive.google.com/uc?id=1HNQW9XMqNYfjEo1TaSqpyAOmLUfL7AQ5)
![App Screenshot](https://drive.google.com/uc?id=12BcEQI7gR9apERxThUKaarhSY0uyd9DM)



# Developers
Jay Birthariya
 
