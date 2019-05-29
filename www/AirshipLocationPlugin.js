  /* Copyright Urban Airship and Contributors */
  
  var cordova = require("cordova"),
      exec = require("cordova/exec"),
      argscheck = require('cordova/argscheck');
  
  // Helper method to call into the native plugin
  function callNative(success, failure, name, args) {
    args = args || []
    exec(success, failure, "AirshipLocation", name, args)
  }
  
  /**
   * @module AirshipLocationPlugin
   */
  module.exports = {

    /**
     * Enables or disables Urban Airship location services.
     *
     * @param {Boolean} enabled true to enable location, false to disable.
     * @param {function} [success] Success callback.
     * @param {function(message)} [failure] Failure callback.
     * @param {string} failure.message The error message.
     */
    setLocationEnabled: function(enabled, success, failure) {
      argscheck.checkArgs('*FF', 'AirshipLocation.setLocationEnabled', arguments)
      callNative(success, failure, "setLocationEnabled", [!!enabled])
    },
  
    /**
     * Checks if location is enabled or not.
     *
     * @param {function(enabled)} success Success callback.
     * @param {boolean} success.enabled Flag indicating if location is enabled or not.
     * @param {function(message)} [failure] Failure callback.
     * @param {string} failure.message The error message.
     */
    isLocationEnabled: function(success, failure) {
      argscheck.checkArgs('fF', 'AirshipLocation.isLocationEnabled', arguments)
      callNative(success, failure, "isLocationEnabled")
    },
  
    /**
     * Enables or disables background location.
     *
     * @param {Boolean} enabled true to enable background location, false to disable.
     * @param {function} [success] Success callback.
     * @param {function(message)} [failure] Failure callback.
     * @param {string} failure.message The error message.
     */
    setBackgroundLocationEnabled: function(enabled, success, failure) {
      argscheck.checkArgs('*FF', 'AirshipLocation.setBackgroundLocationEnabled', arguments)
      callNative(success, failure, "setBackgroundLocationEnabled", [!!enabled])
    },
  
    /**
     * Checks if background location is enabled or not.
     *
     * @param {function(enabled)} success Success callback.
     * @param {boolean} success.enabled Flag indicating if background location updates are enabled or not.
     * @param {function(message)} [failure] Failure callback.
     * @param {string} failure.message The error message.
     */
    isBackgroundLocationEnabled: function(success, failure) {
      argscheck.checkArgs('fF', 'AirshipLocation.isBackgroundLocationEnabled', arguments)
      callNative(success, failure, "isBackgroundLocationEnabled")
    }

  }
  