// this is ues to save some utils function

import { CITY_LEVEL2_LIST } from './Baidu_City_L2';
import DISTRICT from "./District"; // eslint-disable-line

const RCTDeviceEventEmitter = require('RCTDeviceEventEmitter');  // eslint-disable-line
const NRBaiduloc = require('../native/RNBaiduloc'); // eslint-disable-line
const Utils = {};
const noop = function() {};  // eslint-disable-line

Utils.debug = (msg) => {
  if (msg.stack) {
    console.log(msg.stack);
  } else {
    console.log(`test utils: ${msg}`);
  }
};

Utils.getLocation = (callback, timeoutCallback = Function()) => {  // eslint-disable-line
  console.info('Utils.getLocation');
  let isLocating = true;
  const endLoc = () => {
    console.log('endLoc');
    isLocating = false;
    NRBaiduloc.stop();
    // remove the listener,in case duplicated handler add again.
    RNBaiduEventListener.remove();
  };
  /* 定位开始*/
  NRBaiduloc.start();
  // add listener, and set the handler to this.
  let RNBaiduEventListener = RCTDeviceEventEmitter.addListener('OnReceiveLocation', (ev) => {
    if (isLocating) {
      if (ev.cityCode == 9000) {  // 台湾省下所有城市都映射为台湾
        ev.city = '台湾';
      }
      callback(ev);
      endLoc();
    }
  });
  /* 定位结束*/

  // set timeout for the locating
  setTimeout(() => {
    console.log('local timeout');
    if (isLocating) {
      endLoc();
      timeoutCallback();
    }
  }, (15 * 1000));
};

Utils.getLocationRN = (sucessfun, errfun, options) => {
  if (sucessfun === undefined) {
    return false;
  }
  if (options === undefined) {
    options = { enableHighAccuracy: true, timeout: 20000, maximumAge: 1000 };
  }
  if (errfun === undefined) {
    errfun = function (err) {
      console.log(err);
    };
  }
  navigator.geolocation.getCurrentPosition(
    sucessfun,
    errfun,
    options
  );
  return true;
};

Utils.getCityNameById = (id) => {
  let cityName = '';
  for (const item of CITY_LEVEL2_LIST) {
    if (item.children) {
      const city = item.children.find(n => n.code == id);
      if (city) {
        cityName = city.name;
        break;
      }
    } else if (item.code == id) {
      cityName = item.name;
      break;
    }
  }
  return cityName;
};

Utils.getDistricts = (cityId) => {
  let district = [{ label: '全市', value: '-1' }];
  if (!cityId) {
    return district;
  }
  for (const item of DISTRICT) {
    if (item.value == cityId && item.children) {
      district = item.children;
      break;
    } else if (item.children) {
      const city = item.children.find(n => n.value == cityId);
      if (city && city.children) {
        district = city.children;
        break;
      }
    }
  }
  return district;
};

// 把树状结构转list， value => key
Utils.option2remap = (options, prefix) => {
  const _map = {};
  for (let i = 0; i < options.length; i++) {
    if (typeof (options[i]) == 'object') {
      _map[(prefix || '') + options[i].label] = options[i].value;
      if (options[i].children) {
        let _prefix = (prefix || '');
        if (options[i].label.indexOf('省') === -1 && options[i].label.indexOf('自治区') === -1) {
          _prefix += `${options[i].label}$`;
        }
        const _childMap = Utils.option2remap(options[i].children, _prefix);
        Object.assign(_map, _childMap);
      }
    }
  }
  return _map;
};

// 把树状结构转list
Utils.option2map = (options, prefix) => {
  const _map = {};
  for (let i = 0; i < options.length; i++) {
    if (typeof (options[i]) == 'object') {
      _map[(prefix || '') + options[i].label] = options[i].value;
      if (options[i].children) {
        const _childMap = Utils.option2map(options[i].children, `${(prefix || '') + options[i].value}$`);
        Object.assign(_map, _childMap);
      }
    }
  }
  return _map;
};

export default Utils;
