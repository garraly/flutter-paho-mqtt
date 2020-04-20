import 'package:flutter/material.dart';
import 'package:flutter_android_mqtt/flutter_android_mqtt.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
  }

  void _getInstance() {
    FlutterAndroidMqtt.listenMessage((MqttStatus status, String msg){
     print('listener-----------------> '+ MqttStatusExtension.values[status] + msg);
    });
    FlutterAndroidMqtt.connect(serverUri: null, clientId: null, userName: null, password: null);
  }

  void _sub () {
    FlutterAndroidMqtt.subscribe(subscriptionTopic: null, qos: null);
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: Center(
        child: Column(
          children: <Widget>[
            RaisedButton(
              onPressed: _getInstance,
              child: const Text('链接', style: TextStyle(fontSize: 20)),
            ),
            RaisedButton(
              onPressed: _sub,
              child: const Text('订阅', style: TextStyle(fontSize: 20)),
            ),
          ],
        )
      ),
    ));
  }
}
