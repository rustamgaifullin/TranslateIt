package com.rm.translateit.api.translation.source.babla

import com.rm.translateit.api.models.Language
import com.rm.translateit.api.models.translation.Tags.Companion.tags
import com.rm.translateit.api.models.translation.TranslationItem
import com.rm.translateit.api.models.translation.Words.Companion.words
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.HttpException
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import rx.observers.TestSubscriber
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers

class BablaSourceTest {
    private val bablaUrl = mock(BablaUrl::class.java)
    private val bablaHtmlParser = mock(BablaHtmlParser::class.java)
    private val bablaRestService: BablaRestService = Retrofit.Builder()
            .baseUrl("http://bab.la")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
            .create(BablaRestService::class.java)

    private lateinit var server : MockWebServer
    private val word = "WORD"
    private val from = Language("EN", "English")
    private val to = Language("PL", "Polish")

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
        val sut = BablaSource(bablaRestService, bablaUrl, bablaHtmlParser)
        val testSubscriber = TestSubscriber<List<TranslationItem>>()

        //when
        server.enqueue(successfulResponseWithTranslation())
        `when`(bablaUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())
        `when`(bablaHtmlParser.getTranslateItemsFrom(""))
                .thenReturn(expectedResult())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf(expectedResult()))
        testSubscriber.assertCompleted()
    }

    private fun expectedResult() = listOf(
            TranslationItem(words("witaj"), tags("interjection")),
            TranslationItem(words("witam"), tags("interjection"))
    )

    @Test
    fun should_successfully_return_response_without_translation() {
        //given
        val sut = BablaSource(bablaRestService, bablaUrl, bablaHtmlParser)
        val testSubscriber = TestSubscriber<List<TranslationItem>>()

        //when
        server.enqueue(successfulResponseWithoutTranslation())
        `when`(bablaUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())
        `when`(bablaHtmlParser.getTranslateItemsFrom(""))
                .thenReturn(emptyList())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf(emptyList()))
        testSubscriber.assertCompleted()
    }

    @Test
    fun should_successfully_handle_error() {
        //given
        val sut = BablaSource(bablaRestService, bablaUrl, bablaHtmlParser)
        val testSubscriber = TestSubscriber<List<TranslationItem>>()

        //when
        server.enqueue(errorResponse())
        `when`(bablaUrl.construct(word, from, to))
                .thenReturn(server.url("").toString())
        `when`(bablaHtmlParser.getTranslateItemsFrom(""))
                .thenReturn(emptyList())
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertError(HttpException::class.java)
    }

    private fun successfulResponseWithTranslation(): MockResponse? {
        return MockResponse()
                .setResponseCode(200)
                .setBody("")
    }

    private fun successfulResponseWithoutTranslation(): MockResponse? {
        return MockResponse()
                .setResponseCode(200)
                .setBody("")
    }

    private fun errorResponse(): MockResponse? {
        val body = "ERROR"

        return MockResponse()
                .setResponseCode(500)
                .setBody(body)
    }

    @Test
    @Ignore
    fun suggestions() {

    }

}