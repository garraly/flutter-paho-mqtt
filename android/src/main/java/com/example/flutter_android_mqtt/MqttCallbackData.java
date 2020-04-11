package com.example.flutter_android_mqtt;

import androidx.annotation.NonNull;

public class MqttCallbackData {
    private final String key;
    private final String value;

    public MqttCallbackData(@NonNull String key, String value) {
        this.key = key;
        this.value = value;
    }
}
