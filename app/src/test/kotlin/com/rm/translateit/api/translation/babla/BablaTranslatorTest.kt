package com.rm.translateit.api.translation.babla

import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.TranslationItem
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.adapter.rxjava.HttpException
import rx.observers.TestSubscriber
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers
import java.io.File

class BablaTranslatorTest {
    private val bablaUrl = mock(BablaUrl::class.java)

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
        val sut = BablaTranslator(bablaUrl)
        val testSubscriber = TestSubscriber<List<TranslationItem>>()

        //when
        server.enqueue(successfulResponseWithTranslation())
        `when`(bablaUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf(expectedResult()))
        testSubscriber.assertCompleted()
    }

    private fun expectedResult() = listOf(
            TranslationItem("witaj", listOf("interjection")),
            TranslationItem("witam", listOf("interjection"))
    )

    @Test
    fun should_successfully_return_response_without_translation() {
        //given
        val sut = BablaTranslator(bablaUrl)
        val testSubscriber = TestSubscriber<List<TranslationItem>>()

        //when
        server.enqueue(successfulResponseWithoutTranslation())
        `when`(bablaUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf(emptyList()))
        testSubscriber.assertCompleted()
    }

    @Test
    fun should_successfully_handle_error() {
        //given
        val sut = BablaTranslator(bablaUrl)
        val testSubscriber = TestSubscriber<List<TranslationItem>>()

        //when
        server.enqueue(errorResponse())
        `when`(bablaUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())
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