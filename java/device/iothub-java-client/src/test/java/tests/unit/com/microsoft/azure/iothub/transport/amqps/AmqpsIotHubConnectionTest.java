/*
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

package tests.unit.com.microsoft.azure.iothub.transport.amqps;

import com.microsoft.azure.iothub.DeviceClientConfig;
import com.microsoft.azure.iothub.IotHubClientProtocol;
import com.microsoft.azure.iothub.IotHubMessageResult;
import com.microsoft.azure.iothub.auth.IotHubSasToken;
import com.microsoft.azure.iothub.net.IotHubUri;
import com.microsoft.azure.iothub.transport.State;
import com.microsoft.azure.iothub.transport.amqps.AmqpsIotHubConnection;
import com.microsoft.azure.iothub.transport.amqps.AmqpsMessage;
import com.microsoft.azure.iothub.transport.amqps.IotHubReactor;
import mockit.*;
import org.apache.qpid.proton.Proton;
import org.apache.qpid.proton.engine.*;
import org.apache.qpid.proton.message.Message;
import org.apache.qpid.proton.reactor.FlowController;
import org.apache.qpid.proton.reactor.Handshaker;
import org.apache.qpid.proton.reactor.Reactor;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class AmqpsIotHubConnectionTest {

    final String hostName = "test.host.name";
    final String hubName = "test.iothub";
    final String deviceId = "test-deviceId";
    final String deviceKey = "test-devicekey?&test";
    final String resourceUri = "test-resource-uri";
    final String amqpPort = "5671";
    final String amqpWebSocketPort = "443";

    @Mocked
    protected Handshaker mockHandshaker;

    @Mocked
    protected FlowController mockFlowController;

    @Mocked
    protected Proton mockProton;

    @Mocked
    protected IotHubReactor mockReactor;

    @Mocked
    protected DeviceClientConfig mockConfig;

    @Mocked
    protected IotHubUri mockIotHubUri;

    @Mocked
    protected IotHubSasToken mockToken;

    @Mocked
    protected Message mockMessage;

    @Mocked
    protected AmqpsMessage mockAmqpsMessage;

    @Mocked
    protected IotHubSasToken mockSasToken;

    @Mocked
    protected Sender mockSender;

    @Mocked
    protected Receiver mockReceiver;

    @Mocked
    protected Connection mockConnection;

    @Mocked
    protected Session mockSession;

    @Mocked
    protected Event mockEvent;

    @Mocked
    protected Future mockReactorFuture;

    @Mocked
    protected ExecutorService mockExecutorService;

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_001: [The constructor shall throw IllegalArgumentException if
    // any of the parameters of the configuration is null or empty.]
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionIfHostNameIsEmpty(){
        new NonStrictExpectations() {
            {
                mockConfig.getIotHubHostname();
                result = "";
                mockConfig.getIotHubName();
                result = hubName;
                mockConfig.getDeviceId();
                result = deviceId;
                mockConfig.getDeviceKey();
                result = deviceKey;
            }
        };

        new AmqpsIotHubConnection(mockConfig, false);
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_001: [The constructor shall throw IllegalArgumentException if
    // any of the parameters of the configuration is null or empty.]
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionIfHostNameIsNull(){
        new NonStrictExpectations() {
            {
                mockConfig.getIotHubHostname();
                result = null;
                mockConfig.getIotHubName();
                result = hubName;
                mockConfig.getDeviceId();
                result = deviceId;
                mockConfig.getDeviceKey();
                result = deviceKey;
            }
        };

        new AmqpsIotHubConnection(mockConfig, false);
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_001: [The constructor shall throw IllegalArgumentException if
    // any of the parameters of the configuration is null or empty.]
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionIfDeviceIdIsEmpty(){
        new NonStrictExpectations() {
            {
                mockConfig.getIotHubHostname();
                result = hostName;
                mockConfig.getIotHubName();
                result = hubName;
                mockConfig.getDeviceId();
                result = "";
                mockConfig.getDeviceKey();
                result = deviceKey;
            }
        };

        new AmqpsIotHubConnection(mockConfig, false);
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_001: [The constructor shall throw IllegalArgumentException if
    // any of the parameters of the configuration is null or empty.]
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionIfDeviceIdIsNull(){
        new NonStrictExpectations() {
            {
                mockConfig.getIotHubHostname();
                result = hostName;
                mockConfig.getIotHubName();
                result = hubName;
                mockConfig.getDeviceId();
                result = null;
                mockConfig.getDeviceKey();
                result = deviceKey;
            }
        };

        new AmqpsIotHubConnection(mockConfig, false);
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_001: [The constructor shall throw IllegalArgumentException if
    // any of the parameters of the configuration is null or empty.]
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionIfUserNameIsEmpty(){
        new NonStrictExpectations() {
            {
                mockConfig.getIotHubHostname();
                result = hostName;
                mockConfig.getIotHubName();
                result = hubName;
                mockConfig.getDeviceId();
                result = deviceId;
                mockConfig.getDeviceKey();
                result = "";
            }
        };

        new AmqpsIotHubConnection(mockConfig, false);
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_001: [The constructor shall throw IllegalArgumentException if
    // any of the parameters of the configuration is null or empty.]
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionIfUserNameIsNull(){
        new NonStrictExpectations() {
            {
                mockConfig.getIotHubHostname();
                result = hostName;
                mockConfig.getIotHubName();
                result = hubName;
                mockConfig.getDeviceId();
                result = deviceId;
                mockConfig.getDeviceKey();
                result = null;
            }
        };

        new AmqpsIotHubConnection(mockConfig, false);
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_001: [The constructor shall throw IllegalArgumentException if
    // any of the parameters of the configuration is null or empty.]
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionIfHubNameIsEmpty(){
        new NonStrictExpectations() {
            {
                mockConfig.getIotHubHostname();
                result = hostName;
                mockConfig.getIotHubName();
                result = "";
                mockConfig.getDeviceId();
                result = deviceId;
                mockConfig.getDeviceKey();
                result = deviceKey;
            }
        };

        new AmqpsIotHubConnection(mockConfig, false);
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_001: [The constructor shall throw IllegalArgumentException if
    // any of the parameters of the configuration is null or empty.]
    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionIfHubNameIsNull(){
        new NonStrictExpectations() {
            {
                mockConfig.getIotHubHostname();
                result = hostName;
                mockConfig.getIotHubName();
                result = null;
                mockConfig.getDeviceId();
                result = deviceId;
                mockConfig.getDeviceKey();
                result = deviceKey;
            }
        };

        new AmqpsIotHubConnection(mockConfig, false);
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_002: [The constructor shall save the configuration into private member variables.]
    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_003: [The constructor shall initialize the sender and receiver
    // endpoint private member variables using the send/receiveEndpointFormat constants and device id.]
    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_004: [The constructor shall initialize a new Handshaker
    // (Proton) object to handle communication handshake.]
    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_005: [The constructor shall initialize a new FlowController
    // (Proton) object to handle communication flow.]
    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_006: [The constructor shall set its state to CLOSED.]
    @Test
    public void constructorCopiesAllData()
    {
        baseExpectations();

        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, false);

        DeviceClientConfig actualConfig = Deencapsulation.getField(connection, "config");
        String actualHostName = Deencapsulation.getField(connection, "hostName");
        String actualUserName = Deencapsulation.getField(connection, "userName");
        String actualSendEndpoint = Deencapsulation.getField(connection, "sendEndpoint");
        String actualReceiveEndpoint = Deencapsulation.getField(connection, "receiveEndpoint");

        assertEquals(mockConfig, actualConfig);
        assertEquals(hostName + ":" + amqpPort, actualHostName);
        assertEquals(deviceId + "@sas." + hubName, actualUserName);

        String expectedSendEndpoint = "/devices/test-deviceId/messages/events";
        assertEquals(expectedSendEndpoint, actualSendEndpoint);

        String expectedReceiveEndpoint = "/devices/test-deviceId/messages/devicebound";
        assertEquals(expectedReceiveEndpoint, actualReceiveEndpoint);

        new Verifications()
        {
            {
                new Handshaker();
                times = 1;
                new FlowController();
                times = 1;
            }
        };

        State actualState = Deencapsulation.getField(connection, "state");
        assertEquals(State.CLOSED, actualState);
    }

    @Test
    public void constructorSetsHostNameCorrectlyWhenWebSocketsAreEnabled()
    {
        baseExpectations();

        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, true);

        String actualHostName = Deencapsulation.getField(connection, "hostName");
        assertEquals(hostName + ":" + amqpWebSocketPort, actualHostName);
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_007: [If the AMQPS connection is already open, the function shall do nothing.]
    @Test
    public void openDoesNothingIfTheConnectionIsAlreadyOpen() throws IOException
    {
        baseExpectations();

        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, false);

        Deencapsulation.setField(connection, "state", State.OPEN);

        connection.open();

        new Verifications()
        {
            {
                new IotHubSasToken(anyString, anyString, anyLong);
                times = 0;
            }
        };
    }


    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_008: [The function shall create a new sasToken valid for the duration
    // specified in config to be used for the communication with IoTHub.]
    @Test
    public void openCreatesSasToken() throws IOException
    {
        baseExpectations();

        new MockUp<AmqpsIotHubConnection>() {
            @Mock
            Future startReactorAsync()
            {
                return null;
            }

            @Mock
            void connectionReady()
            {
            }
        };

        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, false);

        connection.open();

        new Verifications()
        {
            {
                new IotHubSasToken(anyString, anyString, anyLong);
                times = 1;
            }
        };
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_009: [The function shall trigger the Reactor (Proton) to begin running.]
    @Test
    public void openTriggersProtonReactor() throws IOException
    {
        baseExpectations();

        new NonStrictExpectations() {
            {
                new IotHubReactor((Reactor) any);
                result = mockReactor;
                mockReactor.run();
            }
        };

        new MockUp<AmqpsIotHubConnection>() {
            @Mock
            void connectionReady()
            {
            }
        };

        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, false);

        connection.open();

        new Verifications()
        {
            {
                new IotHubSasToken(anyString, anyString, anyLong);
                times = 1;
                new IotHubReactor((Reactor)any);
                times = 1;
                mockReactor.run();
                times = 1;
            }
        };
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_010: [The function shall wait for the reactor to be ready and for
    // enough link credit to become available.]
    @Test
    public void openWaitsForReactorToBeReadyAndForEnoughLinkCreditToBeAvailable() throws IOException
    {
        baseExpectations();

        final AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, false);

        new MockUp<AmqpsIotHubConnection>() {
            @Mock
            Future startReactorAsync()
            {
                Deencapsulation.setField(connection, "state", State.OPEN);
                Deencapsulation.setField(connection, "linkCredit", 1000);
                return null;
            }
        };

        connection.open();
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_011: [If any exception is thrown while attempting to trigger
    // the reactor, the function shall close the connection and throw an IOException.]
    @Test(expected = IOException.class)
    public void openFailsIfConnectionIsNotOpenedInTime() throws IOException
    {
        baseExpectations();

        final AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, false);

        new MockUp<AmqpsIotHubConnection>() {
            @Mock
            Future startReactorAsync()
            {
                Deencapsulation.setField(connection, "linkCredit", 1000);
                return null;
            }

            @Mock
            void close()
            {
            }
        };

        Deencapsulation.setField(connection, "maxWaitTimeForOpeningConnection", 100);

        connection.open();
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_048 [If the AMQPS connection is already closed, the function shall do nothing.]
    @Test
    public void closeDoesNothingIfTheConnectionWasNeverOpened()
    {
        baseExpectations();

        final AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, false);

        Deencapsulation.setField(connection, "sender", mockSender);
        connection.close();

        new Verifications()
        {
            {
                mockSender.close();
                times = 0;
                mockReceiver.close();
                times = 0;
                mockSession.close();
                times = 0;
                mockConnection.close();
                times = 0;
                mockReactorFuture.cancel(true);
                times = 0;
                mockExecutorService.shutdown();
                times = 0;
            }
        };
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_012: [The function shall set the status of the AMQPS connection to CLOSED.]
    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_013: [The function shall close the AMQPS sender and receiver links,
    // the AMQPS session and the AMQPS connection.]
    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_014: [The function shall stop the Proton reactor.]
    @Test
    public void closeClosesAllProtonVariablesAndStopsProtonReactor() throws IOException
    {
        baseExpectations();

        new NonStrictExpectations()
        {
            {
                mockReactorFuture.cancel(true);
                mockExecutorService.shutdown();
            }
        };

        new MockUp<AmqpsIotHubConnection>() {
            @Mock
            void open()
            {
            }
        };

        final AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, false);

        Deencapsulation.setField(connection, "state", State.OPEN);
        Deencapsulation.setField(connection, "sender", mockSender);
        Deencapsulation.setField(connection, "receiver", mockReceiver);
        Deencapsulation.setField(connection, "session", mockSession);
        Deencapsulation.setField(connection, "connection", mockConnection);
        Deencapsulation.setField(connection, "reactorFuture", mockReactorFuture);
        Deencapsulation.setField(connection, "executorService", mockExecutorService);

        connection.open();
        connection.close();

        State actualState = Deencapsulation.getField(connection, "state");
        assertEquals(State.CLOSED, actualState);

        new Verifications()
        {
            {
                mockSender.close();
                times = 1;
                mockReceiver.close();
                times = 1;
                mockSession.close();
                times = 1;
                mockConnection.close();
                times = 1;
                mockReactorFuture.cancel(true);
                times = 1;
                mockExecutorService.shutdown();
                times = 1;
            }
        };
    }

    // Tests_SRS_AMQPSIOTHUBCONNECTION_15_048 [If the AMQPS connection is already closed, the function shall do nothing.]
    @Test
    public void closeDoesNothingIfTheConnectionWasAlreadyClosed() throws IOException
    {
        baseExpectations();

        new NonStrictExpectations()
        {
            {
                mockReactorFuture.cancel(true);
                mockExecutorService.shutdown();
            }
        };

        new MockUp<AmqpsIotHubConnection>() {
            @Mock
            void open()
            {
            }
        };

        final AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, false);

        Deencapsulation.setField(connection, "state", State.OPEN);
        Deencapsulation.setField(connection, "sender", mockSender);
        Deencapsulation.setField(connection, "receiver", mockReceiver);
        Deencapsulation.setField(connection, "session", mockSession);
        Deencapsulation.setField(connection, "connection", mockConnection);
        Deencapsulation.setField(connection, "reactorFuture", mockReactorFuture);
        Deencapsulation.setField(connection, "executorService", mockExecutorService);

        connection.open();
        connection.close();

        State actualState = Deencapsulation.getField(connection, "state");
        assertEquals(State.CLOSED, actualState);

        connection.close();

        new Verifications()
        {
            {
                mockSender.close();
                times = 1;
                mockReceiver.close();
                times = 1;
                mockSession.close();
                times = 1;
                mockConnection.close();
                times = 1;
                mockReactorFuture.cancel(true);
                times = 1;
                mockExecutorService.shutdown();
                times = 1;
            }
        };
    }


//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_008: [The function shall initialize its AmqpsIotHubConnectionBaseHandler using the saved host name, user name, device ID and sas token.]
//    @Test
//    public void openInitializesBaseHandler(
//            @Mocked Future<Boolean> future) throws InterruptedException, ExecutionException, IOException, TimeoutException {
//        baseExpectations();
//        connectionOpenExpectations(future, false);
//
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        connection.open();
//
//        new Verifications()
//        {
//            {
//                new AmqpsIotHubConnectionBaseHandler(hostName,
//                        deviceId + "@sas." + hubName, mockToken.toString(), deviceId, IotHubClientProtocol.AMQPS, connection);
//            }
//        };
//    }
//
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_009: [The function shall open the Amqps connection and trigger the Reactor (Proton) to begin running.]
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_011: [If the AMQPS connection is already open, the function shall do nothing.]
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_010: [Once the Reactor (Proton) is ready, the function shall set it's state to OPEN.]
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_031: [The function shall get the link credit from it's AmqpsIotHubConnectionBaseHandler and set the private maxQueueSize member variable.]
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_032: [The function shall successfully complete it’s Future status member variable.]
//    @Test
//    public void openTriggersReactorAndOpensConnection(
//            @Mocked Future<Boolean> future) throws InterruptedException, ExecutionException, IOException, TimeoutException {
//        new MockUp<AmqpsIotHubConnection>(){
//            @Mock
//            private void startReactorAsync(Invocation inv){inv.proceed();}
//        };
//
//        baseExpectations();
//        new NonStrictExpectations() {
//            {
//                mockProton.reactor((BaseHandler) any);
//                result = mockReactor;
//                mockReactor.run();
//                new Future<>();
//                result = future;
//                future.get();
//                result = true;
//            }
//        };
//
//        //mockReactor.run() will only be invoked once if the state is set to OPEN after the first invocation
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        connection.open();
//        connection.open();
//
//        new Verifications() {
//            {
//                mockHandler.getLinkCredit(); times = 1;
//                future.complete(new Boolean(true)); times = 1;
//            }
//        };
//    }
//
//    //TODO: // Tests_SRS_AMQPSIOTHUBCONNECTION_14_012: [If the AmqpsIotHubConnectionBaseHandler becomes invalidated before the Reactor (Proton) starts, the function shall throw an IOException.]
//
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_013: [The function shall invalidate the private Reactor (Proton) member variable.]
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_014: [The function shall free the AmqpsIotHubConnectionBaseHandler.]
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_015: [The function shall close the AMQPS connection.]
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_033: [The function shall close the AmqpsIotHubConnectionBaseHandler.]
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_034: [The function shall exceptionally complete all remaining messages that are currently in progress and clear the queue.]
//    @Test
//    public void closeFullTest(
//            @Mocked Future<Boolean> future) throws InterruptedException, ExecutionException, IOException, TimeoutException {
//        baseExpectations();
//        connectionOpenExpectations(future);
//
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        //Will close after opening
//        connection.open();
//        connection.close();
//
//        IotHubReactor actualReactor = Deencapsulation.getField(connection, "iotHubReactor");
//        AmqpsIotHubConnectionBaseHandler actualHandler = Deencapsulation.getField(connection, "amqpsHandler");
//        assertNull(actualReactor);
//        assertNull(actualHandler);
//
//        new Verifications()
//        {
//            {
//                mockHandler.shutdown();
//                Deencapsulation.invoke(connection, "clearInProgressMap");
//                Deencapsulation.invoke((Map)Deencapsulation.getField(connection, "inProgressMessageMap"), "clear");
//                Deencapsulation.invoke(connection, "freeReactor");
//                Deencapsulation.invoke(connection, "freeHandler");
//            }
//        };
//    }
//
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_016: [If the AMQPS connection is already closed, the function shall do nothing.]
//    @Test
//    public void closeDoesNothingIfClosed() throws InterruptedException, ExecutionException, IOException
//    {
//        baseExpectations();
//
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        connection.close();
//
//        new Verifications()
//        {
//            {
//                Deencapsulation.invoke(connection, "freeReactor");
//                //Todo: This should be here. But I cannot get this to cooperate at the moment.
//                //times = 0;
//            }
//        };
//    }
//
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_019: [The function shall attempt to remove a message from the queue.]
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_020: [The function shall return the message if one was pulled from the queue, otherwise it shall return null.]
//    @Test
//    public void consumeMessageAttemptsToRemoveAndPullsSuccessfully(
//            @Mocked LinkedBlockingQueue<Message> queue) throws InterruptedException, ExecutionException, IOException
//    {
//        baseExpectations();
//        new NonStrictExpectations()
//        {
//            {
//                new LinkedBlockingQueue<>();
//                result = queue;
//                queue.size();
//                result = 1;
//                queue.remove();
//                result = mockAmqpsMessage;
//            }
//        };
//
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        Message consumedMessage = connection.consumeMessage();
//
//        assertNotNull(consumedMessage);
//
//        new Verifications()
//        {
//            {
//                queue.remove();
//            }
//        };
//    }
//
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_019: [The function shall attempt to remove a message from the queue.]
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_020: [The function shall return the message if one was pulled from the queue, otherwise it shall return null.]
//    @Test
//    public void consumeMessageAttemptsToRemoveAndPullsUnsuccessfully(
//            @Mocked LinkedBlockingQueue<Message> queue) throws InterruptedException, ExecutionException, IOException
//    {
//        baseExpectations();
//
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        Message consumedMessage = connection.consumeMessage();
//        assertNull(consumedMessage);
//    }
//
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_021: [If the message result is COMPLETE, ABANDON, or REJECT, the function shall acknowledge the last message with acknowledgement type COMPLETE, ABANDON, or REJECT respectively.]
//    @Test
//    public void sendMessageResultSendsProperAckForMessageResult(
//            @Mocked Future<Boolean> future) throws InterruptedException, ExecutionException, IOException, TimeoutException {
//        baseExpectations();
//        connectionOpenExpectations(future);
//
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        connection.open();
//        Deencapsulation.setField(connection, "amqpsHandler", mockHandler);
//        Deencapsulation.setField(connection, "lastMessage", mockAmqpsMessage);
//        connection.sendMessageResult(IotHubMessageResult.COMPLETE);
//        connection.sendMessageResult(IotHubMessageResult.ABANDON);
//        connection.sendMessageResult(IotHubMessageResult.REJECT);
//
//        new Verifications() {
//            {
//
//                mockAmqpsMessage.acknowledge(AmqpsMessage.ACK_TYPE.COMPLETE);
//                mockAmqpsMessage.acknowledge(AmqpsMessage.ACK_TYPE.ABANDON);
//                mockAmqpsMessage.acknowledge(AmqpsMessage.ACK_TYPE.REJECT);
//            }
//        };
//    }
//
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_022: [If sendMessageResult(result) is called before a message is received, the function shall throw an IllegalStateException.]
//    @Test(expected = IllegalStateException.class)
//    public void sendMessageResultThrowsIllegalStateExceptionIfNoMessage(
//            @Mocked Future<Boolean> future) throws InterruptedException, ExecutionException, IOException, TimeoutException {
//        baseExpectations();
//        connectionOpenExpectations(future);
//
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        connection.open();
//        Deencapsulation.setField(connection, "amqpsHandler", mockHandler);
//        connection.sendMessageResult(IotHubMessageResult.COMPLETE);
//    }
//
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_023: [If the acknowledgement fails, the function shall throw an IOException.]
//    @Test(expected = IOException.class)
//    public void sendMessageResultThrowsIOExceptionIfAckFails(
//            @Mocked Future<Boolean> future) throws InterruptedException, ExecutionException, IOException, TimeoutException {
//        baseExpectations();
//        connectionOpenExpectations(future);
//        new NonStrictExpectations() {
//            {
//                mockAmqpsMessage.acknowledge((AmqpsMessage.ACK_TYPE) any);
//                result = new Exception();
//
//            }
//        };
//
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        connection.open();
//        Deencapsulation.setField(connection, "amqpsHandler", mockHandler);
//        Deencapsulation.setField(connection, "lastMessage", mockAmqpsMessage);
//        connection.sendMessageResult(IotHubMessageResult.COMPLETE);
//    }
//
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_024: [If the AMQPS Connection is closed, the function shall throw an IllegalStateException.]
//    @Test(expected = IllegalStateException.class)
//    public void sendMessageResultThrowsIllegalStateExceptionIfConnectionClosed() throws IOException {
//
//        baseExpectations();
//
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        Deencapsulation.setField(connection, "amqpsHandler", mockHandler);
//        connection.sendMessageResult(IotHubMessageResult.COMPLETE);
//    }
//
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_025: [If the AmqpsIotHubConnectionBaseHandler has not been initialized, the function shall throw a new IOException and exceptionally complete it’s Future status member variable with the same exception.]
//    @Test(expected = IOException.class)
//    public void scheduleSendThrowsIOExceptionIfHandlerNotInitialized(
//            @Mocked Future<Boolean> future,
//            @Mocked Future<Boolean> mockCompletionStatus) throws InterruptedException, ExecutionException, IOException, TimeoutException {
//        final byte[] msgBody = { 0x61, 0x62, 0x63 };
//
//        baseExpectations();
//        connectionOpenExpectations(future);
//
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        connection.open();
//        Deencapsulation.setField(connection, "completionStatus", mockCompletionStatus);
//        Deencapsulation.setField(connection, "amqpsHandler", null);
//        connection.sendMessage(msgBody);
//
//        new Verifications()
//        {
//            {
//                mockCompletionStatus.completeExceptionally((IOException)any); times = 1;
//            }
//        };
//    }
//
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_025: [If the AmqpsIotHubConnectionBaseHandler has not been initialized, the function shall throw a new IOException and exceptionally complete it’s Future status member variable with the same exception.]
//    @Test
//    public void scheduleSendCompletesFutureExceptionallyIfHandlerNotInitialized(
//            @Mocked Future<Boolean> future,
//            @Mocked Future<Boolean> mockCompletionStatus) throws InterruptedException, ExecutionException, IOException, TimeoutException {
//        final byte[] msgBody = { 0x61, 0x62, 0x63 };
//
//        baseExpectations();
//        connectionOpenExpectations(future);
//
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        connection.open();
//        Deencapsulation.setField(connection, "completionStatus", mockCompletionStatus);
//        Deencapsulation.setField(connection, "amqpsHandler", null);
//        try {
//            connection.sendMessage(msgBody);
//        } catch(Exception e){}
//
//        new Verifications()
//        {
//            {
//                mockCompletionStatus.completeExceptionally((IOException)any);
//            }
//        };
//    }
//
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_026: [The function shall create a new Future for the message acknowledgement.]
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_027: [The function shall create a new Tuple containing the Future, message content, and message ID.]
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_028: [The function shall acquire a lock and attempt to send the message.]
//    @Test
//    public void scheduleSendCreatesFutureAndSends(
//            @Mocked Future<Boolean> future) throws InterruptedException, ExecutionException, IOException, TimeoutException {
//        final byte[] msgBody = { 0x61, 0x62, 0x63 };
//
//        baseExpectations();
//        connectionOpenExpectations(future);
//
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        connection.open();
//        connection.sendMessage(msgBody);
//
//        new Verifications()
//        {
//            {
//                //TODO: Improve by using actual Tuple type
//                new Future<>();
//                mockHandler.sendBinaryMessage(msgBody, null);
//            }
//        };
//    }
//
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_029: [If the AMQPS Connection is closed, the function shall throw an IllegalStateException.]
//    @Test(expected = IllegalStateException.class)
//    public void scheduleSendThrowsIllegalStateExceptionIfClosed() throws InterruptedException, ExecutionException, IOException
//    {
//        final byte[] msgBody = { 0x61, 0x62, 0x63 };
//
//        baseExpectations();
//
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        connection.sendMessage(msgBody);
//    }
//
//    // Tests_SRS_AMQPSIOTHUBCONNECTION_14_030: [The event handler shall set the member AmqpsIotHubConnectionBaseHandler object to handle the connection events.]
//    @Test
//    public void onReactorInitSetsConnectionToHandler(
//            @Mocked Future<Boolean> future) throws InterruptedException, ExecutionException, IOException, TimeoutException {
//        baseExpectations();
//        connectionOpenExpectations(future);
//
//        AmqpsIotHubConnection connection = new AmqpsIotHubConnection(mockConfig, IotHubClientProtocol.AMQPS);
//        connection.open();
//        connection.onReactorInit(mockEvent);
//
//        new Verifications()
//        {
//            {
//                mockEvent.getReactor();
//                mockReactor.connection(mockHandler);
//            }
//        };
//    }
//
//
//    public void connectionOpenExpectations(@Mocked Future<Boolean> future, boolean needsBaseHandler) throws IOException, ExecutionException, InterruptedException, TimeoutException {
//        new MockUp<AmqpsIotHubConnection>(){
//            @Mock
//            private void startReactorAsync(){}
//        };
//
//        if(needsBaseHandler) {
//            new NonStrictExpectations() {
//                {
//                    new AmqpsIotHubConnectionBaseHandler(anyString, anyString, anyString, anyString, IotHubClientProtocol.AMQPS, (AmqpsIotHubConnection) any);
//                    result = mockHandler;
//                    mockProton.reactor((BaseHandler) any);
//                    result = mockReactor;
//                    mockReactor.run();
//                    new Future<>();
//                    result = future;
//                    future.get();
//                    result = true;
//                    mockHandler.getLinkCredit();
//                    result = 10;
//                }
//            };
//        } else {
//            new NonStrictExpectations() {
//                {
//                    mockProton.reactor((BaseHandler) any);
//                    result = mockReactor;
//                    mockReactor.run();
//                    new Future<>();
//                    result = future;
//                    future.get();
//                    result = true;
//                    mockHandler.getLinkCredit();
//                    result = 10;
//                }
//            };
//        }
//    }
//
//    public void connectionOpenExpectations(@Mocked Future<Boolean> future) throws InterruptedException, ExecutionException, IOException, TimeoutException {
//        connectionOpenExpectations(future, true);
//    }

    public void baseExpectations()
    {
        new NonStrictExpectations() {
            {
                mockConfig.getIotHubHostname();
                result = hostName;
                mockConfig.getIotHubName();
                result = hubName;
                mockConfig.getDeviceId();
                result = deviceId;
                mockConfig.getDeviceKey();
                result = deviceKey;
                IotHubUri.getResourceUri(hostName, deviceId);
                result = resourceUri;
            }
        };
    }
}
