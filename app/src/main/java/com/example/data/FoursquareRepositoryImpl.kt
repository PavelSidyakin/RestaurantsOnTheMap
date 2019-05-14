package com.example.data

import com.example.domain.FoursquareRepository
import com.example.model.foursquare.details.FoursquareDetailsResult
import com.example.model.foursquare.details.FoursquareDetailsResultCode
import com.example.model.foursquare.details.response.FoursquareDetailsResponseData
import com.example.model.foursquare.search.FoursquareSearchResult
import com.example.model.foursquare.search.FoursquareSearchResultCode
import com.example.model.foursquare.search.response.FoursquareSearchResponseData
import com.example.utils.Logger
import com.example.utils.rx.SchedulersProvider
import com.google.gson.Gson
import io.reactivex.Single
import javax.inject.Inject

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient

class FoursquareRepositoryImpl
    @Inject constructor(
        val logger: Logger,
        val schedulersProvider: SchedulersProvider
    )
    : FoursquareRepository {
    
    private val forsquareRetrofit = createRetrofit()

    override fun searchRestaurants(lat: Double, lng: Double, radius: Int): Single<FoursquareSearchResult> {
        return Single.fromCallable { createSearchRestaurantsService() }
            .flatMap { service -> service.searchRestaurantsRequest("$lat,$lng", radius) }
            .flatMap { foursquareSearchResponseData ->
                    logger.i(TAG, "searchRestaurants() foursquareSearchResponseData=${foursquareSearchResponseData}")
                    if (foursquareSearchResponseData.meta == null || foursquareSearchResponseData.meta.code != 200) {
                        Single.error(RuntimeException("Unexpected result"))
                    } else {
                        Single.just(foursquareSearchResponseData)
                    }
            }
            .map { foursquareSearchResponseData ->
                FoursquareSearchResult(FoursquareSearchResultCode.OK, foursquareSearchResponseData)
            }
            .onErrorResumeNext { Single.just(FoursquareSearchResult(FoursquareSearchResultCode.GENERAL_ERROR, null)) }
            .subscribeOn(schedulersProvider.io())
            .doOnSubscribe { logger.i(TAG, "searchRestaurants() subscribe") }
            .doOnError { throwable ->  logger.w(TAG, "searchRestaurants() error", throwable) }
            .doOnSuccess { result -> logger.i(TAG, "searchRestaurants() next $result") }

    }
	
    override fun requestDetailsForVenue(venueId: String): Single<FoursquareDetailsResult> {
        return Single.fromCallable { createDetailsForVenueService() }
            .flatMap { service -> service.detailsForVenueRequest(venueId) }
            .flatMap { foursquareDetailsResponseData ->
                if (foursquareDetailsResponseData.meta == null || foursquareDetailsResponseData.meta.code != 200) {
                    Single.error(RuntimeException("Unexpected result"))
                } else
                    Single.just(foursquareDetailsResponseData)
            }
            .map { foursquareDetailsResponseData ->
                FoursquareDetailsResult(FoursquareDetailsResultCode.OK, foursquareDetailsResponseData)
            }
            .onErrorResumeNext { Single.just(FoursquareDetailsResult(FoursquareDetailsResultCode.GENERAL_ERROR, null)) }
            .subscribeOn(schedulersProvider.io())
    }

	private fun createSearchRestaurantsService(): SearchRestaurantsService {
        return forsquareRetrofit.create(SearchRestaurantsService::class.java);	
	}

	private fun createDetailsForVenueService(): DetailsForVenueService {
        return forsquareRetrofit.create(DetailsForVenueService::class.java);	
	}
	
    private fun createRetrofit(): Retrofit {
        val interceptor: HttpLoggingInterceptor =  HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        val client: OkHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    
    
        return Retrofit.Builder()
            .baseUrl("https://api.foursquare.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build();  
     
    }
    
	private interface SearchRestaurantsService {
		@GET("v2/venues/search?client_id=${CLIENT_ID}&client_secret=${CLIENT_SECRET}&v=${VER}&categoryId=${CATEGORY_RESTAURANTS}&intent=browse")
		fun searchRestaurantsRequest(@Query("ll") ll: String, @Query("radius") radius: Int): Single<FoursquareSearchResponseData> 
	}

	private interface DetailsForVenueService {
		@GET("v2/venues/{venueId}?client_id=${CLIENT_ID}&client_secret=${CLIENT_SECRET}&v=${VER}")
		fun detailsForVenueRequest(@Path("venueId") venueId: String): Single<FoursquareDetailsResponseData> 
	}
		
    private companion object {
        const val TAG = "FoursquareRepo"
		const val CLIENT_ID = "SQKJ0TXEPU1TRNRVRL1FNEBYGBQU0RBZQLFJ5HNASJBL4TFG"
		const val CLIENT_SECRET = "GR2EKW35DQZCWHE5XKJAXPO2K2GMRQA5AZPZASFRSK21VART"
		const val VER = "20190509"
		const val CATEGORY_RESTAURANTS = "4d4b7105d754a06374d81259"
		
    }

}