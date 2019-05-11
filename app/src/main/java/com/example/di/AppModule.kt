package com.example.di

import com.example.data.ApplicationProviderImpl
import com.example.data.FoursquareRepositoryImpl
import com.example.data.FoursquareUrlsGenerator
import com.example.data.FoursquareUrlsGeneratorImpl
import com.example.domain.ApplicationProvider
import com.example.domain.FoursquareRepository
import com.example.domain.PlacesInteractor
import com.example.domain.PlacesInteractorImpl
import com.example.utils.HttpClient
import com.example.utils.HttpClientImpl
import com.example.utils.Logger
import com.example.utils.LoggerImpl
import com.example.utils.NetworkUtils
import com.example.utils.NetworkUtilsImpl
import com.example.utils.PermissionsUtils
import com.example.utils.PermissionsUtilsImpl
import com.example.utils.rx.SchedulersProvider
import com.example.utils.rx.SchedulersProviderImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Singleton
    @Binds
    abstract fun provideApplicationProvider(applicationProvider: ApplicationProviderImpl) : ApplicationProvider

    @Singleton
    @Binds
    abstract fun provideLogger(logger: LoggerImpl) : Logger

    @Singleton
    @Binds
    abstract fun provideHttpClient(httpClient: HttpClientImpl) : HttpClient

    @Singleton
    @Binds
    abstract fun provideNetworkUtils(networkUtils: NetworkUtilsImpl) : NetworkUtils

    @Singleton
    @Binds
    abstract fun provideFoursquareRestaurantRepository(foursquareRestaurantRepository: FoursquareRepositoryImpl) : FoursquareRepository

    @Singleton
    @Binds
    abstract fun provideRestaurantInteractor(restaurantInteractor: PlacesInteractorImpl) : PlacesInteractor

    @Singleton
    @Binds
    abstract fun provideSchedulersProvider(schedulersProvider: SchedulersProviderImpl) : SchedulersProvider

    @Singleton
    @Binds
    abstract fun provideFoursquareUrlsGenerator(foursquareUrlsGenerator: FoursquareUrlsGeneratorImpl) : FoursquareUrlsGenerator

    @Singleton
    @Binds
    abstract fun providePermissionsUtils(permissionsUtils: PermissionsUtilsImpl) : PermissionsUtils

}