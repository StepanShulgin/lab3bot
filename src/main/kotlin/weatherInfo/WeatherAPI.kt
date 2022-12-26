package weatherInfo

import kotlinx.coroutines.Deferred
import models.CurrentWeather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("current.json")
    fun getCurrentWeather(
        @Query("key") apiKey:String,
        @Query("q") cityName: String,
        @Query("api") airQualityData: String
    ): Deferred<CurrentWeather>
}