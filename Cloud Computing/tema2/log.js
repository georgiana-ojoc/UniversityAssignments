const log = {
    information: function (information) {
        console.log("Information: " + information)
    },
    warning: function (warning) {
        console.warn("Warning: " + warning)
    },
    error: function (error) {
        console.error("Error: " + error)
    },
}

module.exports = log