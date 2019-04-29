//
//  ViewController.swift
//  MTR_Mobile
//
//  Created by Emil Berger on 16.04.19.
//  Copyright © 2019 PMB Development. All rights reserved.
//

import UIKit
import SwiftSocket

class ViewController: UIViewController {
    
    var client: TCPClient = TCPClient(address: "", port: 1804)
    var connected: Bool = false
    
    @IBOutlet weak var ipAddressTF: UITextField!
    
    @IBAction func connectTapped(_ sender: UIButton) {
        client = TCPClient(address: ipAddressTF.text!, port: 1804)
        connected = true
    }
    
    @IBAction func disconnectTapped(_ sender: UIButton) {
        connected = false
    }
    
    @IBAction func velocityValueChanged(_ sender: Any) {
    }
    
    @IBAction func speedModeSwitch(_ sender: Any) {
    }
    
    @IBAction func moveForwardTapped(_ sender: Any) {
    }
    
    @IBAction func moveBackwardTapped(_ sender: Any) {
    }
    
    @IBAction func moveRightTapped(_ sender: Any) {
    }
    
    @IBAction func moveLeftTapped(_ sender: Any) {
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
    }


}

