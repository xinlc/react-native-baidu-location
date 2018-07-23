package com.rnbaidulocation;


import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class RNBaidulocModule extends ReactContextBaseJavaModule {

    private LocationService locationService;
    private ReactContext mReactContext;

    public RNBaidulocModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNBaiduloc";
    }

    //发送参数到js端
    private void sendEvent(String eventName, WritableMap params) {
        mReactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    @ReactMethod
    public void start(){
        if(null==locationService){
            locationService=new LocationService(mReactContext);
        }
        locationService.registerListener(mListener);
        locationService.start();
    }

    @ReactMethod
    public void stop(){
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop();
    }

    /*****
     * 定位结果回调
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                sb.append(location.getTime());
                sb.append("\nreturned code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");
                sb.append(location.getCityCode());
                sb.append("\ncity : ");
                sb.append(location.getCity());
                sb.append("\nDistrict : ");
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");
                sb.append(location.getStreet());
                sb.append("\nStreetNumber : ");
                sb.append(location.getStreetNumber());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }

                WritableMap params = Arguments.createMap();
                params.putString("description", sb.toString());
                params.putInt("locType", location.getLocType());
                if(location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation || location.getLocType() == BDLocation.TypeOffLineLocation){
                    params.putDouble("latitude", location.getLatitude());
                    params.putDouble("longitude", location.getLongitude());
                    params.putString("cityCode", location.getCityCode());
                    params.putString("city", location.getCity());
                    params.putString("district", location.getDistrict());
                    params.putString("street", location.getStreet());
                    params.putString("streetNumber", location.getStreetNumber());
                    params.putString("addr", location.getAddrStr());
                }else{
                    //定位不成功,给一个默认值 :北京
                    params.putDouble("latitude", 39.9375346);
                    params.putDouble("longitude", 115.8370122);
                    params.putString("cityCode", "131");
                    params.putString("city", "北京市");
                    params.putString("district", "");
                    params.putString("street", "");
                    params.putString("streetNumber", "");
                    params.putString("addr", "");
                }
                sendEvent("OnReceiveLocation",params);
            }
        }

    };
}
