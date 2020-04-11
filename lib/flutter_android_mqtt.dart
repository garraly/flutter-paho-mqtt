import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:flutter_android_mqtt/entities/enum/MqttCallbackEnum.dart';

class FlutterAndroidMqtt {
  static const String CONNECT = "CONNECT";
  static const String UN_SUBSCRIBE = "UN_SUBSCRIBE";
  static const String SUBSCRIBE = "SUBSCRIBE";

  static const MethodChannel _channel = const MethodChannel('flutter_android_mqtt');
  static const EventChannel _eventChannel = const EventChannel('flutter_android_mqtt_event');

  static EventChannel get eventChannel => _eventChannel;

  static void connect({
    @required final String serverUri,
    @required String clientId,
    @required String userName,
    @required String password,
  }) {
    _channel.invokeMethod(CONNECT, {
      "serverUri": serverUri,
      "clientId": clientId,
      "userName": userName,
      "password": password,
    });
  }

  static void subscribe({@required String subscriptionTopic, @required int qos}) {
    _channel.invokeListMethod(SUBSCRIBE,{
      "subscriptionTopic": subscriptionTopic,
      "qos": qos,
    });
  }

  static void listenMessage(Function(MqttStatus, String) callback) {
    eventChannel.receiveBroadcastStream().listen((data) {
      if (callback != null) {
        callback(data.key, data.value);
      }
    });
  }
}
