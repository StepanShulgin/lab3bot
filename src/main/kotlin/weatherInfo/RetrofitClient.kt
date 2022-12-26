package weatherInfo

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val weatherURL = "http://api.weatherapi.com/v1/"
const val cityByCoordinate = "https://nominatim.openstreetmap.org/"
const val apiKey = "28d3eb7455b74339a11164255222512"


enum class RetrofitType(val baseUrl:String){
    WEATHER(weatherURL),
    CITY_COORDINATE(cityByCoordinate)
}
class RetrofitClient {

    fun getRetrofitClient(retrofitType: RetrofitType): Retrofit {
        return Retrofit.Builder()
            .baseUrl(retrofitType.baseUrl)
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getWeatherApi(retrofit: Retrofit): WeatherAPI {
        return retrofit.create(WeatherAPI::class.java)
    }

    fun getCoordinateApi(retrofit: Retrofit): CoordinateAPI{
        return  retrofit.create(CoordinateAPI::class.java)
    }

}