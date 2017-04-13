package com.rm.translateit.api.translation.source.wiki

import com.google.gson.GsonBuilder
import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.models.translation.Words.Companion.words
import com.rm.translateit.api.translation.source.wiki.deserializers.LanguageDeserializer
import com.rm.translateit.api.translation.source.wiki.response.LanguageLinksResult
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
    private val restService: WikiRestService
    private lateinit var server: MockWebServer
    private val word = "WORD"
    private val from = LanguageModel("EN", "English")
    private val to = LanguageModel("PL", "Polish")

    init {
        val gson = GsonBuilder()
                .registerTypeAdapter(LanguageLinksResult::class.java, LanguageDeserializer())
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

        RxJavaHooks.setOnIOScheduler { Schedulers.immediate() }
    }

    @After
    fun tearDown() {
        server.shutdown()
        RxJavaHooks.reset()
    }

    @Test
    fun should_successfully_return_response_with_translation() {
        //given
        val sut = WikiSource(wikiUrl, restService)
        val testSubscriber = TestSubscriber<List<TranslationItem>>()

        //when
        server.enqueue(successfulResponseWithTranslation())
        `when`(wikiUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf(listOf(TranslationItem(words("Translate")))))
        testSubscriber.assertCompleted()
    }

    @Test
    fun should_successfully_return_response_without_translation() {
        //given
        val sut = WikiSource(wikiUrl, restService)
        val testSubscriber = TestSubscriber<List<TranslationItem>>()

        //when
        server.enqueue(successfulResponseWithoutTranslation())
        `when`(wikiUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf(emptyList()))
        testSubscriber.assertCompleted()
    }

    @Test
    fun should_return_error_exception() {
        //given
        val sut = WikiSource(wikiUrl, restService)
        val testSubscriber = TestSubscriber<List<TranslationItem>>()

        //when
        server.enqueue(errorResponse())
        `when`(wikiUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertError(HttpException::class.java)
    }

    //TODO: rewrite this with UnknownHostException
    @Test
    fun should_work_when_no_connection() {
        //given
        val sut = WikiSource(wikiUrl, restService)
        val testSubscriber = TestSubscriber<List<TranslationItem>>()

        //when
        server.shutdown()
        `when`(wikiUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertError(ConnectException::class.java)
    }

    private fun successfulResponseWithTranslation(): MockResponse? {
        val responsePath = getResponsePath(forFile = "wiki_translation_response.json")

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