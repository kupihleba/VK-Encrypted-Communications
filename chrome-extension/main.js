"use strict";

const consoleID = "#Kupihleba_console";
const mainConsole = $(".im_editable.im-chat-input--text._im_text");
const IMpage = /https:\/\/vk.com\/im.*/i;

/**
 * Class for message encryption
 * @type {EncryptEngine}
 */
const EncryptEngine = class {

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
        $.ajax({
            type: "POST",
            url: "http://127.0.0.1:5005/encrypt",
            data: data,
            success: (data, txtStatus, jqXHR) => {
                if (this._callback) {
                    this._callback(data);
                }
            }
        });
        return "Encrypting ...";
    }

    decryptMessage(message) {
        const request = EncryptEngine._decrypt(message.innerHTML);
        request.done((data, textStatus, xhr) => {
            if (xhr.status === 200) {
                message.innerHTML = data;
                //console.log(data);
                message.className += " encrypted_msg";
                $(".encrypted_msg").parent().parent().addClass("encrypted_msg_cover");
            } else {
                // 205 for common msg
            }
        });
        //request.onerror((data) => console.log(data));
    }

    static _decrypt(data) {
        if (data !== "") {
            return $.ajax({
                type: "POST",
                url: "http://127.0.0.1:5005/decrypt",
                data: data.replace("\0", ""),
            });
        }
    }
};

const encryptor = new EncryptEngine();  // creating encryptor object
encryptor.setCallbackFunc(dataArrived); // setting ui update function

/**
 * function creates input field in html for encrypted messages
 */
function init() {
    if (document.URL.match(IMpage)) {
        $("<div class=\"im_editable im-chat-input--text _im_text\" tabindex=\"0\" id=\"Kupihleba_console\" contenteditable=\"true\" role=\"textbox\" aria-multiline=\"false\"></div>").insertAfter(".im_editable.im-chat-input--text._im_text");
        //console.log($("#im_dialogs")[0].children);
        Array.from($("#im_dialogs")[0].children).forEach(obj => obj.onclick = () => {
            setTimeout(() => decryptAll(getMessages()), 500);
        });

        Array.from($("._im_ui_peers_list")[0].children).forEach(elem => elem.onclick = () => {
            setTimeout(() => {
                let dialogs = $("#im_dialogs");
                if (dialogs) {
                    decryptAll(getMessages());
                }
            }, 500);
        });
    }
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
    const all = $(".im_msg_text[class!='encrypted_msg']");
        //.add(".im-mess--text.wall_module._im_log_body");
    return all;//.filter(":not(.encrypted_msg)");
}


function decryptAll(messages) {
    messages.each((i, msg) => {
        if (msg.innerHTML !== "") {
            //console.log(msg.innerHTML);
            encryptor.decryptMessage(msg);
        }
    });
}

decryptAll(getMessages());
init();

/**
 * Sets the encrypted text, given by EncryptEngine
 * @param data encrypted message
 */
function dataArrived(data) {
    const kupihleba = $(consoleID);
    mainConsole.html(data);
    mainConsole.focus();
//im-send-btn im-chat-input--send _im_send _im_send.im-send-btn_send im-send-btn_audio
//im-send-btn im-chat-input--send _im_send _im_send.im-send-btn_send im-send-btn_send
    //$(".im-send-btn.im-chat-input--send._im_send.im-send-btn_audio").removeClass("im-send-btn_audio").removeClass("im-send-btn_saudio").addClass("_im_send.im-send-btn_send");
    $(".im-send-btn.im-send-btn_send").click();
    //mainConsole.html(tube(kupihleba.html()));
    kupihleba.html("");
    $(".ph_content").html("cleartext message here")
}
$(".im-send-btn.im-chat-input--send._im_send.im-send-btn_audio").removeClass("im-send-btn_audio").removeClass("im-send-btn_saudio").addClass("_im_send.im-send-btn_send");

let timerEnabled = false;
function timer() {
    if (document.URL.match(IMpage)) {
        decryptAll(getMessages());
    }
    if (timerEnabled) {
        setTimeout(timer, 2000);
    }
}
timer();

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
        $(".ph_content").html(tube(kupihleba.html()));
        kupihleba.html("");
        //mainConsole.focus();
    }
});