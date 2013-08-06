//
//  AppDelegate.m
//  nocturne
//
//  Created by Nico teWinkel on 13-07-24.
//  Copyright (c) 2013 mulBerryLand.com. All rights reserved.
//

#import "AppDelegate.h"
#import "MainViewController.h"

#import <RestKit/Restkit.h>

#import "Client.h"
#import "Caregiver.h"

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
    [self configureRestKit];

    self.window = [[UIWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
    // Override point for customization after application launch.
    self.mainViewController = [[MainViewController alloc] initWithNibName:@"MainViewController" bundle:nil];
    self.window.rootViewController = self.mainViewController;
    [self.window makeKeyAndVisible];
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application
{
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application
{
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application
{
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}

- (void)applicationDidBecomeActive:(UIApplication *)application
{
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application
{
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

- (void)configureRestKit {
    //  "RestKit is configured to show info messages and above in DEBUG builds. In non-DEBUG builds, only warnings, errors, and critical messages are logged. This is defined via RKLogLevelDefault in RKLog.h."
    //    RKLogConfigureByName("RestKit", RKLogLevelWarning);
    //    RKLogConfigureByName("RestKit/Network*", RKLogLevelWarning);
    //    RKLogConfigureByName("RestKit/ObjectMapping", RKLogLevelWarning);
    
    //let AFNetworking manage the activity indicator
    [AFNetworkActivityIndicatorManager sharedManager].enabled = YES;
    
    // Initialize HTTPClient
    NSURL *baseURL = [NSURL URLWithString:@"https://eval.espressologic.com/rest/ntewinkel/demo/v1"];
    AFHTTPClient* client = [[AFHTTPClient alloc] initWithBaseURL:baseURL];
    //we want to work with JSON-Data
    [client setDefaultHeader:@"Accept" value:RKMIMETypeJSON];
    
    // Initialize RestKit
    RKObjectManager *objectManager = [[RKObjectManager alloc] initWithHTTPClient:client];
    
    // Setup our object mappings
    RKObjectMapping *clientMapping = [RKObjectMapping mappingForClass:[client class]];
    [clientMapping addAttributeMappingsFromDictionary:@{
     @"userid" : @"userid",
     @"username" : @"username",
     @"lastname" : @"lastname",
     @"firstname" : @"firstname",
     @"email" : @"email",
     @"phone_home" : @"phone_home",
     @"phone_cell" : @"phone_cell",
     @"address" : @"address",
     @"description" : @"description",
     @"lastknownstatus" : @"lastknownstatus",
     @"laststatusupdate" : @"laststatusupdate"
     }];
    
    RKObjectMapping *caregiverMapping = [RKObjectMapping mappingForClass:[Caregiver class]];
    [caregiverMapping addAttributeMappingsFromDictionary:@{
     @"userid" : @"userid",
     @"username" : @"username",
     @"lastname" : @"lastname",
     @"firstname" : @"firstname",
     @"email" : @"email",
     @"phone_home" : @"phone_home",
     @"phone_cell" : @"phone_cell",
     @"address" : @"address",
     @"description" : @"description"
     }];
    
    // Update date format so that we can parse dates properly
    // Wed Sep 29 15:31:08 +0000 2010
    [RKObjectMapping addDefaultDateFormatterForString:@"E MMM d HH:mm:ss Z y" inTimeZone:nil];
    
    // Register our mappings with the provider using a response descriptor
    RKResponseDescriptor *loginResponseDescriptor =
    [RKResponseDescriptor responseDescriptorWithMapping:clientMapping
                                            pathPattern:@"/Clients"
                                                keyPath:nil
                                            statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    
    [objectManager addResponseDescriptor:loginResponseDescriptor];
    
    RKResponseDescriptor *fileInfoResponseDescriptor =
    [RKResponseDescriptor responseDescriptorWithMapping:caregiverMapping
                                            pathPattern:@"/Caregivers"
                                                keyPath:nil
                                            statusCodes:RKStatusCodeIndexSetForClass(RKStatusCodeClassSuccessful)];
    
    [objectManager addResponseDescriptor:fileInfoResponseDescriptor];
    
}

@end
