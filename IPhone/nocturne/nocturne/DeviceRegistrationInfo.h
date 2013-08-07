//
//  User.h
//  nocturne
//
//  Created by Nico teWinkel on 13-08-07.
//  Copyright (c) 2013 mulBerryLand.com. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DeviceRegistrationInfo : NSObject {
    
}

// TODO: store user registration information
//   We need a Registration popup view for this, shown when app is not yet registered
//   this should be stored in NSUserDefaults
//   when created or updated, this information should also be sent to the Server.


// TODO: Store bedpad connection info in here too

+(void)setApiKey:(NSString *)apiKey;
+(NSString *)apiKey;

@end
