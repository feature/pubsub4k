/*
 * MIT License
 *
 * Copyright (c) 2019 Stamina Development
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package pw.stamina.pubsub4k.publish

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.verify
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldEqual
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pw.stamina.pubsub4k.subscribe.Subscription

object PublisherContainerSpec : Spek({

    val topic = Any::class.java

    describe("A publisher container") {
        val subscription: Subscription<Any> = mock(stubOnly = true) {
            on { messageHandler } doReturn mock()
        }

        val container = PublisherContainer(topic, emptySet())

        it("topic should be specified topic") {
            container.topic shouldBe topic
        }

        it("should have no subscriptions") {
            container.subscriptions.shouldBeEmpty()
        }

        describe("publishing message") {
            before {
                container.publish(Unit)
            }

            it("should do nothing") {
                verify(subscription.messageHandler, never()).accept(any())
            }
        }

        describe("with added subscription") {
            before {
                container.add(subscription)
            }

            it("subscriptions should contain only specified subscription") {
                container.subscriptions shouldEqual setOf(subscription)
            }

            describe("publishing message") {
                val message = Unit
                before {
                    container.publish(message)
                }

                it("should call message handler of subscription") {
                    verify(subscription.messageHandler).accept(message)
                }

                after {
                    reset(subscription.messageHandler)
                }
            }

            describe("with removed subscription") {
                before {
                    container.remove(subscription)
                }

                it("subscriptions should be empty") {
                    container.subscriptions.shouldBeEmpty()
                }

                describe("publishing message") {
                    before {
                        container.publish(Unit)
                    }

                    it("should do nothing") {
                        verify(subscription.messageHandler, never()).accept(any())
                    }
                }
            }

            describe("cleared subscriptions") {
                before {
                    container.clear()
                }

                it("subscriptions should be empty") {
                    container.subscriptions.shouldBeEmpty()
                }

                describe("publishing message") {
                    before {
                        container.publish(Unit)
                    }

                    it("should do nothing") {
                        verify(subscription.messageHandler, never()).accept(any())
                    }
                }
            }
        }
    }
})