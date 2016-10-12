package com.rm.translateit.api.translation.mock

import org.junit.After
import org.junit.Before
import org.junit.Test
import rx.observers.TestSubscriber
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers

class FakeTranslaterTest {

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
        val sut = FakeTranslater()

        //when
        sut.translate("AWESOME", "PL", "EN").subscribe(testSubscriber)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf("Translation"))
    }

    @Test
    fun suggestions() {
        //given
        val testSubscriber = TestSubscriber<String>()
        val sut = FakeTranslater()

        //when
        sut.suggestions("AWESOME", "FU", 0)

        //then
        testSubscriber.assertNoErrors()
        testSubscriber.assertReceivedOnNext(listOf())
    }

}