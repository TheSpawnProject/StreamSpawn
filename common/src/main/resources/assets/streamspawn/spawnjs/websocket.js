// Theoretical Websocket implementation, built by TSC Target=ES5

"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.WebsocketClient = void 0;
var spawnjs_network_1 = require("spawnjs:network");
var TcpClient = spawnjs_network_1.Network.TcpClient;
var WebsocketClient = /** @class */ (function () {
    function WebsocketClient(address, port) {
        this.name = "foo";
        this.tcp = new TcpClient(address, port);
    }
    return WebsocketClient;
}());
exports.WebsocketClient = WebsocketClient;
