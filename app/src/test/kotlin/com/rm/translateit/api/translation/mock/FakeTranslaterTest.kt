package com.rm.translateit.api.translation.mock

import com.rm.translateit.BaseRxTest
import org.junit.Test
import rx.observers.TestSubscriber

class FakeTranslaterTest : BaseRxTest() {
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