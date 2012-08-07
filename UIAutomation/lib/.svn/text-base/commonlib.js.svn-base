/*
--------------------------------------------------------------------------------
FUNCTION LIST
This section is the place where you list functions. Please list them as comments
--------------------------------------------------------------------------------

function swipeLeft();
function swipeRight();
function killApp();
function convert2DateObject(strDate);
function dateFormat();
function verifyStaticTextExist(id,timeout);
function verifyButtonExist(id,timeout,btab);
function regexp(strVerify,strExp);
function lockApp(type,atime);

*/


 
/*
--------------------------------------------------------------------------------
IMPLEMENTATION
This section is the place where you implement functions declared above
--------------------------------------------------------------------------------
*/

var target = UIATarget.localTarget();
var app = target.frontMostApp();

/*
 * Date Format 1.2.3
 * (c) 2007-2009 Steven Levithan <stevenlevithan.com>
 * MIT license
 *
 * Includes enhancements by Scott Trenda <scott.trenda.net>
 * and Kris Kowal <cixar.com/~kris.kowal/>
 *
 * Accepts a date, a mask, or a date and a mask.
 * Returns a formatted version of the given date.
 * The date defaults to the current date/time.
 * The mask defaults to dateFormat.masks.default.
 */

var dateFormat = function () {
    var token = /d{1,4}|m{1,4}|yy(?:yy)?|([HhMsTt])\1?|[LloSZ]|"[^"]*"|'[^']*'/g,
        timezone = /\b(?:[PMCEA][SDP]T|(?:Pacific|Mountain|Central|Eastern|Atlantic) (?:Standard|Daylight|Prevailing) Time|(?:GMT|UTC)(?:[-+]\d{4})?)\b/g,
        timezoneClip = /[^-+\dA-Z]/g,
        pad = function (val, len) {
            val = String(val);
            len = len || 2;
            while (val.length < len) val = "0" + val;
            return val;
        };

    // Regexes and supporting functions are cached through closure
    return function (date, mask, utc) {
        var dF = dateFormat;

        // You can't provide utc if you skip other args (use the "UTC:" mask prefix)
        if (arguments.length == 1 && Object.prototype.toString.call(date) == "[object String]" && !/\d/.test(date)) {
            mask = date;
            date = undefined;
        }

        // Passing date through Date applies Date.parse, if necessary
        date = date ? new Date(date) : new Date;
        if (isNaN(date)) throw SyntaxError("invalid date");

        mask = String(dF.masks[mask] || mask || dF.masks["default"]);

        // Allow setting the utc argument via the mask
        if (mask.slice(0, 4) == "UTC:") {
            mask = mask.slice(4);
            utc = true;
        }

        var _ = utc ? "getUTC" : "get",
            d = date[_ + "Date"](),
            D = date[_ + "Day"](),
            m = date[_ + "Month"](),
            y = date[_ + "FullYear"](),
            H = date[_ + "Hours"](),
            M = date[_ + "Minutes"](),
            s = date[_ + "Seconds"](),
            L = date[_ + "Milliseconds"](),
            o = utc ? 0 : date.getTimezoneOffset(),
            flags = {
                d:    d,
                dd:   pad(d),
                ddd:  dF.i18n.dayNames[D],
                dddd: dF.i18n.dayNames[D + 7],
                m:    m + 1,
                mm:   pad(m + 1),
                mmm:  dF.i18n.monthNames[m],
                mmmm: dF.i18n.monthNames[m + 12],
                yy:   String(y).slice(2),
                yyyy: y,
                h:    H % 12 || 12,
                hh:   pad(H % 12 || 12),
                H:    H,
                HH:   pad(H),
                M:    M,
                MM:   pad(M),
                s:    s,
                ss:   pad(s),
                l:    pad(L, 3),
                L:    pad(L > 99 ? Math.round(L / 10) : L),
                t:    H < 12 ? "a"  : "p",
                tt:   H < 12 ? "am" : "pm",
                T:    H < 12 ? "A"  : "P",
                TT:   H < 12 ? "AM" : "PM",
                Z:    utc ? "UTC" : (String(date).match(timezone) || [""]).pop().replace(timezoneClip, ""),
                o:    (o > 0 ? "-" : "+") + pad(Math.floor(Math.abs(o) / 60) * 100 + Math.abs(o) % 60, 4),
                S:    ["th", "st", "nd", "rd"][d % 10 > 3 ? 0 : (d % 100 - d % 10 != 10) * d % 10]
            };

        return mask.replace(token, function ($0) {
            return $0 in flags ? flags[$0] : $0.slice(1, $0.length - 1);
        });
    };
}();

