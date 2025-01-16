// Theoretical Websocket implementation, built by TSC Target=ES5

"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.WebsocketClient = void 0;
var spawnjs_network_1 = require("spawnjs:network");
var TcpConnection = spawnjs_network_1.TcpConnection;
var WebsocketClient = /** @class */ (function () {
    function WebsocketClient(address, port) {
        this.name = "foo";
        this.tcp = new TcpConnection(address, port);
    }
    return WebsocketClient;
}());
exports.WebsocketClient = WebsocketClient;
