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

  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: Center(
        child: RaisedButton(
          onPressed: _getInstance,
          child: const Text('获取', style: TextStyle(fontSize: 20)),
        ),
      ),
    ));
  }
}
