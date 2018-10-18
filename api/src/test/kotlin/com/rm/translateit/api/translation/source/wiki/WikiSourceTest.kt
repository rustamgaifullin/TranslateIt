package com.rm.translateit.api.translation.source.wiki

import com.nhaarman.mockitokotlin2.any
import com.rm.translateit.api.models.LanguageModel
import com.rm.translateit.api.models.NameModel
import com.rm.translateit.api.models.translation.Details
import com.rm.translateit.api.models.translation.Translation
import com.rm.translateit.api.models.translation.Words.Companion.words
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import rx.observers.TestSubscriber
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers
import java.io.File
import java.net.ConnectException

class WikiSourceTest {
    private val wikiUrl = mock(WikiUrl::class.java)
    private val wikiDetailsUrl = mock(WikiDetailsUrl::class.java)
    private val restService: WikiRestService = Retrofit.Builder()
            .baseUrl("http://wikipedia.org")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
            .create(WikiRestService::class.java)
    private val word = "WORD"
    private val names = listOf(
            NameModel("en", "english"),
            NameModel("pl", "polish")
    )
    private val from = LanguageModel("EN", names)
    private val to = LanguageModel("PL", names)

    private lateinit var server: MockWebServer
    private lateinit var testSubscriber: TestSubscriber<Translation>
    private lateinit var sut: WikiSource

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        testSubscriber = TestSubscriber()

        sut = WikiSource(wikiUrl, wikiDetailsUrl, restService)

        `when`(wikiUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())
        `when`(wikiDetailsUrl.construct(any(), any(), any()))
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
    fun should_successfully_return_response_with_translation_without_details() {
        //when
        server.enqueue(successfulResponseWithTranslation())
        server.enqueue(successfulResponseWithoutDetails())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //expect
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(expectedTranslationWithoutDetails())
        testSubscriber.assertCompleted()
    }

    @Test
    fun should_successfully_return_response_with_translation_without_details_for_empty_details_json() {
        //when
        server.enqueue(successfulResponseWithTranslation())
        server.enqueue(successfulResponseForEmptyJson())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //expect
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(expectedTranslationWithoutDetails())
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
    fun should_successfully_return_response_without_langlinks() {
        //when
        server.enqueue(successfulResponseWithoutLanglinks())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //expect
        testSubscriber.assertNoErrors()
        testSubscriber.assertCompleted()
    }

    @Test
    fun should_successfully_return_response_for_empty_json() {
        //when
        server.enqueue(successfulResponseForEmptyJson())
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
            Translation(listOfWords(), details())
    )

    private fun expectedTranslationWithoutDetails() = listOf(
            Translation(listOfWords(), emptyDetails())
    )

    private fun listOfWords() = words("Translate")

    private fun details() = Details("Full translation description", "https://en.wikipedia.org/wiki/Translate")

    private fun emptyDetails() = Details("", "")

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

    private fun successfulResponseWithoutDetails(): MockResponse? {
        val responsePath = getResponsePath(forFile = "wiki_translation_details_empty_response.json")

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

    private fun successfulResponseWithoutLanglinks(): MockResponse? {
        val responsePath = getResponsePath(forFile = "wiki_translation_response_without_langlinks.json")

        return MockResponse()
                .setResponseCode(200)
                .setBody(File(responsePath).readText())
    }

    private fun successfulResponseForEmptyJson(): MockResponse? {
        val responsePath = getResponsePath(forFile = "wiki_empty_json.json")

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