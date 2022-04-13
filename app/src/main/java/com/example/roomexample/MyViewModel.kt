package com.example.roomexample


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.roomexample.database.Scene
import com.example.roomexample.database.SceneDatabase
import com.example.roomexample.network.GetService
import com.example.roomexample.network.TemperatureEntity
import com.example.roomexample.network.asTemperatureEntity
import kotlinx.coroutines.launch

//viewmodel need to know the database dao (passed argument here) for accessing data from the database
//class MyViewModel(dataSource: SceneDatabaseDao) : ViewModel(){

//use the AndroidViewModel with default parameter: application context
class MyViewModel(application: Application) : AndroidViewModel(application) {

    //get the reference to the database dao
    private val database = SceneDatabase.getInstance(application).sceneDatabaseDao

    //a list of all scenes or matched scenes from the database (LiveData)
    val sceneList = MediatorLiveData<List<Scene>>()

    //get the selected scene (livedata for easy observing)
    val selectedScene = MutableLiveData<Scene>()

    //get the city weather (livedata)
    val cityWeather = MutableLiveData<TemperatureEntity?>()

    //list of cities in Taiwan
    //the following way is sometime not working: don't know
    val cityList : Array<String> = application.resources.getStringArray(R.array.city_array)
    init {
        getAllScenes()
    }

    fun getAllScenes() {  //set the livedata source of the sceneList to be all scenes
        sceneList.addSource(database.loadAllScenes()) { scenes ->
            sceneList.setValue(scenes)
        }
    }

    fun searchAllScenes(name: String) { //set the livedata source of the sceneList to be matched scenes
        sceneList.addSource(database.findScenes(name)) { scenes ->
            sceneList.setValue(scenes)
        }
    }

    fun getScene(sceneId: Long) {  //get one scene from the sceneList
        sceneList.value?.let {
            selectedScene.value = it.find { it.id == sceneId }
        }
    }

    fun insertScene(newScene: Scene) {  //add a new scene into the database
        viewModelScope.launch {
            database.insertScene(newScene)
        }
    }

    fun updateScene(oldScene: Scene) {  //add a new scene into the database
        viewModelScope.launch {
            database.updateScene(oldScene)
        }
    }

    fun deleteScene(oldScene: Scene) {  //delete a scene from the database
        viewModelScope.launch {
            database.deleteScene(oldScene)
        }
    }

    fun retrieveWeather(location: String) {
        //initially set null
        cityWeather.value = null
        viewModelScope.launch {
            try {
                val result =
                    GetService.retrofitService.getAppData(location)
                cityWeather.value = result.asTemperatureEntity()
            } catch (e: Exception) {
                Log.d("Main", "Fail to access: ${e.message}")
            }
        }
    }

    fun initDB() {  //setup the initial database
        viewModelScope.launch {
            repeat(1) {
                database.insertScene(
                    Scene(
                        "朱緣餐館",
                        "花蓮縣壽豐鄉中正路172號",
                        "0912212248",
                        "志學古早味選擇之一，推薦牛肉麵、麻油松阪豬套餐。",
                        R.drawable.photo_3

                    )
                )
                database.insertScene(
                    Scene(
                        "牛排先生",
                        "花蓮縣壽豐鄉中正路146-3號",
                        "038661235",
                        "志學街上平價夜市牛排餐，除排餐外，內用湯品及飲料都可無限取用。",
                        R.drawable.photo_4

                    )
                )
                database.insertScene(
                    Scene(
                        "好佳小吃",
                        "花蓮縣壽豐鄉中正路173號",
                        "038662708",
                        "志學街上販售炒飯、炒麵的選擇之一，口味多樣。",
                        R.drawable.photo_5
                    )
                )
                database.insertScene(
                    Scene(
                        "丸山和食",
                        "花蓮縣壽豐鄉志學村中正路96巷2-1號",
                        "0958170620",
                        "志學街巷弄內的隱藏美食，從五點開店到關門前往往都是大排長龍的盛況。",
                        R.drawable.photo_1
                    )
                )
                database.insertScene(
                    Scene(
                        "伊均蘭州拉麵",
                        "花蓮縣壽豐鄉中正路95號",
                        "0912515222",
                        "志學街上手工現做麵食，每份附有小菜及當令水果，飲料無限暢飲。",
                        R.drawable.photo_2
                    )
                )
                database.insertScene(
                    Scene(
                        "賀田屋蓋飯咖哩",
                        "花蓮縣壽豐鄉志學村中正路125號",
                        "038662095",
                        "餐點多元，從拉麵到炒飯，到各式炸物應有盡有，在不曉得該吃什麼的時候的一家好選擇。",
                        R.drawable.photo_6
                    )
                )
                database.insertScene(
                    Scene(
                        "迷途鯨魚",
                        "花蓮縣壽豐鄉中山路三段62號",
                        "038661097",
                        "新開張的網美名店，從店外到店內處處可見裝潢的用心，主要販售義大利麵、咖哩、甜點。",
                        R.drawable.photo_7
                    )
                )
                database.insertScene(
                    Scene(
                        "韓屋韓式小吃",
                        "花蓮縣壽豐鄉中正路45巷47號",
                        "038661580",
                        "志學街上韓式料理的唯一選擇，老闆是一位來自韓國的阿姨，內用白飯可以無限續，是學生添飽肚子的好選擇。",
                        R.drawable.photo_8
                    )
                )
                database.insertScene(
                    Scene(
                        "少爺丼飯",
                        "花蓮縣壽豐鄉志光街1號",
                        "038663903",
                        "志學街巷弄內的丼飯店，店內空間較小，以外帶客為主，餐點內容豐富實在，點餐時可以免費加飯一次。",
                        R.drawable.word
                    )
                )
            }
        }
    }
}