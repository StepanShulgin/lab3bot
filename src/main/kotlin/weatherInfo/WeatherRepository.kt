package weatherInfo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import models.CoordinateCity
import models.CurrentWeather

class WeatherRepository(private val weatherApi: WeatherAPI, private val coordinateAPI: CoordinateAPI) {
    suspend fun getCurrentWeather(apiKey: String, cityName:String, airQualityData: String): CurrentWeather{
        return withContext(Dispatchers.IO){
            weatherApi.getCurrentWeather(apiKey,cityName,airQualityData)
        }.await()
    }

    suspend fun getCoordinateCity(latitude:String, longitude: String, format: String): CoordinateCity{
        return withContext(Dispatchers.IO){
            coordinateAPI.getCity(latitude,longitude,format)
        }.await()

    }
}