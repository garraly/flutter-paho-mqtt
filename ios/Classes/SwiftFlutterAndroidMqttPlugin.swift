import Flutter
import UIKit

public class SwiftFlutterAndroidMqttPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "flutter_android_mqtt", binaryMessenger: registrar.messenger())
    let instance = SwiftFlutterAndroidMqttPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
