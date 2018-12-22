<%-- Created by IntelliJ IDEA. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body id="mainBody">
    HELLO
  </body>
  <script>
    var body = document.getElementById("mainBody");
    body.innerText = 'Connecting to Websocket';
    var chatClient = new WebSocket("ws://localhost:8080/chat");

    chatClient.onerror = function (ev) {
        console.log(ev);
        body.innerText = 'Fail connection to websocket';
    }

    chatClient.onopen = function () {
        console.log('Connection Success');
        body.innerText = 'Connected to Websocket';
    };
    

  </script>
</html>