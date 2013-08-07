//
//  Caregiver.m
//  nocturne
//
//  Created by Nico teWinkel on 13-07-24.
//  Copyright (c) 2013 mulBerryLand.com. All rights reserved.
//

#import "Caregiver.h"
#import <RestKit/RestKit.h>
#import "DeviceRegistrationInfo.h"

@implementation Caregiver

@synthesize userid;
@synthesize username;
@synthesize lastname;
@synthesize firstname;
@synthesize email;
@synthesize phone_home;
@synthesize phone_cell;
@synthesize address;
@synthesize description;

static NSArray *_caregiverList;

+(void) getCaregiversWithCallback:(void (^)(NSArray *caregivers, NSString *error))theCallback {
    NSString *apiKey = [DeviceRegistrationInfo apiKey];
    
// TODO: hide the API key by making it part of the http header
    
    NSDictionary *queryParams = [NSDictionary dictionaryWithObjectsAndKeys:apiKey, @"auth", nil];
    RKObjectManager *objectManager = [RKObjectManager sharedManager];
    
    [objectManager getObjectsAtPath:@"/rest/ntewinkel/demo/v1/Caregivers" parameters:queryParams
            success:^(RKObjectRequestOperation *operation, RKMappingResult *mappingResult) {
                
                _caregiverList = [mappingResult array];
                
//                NSLog(@"Successfully received caregiver list!");
//                NSLog(@"caregiverList = %@", _caregiverList);
                
                theCallback(_caregiverList, nil);
            }

            failure:^(RKObjectRequestOperation *operation, NSError *error) {
                NSArray *responses = [operation responseDescriptors];
                NSUInteger responseCode = 0;
                
                if (responses.count > 0) {
                    RKResponseDescriptor *response = responses[0];
                    responseCode = response.statusCodes.firstIndex;
                    NSLog(@"Response status code = %d", responseCode);
                }
                
                NSString *errorMsg = [NSString stringWithFormat:@"Caregiver list fetch failed. Error = '%@'", error];
                
                NSLog(@"%@", errorMsg);
                
                theCallback(nil, errorMsg);
            }];
    
}

+(NSInteger)numberOfCaregivers {
    return _caregiverList.count;
}

+(Caregiver *)caregiverAtIndex:(NSInteger)index {
    return (index<_caregiverList.count)?_caregiverList[index]:nil;
}

-(NSString *)description {
    
    return [NSString stringWithFormat:@"Caregiver: %@ %@, username = %@, id = %@", self.firstname, self.lastname, self.username, self.userid];
}

@end
