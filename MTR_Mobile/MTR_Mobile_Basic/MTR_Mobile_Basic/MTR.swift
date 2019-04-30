//
//  Data.swift
//  MTR_Mobile
//
//  Created by Emil Berger on 29.04.19.
//  Copyright © 2019 PMB Development. All rights reserved.
//

import Foundation
import SwiftSocket

class MTR {

    // Variables
    private var _velocity: Int = 0
    private var _speedMode: Bool = false
    
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
    
    private var _request: String = ""
    
    private var _connected: Bool = false

    //Singleton
    class var sharedInstance: MTR {
        struct Static {
            static let instance = MTR()
        }
        return Static.instance
    }

    private init(){}
    
    //Methods
    private func updateRequest() -> Void {
        let leftMotorAsString = String(format: "%03d", _leftMotor)
        let rightMotorAsString = String(format: "%03d", _rightMotor)
        
        var pneumaticString = "P-"
        pneumaticString.append(String(_compressor))
        pneumaticString.append(";")
        pneumaticString.append(String(_pneumaticV1))
        pneumaticString.append(";")
        pneumaticString.append(String(_pneumaticV2))
        pneumaticString.append(";")
        pneumaticString.append(String(_pneumaticV3))
        pneumaticString.append(";")
        pneumaticString.append(String(_pneumaticV4))
        pneumaticString.append(";")
        pneumaticString.append(String(_pneumaticV5))
        pneumaticString.append(";")
        pneumaticString.append(String(_pneumaticV6))
        pneumaticString.append(";")
        
        var lightString = "L-"
        lightString.append(String(_interiorLight))
        lightString.append(";")
        lightString.append(String(_headLight))
        lightString.append(";")
        lightString.append(String(_floodLight))
        lightString.append(";")
        lightString.append(String(_warningLight))
        lightString.append(";")
        
        var outputString = "O-"
        outputString.append(String(_output1Front))
        outputString.append(";")
        outputString.append(String(_output2Front))
        outputString.append(";")
        outputString.append(String(_output3Back))
        outputString.append(";")
        outputString.append(String(_output4Back))
        outputString.append(";")
        
        let displayString = ("D-" + String(_display) + ";")
        
        let motorString = ("M-" + leftMotorAsString + ";" + rightMotorAsString + ";")
        
        _request = ("START|" + pneumaticString + "|" + lightString + "|" + outputString + "|" + displayString + "|" + motorString + "|STOP")
    }
    
    public func sendRequest(client: TCPClient) throws -> String? {
        if _connected {
            switch client.send(string: _request) {
                case .success:
                    guard let data = client.read(1024*10) else {
                        return nil
                    }
                    if let response = String(bytes: data, encoding: .utf8) {
                        return response
                    }
                case .failure(let error):
                    throw error
            }
            return nil
        } else {
            return nil
        }
    }
    
    //Properties
    public var connected: Bool {
        get { return _connected }
        set { _connected = newValue }
    }
    
    public var velocity: Int {
        get { return _velocity }
        set { _velocity = newValue }
    }
    
    public var speedMode: Bool {
        get { return _speedMode }
        set { _speedMode = newValue }
    }
    
    public var leftMotor: Int {
        get { return _leftMotor }
        set {
            _leftMotor = newValue
            updateRequest()
        }
    }
    
    public var rightMotor: Int {
        get { return _rightMotor }
        set {
            _rightMotor = newValue
            updateRequest()
        }
    }
    
    public var pneumaticV1: Int {
        get { return _pneumaticV1 }
        set {
            _pneumaticV1 = newValue
            updateRequest()
        }
    }
    
    public var pneumaticV2: Int {
        get { return _pneumaticV2 }
        set {
            _pneumaticV2 = newValue
            updateRequest()
        }
    }
    
    public var pneumaticV3: Int {
        get { return _pneumaticV3 }
        set {
            _pneumaticV3 = newValue
            updateRequest()
        }
    }
    
    public var pneumaticV4: Int {
        get { return _pneumaticV4 }
        set {
            _pneumaticV4 = newValue
            updateRequest()
        }
    }
    
    public var pneumaticV5: Int {
        get { return _pneumaticV5 }
        set {
            _pneumaticV5 = newValue
            updateRequest()
        }
    }
    
    public var pneumaticV6: Int {
        get { return _pneumaticV6 }
        set {
            _pneumaticV6 = newValue
            updateRequest()
        }
    }
    
    public var compressor: Int {
        get { return _compressor }
        set {
            _compressor = newValue
            updateRequest()
        }
    }
    
    public var interiorLight: Int {
        get { return _interiorLight }
        set {
            _interiorLight = newValue
            updateRequest()
        }
    }
    
    public var headLight: Int {
        get { return _headLight }
        set {
            _headLight = newValue
            updateRequest()
        }
    }
    
    public var floodLight: Int {
        get { return _floodLight }
        set {
            _floodLight = newValue
            updateRequest()
        }
    }
    
    public var warningLight: Int {
        get { return _warningLight }
        set {
            _warningLight = newValue
            updateRequest()
        }
    }
    
    public var output1front: Int {
        get { return _output1Front }
        set {
            _output1Front = newValue
            updateRequest()
        }
    }
    
    public var output2Front: Int {
        get { return _output2Front }
        set {
            _output2Front = newValue
            updateRequest()
        }
    }
    
    public var output3Back: Int {
        get { return _output3Back }
        set {
            _output3Back = newValue
            updateRequest()
        }
    }
    
    public var output4Back: Int {
        get { return _output4Back }
        set {
            _output4Back = newValue
            updateRequest()
        }
    }
    
    public var display: Int {
        get { return _display }
        set {
            _display = newValue
            updateRequest()
        }
    }
}
