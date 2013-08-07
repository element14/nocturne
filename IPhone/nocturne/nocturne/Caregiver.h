//
//  Caregiver.h
//  nocturne
//
//  Created by Nico teWinkel on 13-07-24.
//  Copyright (c) 2013 mulBerryLand.com. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Caregiver : NSObject {
    NSNumber *userid;
    NSString *username;
    NSString *lastname;
    NSString *firstname;
    NSString *email;
    NSString *phone_home;
    NSString *phone_cell;
    NSString *address;
    NSString *description;
}

@property (strong, nonatomic) NSNumber *userid;
@property (strong, nonatomic) NSString *username;
@property (strong, nonatomic) NSString *lastname;
@property (strong, nonatomic) NSString *firstname;
@property (strong, nonatomic) NSString *email;
@property (strong, nonatomic) NSString *phone_home;
@property (strong, nonatomic) NSString *phone_cell;
@property (strong, nonatomic) NSString *address;
@property (strong, nonatomic) NSString *description;

+(void) getCaregiversWithCallback:(void (^)(NSArray *caregivers, NSString *error))theCallback;

// The caregiver list is stored as part of this Caregiver class
// The views can then ask this class for the required bits of data
+(NSInteger)numberOfCaregivers;
+(Caregiver *)caregiverAtIndex:(NSInteger)index;

@end
