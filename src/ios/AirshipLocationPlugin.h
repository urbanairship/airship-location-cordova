/* Copyright Urban Airship and Contributors */

#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>

/**
 * The Urban Airship Location plugin.
 */
@interface AirshipLocationPlugin : CDVPlugin

/**
 * Enables or disables location.
 *
 * Expected arguments: Boolean
 *
 * @param command The cordova command.
 */
- (void)setLocationEnabled:(CDVInvokedUrlCommand *)command;

/**
 * Checks if location is enabled or not.
 *
 * @param command The cordova command.
 */
- (void)isLocationEnabled:(CDVInvokedUrlCommand *)command;

/**
 * Enables or disables background location.
 *
 * Expected arguments: Boolean
 *
 * @param command The cordova command.
 */
- (void)setBackgroundLocationEnabled:(CDVInvokedUrlCommand *)command;

/**
 * Checks if background location is enabled or not.
 *
 * @param command The cordova command.
 */
- (void)isBackgroundLocationEnabled:(CDVInvokedUrlCommand *)command;

@end
