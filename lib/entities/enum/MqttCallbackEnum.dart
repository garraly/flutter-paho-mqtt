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
