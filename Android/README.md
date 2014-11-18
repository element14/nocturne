Project Nocturne Android App
============================

This is the top level source directory for the Android app for Project Nocturne.

----
Todo
----

* [ ] MockServer : Add parsing user connection request
* [ ] Android : Send user connection request to server
* [ ] 
* [ ] 
* [ ] 
* [ ] 


----


we can fairly easily have the caregiver side of the app allow setup for sleep time and allowable variations, set up per client.
This approach allows different caregivers to set different levels of alerts for a given patient. 
For example, a primary caregiver can set up alerts, but another caregiver (the client's child, 
for example) can just set it up to allow check ins without alerts.
 
How about something like this, set up in the Caregiver's settings (done for each client the caregiver is monitoring)...

```
Client:             [Mr. X]
Alerts              [ON]
Bed Time:           [9:00pm]
Warn After:         [9:30pm]
Sleep Confirmation: [ √ ]
Wake Up:            [7:00am]
Warn After:         [9:00am]
Wake Confirmation:  [ √ ]
```

Then we can add some functions on the server, based on the client's last few months of sleep and wake times. 
We can easily get the mean bed and wake times. Not sure what would be best for the "warn after" value - 
we should probably use something based on the statistical standard deviation.
 
The app can then ping the server to get those values - I think that should be a manual step the 
Caregiver does when viewing the settings, so that the caregiver MUST review and OK the new settings 
(just in case something goes wonky).


----


Frameworks in use:
====
TestServer
----
 * Simple      - http://www.simpleframework.org/
 * json-simple - https://code.google.com/p/json-simple
 * sqlite-jdbc -

Android App
----
 * Spring RestTemplate - http://spring.io/guides/gs/consuming-rest-android/
