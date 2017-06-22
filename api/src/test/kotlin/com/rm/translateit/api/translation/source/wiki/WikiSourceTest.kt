package com.rm.translateit.api.translation.source.wiki

import com.google.gson.GsonBuilder
import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.translation.Details
import com.rm.translateit.api.models.translation.Translation
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.models.translation.Words.Companion.words
import com.rm.translateit.api.translation.source.wiki.deserializers.LanguageTypeAdapterFactory
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.HttpException
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.observers.TestSubscriber
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers
import java.io.File
import java.net.ConnectException

class WikiSourceTest {
    private val wikiUrl = mock(WikiUrl::class.java)
    private val wikiDetailsUrl = mock(WikiDetailsUrl::class.java)
    private val restService: WikiRestService
    private val word = "WORD"
    private val from = LanguageModel("EN", "English")
    private val to = LanguageModel("PL", "Polish")

    private lateinit var server: MockWebServer
    private lateinit var testSubscriber: TestSubscriber<Translation>
    private lateinit var sut: WikiSource

    init {
        val gson = GsonBuilder()
                .registerTypeAdapterFactory(LanguageTypeAdapterFactory())
                .create()

        restService = Retrofit.Builder()
                .baseUrl("http://wikipedia.org")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(WikiRestService::class.java)
    }

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        testSubscriber = TestSubscriber<Translation>()

        sut = WikiSource(wikiUrl, wikiDetailsUrl, restService)

        `when`(wikiUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())
        `when`(wikiDetailsUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())


        RxJavaHooks.setOnIOScheduler { Schedulers.immediate() }
    }

    @After
    fun tearDown() {
        server.shutdown()
        RxJavaHooks.reset()
    }

    @Test
    fun should_successfully_return_response_with_translation() {
        //when
        server.enqueue(successfulResponseWithTranslation())
        server.enqueue(successfulResponseWithDetails())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //expect
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(expectedTranslation())
        testSubscriber.assertCompleted()
    }

    @Test
    fun should_successfully_return_response_without_translation() {
        //when
        server.enqueue(successfulResponseWithoutTranslation())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //expect
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun should_return_error_exception() {
        //when
        server.enqueue(errorResponse())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertError(HttpException::class.java)
    }

    //TODO: rewrite this with UnknownHostException
    @Test
    fun should_be_able_manage_no_connection_behavior() {
        //when
        server.shutdown()
        sut.translate(word, from, to).subscribe(testSubscriber)

        //expect
        testSubscriber.assertError(ConnectException::class.java)
    }

    private fun expectedTranslation() = listOf(
            Translation(translationItemList(), details())
    )

    private fun translationItemList() = listOf(TranslationItem(words("Translate")))

    private fun details() = Details("Full translation description", "https://en.wikipedia.org/wiki/Translate")

    private fun successfulResponseWithTranslation(): MockResponse? {
        val responsePath = getResponsePath(forFile = "wiki_translation_response.json")

        return MockResponse()
                .setResponseCode(200)
                .setBody(File(responsePath).readText())
    }

    private fun successfulResponseWithDetails(): MockResponse? {
        val responsePath = getResponsePath(forFile = "wiki_translation_details_response.json")

        return MockResponse()
                .setResponseCode(200)
                .setBody(File(responsePath).readText())
    }

    private fun successfulResponseWithoutTranslation(): MockResponse? {
        val responsePath = getResponsePath(forFile = "wiki_translation_empty_response.json")

        return MockResponse()
                .setResponseCode(200)
                .setBody(File(responsePath).readText())
    }

    private fun errorResponse(): MockResponse? {
        return MockResponse()
                .setResponseCode(500)
                .setBody("error")
    }

    private fun getResponsePath(forFile: String) = WikiSourceTest::class.java.classLoader.getResource(forFile).path
}