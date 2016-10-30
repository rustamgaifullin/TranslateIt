package com.rm.translateit.api.translation.dummy

import com.rm.translateit.api.models.Language
import org.junit.After
import org.junit.Before
import org.junit.Test
import rx.observers.TestSubscriber
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers

class DummyTranslatorTest {

    @Before
    fun setUp() {
        RxJavaHooks.setOnIOScheduler { scheduler -> Schedulers.immediate() }
    }

    @After
    fun tearDown() {
        RxJavaHooks.reset()
    }

    @Test
    fun translate() {
        //given
        val testSubscriber = TestSubscriber<String>()
        val sut = DummyTranslator()
        val word = "AWESOME"
        val from = Language("pl", "Polish")
        val to = Language("en", "English")

        //when
        sut.translate(word, from, to).subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf("Translation"))
    }

    @Test
    fun suggestions() {
        //given
        val testSubscriber = TestSubscriber<String>()
        val sut = DummyTranslator()

        //when
        sut.suggestions("AWESOME", "FU", 0)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf())
    }
}