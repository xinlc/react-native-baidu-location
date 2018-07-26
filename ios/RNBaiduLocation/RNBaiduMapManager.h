//
//  RNBaiduMapManager.h
//  RNBaiduLocation
//
//  Created by Leo on 25/07/2018.
//

#import <React/RCTBridgeModule.h>
#import <BaiduMapAPI_Base/BMKBaseComponent.h>
#import <BaiduMapAPI_Location/BMKLocationComponent.h>
#import <BaiduMapAPI_Search/BMKGeocodeSearch.h>

@interface RNBaiduMapManager : NSObject<RCTBridgeModule, BMKLocationServiceDelegate, BMKGeoCodeSearchDelegate>

@end
