/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.plackemacher.rxreceiver;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.observers.TestObserver;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RxReceiverTest {
    @Test
    public void testBroadcast() {
        String action = "TEST_ACTION";
        IntentFilter intentFilter = new IntentFilter(action);
        Application application = RuntimeEnvironment.application;
        Observable<Intent> observable = RxReceiver.fromBroadcast(application, intentFilter);
        final Observer<Intent> observer = mock(Observer.class);
        final Subscription subscription = observable.subscribe(new TestObserver<Intent>(observer));

        final InOrder inOrder = inOrder(observer);

        inOrder.verify(observer, never()).onNext(any(Intent.class));

        Intent intent = new Intent(action);
        application.sendBroadcast(intent);
        inOrder.verify(observer, times(1)).onNext(intent);

        application.sendBroadcast(intent);
        inOrder.verify(observer, times(1)).onNext(intent);

        subscription.unsubscribe();
        application.sendBroadcast(intent);
        inOrder.verify(observer, never()).onNext(any(Intent.class));

        inOrder.verify(observer, never()).onError(any(Throwable.class));
        inOrder.verify(observer, never()).onCompleted();
    }

    @Test
    public void testStickyBroadcast() {
        String action = "TEST_STICKY_ACTION";
        IntentFilter intentFilter = new IntentFilter(action);
        Application application = RuntimeEnvironment.application;
        Intent intent = new Intent(action);
        application.sendStickyBroadcast(intent);
        Observable<Intent> observable = RxReceiver.fromBroadcast(application, intentFilter);
        final Observer<Intent> observer = mock(Observer.class);
        final Subscription subscription = observable.subscribe(new TestObserver<Intent>(observer));

        final InOrder inOrder = inOrder(observer);

        inOrder.verify(observer, times(1)).onNext(intent);

        application.sendBroadcast(intent);
        inOrder.verify(observer, times(1)).onNext(intent);

        subscription.unsubscribe();
        application.sendBroadcast(intent);
        inOrder.verify(observer, never()).onNext(any(Intent.class));

        inOrder.verify(observer, never()).onError(any(Throwable.class));
        inOrder.verify(observer, never()).onCompleted();
    }

    @Test
    public void testPermissionBroadcast() {
        String action = "TEST_ACTION";
        String permission = "test_permission";
        IntentFilter intentFilter = new IntentFilter(action);
        Application application = RuntimeEnvironment.application;
        Observable<Intent> observable = RxReceiver.fromBroadcast(application, intentFilter, permission, null);
        final Observer<Intent> observer = mock(Observer.class);
        final Subscription subscription = observable.subscribe(new TestObserver<Intent>(observer));

        final InOrder inOrder = inOrder(observer);

        inOrder.verify(observer, never()).onNext(any(Intent.class));

        Intent intent = new Intent(action);
        application.sendBroadcast(intent);
        inOrder.verify(observer, never()).onNext(intent);

        application.sendBroadcast(intent, permission);
        inOrder.verify(observer, times(1)).onNext(intent);

        subscription.unsubscribe();
        application.sendBroadcast(intent);
        application.sendBroadcast(intent, permission);
        inOrder.verify(observer, never()).onNext(any(Intent.class));

        inOrder.verify(observer, never()).onError(any(Throwable.class));
        inOrder.verify(observer, never()).onCompleted();
    }

    @Test
    public void testLocalBroadcast() {
        String action = "TEST_ACTION";
        IntentFilter intentFilter = new IntentFilter(action);
        Application application = RuntimeEnvironment.application;
        Observable<Intent> observable = RxReceiver.fromLocalBroadcast(application, intentFilter);
        final Observer<Intent> observer = mock(Observer.class);
        final Subscription subscription = observable.subscribe(new TestObserver<Intent>(observer));

        final InOrder inOrder = inOrder(observer);

        inOrder.verify(observer, never()).onNext(any(Intent.class));

        Intent intent = new Intent(action);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(application);
        localBroadcastManager.sendBroadcast(intent);
        inOrder.verify(observer, times(1)).onNext(intent);

        localBroadcastManager.sendBroadcast(intent);
        inOrder.verify(observer, times(1)).onNext(intent);

        subscription.unsubscribe();
        localBroadcastManager.sendBroadcast(intent);
        inOrder.verify(observer, never()).onNext(any(Intent.class));

        inOrder.verify(observer, never()).onError(any(Throwable.class));
        inOrder.verify(observer, never()).onCompleted();
    }
}