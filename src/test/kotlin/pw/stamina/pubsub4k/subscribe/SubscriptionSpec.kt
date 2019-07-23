/*
 * Copyright 2019 Stamina Development
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pw.stamina.pubsub4k.subscribe

import com.nhaarman.mockitokotlin2.mock
import org.amshove.kluent.shouldBe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import pw.stamina.pubsub4k.MessageSubscriber
import pw.stamina.pubsub4k.Topic
import java.util.function.Consumer
import java.util.function.Predicate

object SubscriptionSpec : Spek({
    describe("A subscription") {
        val topic = Any::class.java

        val subscriber by memoized { mock<MessageSubscriber>() }
        val topicFilter by memoized { mock<Predicate<Topic<out Any>>>() }
        val messageHandler by memoized { mock<Consumer<Any>>() }

        val subscription by memoized { Subscription(topic, subscriber, topicFilter, messageHandler) }

        it("topic should be specified topic") {
            subscription.topic shouldBe topic
        }

        it("subscriber should be specified subscriber") {
            subscription.subscriber shouldBe subscriber
        }

        it("topicFilter should be specified topicFilter") {
            subscription.topicFilter shouldBe topicFilter
        }

        it("messageHandler should be specified messageHandler") {
            subscription.messageHandler shouldBe messageHandler
        }
    }
})