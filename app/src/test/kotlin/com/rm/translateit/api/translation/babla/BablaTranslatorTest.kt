package com.rm.translateit.api.translation.babla

import com.rm.translateit.api.translation.models.Language
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import rx.observers.TestSubscriber
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers
import java.io.File

class BablaTranslatorTest {
    private lateinit var server : MockWebServer

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
        val testSubscriber = TestSubscriber<String>()
        val word = "WORD"
        val from = Language("EN", "English")
        val to = Language("PL", "Polish")

        //when
        server.enqueue(successfulResponseWithTranslation())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf("witaj"))
        testSubscriber.assertCompleted()
    }

    private fun successfulResponseWithTranslation(): MockResponse? {
        val responsePath = getResponsePath(forFile = "babla_result.html")

        return MockResponse()
                .setResponseCode(200)
                .setBody(File(responsePath).readText())
    }

    private fun getResponsePath(forFile: String) = BablaTranslatorTest::class.java.classLoader.getResource(forFile).path

    @Test
    @Ignore
    fun suggestions() {

    }

}