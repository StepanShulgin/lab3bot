import botLogic.WtBot
import weatherInfo.CoordinateAPI
import weatherInfo.RetrofitClient
import weatherInfo.RetrofitType
import weatherInfo.WeatherRepository

fun main(){
    val weatherRetrofit = RetrofitClient().getRetrofitClient(RetrofitType.WEATHER)
    val coordinateRetrofit = RetrofitClient().getRetrofitClient(RetrofitType.CITY_COORDINATE)
    val weatherAPI = RetrofitClient().getWeatherApi(weatherRetrofit)
    val coordinateAPI = RetrofitClient().getCoordinateApi(coordinateRetrofit)
    val weatherRepository = WeatherRepository(weatherAPI, coordinateAPI)
    val wtBot = WtBot(weatherRepository).createBot()
    wtBot.startPolling()

}