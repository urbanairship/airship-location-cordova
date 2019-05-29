/* Copyright Urban Airship and Contributors */

#import <Foundation/Foundation.h>
#import "AirshipLocationPlugin.h"
#import "UAirship.h"
#import <Cordova/CDVPlugin.h>
#import <CoreLocation/CoreLocation.h>
#import "UALocation.h"

typedef void (^UACordovaCompletionHandler)(CDVCommandStatus, id);
typedef void (^UACordovaExecutionBlock)(NSArray *args, UACordovaCompletionHandler completionHandler);

@interface AirshipLocationPlugin()
@property(nonatomic, strong) UALocation *location;
@end

@implementation AirshipLocationPlugin

- (void)pluginInitialize {
    UA_LINFO("Initializing Airship location cordova plugin.");

    self.location = [UALocation sharedLocation];
}

- (void)performCallbackWithCommand:(CDVInvokedUrlCommand *)command withBlock:(UACordovaExecutionBlock)block {
    [self.commandDelegate runInBackground:^{
        UACordovaCompletionHandler completionHandler = ^(CDVCommandStatus status, id value) {
            CDVPluginResult *result = [self pluginResultForValue:value status:status];
            [self.commandDelegate sendPluginResult:result callbackId:command.callbackId];
        };

        if (!block) {
            completionHandler(CDVCommandStatus_OK, nil);
        } else {
            block(command.arguments, completionHandler);
        }
    }];
}

- (CDVPluginResult *)pluginResultForValue:(id)value status:(CDVCommandStatus)status{
    // String
    if ([value isKindOfClass:[NSString class]]) {
        return [CDVPluginResult resultWithStatus:status
                                 messageAsString:[value stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
    }

    // Number
    if ([value isKindOfClass:[NSNumber class]]) {
        CFNumberType numberType = CFNumberGetType((CFNumberRef)value);
        //note: underlyingly, BOOL values are typedefed as char
        if (numberType == kCFNumberIntType || numberType == kCFNumberCharType) {
            return [CDVPluginResult resultWithStatus:status messageAsInt:[value intValue]];
        } else  {
            return [CDVPluginResult resultWithStatus:status messageAsDouble:[value doubleValue]];
        }
    }

    // Array
    if ([value isKindOfClass:[NSArray class]]) {
        return [CDVPluginResult resultWithStatus:status messageAsArray:value];
    }

    // Object
    if ([value isKindOfClass:[NSDictionary class]]) {
        return [CDVPluginResult resultWithStatus:status messageAsDictionary:value];
    }

    // Null
    if ([value isKindOfClass:[NSNull class]]) {
        return [CDVPluginResult resultWithStatus:status];
    }

    // Nil
    if (!value) {
        return [CDVPluginResult resultWithStatus:status];
    }

    return [CDVPluginResult resultWithStatus:status];
}

- (void)isLocationEnabled:(CDVInvokedUrlCommand *)command {
    UA_LTRACE("isLocationEnabled called with command arguments: %@", command.arguments);

    [self performCallbackWithCommand:command withBlock:^(NSArray *args, UACordovaCompletionHandler completionHandler) {
        BOOL enabled = self.location.locationUpdatesEnabled;
        completionHandler(CDVCommandStatus_OK, [NSNumber numberWithBool:enabled]);
    }];
}

- (void)isBackgroundLocationEnabled:(CDVInvokedUrlCommand *)command {
    UA_LTRACE("isBackgroundLocationEnabled called with command arguments: %@", command.arguments);

    [self performCallbackWithCommand:command withBlock:^(NSArray *args, UACordovaCompletionHandler completionHandler) {
        BOOL enabled = self.location.backgroundLocationUpdatesAllowed;
        completionHandler(CDVCommandStatus_OK, [NSNumber numberWithBool:enabled]);
    }];
}

- (void)setLocationEnabled:(CDVInvokedUrlCommand *)command {
    UA_LTRACE("setLocationEnabled called with command arguments: %@", command.arguments);

    [self performCallbackWithCommand:command withBlock:^(NSArray *args, UACordovaCompletionHandler completionHandler) {
        BOOL enabled = [[args objectAtIndex:0] boolValue];
        UA_LTRACE("setLocationEnabled set to:%@", enabled ? @"true" : @"false");

        self.location.locationUpdatesEnabled = enabled;

        completionHandler(CDVCommandStatus_OK, nil);
    }];
}

- (void)setBackgroundLocationEnabled:(CDVInvokedUrlCommand *)command {
    UA_LTRACE("setBackgroundLocationEnabled called with command arguments: %@", command.arguments);

    [self performCallbackWithCommand:command withBlock:^(NSArray *args, UACordovaCompletionHandler completionHandler) {
        BOOL enabled = [[args objectAtIndex:0] boolValue];

        self.location.backgroundLocationUpdatesAllowed = enabled;

        completionHandler(CDVCommandStatus_OK, nil);
    }];
}

@end
