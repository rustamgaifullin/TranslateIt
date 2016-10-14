package com.rm.translateit.api.translation.wiki

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.adapter.rxjava.HttpException
import rx.observers.TestSubscriber
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers
import java.io.File
import java.net.ConnectException

class WikiTranslatorTest {
    private lateinit var server: MockWebServer
    private val word = "WORD"
    private val from = "en"
    private val to = "pl"

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
        val sut = WikiTranslator(server.url("").toString())
        server.enqueue(successfulResponseWithTranslation())
        val testSubscriber = TestSubscriber<String>()

        //when
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf("Translate"))
        testSubscriber.assertCompleted()
    }

    @Test
    fun should_successfully_return_response_without_translation() {
        //given
        val sut = WikiTranslator(server.url("").toString())
        server.enqueue(successfulResponseWithoutTranslation())
        val testSubscriber = TestSubscriber<String>()

        //when
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf(""))
        testSubscriber.assertCompleted()
    }

    @Test
    fun should_return_error_exception() {
        //given
        val sut = WikiTranslator(server.url("").toString())
        server.enqueue(errorResponse())
        val testSubscriber = TestSubscriber<String>()

        //when
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertError(HttpException::class.java)
    }

    //TODO: rewrite this with UnknownHostException
    @Test
    fun should_work_when_no_connection() {
        //given
        val sut = WikiTranslator(server.url("").toString())
        server.shutdown()
        val testSubscriber = TestSubscriber<String>()

        //when
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

    private fun getResponsePath(forFile: String) = WikiTranslatorTest::class.java.classLoader.getResource(forFile).path
}