// Some common format strings
dateFormat.masks = {
    "default":      "ddd mmm dd yyyy HH:MM:ss",
    shortDate:      "m/d/yy",
    mediumDate:     "mmm d, yyyy",
    longDate:       "mmmm d, yyyy",
    fullDate:       "dddd, mmmm d, yyyy",
    shortTime:      "h:MM TT",
    mediumTime:     "h:MM:ss TT",
    longTime:       "h:MM:ss TT Z",
    isoDate:        "yyyy-mm-dd",
    isoTime:        "HH:MM:ss",
    isoDateTime:    "yyyy-mm-dd'T'HH:MM:ss",
    isoUtcDateTime: "UTC:yyyy-mm-dd'T'HH:MM:ss'Z'"
};

// Internationalization strings
dateFormat.i18n = {
    dayNames: [
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat",
        "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    ],
    monthNames: [
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
        "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    ]
};

// For convenience...
Date.prototype.format = function (mask, utc) {
    return dateFormat(this, mask, utc);
};

// strDate in dd/mm/yyyy format
function convert2DateObject(strDate) {
    strDate = strDate.split("/");
    var d = parseInt(strDate[0]);
    var m = parseInt(strDate[1]) - 1;
    var y = parseInt(strDate[2]);
    var showDate = new Date(y, m, d, 0, 0, 0 ,0);
    return showDate;
}

function verifyStaticTextExist(id,timeout) {
    // timeout is optional, default = 1
    if (typeof timeout == "undefined") {
        timeout = 1;
    }

    target.pushTimeout(timeout);
    if (target.frontMostApp().mainWindow().staticTexts()[id].isValid()) { 
        UIALogger.logPass(id + " text is valid");
    } else {
        UIALogger.logFail(id + " text is not valid");
    }
    target.popTimeout();
}

// id: id of button
// timeout: maxt time wait for button appared
// btab: press button
function verifyButtonExist(id,timeout,btab) {
    // timeout is optional, default = 1
    if (typeof timeout == "undefined") {
        timeout = 1;
    }

    if (typeof btab == "undefined") {
        btab = 0;
    }

    target.pushTimeout(timeout);
    if (target.frontMostApp().mainWindow().buttons()[id].isValid()) { 
        UIALogger.logPass(id + " button is valid");
        if (btab) {
            target.frontMostApp().mainWindow().buttons()[id].tap();
        }
    } else {
        UIALogger.logFail(id + " button is not valid");
    }
    target.popTimeout();
}

function regexp(strVerify,strExp) {
    UIALogger.logDebug("strVerify = " + strVerify + " - strExp = " + strExp);
    return strExp.test(strVerify);
}

function swipeLeft() {
    // swipe left
    target.delay(1);
    if (deviceType == "IPOD") {
        target.dragFromToForDuration({x:240, y:416}, {x:16, y:402}, 0.6);
    } else if (deviceType == "IPAD") {
        // IPAD
        target.dragFromToForDuration({x:440, y:416}, {x:16, y:402}, 0.6);
    }
    target.delay(1);
}

function swipeRight() {
    // swipe right
    target.delay(1);
    if (deviceType == "IPOD") {
        target.dragFromToForDuration({x:16, y:402}, {x:240, y:416}, 0.6);
    } else if (deviceType == "IPAD") {
        // IPAD
        target.dragFromToForDuration({x:16, y:402}, {x:440, y:416}, 0.6);
    }
    target.delay(1);
}

function killApp() {
    // Need go to MISFIT Collection first before call this function
    target.frontMostApp().mainWindow().buttons()["Kill"].tap();
    return 0;
}

/*
type:
    Home: Press home button
    Lock: Press lock button
atime: block in seconds 
*/
function lockApp(type,atime) {
    if (type == "Home") {
        target.deactivateAppForDuration(atime);
        target.delay(2);
    } else if (type == "Lock") {
        target.lockForDuration(atime);
        target.delay(2);
    }
}