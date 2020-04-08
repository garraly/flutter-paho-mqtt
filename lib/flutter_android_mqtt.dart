import 'dart:async';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';

class FlutterAndroidMqtt {
  static const String CREATE = "CREATE";

  static const MethodChannel _channel = const MethodChannel('flutter_android_mqtt');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static void createMqtt(
      {@required final String serverUri, @required String clientId, @required String subscriptionTopic}) {
    _channel.invokeMethod(CREATE, {serverUri: serverUri, clientId: clientId, subscriptionTopic: subscriptionTopic});
  }
}
