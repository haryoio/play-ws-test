
function timerCreator(action) {
    return JSON.stringify({
        "type": "timer",
        "action": action,
    })
}
function messageCreator(message) {
    return JSON.stringify({
        "type": "broadcast",
        "message": message,
    })
}
function joinRoomCreator(roomName) {
    return JSON.stringify({
        "type": "join",
        "room": roomName,
    })
}

document.addEventListener("DOMContentLoaded", function () {
  const roomName = document.querySelector("h2").textContent.substring(6);
  const wsProtocol = window.location.protocol === "https:" ? "wss://" : "ws://";
  const socket = new WebSocket(
    wsProtocol + window.location.host + "/socket/" + roomName
  );

  const messageForm = document.getElementById("message-form");
  const messageInput = document.getElementById("message-input");
  const messagesDiv = document.getElementById("messages");

  // Handle form submission
  messageForm.addEventListener("submit", function (event) {
    event.preventDefault();
    if (messageInput.value) {
      socket.send(messageCreator(messageInput.value));
      messageInput.value = "";
    }
  });

  const timerElement = document.getElementById("timer");
  const startButton = document.getElementById("startButton");
  const stopButton = document.getElementById("stopButton");
  const resetButton = document.getElementById("resetButton");

  socket.onopen = function (event) {
    socket.send(joinRoomCreator(roomName));
  };

  socket.onmessage = function (event) {
    const message = JSON.parse(event.data);
    if (message.type === "elapsed") {
      timerElement.textContent = message.elapsed;
    }
    if (message.type === "message") {
        const messageEl = document.createElement("div");
        messageEl.textContent = message.content;
        messagesDiv.appendChild(messageEl);
    }
  };
  socket.onclose = function (event) {
    console.log("WebSocket closed");
  };

  startButton.addEventListener("click", function (event) {
    socket.send(timerCreator("start"));
  });
  stopButton.addEventListener("click", function (event) {
    socket.send(timerCreator("stop"));
  });
  resetButton.addEventListener("click", function (event) {
    socket.send(timerCreator("reset"));
  });
});

