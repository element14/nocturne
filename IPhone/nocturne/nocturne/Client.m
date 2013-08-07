//
//  Client.m
//  nocturne
//
//  Created by Nico teWinkel on 13-07-24.
//  Copyright (c) 2013 mulBerryLand.com. All rights reserved.
//

#import "Client.h"
#import <RestKit/RestKit.h>
#import "DeviceRegistrationInfo.h"

@implementation Client

@synthesize userid;
@synthesize username;
@synthesize lastname;
@synthesize firstname;
@synthesize email;
@synthesize phone_home;
@synthesize phone_cell;
@synthesize address;
@synthesize description;
@synthesize lastknownstatus;
@synthesize laststatusupdate;

static NSArray *_clientList;

+(void) getClientsWithCallback:(void (^)(NSArray *clients, NSString *error))theCallback {
    NSString *apiKey = [DeviceRegistrationInfo apiKey];
    
// TODO: hide the API key by making it part of the http header

    NSDictionary *queryParams = [NSDictionary dictionaryWithObjectsAndKeys:apiKey, @"auth", nil];
    RKObjectManager *objectManager = [RKObjectManager sharedManager];
    
    [objectManager getObjectsAtPath:@"/rest/ntewinkel/demo/v1/Clients" parameters:queryParams
            success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
                
                _clientList = [mappingResult array];
                
//                NSLog(@"Successfully received client list!");
//                NSLog(@"clientList = %@", _clientList);
                
                theCallback(_clientList, nil);
            }

            failure:^(RKObjectRequestOperation *operation, NSError *error) {
                NSArray *responses = [operation responseDescriptors];
                NSUInteger responseCode = 0;
                
                if (responses.count > 0) {
                    RKResponseDescriptor *response = responses[0];
                    responseCode = response.statusCodes.firstIndex;
                    NSLog(@"Response status code = %d", responseCode);
                }
                
                NSString *errorMsg = [NSString stringWithFormat:@"Client list fetch failed. Error = '%@'", error];
                
                NSLog(@"%@", errorMsg);
                                                
                theCallback(nil, errorMsg);
            }];

}

+(NSInteger)numberOfClients {
    return _clientList.count;
}

+(Client *)clientAtIndex:(NSInteger)index {
    return (index<_clientList.count)?_clientList[index]:nil;
}

-(NSString *)description {
    
    return [NSString stringWithFormat:@"Client: %@ %@, username = %@, id = %@", self.firstname, self.lastname, self.username, self.userid];
}

@end
