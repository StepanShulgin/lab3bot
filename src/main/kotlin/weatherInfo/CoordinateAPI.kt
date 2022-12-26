package weatherInfo

import kotlinx.coroutines.Deferred
import models.CoordinateCity
import retrofit2.http.GET
import retrofit2.http.Query

interface CoordinateAPI {

    @GET("reverse")
    fun getCity(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("format") formatData: String

    ): Deferred<CoordinateCity>



}