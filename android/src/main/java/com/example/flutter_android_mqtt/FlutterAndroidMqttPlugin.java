package com.example.flutter_android_mqtt;

import android.content.Context;

import androidx.annotation.NonNull;

import io.flutter.Log;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.EventChannel.StreamHandler;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** FlutterAndroidMqttPlugin */
public class FlutterAndroidMqttPlugin implements FlutterPlugin, MethodCallHandler, StreamHandler {
  private static MqttUtil mqttUtil;
  private String CREATE = "CREATE";
  private String UN_SUBSCRIBE_TO_PIC = "UN_SUBSCRIBE_TO_PIC";
  private String PUBLISH = "PUBLISH";
  private static Context myContext;
  static EventChannel.EventSink eventSink;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    final MethodChannel channel = new MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "flutter_android_mqtt");
    FlutterAndroidMqttPlugin flutterAndroidMqttPlugin = new FlutterAndroidMqttPlugin();
    channel.setMethodCallHandler(flutterAndroidMqttPlugin);
    this.myContext = flutterPluginBinding.getApplicationContext();
    Log.i("-------------------===",this.myContext.toString());
    mqttUtil= new MqttUtil();
    final EventChannel eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), "android_mqtt_event");
    eventChannel.setStreamHandler(flutterAndroidMqttPlugin);
  }

  // This static function is optional and equivalent to onAttachedToEngine. It supports the old
  // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
  // plugin registration via this function while apps migrate to use the new Android APIs
  // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
  //
  // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
  // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
  // depending on the user's project. onAttachedToEngine or registerWith must both be defined
  // in the same class.
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "flutter_android_mqtt");
    channel.setMethodCallHandler(new FlutterAndroidMqttPlugin());
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals(CREATE)) {
      System.out.println("LOG:----------> jskdlfjsdklfj");
      mqttUtil.onCreate(this.myContext,getServerUri(call), getClientId(call),getSubscriptionTopic(call));
      mqttUtil.addListener();
      result.success(null);
    } else if (call.method.equals(UN_SUBSCRIBE_TO_PIC)){
      mqttUtil.unSubscribeToTopic();
    } else if (call.method.equals(PUBLISH)) {
      mqttUtil.publishMessage("123","123");
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
  }

  @Override
  public void onListen(Object arguments, EventChannel.EventSink events) {
    eventSink = events;
  }

  @Override
  public void onCancel(Object arguments) {
    eventSink = null;
  }

  private String getServerUri(MethodCall call) {
    String fontSizeType = call.argument("serverUri");
    if (fontSizeType == null || fontSizeType.equals("")) {
      return "";
    }
    return fontSizeType;
  }
  private String getClientId(MethodCall call) {
    String fontSizeType = call.argument("clientId");
    if (fontSizeType == null || fontSizeType.equals("")) {
      return "";
    }
    return fontSizeType;
  }
  private String getSubscriptionTopic(MethodCall call) {
    String fontSizeType = call.argument("subscriptionTopic");
    if (fontSizeType == null || fontSizeType.equals("")) {
      return "";
    }
    return fontSizeType;
  }
}
