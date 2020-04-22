import 'dart:convert';
import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

class FlutterAndroidMqtt {
  static const String CONNECT = "CONNECT";
  static const String UN_SUBSCRIBE = "UN_SUBSCRIBE";
  static const String SUBSCRIBE = "SUBSCRIBE";

  static const MethodChannel _channel = const MethodChannel('flutter_android_mqtt');
  static const EventChannel _eventChannel = const EventChannel('flutter_android_mqtt_event');

  static EventChannel get eventChannel => _eventChannel;

  static void connect({
    @required final String serverUri,
    String clientId,
    @required String userName,
    @required String password,
  }) {
    if (Platform.isAndroid) {
      _channel.invokeMethod(CONNECT, {
        "serverUri": serverUri,
        "clientId": clientId,
        "userName": userName,
        "password": password,
      });
    }
  }

  static void subscribe({@required String subscriptionTopic, @required int qos}) {
    if (Platform.isAndroid) {
      _channel.invokeListMethod(SUBSCRIBE, {
        "subscriptionTopic": subscriptionTopic,
        "qos": qos,
      });
    }
  }

  static void listenMessage(Function(MqttStatus, String, String) callback) {
    if (Platform.isAndroid) {
      eventChannel.receiveBroadcastStream().listen((data) {
        if (callback != null) {
          final String value = data;
          List<String> msg = value.split(',');
          // 保证最低长度为3的填充
          msg.add(null);
          msg.add(null);
          Map<int,String> msgMap = msg.asMap();
          callback(MqttStatusExtension.keys[msgMap[0]], msgMap[1], msgMap[2]);
        }
      });
    }
  }
}

enum MqttStatus {
  CONNECT_SUCCESS,
  CONNECT_FAIL,
  CONNECT_LOST,
  SUBSCRIBE_SUCCESS,
  SUBSCRIBE_FAIL,
  RECONNECT,
  MESSAGE_ARRIVED,
}

extension MqttStatusExtension on MqttStatus {
  static const values = {
    MqttStatus.CONNECT_SUCCESS: "CONNECT_SUCCESS",
    MqttStatus.CONNECT_FAIL: "CONNECT_FAIL",
    MqttStatus.CONNECT_LOST: "CONNECT_LOST",
    MqttStatus.SUBSCRIBE_SUCCESS: "SUBSCRIBE_SUCCESS",
    MqttStatus.SUBSCRIBE_FAIL: "SUBSCRIBE_FAIL",
    MqttStatus.RECONNECT: "RECONNECT",
    MqttStatus.MESSAGE_ARRIVED: "MESSAGE_ARRIVED",
  };

  static const keys = {
    "CONNECT_SUCCESS": MqttStatus.CONNECT_SUCCESS,
    "CONNECT_FAIL": MqttStatus.CONNECT_FAIL,
    "CONNECT_LOST": MqttStatus.CONNECT_LOST,
    "SUBSCRIBE_SUCCESS": MqttStatus.SUBSCRIBE_SUCCESS,
    "SUBSCRIBE_FAIL": MqttStatus.SUBSCRIBE_FAIL,
    "RECONNECT": MqttStatus.RECONNECT,
    "MESSAGE_ARRIVED": MqttStatus.MESSAGE_ARRIVED,
  };

  String get value => values[this];

  MqttStatus get key => keys[this];
}
