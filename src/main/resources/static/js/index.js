$( window ).ready(function() {
  connect();
});

function connect() {
  var socket = new SockJS('/websocket');
  stompClient = Stomp.over(socket);
  var data = "";
  stompClient.connect({}, function (frame) {
      // 订阅。服务端给客户端发送消息
      stompClient.subscribe('/topic/pushNotification', function (notification) {
          // data = data + notification.body;
          $('#textArea').val(notification.body);
       });
  });
}