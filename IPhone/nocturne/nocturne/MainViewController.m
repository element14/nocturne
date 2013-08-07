//
//  MainViewController.m
//  nocturne
//
//  Created by Nico teWinkel on 13-07-24.
//  Copyright (c) 2013 mulBerryLand.com. All rights reserved.
//

#import "MainViewController.h"

@interface MainViewController ()

@end

@implementation MainViewController

static NSArray *_bedStatusImages;
static NSArray *_bedStatusAlertImages;

- (NSArray *)bedStatusImages {
    
    if (_bedStatusImages == nil) {
        _bedStatusImages = [NSArray arrayWithObjects:[UIImage imageNamed:@"inbed.png"], [UIImage imageNamed:@"empty_bed.png"], [UIImage imageNamed:@"nostatus_bed.png"], nil];
    }
    
    return _bedStatusImages;
}

- (NSArray *)bedStatusAlertImages {
    
    if (_bedStatusAlertImages == nil) {
        _bedStatusAlertImages = [NSArray arrayWithObjects:[UIImage imageNamed:@"inbed_alert.png"], [UIImage imageNamed:@"empty_bed_alert.png"], [UIImage imageNamed:@"nostatus_bed_alert.png"], nil];
    }
    
    return _bedStatusAlertImages;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
    
    NSLog(@"About to do server call...");

    [Client getClientsWithCallback:^(NSArray *clients, NSString *error) {
        if (error == nil) {
        NSLog(@"clients = %@", clients);
        
        // This client list is stored as part of the Client object
        // The views can then ask the Client object for the required bits of data
        NSLog(@"Client list from Client class:");
        for (int i=0; i<[Client numberOfClients]; i++) {
            NSLog(@"     %@", [Client clientAtIndex:i]);
        }
        }
        else {
            NSLog(@"getClientsWithCallback returned error: %@", error);
        }
    }];
    
    // TODO: Server side needs Caregiver list
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Flipside View

- (void)flipsideViewControllerDidFinish:(FlipsideViewController *)controller
{
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)showInfo:(id)sender
{    
    FlipsideViewController *controller = [[FlipsideViewController alloc] initWithNibName:@"FlipsideViewController" bundle:nil];
    controller.delegate = self;
    controller.modalTransitionStyle = UIModalTransitionStyleFlipHorizontal;
    [self presentViewController:controller animated:YES completion:nil];
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 4;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"OverviewCell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:CellIdentifier];
        cell.accessoryType = UITableViewCellAccessoryNone;
    }
        
    cell.textLabel.text = [NSString stringWithFormat:@"Client #%d", indexPath.row + 1];
    cell.detailTextLabel.text = @"7 minutes ago.";
    
    if (indexPath.row < 3) {
        cell.imageView.image = self.bedStatusImages[indexPath.row];
    }
    else {
        cell.imageView.image = self.bedStatusAlertImages[2];
    }
    
    return cell;
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{

    NSLog(@"cell was selected: %d", indexPath.row);
}

@end
