"use strict";

const consoleID = "#Kupihleba_console";
const mainConsole = $(".im_editable.im-chat-input--text._im_text");

const EncryptEngine = class {
    constructor() {
        //this._socket = io.connect("127.0.0.1:5005");
        //this._socket.onmessage((ev) => this._callback(ev.data));
    }

    /**
     * Set function, that would be executed with data parameter, when the CryptonServer sent the respond
     * @param func
     */
    setCallbackFunc(func) {
        this._callback = func;
    }

    /**
     * Encrypts the messages
     * @param data given message
     * @returns {string} encrypting status string
     */
    encrypt(data) {
        //this._socket.emit(JSON.stringify(data));
        $.ajax({
            type: "POST",
            url:"http://127.0.0.1:5005/encrypt",
            data: data,
            success: (data, txtStatus, jqXHR) => {
            if (this._callback) {
            this._callback(data);
        }
    }});
        return "Encrypting ...";
    }
    decryptMessage(message) {
        const request = this._decrypt(message.innerHTML);
        request.done((data, textStatus, xhr) => {
            if (xhr.status === 200) {
            message.innerHTML = data;
            console.log(message);
            message.className += " encrypted_msg";
        } else {
            // 205 for common msg
        }
    });
        //request.onerror((data) => console.log(data));
    }

    _decrypt(data) {
        //console.log("decrypt ->", data);
        return $.ajax({
            type: "POST",
            url: "http://127.0.0.1:5005/decrypt",
            data: data,
        });
    }
};
const encryptor = new EncryptEngine();  // creating encryptor object
encryptor.setCallbackFunc(dataArrived); // setting ui update function

/**
 * function creates input field in html for encrypted messages
 */
function init() {
    $("<div class=\"im_editable im-chat-input--text _im_text\" tabindex=\"0\" id=\"Kupihleba_console\" contenteditable=\"true\" role=\"textbox\" aria-multiline=\"false\"></div>").insertAfter(".im_editable.im-chat-input--text._im_text");
    //console.log($("#im_dialogs")[0].children);
    Array.from($("#im_dialogs")[0].children).forEach(obj => obj.onclick = () => {
        setTimeout(() => { decryptAll(getMessages()); }, 500);
    });
}

/**
 * Loads hack design to show, that the extension is enabled
 * @deprecated
 */
function loadCSS() {
    const link = document.createElement("link");
    link.rel = "stylesheet";
    link.type = "text/css";
    link.href = "file:///css/style.css";
    document.documentElement.appendChild(link);
}

function getMessages() {
    console.log($(".im_msg_text"));
    return $(".im_msg_text");
}

function decryptAll(messages) {
    messages.each((i, msg) => {
        if (msg.innerHTML !== "") {
        encryptor.decryptMessage(msg);
    }
});
}
decryptAll(getMessages());

function getCurrentAddress() {
    return document.URL;
}
document.onclick = () => {
    //console.log(getCurrentAddress());
};
/**
 * Class for message encryption
 * @type {EncryptEngine}
 */


/**
 * Sets the encrypted text, given by EncryptEngine
 * @param data encrypted message
 */
function dataArrived(data) {
    const kupihleba = $(consoleID);
    mainConsole.html(data);
    //mainConsole.html(tube(kupihleba.html()));
    kupihleba.html("");
    mainConsole.focus();
}

init();

/**
 * evaluates the data and executes encrypt method of encryptor
 * @param data
 * @returns {string|*}
 */
function tube(data) {
    let t = new Date();
    data = data.replace("@TIME", `${t.getHours()}:${t.getMinutes()}:${t.getSeconds()}`);
    data = encryptor.encrypt(data);
    return data;//btoa(unescape(encodeURIComponent(data)));
}

$(consoleID).on("keypress", (event) => {
    const key = event.which || event.keyCode;
if (key === 13) {
    const kupihleba = $(consoleID);
    mainConsole.html(tube(kupihleba.html()));
    kupihleba.html("");
    //mainConsole.focus();
}
});