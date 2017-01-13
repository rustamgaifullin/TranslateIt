package com.rm.translateit.api.translation.babla

import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.TranslationItem
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import retrofit2.adapter.rxjava.HttpException
import rx.observers.TestSubscriber
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers
import java.io.File

class BablaTranslatorTest {
    private lateinit var server : MockWebServer
    private val word = "WORD"
    private val from = Language("EN", "English")
    private val to = Language("PL", "Polish")


    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()

        RxJavaHooks.setOnIOScheduler { scheduler -> Schedulers.immediate() }
    }

    @After
    fun tearDown() {
        server.shutdown()

        RxJavaHooks.reset()
    }

    @Test
    fun should_successfully_return_response_with_translation() {
        //given
        val sut = BablaTranslator(server.url("").toString())
        val testSubscriber = TestSubscriber<List<TranslationItem>>()

        //when
        server.enqueue(successfulResponseWithTranslation())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf(expectedResult()))
        testSubscriber.assertCompleted()
    }

    private fun expectedResult() = listOf(
            TranslationItem("witaj"),
            TranslationItem("witam"),
            TranslationItem("halo"),
            TranslationItem("cześć"),
            TranslationItem("słucham"),
            TranslationItem("witajcie"),
            TranslationItem("czołem"),
            TranslationItem("dzień dobry"),
            TranslationItem("serwus"),
            TranslationItem("siema"),
            TranslationItem("no, no"),
            TranslationItem("coś takiego")
    )

    @Test
    fun should_successfully_return_response_without_translation() {
        //given
        val sut = BablaTranslator(server.url("").toString())
        val testSubscriber = TestSubscriber<List<TranslationItem>>()

        //when
        server.enqueue(successfulResponseWithoutTranslation())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf(emptyList()))
        testSubscriber.assertCompleted()
    }

    @Test
    fun should_successfully_handle_error() {
        //given
        val sut = BablaTranslator(server.url("").toString())
        val testSubscriber = TestSubscriber<List<TranslationItem>>()

        //when
        server.enqueue(errorResponse())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertError(HttpException::class.java)
    }

    private fun successfulResponseWithTranslation(): MockResponse? {
        val responsePath = getResponsePath(forFile = "babla_response_with_translation.html")

        return MockResponse()
                .setResponseCode(200)
                .setBody(File(responsePath).readText())
    }

    private fun successfulResponseWithoutTranslation(): MockResponse? {
        val responsePath = getResponsePath(forFile = "babla_response_no_translation.html")

        return MockResponse()
                .setResponseCode(200)
                .setBody(File(responsePath).readText())
    }

    private fun errorResponse(): MockResponse? {
        val body = "ERROR"

        return MockResponse()
                .setResponseCode(500)
                .setBody(body)
    }

    private fun getResponsePath(forFile: String) = BablaTranslatorTest::class.java.classLoader.getResource(forFile).path

    @Test
    @Ignore
    fun suggestions() {

    }

}