# MovieToWatch
<img align="right" src="/previews/preview_1.gif" width="33%"/>
MovieToWatch is a demo movie app developed in <b>Kotlin</b>, that demonstrates use of the latest Android development techniques. The app integrates <b>Kotlin Coroutines with Retrofit</b> to fetch movie information and binds all UI components in the XML layout to data sources using a <b>DataBinding</b> rather than programmatically. The UI calls are done in <b>Binding Adapters</b> reducing boilerplate code in the fragments. All is done following <b>MVVM</b> pattern for the presentation layer. Using <b>repository</b> the web services and <b>Room</b> operations are abstracted from the rest of the app.  


## Tech stack & Open-source libraries
### Android Architecture Components & Third Party:

+ <b>Retrofit</b> library to connect to a REST web service on the internet and get a response.
+ <b>Moshi</b> library to parse the JSON response into a data object.
+ Android Architecture Components: <b>Room, ViewModels, LiveData, DataBinding </b> to hold the data and update the UI. 
+ <b>WorkManager</b> to schedule background work.
+ <b>ServiceLocator</b> to construct and store a repository.
+ <b>Kotlin Coroutines</b> to manage long running network and database tasks.
+ <b>Navigation Components</b> and <b>SafeArgs</b> for navigating and passing data between fragments.
+ <b>Glide</b> to load posters.
+ <b>Timber</b> for better log readability.

## Testing  
### Instrumented Tests
+ <b>Database Testing:</b>
Database is tested with small instrumented unit tests. The project creates an in memory database for database test but still runs them on the device. runBlockingTest is used whenever Coroutines are run from the tests.
+ <b>Fragments Tests:</b>
Fragments' are tested using Espresso UI framework, FragmentScenario for Fragments' lifecycle state, test doubles FakeRepository and NavController mock created with Mockito. 

### Local Unit Tests
+ <b>ViewModel Test:</b>
ViewModels are tested using local unit tests with fake Repository implementation.
+ <b> Repository Test:</b>
Repository is tested using local unit tests with fake versions of DataSource and network Service.


## Features
+ Get the list instantly when typing.
+ Save the selected movies to the list.
+ Delete by clicking on the button revealed when swiping the selected movie


## Open API
+ MovieToWatch uses the [TMDBApi](https://www.themoviedb.org/signup) for constructing RESTful API. Obtain your free [API_KEY:](https://www.themoviedb.org/signup) and paste it to the Constants file to try the app.

<img src="/previews/preview_2.gif" width="33%"/>  <img src="/previews/screen_1.jpg" width="33%"/>





