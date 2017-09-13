"use strict";

const consoleID = "#Kupihleba_console";
const mainConsole = $(".im_editable.im-chat-input--text._im_text");

/**
 * function creates input field in html for encrypted messages
 */
function init() {
    $("<div class=\"im_editable im-chat-input--text _im_text\" tabindex=\"0\" id=\"Kupihleba_console\" contenteditable=\"true\" role=\"textbox\" aria-multiline=\"false\"></div>").insertAfter(".im_editable.im-chat-input--text._im_text");
}

/**
 * Loads hack design to show, that the extension is enabled
 */
function loadCSS() {
    const link = document.createElement("link");
    link.rel = "stylesheet";
    link.type = "text/css";
    link.href = "file:///css/style.css";
    document.documentElement.appendChild(link);
}

/**
 * Class for message encryption
 * @type {EncryptEngine}
 */
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
            url: "http://127.0.0.1:5005/encrypt",
            data: data,
            success: (data, txtStatus, jqXHR) => {
                console.log(data);
                if (this._callback) {
                    this._callback(data);
                }

            }
        });
        return "Encrypting ...";
    }

    decrypt(data) {

    }
};


const encryptor = new EncryptEngine();  // creating encryptor object
encryptor.setCallbackFunc(dataArrived); // setting ui update function

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

console.log($(consoleID));

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
    console.log(key);
    if (key === 13) {
        const kupihleba = $(consoleID);
        mainConsole.html(tube(kupihleba.html()));
        kupihleba.html("");
        //mainConsole.focus();
    }
});