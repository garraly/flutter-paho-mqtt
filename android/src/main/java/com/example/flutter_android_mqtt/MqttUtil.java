package com.example.flutter_android_mqtt;


import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import androidx.annotation.NonNull;

public class MqttUtil extends AppCompatActivity {

    MqttAndroidClient mqttAndroidClient;

    private String subscriptionTopic = "";
    private int qos = 0;

    // 对应flutter 的枚举状态
    private final String CONNECT_SUCCESS = "CONNECT_SUCCESS";
    private final String CONNECT_FAIL = "CONNECT_FAIL";
    private final String SUBSCRIBE_SUCCESS = "CONNECT_SUCCESS";
    private final String SUBSCRIBE_FAIL = "CONNECT_FAIL";
    private final String RECONNECT = "RECONNECT";
    private final String CONNECT_LOST = "CONNECT_LOST";
    private final String MESSAGE_ARRIVED = "MESSAGE_ARRIVED";


    // mqtt链接
    public void connect(@NonNull Context context, @NonNull final String serverUri, @NonNull String clientId, String userName, String password) {
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {

            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    addToHistory("Reconnected to : " + serverURI);
                    final MqttCallbackData data = new MqttCallbackData(RECONNECT, null);
                    FlutterAndroidMqttPlugin.eventSink.success(data);
                    subscribeToTopic(subscriptionTopic, qos);
                } else {
                    addToHistory("Connected to: " + serverURI);
                    final MqttCallbackData data = new MqttCallbackData(CONNECT_SUCCESS, null);
                    FlutterAndroidMqttPlugin.eventSink.success(data);
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                addToHistory("The Connection was lost.");
                final MqttCallbackData data = new MqttCallbackData(CONNECT_LOST, null);
                FlutterAndroidMqttPlugin.eventSink.success(data);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                addToHistory("Incoming message: " + new String(message.getPayload()));
                final MqttCallbackData data = new MqttCallbackData(MESSAGE_ARRIVED, new String(message.getPayload()));
                FlutterAndroidMqttPlugin.eventSink.success(data);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        final MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        if (!userName.isEmpty()) {
            mqttConnectOptions.setUserName(userName);
        }

        if (!password.isEmpty()) {
            mqttConnectOptions.setPassword(password.toCharArray());
        }

        try {
            addToHistory("Connecting to " + serverUri);
            mqttAndroidClient.connect(mqttConnectOptions, context, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to connect : message = " + exception.getMessage());
                    final MqttCallbackData data = new MqttCallbackData(CONNECT_FAIL, null);
                    FlutterAndroidMqttPlugin.eventSink.success(data);
                }
            });

        } catch (MqttException ex) {
            ex.printStackTrace();
        }

    }


    private void addToHistory(String mainText) {
        System.out.println("LOG:----------> " + mainText);
    }

    // mqtt 的监听
    public void subscribeToTopic(@NonNull String pic, int qos) {
        this.subscriptionTopic = pic;
        this.qos = qos;
        try {
            mqttAndroidClient.subscribe(pic, qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    addToHistory("Subscribed!");
                    final MqttCallbackData data = new MqttCallbackData(SUBSCRIBE_SUCCESS, null);
                    FlutterAndroidMqttPlugin.eventSink.success(data);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    addToHistory("Failed to subscribe");
                    final MqttCallbackData data = new MqttCallbackData(SUBSCRIBE_FAIL, null);
                    FlutterAndroidMqttPlugin.eventSink.success(data);
                }
            });

            // THIS DOES NOT WORK!
            mqttAndroidClient.subscribe(subscriptionTopic, 0, new IMqttMessageListener() {
                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // message Arrived!
                    System.out.println("Message: " + topic + " : " + new String(message.getPayload()));
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    // mqtt解除监听
    public void unSubscribeToTopic() {
        try {
            mqttAndroidClient.unsubscribe(subscriptionTopic, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // mqtt的推送消息
    public void publishMessage(@NonNull String publishTopic, @NonNull String publishMessage) {
        try {
            MqttMessage message = new MqttMessage();
            message.setPayload(publishMessage.getBytes());
            mqttAndroidClient.publish(publishTopic, message);
            addToHistory("Message Published");
            if (!mqttAndroidClient.isConnected()) {
                addToHistory(mqttAndroidClient.getBufferedMessageCount() + " messages in buffer.");
            }
        } catch (MqttException e) {
            System.err.println("Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }
    }

}