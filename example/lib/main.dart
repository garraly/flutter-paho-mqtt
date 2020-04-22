import 'package:flutter/material.dart';
import 'package:flutter_android_mqtt/flutter_android_mqtt.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  String msg = '123';

  @override
  void initState() {
    super.initState();
  }

  void _getInstance() {
    final self = this;
    FlutterAndroidMqtt.listenMessage((MqttStatus status, String topic, String msg) {
      String printText = MqttStatusExtension.values[status];
      if (msg != null) {
         printText += ' : ';
         printText += msg;
      }
      print('listener-----------------> ' + printText);
      changeMsg(printText);
    });
    FlutterAndroidMqtt.connect(serverUri: 'tcp://192.168.31.99:1883', clientId: 'qwer123', userName: 'chisj', password: '123456');
  }

  void changeMsg (String data) {
    setState(() {
      msg = data;
    });
  }

  void _sub() {
    FlutterAndroidMqtt.subscribe(subscriptionTopic: 'mtopic', qos: 1);
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
                  Text(msg)
                ],
              )
          ),
        ));
  }
}
