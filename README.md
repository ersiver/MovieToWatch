# MovieToWatch
<img align="right" src="https://user-images.githubusercontent.com/58771510/85013142-fcd24380-b15b-11ea-95e7-704447b2d585.gif" width="33%"/>
MovieToWatch is a demo movie app developed in <b>Kotlin</b>, that demonstrates use of the latest Android development techniques. The app integrates <b>Kotlin Coroutines with Retrofit</b> to fetch movie information and binds all UI components in the XML layout to data sources using a <b>DataBinding</b> rather than programmatically. The UI calls are done in <b>Binding Adapters</b> reducing boilerplate code in the fragments. All is done following <b>MVVM</b> pattern for the presentation layer. Using <b>repository</b> the web services and <b>Room</b> operations are abstracted from the rest of the app.  


## Tech stack & Open-source libraries
### Android Architecture Components & Third Party

+ <b>Retrofit</b> library to connect to a REST web service on the internet and get a response.
+ <b>Moshi</b> library to parse the JSON response into a data object.
+ Android Architecture Components: <b>Room, ViewModels, LiveData, DataBinding </b> to hold the data and update the UI. 
+ <b>WorkManager</b> to schedule background work.
+ <b>Kotlin Coroutines</b> to manage long running network and database tasks.
+ <b>Navigation Components</b> and <b>SafeArgs</b> for navigating and passing data between fragments.
+ <b>Glide</b> to load posters.
+ <b>Timber</b> for better log readability.

### Testing:  
####  Device Tests
  - <b>Database Testing</b> - The project creates an in memory database for database test but still runs them on the device. 

### Features
+ Get the list instantly when typing.
+ Save the selected movies to the list.
+ Delete by clicking on the button revelad when swiping the selected movie

## Open API
+ MovieToWatch uses the [TMDBApi] for constructing RESTful API. Obtain your free API_KEY: https://www.themoviedb.org/signup and paste it to the Constants file to try the app.
<img src="https://user-images.githubusercontent.com/58771510/85013170-08256f00-b15c-11ea-96e1-139b6ccd7569.gif" width="33%"/>
