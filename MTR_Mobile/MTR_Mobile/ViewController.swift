//
//  ViewController.swift
//  MTR_Mobile
//
//  Created by Emil Berger on 16.04.19.
//  Copyright © 2019 PMB Development. All rights reserved.
//

import UIKit
import SwiftSocket

class ViewController: UIViewController, UITextFieldDelegate {
    
    // Variables
    var client: TCPClient = TCPClient(address: "", port: 1804)
    
    enum ConnectionError: Error {
        case ipEmpty
    }
    
    // Methods
    private func refreshGui() -> Void {
        disconnectButton.isEnabled = false
        velocitySlider.isEnabled = false
        speedModeSwitch.isEnabled = false
        forwardButton.isEnabled = false
        backwardButton.isEnabled = false
        rightButton.isEnabled = false
        leftButton.isEnabled = false
        
        if MTR.sharedInstance.connected {
            disconnectButton.isEnabled = true
            velocitySlider.isEnabled = true
            speedModeSwitch.isEnabled = true
            forwardButton.isEnabled = true
            backwardButton.isEnabled = true
            rightButton.isEnabled = true
            leftButton.isEnabled = true
        }
    }

    // GUI Variables
    @IBOutlet weak var ipAddressTF: UITextField! { didSet { ipAddressTF.delegate = self } }
    @IBOutlet weak var connectButton: UIButton!
    @IBOutlet weak var disconnectButton: UIButton!
    @IBOutlet weak var velocitySlider: UISlider!
    @IBOutlet weak var speedModeSwitch: UISwitch!
    @IBOutlet weak var forwardButton: UIButton!
    @IBOutlet weak var backwardButton: UIButton!
    @IBOutlet weak var rightButton: UIButton!
    @IBOutlet weak var leftButton: UIButton!
    
    
    // Event Handler
    @IBAction func connectTapped(_ sender: UIButton) {
        do {
            if (ipAddressTF.text!.isEmpty) {
                throw ConnectionError.ipEmpty
            }
            client = TCPClient(address: ipAddressTF.text!, port: 1804)
            let result = client.connect(timeout: 3)
            if (result.isFailure) {
                throw result.error!
            }
            MTR.sharedInstance.connected = true
            
        } catch (ConnectionError.ipEmpty) {
            MTR.sharedInstance.connected = false
            let alert = UIAlertController(title: "IP Address Error", message: "IP Address Field must no be empty!", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
            self.present(alert, animated: true)
            
        } catch (SocketError.connectionTimeout) {
            MTR.sharedInstance.connected = false
            let alert = UIAlertController(title: "Connection Error", message: "Connection timed out!", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
            self.present(alert, animated: true)
            
        } catch (SocketError.queryFailed) {
            MTR.sharedInstance.connected = false
            let alert = UIAlertController(title: "Connection Error", message: "Query failed!", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
            
        } catch (SocketError.connectionClosed) {
            MTR.sharedInstance.connected = false
            let alert = UIAlertController(title: "Connection Error", message: "Connection closed!", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
            
        } catch let unknownErrors {
            MTR.sharedInstance.connected = false
            print(unknownErrors)
        }
        refreshGui()
    }
    
    @IBAction func disconnectTapped(_ sender: UIButton) {
        client.close()
        MTR.sharedInstance.connected = false
    }
    
    @IBAction func velocityValueChanged(_ sender: UISlider) {
        MTR.sharedInstance.velocity = Int(sender.value)
    }
    
    @IBAction func speedModeSwitch(_ sender: UISwitch) {
        MTR.sharedInstance.speedMode = sender.isOn
    }
    
    @IBAction func moveForwardTapped(_ sender: UIButton) {
        
    }
    
    @IBAction func moveBackwardTapped(_ sender: UIButton) {
    }
    
    @IBAction func moveRightTapped(_ sender: UIButton) {
    }
    
    @IBAction func moveLeftTapped(_ sender: UIButton) {
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        refreshGui()
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
        
    }

    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }

}

