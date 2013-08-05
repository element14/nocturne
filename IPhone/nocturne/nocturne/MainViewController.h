//
//  MainViewController.h
//  nocturne
//
//  Created by Nico teWinkel on 13-07-24.
//  Copyright (c) 2013 mulBerryLand.com. All rights reserved.
//

#import "FlipsideViewController.h"

@interface MainViewController : UIViewController <FlipsideViewControllerDelegate> {
    
}

@property (weak, nonatomic) IBOutlet UILabel *_lblGreeting;
@property (weak, nonatomic) IBOutlet UILabel *_lblAlerts;
@property (weak, nonatomic) IBOutlet UISegmentedControl *_bedStatus;
@property (weak, nonatomic) IBOutlet UITableView *_overviewTableview;


- (IBAction)showInfo:(id)sender;

@end
