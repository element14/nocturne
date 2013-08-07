//
//  FlipsideViewController.h
//  nocturne
//
//  Created by Nico teWinkel on 13-07-24.
//  Copyright (c) 2013 mulBerryLand.com. All rights reserved.
//

#import <UIKit/UIKit.h>

@class FlipsideViewController;

@protocol FlipsideViewControllerDelegate
- (void)flipsideViewControllerDidFinish:(FlipsideViewController *)controller;
@end

@interface FlipsideViewController : UIViewController

@property (weak, nonatomic) id <FlipsideViewControllerDelegate> delegate;

@property (weak, nonatomic) IBOutlet UILabel *_greetingLabel;
@property (weak, nonatomic) IBOutlet UIButton *_connectBedPadButton;
@property (weak, nonatomic) IBOutlet UITableView *_connectionsView;

- (IBAction)done:(id)sender;
- (IBAction)connectBedpad:(UIButton *)sender;
- (IBAction)addClient:(UIButton *)sender;
- (IBAction)addCaregiver:(UIButton *)sender;

@end
