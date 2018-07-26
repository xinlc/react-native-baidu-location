//
//  RNBaiduMapManager.m
//  RNBaiduLocation
//
//  Created by Leo on 25/07/2018.
//

#import "RNBaiduMapManager.h"
#import <React/RCTBridge.h>
#import <React/RCTLog.h>
//#import "RCTEventDispatcher.h"  // remove by Francis
#import <React/RCTEventDispatcher.h>

@interface RNBaiduMapManager () {

}

@property (nonatomic, strong) BMKLocationService * loc;
@property (nonatomic, strong) BMKGeoCodeSearch * geo;

@end

@implementation RNBaiduMapManager

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(start) {
   NSLog(@"RNBaiduMapManager.start");
  dispatch_async(dispatch_get_main_queue(), ^{
    if (_loc == nil) {
      [self isAuthorizeOpenLocation];
      self.loc = [[BMKLocationService alloc] init];
      _loc.delegate = self;
      _loc.distanceFilter = 100.0f;
      _loc.desiredAccuracy = kCLLocationAccuracyBest;
    }
    [_loc startUserLocationService];

  });
}

RCT_EXPORT_METHOD(stop) {
  if (_loc != nil) {
    [_loc stopUserLocationService];
  }
}

- (void)didUpdateBMKUserLocation:(BMKUserLocation *)userLocation {
  NSLog(@"didUpdateUserLocation lat %f,long %f",userLocation.location.coordinate.latitude,userLocation.location.coordinate.longitude);

  if (self.geo == nil) {
    _geo = [[BMKGeoCodeSearch alloc] init];
    _geo.delegate = self;
  }
  CLLocationCoordinate2D pt = (CLLocationCoordinate2D){userLocation.location.coordinate.latitude, userLocation.location.coordinate.longitude};
  BMKReverseGeoCodeOption *reverseGeoCodeSearchOption = [[BMKReverseGeoCodeOption alloc] init];
  reverseGeoCodeSearchOption.reverseGeoPoint = pt;
  BOOL flag = [_geo reverseGeoCode:reverseGeoCodeSearchOption];
  if(flag) {
    NSLog(@"反geo检索发送成功");
  } else {
    NSLog(@"反geo检索发送失败");
  }

}

- (void)onGetReverseGeoCodeResult:(BMKGeoCodeSearch *)searcher result:(BMKReverseGeoCodeResult *)result errorCode:(BMKSearchErrorCode)error {
  __block NSString * cityCode = @"";
  NSString * city = @"";
  if (error == BMK_SEARCH_NO_ERROR) {

    city = result.addressDetail.city;

    NSArray * list = [NSArray arrayWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"citycode" ofType:@"plist"]];
    [list enumerateObjectsUsingBlock:^(NSDictionary * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
      NSString * item = obj[@"cityName"];
      if ([city isEqualToString:item]) {
        cityCode = obj[@"cityCode"];
      }
    }];
  }
  NSLog(@"%@:%@", city, cityCode);

  [self.bridge.eventDispatcher sendAppEventWithName:@"OnReceiveLocation" body:@{@"latitude":@(result.location.latitude),
                                                                                @"longitude":@(result.location.longitude),
                                                                                @"city":city,
                                                                                @"cityCode":cityCode,
                                                                                @"district":result.addressDetail.district,
                                                                                @"street":result.addressDetail.streetName,
                                                                                @"streetNumber":result.addressDetail.streetNumber,
                                                                                @"addr":result.address}];
}

- (void)didFailToLocateUserWithError:(NSError *)error {
  NSLog(@"loc failed:%@", error);
}

- (BOOL)isAuthorizeOpenLocation {
  if ([CLLocationManager locationServicesEnabled] &&
      ([CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorizedAlways
       || [CLLocationManager authorizationStatus] == kCLAuthorizationStatusNotDetermined || [CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorizedWhenInUse)) {
        return YES;
      }
  return NO;
}

@end
