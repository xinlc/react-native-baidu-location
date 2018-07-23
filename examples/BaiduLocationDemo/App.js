/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View, Button} from 'react-native';
import NRBaiduloc from 'react-native-baidu-location';

const RCTDeviceEventEmitter = require('RCTDeviceEventEmitter');  // eslint-disable-line

type Props = {};
export default class App extends Component<Props> {

  state = {
    location: {},
  }

  componentDidMount() {
    // add listener, and set the handler to this.
    let RNBaiduEventListener = RCTDeviceEventEmitter.addListener('OnReceiveLocation', (ev) => {
      if (ev.cityCode == 9000) {  // 台湾省下所有城市都映射为台湾
        ev.city = '台湾';
      }
      this.setState({ location: ev });
    });
  }
  _loc () {
    NRBaiduloc.start();
  }

  render() {
    return (
      <View style={styles.container}>
        <Button
          onPress={this._loc}
          title="定位"
          color="#841584"
        />
        <Text style={styles.welcome}>{
          JSON.stringify(this.state.location)
        }</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});
