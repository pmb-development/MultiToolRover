//
//  Data.swift
//  MTR_Mobile
//
//  Created by Emil Berger on 29.04.19.
//  Copyright © 2019 PMB Development. All rights reserved.
//

import Foundation

class Data {

    private var _leftMotor: Int = 0
    private var _rightMotor: Int = 0
    
    private var _pneumaticV1: Int = 0
    private var _pneumaticV2: Int = 0
    private var _pneumaticV3: Int = 0
    private var _pneumaticV4: Int = 0
    private var _pneumaticV5: Int = 0
    private var _pneumaticV6: Int = 0
    private var _compressor: Int = 0
    
    private var _interiorLight: Int = 0
    private var _headLight: Int = 0
    private var _floodLight: Int = 0
    private var _warningLight: Int = 0
    
    private var _output1Front: Int = 0
    private var _output2Front: Int = 0
    private var _output3Back: Int = 0
    private var _output4Back: Int = 0
    
    private var _display: Int = 0

    
    class var sharedInstance: Data {
        struct Static {
            static let instance = Data()
        }
        return Static.instance
    }

    private init(){}
    
    
    public var leftMotor: Int {
        get { return _leftMotor }
        set { _leftMotor = newValue }
    }
    
    public var rightMotor: Int {
        get { return _rightMotor }
        set { _rightMotor = newValue }
    }
    
    public var pneumaticV1: Int {
        get { return _pneumaticV1 }
        set { _pneumaticV1 = newValue }
    }
    
    public var pneumaticV2: Int {
        get { return _pneumaticV2 }
        set { _pneumaticV2 = newValue }
    }
    
    public var pneumaticV3: Int {
        get { return _pneumaticV3 }
        set { _pneumaticV3 = newValue }
    }
    
    public var pneumaticV4: Int {
        get { return _pneumaticV4 }
        set { _pneumaticV4 = newValue }
    }
    
    public var pneumaticV5: Int {
        get { return _pneumaticV5 }
        set { _pneumaticV5 = newValue }
    }
    
    public var pneumaticV6: Int {
        get { return _pneumaticV6 }
        set { _pneumaticV6 = newValue }
    }
    
    public var compressor: Int {
        get { return _compressor }
        set { _compressor = newValue }
    }
    
    public var interiorLight: Int {
        get { return _interiorLight }
        set { _interiorLight = newValue }
    }
    
    public var headLight: Int {
        get { return _headLight }
        set { _headLight = newValue }
    }
    
    public var floodLight: Int {
        get { return _floodLight }
        set { _floodLight = newValue }
    }
    
    public var warningLight: Int {
        get { return _warningLight }
        set { _warningLight = newValue }
    }
    
    public var output1front: Int {
        get { return _output1Front }
        set { _output1Front = newValue }
    }
    
    public var output2Front: Int {
        get { return _output2Front }
        set { _output2Front = newValue }
    }
    
    public var output3Back: Int {
        get { return _output3Back }
        set { _output3Back = newValue }
    }
    
    public var output4Back: Int {
        get { return _output4Back }
        set { _output4Back = newValue }
    }
    
    public var display: Int {
        get { return _display }
        set { _display = newValue }
    }
}
