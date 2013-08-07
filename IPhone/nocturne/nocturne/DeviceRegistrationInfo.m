//
//  User.m
//  nocturne
//
//  Created by Nico teWinkel on 13-08-07.
//  Copyright (c) 2013 mulBerryLand.com. All rights reserved.
//

#import "DeviceRegistrationInfo.h"

@implementation DeviceRegistrationInfo

+(void) setApiKey: (NSString *)newApiKey {
    [[NSUserDefaults standardUserDefaults] setObject:newApiKey forKey:@"apiKey"];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

+(NSString *) apiKey {
    return [[NSUserDefaults standardUserDefaults] objectForKey:@"apiKey"];
}

@end
