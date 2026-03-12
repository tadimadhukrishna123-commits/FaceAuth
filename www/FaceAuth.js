var exec = require('cordova/exec');

var FaceAuth = {

    startAuth: function (payload, success, error) {

        exec(
            success,
            error,
            "FaceAuth",
            "startAuth",
             [payload]
        );

    }

};

module.exports = FaceAuth;
